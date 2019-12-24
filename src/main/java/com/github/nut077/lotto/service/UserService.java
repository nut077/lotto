package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.UserCreateDto;
import com.github.nut077.lotto.dto.mapper.PeriodCreateMapper;
import com.github.nut077.lotto.dto.mapper.UserCreateMapper;
import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.exception.NotFoundException;
import com.github.nut077.lotto.repository.LottoRepository;
import com.github.nut077.lotto.repository.UserRepository;
import com.nutfreedom.utilities.ParseNumberFreedom;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PeriodService periodService;
  private final LottoRepository lottoRepository;
  private final UserCreateMapper userCreateMapper;
  private final PeriodCreateMapper periodCreateMapper;
  private String fileLocation;

  public List<User> findByPeriod(Long id) {
    return userRepository.findByPeriod(periodService.findById(id));
  }

  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User id: " + id + " -->> Not Found"));
  }

  public List<User> getWinnerLotto(Long periodId, List<String> numberOfWinner) {
    return userRepository.queryWinnerLotto(periodId, numberOfWinner);
  }

  public User createLotto(Long id, User user) {
    findById(id);
    user.setId(id);
    return userRepository.save(user);
  }

  @Transactional
  public User createLotto(Long id, HttpServletRequest request) {
    User user = findById(id);
    lottoRepository.deleteLottoById(id);
    String line = request.getParameter("line");
    if (!StringUtils.isEmpty(line)) {
      ParseNumberFreedom parse = new ParseNumberFreedom();
      String[] spLine = line.split(",");
      for (String i : spLine) {
        Lotto lotto = Lotto.builder()
          .numberLotto(request.getParameter("numberLotto" + i))
          .buyOn(parse.parseInt(request.getParameter("buyOn" + i)))
          .buyDown(parse.parseInt(request.getParameter("buyDown" + i)))
          .buyTote(parse.parseInt(request.getParameter("buyTote" + i)))
          .buyTotal(parse.parseInt(request.getParameter("buyTotal" + i)))
          .percent(Lotto.Percent.codeToPercent(request.getParameter("percent" + i)))
          .build();
        user.addLotto(lotto);
      }
      user.setBuy(parse.parseInt(request.getParameter("buyAll")));
      user.setBuyPercent(parse.parseInt(request.getParameter("buyAllPercent")));
      User userSaved = userRepository.saveAndFlush(user);
      updateBuyPeriod(userSaved.getPeriod().getId());
      return userSaved;
    } else {
      user.setBuy(0);
      user.setBuyPercent(0);
      userRepository.saveAndFlush(user);
      updateBuyPeriod(user.getPeriod().getId());
    }
    return user;
  }

  private void updateBuyPeriod(Long periodId) {
    Period period = periodService.findById(periodId);
    int sumBuy = period.getUsers().stream().mapToInt(User::getBuy).sum();
    int sumBuyPercent = period.getUsers().stream().mapToInt(User::getBuyPercent).sum();
    period.setBuyTotal(sumBuy);
    period.setBuyPercentTotal(sumBuyPercent);
    periodService.update(period);
  }

  public UserCreateDto create(Long periodId, UserCreateDto dto) {
    dto.setPeriod(periodCreateMapper.mapToDto(periodService.findById(periodId)));
    User user = userRepository.save(userCreateMapper.mapToEntity(dto));
    return userCreateMapper.mapToDto(user);
  }

  public void delete(Long id) {
    User user = findById(id);
    userRepository.deleteById(id);
    updateBuyPeriod(user.getPeriod().getId());
  }

  public void updateUpdateForm(Long id, UserCreateDto dto) {
    User user = findById(id);
    user.setName(dto.getName());
    userRepository.save(user);
  }

  public boolean checkDuplicateName(Long periodId, String name) {
    Period period = periodService.findById(periodId);
    Optional<User> user = period.getUsers().stream().filter(u -> u.getName().equalsIgnoreCase(name)).findFirst();
    return user.isPresent();
  }

  private void uploadExcelFile(MultipartFile file) {
    try {
      InputStream in = file.getInputStream();
      File currDir = new File(".");
      String path = currDir.getAbsolutePath();
      fileLocation = path.substring(0, path.length() - 1) + "excels\\" + file.getOriginalFilename();
      File directory = new File(path.substring(0, path.length() - 1) + "excels");
      if (!directory.exists()) {
        directory.mkdirs();
      }
      FileOutputStream f = new FileOutputStream(fileLocation);
      int ch;
      while ((ch = in.read()) != -1) {
        f.write(ch);
      }
      f.flush();
      f.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readFileExcel() {
    DecimalFormat numberFormat = new DecimalFormat("0");
    try {
      FileInputStream file = new FileInputStream(new File(fileLocation));
      //Create Workbook instance holding reference to .xlsx file
      XSSFWorkbook workbook = new XSSFWorkbook(file);

      //Get first/desired sheet from the workbook
      XSSFSheet sheet = workbook.getSheetAt(0);

      //Iterate through each rows one by one
      Iterator<Row> rowIterator = sheet.iterator();
      rowIterator.next();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        //For each row, iterate through all the columns
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
          Cell cell = cellIterator.next();
          switch (cell.getCellType()) {
            case NUMERIC:
              String number = numberFormat.format(cell.getNumericCellValue());
              System.out.print(number + "  ");
              break;
            case STRING:
              boolean isPercent = cell.getStringCellValue().equalsIgnoreCase("Y");
              System.out.print(isPercent + "  ");
              break;
          }
        }
      }
      file.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void importLotto(Long periodId, MultipartFile multipartFile, String name) {
    uploadExcelFile(multipartFile);
    readFileExcel();
  }
}
