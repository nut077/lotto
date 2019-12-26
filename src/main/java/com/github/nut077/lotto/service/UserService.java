package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.UserCreateDto;
import com.github.nut077.lotto.dto.mapper.PeriodCreateMapper;
import com.github.nut077.lotto.dto.mapper.UserCreateMapper;
import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.exception.NotFoundException;
import com.github.nut077.lotto.repository.LottoRepository;
import com.github.nut077.lotto.repository.UserRepository;
import com.nutfreedom.utilities.ParseNumberFreedom;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
    var period = periodService.findById(periodId);
    var sumBuy = period.getUsers().stream().mapToInt(User::getBuy).sum();
    var sumBuyPercent = period.getUsers().stream().mapToInt(User::getBuyPercent).sum();
    period.setBuyTotal(sumBuy);
    period.setBuyPercentTotal(sumBuyPercent);
    periodService.update(period);
  }

  public UserCreateDto create(Long periodId, UserCreateDto dto) {
    dto.setPeriod(periodCreateMapper.mapToDto(periodService.findById(periodId)));
    var user = userRepository.save(userCreateMapper.mapToEntity(dto));
    return userCreateMapper.mapToDto(user);
  }

  public void delete(Long id) {
    var user = findById(id);
    userRepository.deleteById(id);
    updateBuyPeriod(user.getPeriod().getId());
  }

  public void updateUpdateForm(Long id, UserCreateDto dto) {
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
    try {
      var in = file.getInputStream();
      var currDir = new File(".");
      var path = currDir.getAbsolutePath();
      fileLocation = path.substring(0, path.length() - 1) + "excels\\" + file.getOriginalFilename();
      var directory = new File(path.substring(0, path.length() - 1) + "excels");
      if (!directory.exists()) {
        directory.mkdirs();
      }
      var f = new FileOutputStream(fileLocation);
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

  @SneakyThrows
  private void readFileExcelAndSaveToDatabase(User user) {
    var parse = new ParseNumberFreedom();
    var numberFormat = new DecimalFormat("0");
    var file = new FileInputStream(new File(fileLocation));
    var workbook = new XSSFWorkbook(file);
    var sheet = workbook.getSheetAt(0);

    var rowIterator = sheet.iterator();
    rowIterator.next();

    List<Lotto> lottos = new ArrayList<>();

    while (rowIterator.hasNext()) {
      var row = rowIterator.next();
      var numberLotto = Stream.of(row.getCell(0).toString().split("\\.")).findFirst().orElse("");
      var buyOn = parse.parseInt(numberFormat.format(parse.parseDouble(row.getCell(1).toString())));
      var buyDown = parse.parseInt(numberFormat.format(parse.parseDouble(row.getCell(2).toString())));
      var buyTote = parse.parseInt(numberFormat.format(parse.parseDouble(row.getCell(3).toString())));

      var codePercent = "Y";
      if (Objects.nonNull(row.getCell(4))) {
        codePercent = row.getCell(4).toString();
      }

      var buyTotal = 0;
      if (codePercent.equals("Y")) {
        buyTotal = (buyOn * 100 / 120) + (buyDown * 100 / 120) + (buyTote * 100 / 120);
      } else if (codePercent.equals("N")) {
        buyTotal = buyOn + buyDown + buyTote;
      }
      lottos.add(Lotto
        .builder()
        .numberLotto(numberLotto)
        .buyOn(buyOn)
        .buyDown(buyDown)
        .buyTote(buyTote)
        .buyTotal(buyTotal)
        .percent(Lotto.Percent.codeToPercent(codePercent))
        .build());
    }
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

    file.close();
    workbook.close();
    new File(fileLocation).delete();
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
      UserCreateDto createUser = new UserCreateDto();
      createUser.setName(name);
      return userCreateMapper.mapToEntity(create(periodId, createUser));
    }
  }

  public void importLotto(Long periodId, MultipartFile multipartFile, String name) {
    uploadExcelFile(multipartFile);
    var user = getUser(periodId, name);
    readFileExcelAndSaveToDatabase(user);
  }

}
