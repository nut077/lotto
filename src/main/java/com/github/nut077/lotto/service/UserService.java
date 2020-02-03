package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.UserDto;
import com.github.nut077.lotto.dto.mapper.PeriodMapper;
import com.github.nut077.lotto.dto.mapper.UserCreateMapper;
import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.exception.NotFoundException;
import com.github.nut077.lotto.repository.LottoRepository;
import com.github.nut077.lotto.repository.UserRepository;
import com.nutfreedom.utilities.ParseNumberFreedom;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PeriodService periodService;
  private final LottoRepository lottoRepository;
  private final UserCreateMapper userCreateMapper;
  private final PeriodMapper mapper;
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
  public User createLotto(Long userId, HttpServletRequest request) {
    var user = findById(userId);
    lottoRepository.deleteLottoByUserId(userId);
    var line = request.getParameter("line");
    if (!StringUtils.isEmpty(line)) {
      var parse = new ParseNumberFreedom();
      var spLine = line.split(",");
      for (var i : spLine) {
        Lotto lotto = Lotto.builder()
          .numberLotto(request.getParameter("numberLotto" + i))
          .buyOn(parse.parseInt(request.getParameter("buyOn" + i)))
          .buyDown(parse.parseInt(request.getParameter("buyDown" + i)))
          .buyTote(parse.parseInt(request.getParameter("buyTote" + i)))
          .buyTotal(parse.parseInt(request.getParameter("buyTotal" + i)))
          .percentOn(Lotto.Percent.codeToPercent(request.getParameter("percentOn" + i)))
          .percentDown(Lotto.Percent.codeToPercent(request.getParameter("percentDown" + i)))
          .percentTote(Lotto.Percent.codeToPercent(request.getParameter("percentTote" + i)))
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
    var period = periodService.findById(periodId);
    var sumBuy = period.getUsers().stream().mapToInt(User::getBuy).sum();
    var sumBuyPercent = period.getUsers().stream().mapToInt(User::getBuyPercent).sum();
    period.setBuyTotal(sumBuy);
    period.setBuyPercentTotal(sumBuyPercent);
    periodService.update(period);
  }

  public UserDto create(Long periodId, UserDto dto) {
    dto.setPeriod(mapper.mapToDto(periodService.findById(periodId)));
    var user = userRepository.save(userCreateMapper.mapToEntity(dto));
    return userCreateMapper.mapToDto(user);
  }

  public void delete(Long id) {
    var user = findById(id);
    userRepository.deleteById(id);
    updateBuyPeriod(user.getPeriod().getId());
  }

  public void updateUpdateForm(Long id, UserDto dto) {
    var user = findById(id);
    user.setName(dto.getName());
    userRepository.save(user);
  }

  public boolean checkDuplicateName(Long periodId, String name) {
    var period = periodService.findById(periodId);
    var user = period.getUsers().stream().filter(u -> u.getName().equalsIgnoreCase(name)).findFirst();
    return user.isPresent();
  }

  private void uploadExcelFile(MultipartFile file) {
    var currDir = new File(".");
    var path = currDir.getAbsolutePath();
    fileLocation = path.substring(0, path.length() - 1) + "excels\\" + file.getOriginalFilename();
    try (var f = new FileOutputStream(fileLocation)) {
      var in = file.getInputStream();
      var directory = new File(path.substring(0, path.length() - 1) + "excels");
      if (!directory.exists()) {
        directory.mkdirs();
      }
      int ch;
      while ((ch = in.read()) != -1) {
        f.write(ch);
      }
      f.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @SneakyThrows
  private void readFileExcelAndSaveToDatabase(User user) {
      List<Lotto> lottos = getLottoList();

      var sumBuyTotal = lottos.stream().mapToInt(Lotto::getBuyTotal).sum();
      var sumBuyTotalPercent = lottos.stream()
        .map(lotto -> lotto.getBuyOn() + lotto.getBuyDown() + lotto.getBuyTote())
        .mapToInt(lotto -> lotto).sum();
      if (Objects.nonNull(user.getLottos())) {
        sumBuyTotal += user.getLottos().stream().mapToInt(Lotto::getBuyTotal).sum();
        sumBuyTotalPercent += user.getLottos().stream()
          .map(lotto -> lotto.getBuyOn() + lotto.getBuyDown() + lotto.getBuyTote())
          .mapToInt(lotto -> lotto).sum();
      }

      user.setLottos(lottos);
      user.setBuy(sumBuyTotal);
      user.setBuyPercent(sumBuyTotalPercent);

      var userSaved = userRepository.saveAndFlush(user);
      var period = periodService.findById(userSaved.getPeriod().getId());
      var isFoundUserInPeriod = period.getUsers().stream().anyMatch(u -> u.getName().equals(userSaved.getName()));
      var sumBuy = period.getUsers().stream().mapToInt(User::getBuy).sum();
      var sumBuyPercent = period.getUsers().stream().mapToInt(User::getBuyPercent).sum();
      if (!isFoundUserInPeriod) {
        sumBuy += userSaved.getBuy();
        sumBuyPercent += userSaved.getBuyPercent();
      }
      period.setBuyTotal(sumBuy);
      period.setBuyPercentTotal(sumBuyPercent);
      periodService.update(period);


      Path path = Paths.get(fileLocation);
      Files.delete(path);

  }

  @SneakyThrows
  private List<Lotto> getLottoList() {

    var file = new FileInputStream(new File(fileLocation));
    List<Lotto> lottos = new ArrayList<>();
    try (var workbook = new XSSFWorkbook(file)) {
      var sheet = workbook.getSheetAt(0);

      var rowIterator = sheet.iterator();
      rowIterator.next();

      while (rowIterator.hasNext()) {
        var row = rowIterator.next();
        setDetailLotto(row, lottos);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return lottos;
  }

  private void setDetailLotto(Row row, List<Lotto> lottos) {
    var parse = new ParseNumberFreedom();
    var numberFormat = new DecimalFormat("0");
    var numberLotto = Stream.of(getString(row.getCell(0)).split("\\.")).findFirst().orElse("");
    var buyOn = parse.parseInt(numberFormat.format(parse.parseDouble(getString(row.getCell(1)))));
    var buyDown = parse.parseInt(numberFormat.format(parse.parseDouble(getString(row.getCell(2)))));
    var buyTote = parse.parseInt(numberFormat.format(parse.parseDouble(getString(row.getCell(3)))));

    var codePercentOn = "Y";
    if (row.getCell(4) != null) {
      codePercentOn = row.getCell(4).toString();
    }

    var codePercentDown = "Y";
    if (row.getCell(5) != null) {
      codePercentDown = row.getCell(5).toString();
    }

    var codePercentTote = "Y";
    if (row.getCell(6) != null) {
      codePercentTote = row.getCell(6).toString();
    }

    var buyTotal = 0;

    if (codePercentOn.equals("Y")) {
      buyTotal += (buyOn * 100 / 120);
    } else {
      buyTotal += buyOn;
    }

    if (codePercentDown.equals("Y")) {
      buyTotal += (buyDown * 100 / 120);
    } else {
      buyTotal += buyDown;
    }

    if (codePercentTote.equals("Y")) {
      buyTotal += (buyTote * 100 / 120);
    } else {
      buyTotal += buyTote;
    }

    lottos.add(Lotto
      .builder()
      .numberLotto(numberLotto)
      .buyOn(buyOn)
      .buyDown(buyDown)
      .buyTote(buyTote)
      .buyTotal(buyTotal)
      .percentOn(Lotto.Percent.codeToPercent(codePercentOn))
      .percentDown(Lotto.Percent.codeToPercent(codePercentDown))
      .percentTote(Lotto.Percent.codeToPercent(codePercentTote))
      .build());
  }

  private User getUser(Long periodId, String name) {
    var period = periodService.findById(periodId);
    var user = period.getUsers()
      .stream()
      .filter(u -> u.getName().equals(name))
      .findAny();
    if (user.isPresent()) {
      return user.get();
    } else {
      UserDto createUser = new UserDto();
      createUser.setName(name);
      return userCreateMapper.mapToEntity(create(periodId, createUser));
    }
  }

  public void importLotto(Long periodId, MultipartFile multipartFile, String name) {
    uploadExcelFile(multipartFile);
    var user = getUser(periodId, name);
    readFileExcelAndSaveToDatabase(user);
  }

  private String getString(Cell val) {
    if (val == null) {
      return "";
    }
    return val.toString();
  }

}
