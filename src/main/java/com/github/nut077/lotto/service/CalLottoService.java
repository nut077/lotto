package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.PeriodDto;
import com.github.nut077.lotto.dto.mapper.PeriodMapper;
import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.utility.NumberUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CalLottoService {

  private final PeriodService periodService;
  private final UserService userService;
  private final NumberUtility numberUtility;
  private final PeriodMapper mapper;

  public List<User> calLotto(PeriodDto dto) {
    Period period = periodService.findById(dto.getId());
    String threeOn1 = dto.getThreeOn();
    String twoOn1 = threeOn1.substring(1, 3);
    period = mapper.mapToEntity(dto, period);
    period.setTwoOn(twoOn1);

    String threeOn = period.getThreeOn();
    String twoOn = period.getTwoOn();
    String threeDown1 = period.getThreeDown1();
    String threeDown2 = period.getThreeDown2();
    String threeDown3 = period.getThreeDown3();
    String threeDown4 = period.getThreeDown4();
    String twoDown = period.getTwoDown();
    List<String> toteList = getTote(threeOn);
    List<String> runOn = getRun(threeOn);
    List<String> runDown = getRun(twoDown);

    Set<String> unionRun = new HashSet<>();
    unionRun.addAll(runOn);
    unionRun.addAll(runDown);
    List<String> run = Arrays.asList(unionRun.toArray(new String[0]));

    List<String> numberOfWinner = Stream.concat(toteList.stream(), run.stream()).collect(Collectors.toList());
    numberOfWinner.add(threeOn);
    numberOfWinner.add(twoOn);
    numberOfWinner.add(threeDown1);
    numberOfWinner.add(threeDown2);
    numberOfWinner.add(threeDown3);
    numberOfWinner.add(threeDown4);
    numberOfWinner.add(twoDown);
    List<User> userWinnerLotto = userService.getWinnerLotto(period.getId(), numberOfWinner);
    for (User user : userWinnerLotto) {
      List<Lotto> lottoList = user.getLottos();
      int pay = 0;
      int buy = 0;
      if (lottoList != null) {
        for (Lotto lotto : lottoList) {
          int buyOn = numberUtility.getInt(lotto.getBuyOn());
          int buyDown = numberUtility.getInt(lotto.getBuyDown());
          int buyTote = numberUtility.getInt(lotto.getBuyTote());
          String numberLotto = lotto.getNumberLotto();
          lotto.setPayOn(0);
          lotto.setPayDown(0);
          lotto.setPayTote(0);
          lotto.setPayTotal(0);
          if (numberLotto.length() == 1) {
            if (threeOn.contains(numberLotto)) {
              lotto.setPayOn(period.getPayRunOn() * buyOn);
            }
            if (twoDown.contains(numberLotto)) {
              lotto.setPayDown(period.getPayRunDown() * buyDown);
            }
          } else {
            if (numberLotto.equals(threeOn)) {
              lotto.setPayOn(period.getPayThreeOn() * buyOn);
            }
            if (numberLotto.equals(twoOn)) {
              lotto.setPayOn(period.getPayTwoOn() * buyOn);
            }
            if (numberLotto.equals(threeDown1)) {
              lotto.setPayDown(period.getPayThreeDown1() * buyDown);
            }
            if (numberLotto.equals(threeDown2)) {
              lotto.setPayDown(period.getPayThreeDown2() * buyDown);
            }
            if (numberLotto.equals(threeDown3)) {
              lotto.setPayDown(period.getPayThreeDown3() * buyDown);
            }
            if (numberLotto.equals(threeDown4)) {
              lotto.setPayDown(period.getPayThreeDown4() * buyDown);
            }
            if (numberLotto.equals(twoDown)) {
              lotto.setPayDown(period.getPayTwoDown() * buyDown);
            }
            if (toteList.contains(numberLotto)) {
              lotto.setPayTote(period.getPayTote() * buyTote);
            }
          }

          buy += lotto.getBuyTotal();
          lotto.setPayTotal(numberUtility.getInt(lotto.getPayOn()) + numberUtility.getInt(lotto.getPayDown()) + numberUtility.getInt(lotto.getPayTote()));
          pay += lotto.getPayTotal();
        }
      }
      user.setBuy(buy);
      user.setPay(pay);
      userService.createLotto(user.getId(), user);
    }
    List<User> userList = userService.getWinnerLotto(period.getId(), numberOfWinner);
    Period periodSaved = updatePeriodBuyAndPay(period, userList);
    periodService.savedSendBoss(periodSaved);
    userList.forEach(user -> {
      List<Lotto> lottoSuccess = user.getLottos().stream().filter(lotto -> lotto.getPayTotal() > 0).collect(Collectors.toList());
      user.setLottos(lottoSuccess);
    });
    userList = userList.stream().filter(user -> !user.getLottos().isEmpty()).collect(Collectors.toList());
    return userList;
  }

  private Period updatePeriodBuyAndPay(Period period, List<User> userList) {
    int pay = 0;
    for (User user : userList) {
      pay += user.getPay();
    }
    period.setPayTotal(pay);
    return periodService.update(period);
  }

  private List<String> getTote(String tote) {
    List<Byte[]> predicateTote = getPredicateTote();
    Set<String> set = new HashSet<>();

    predicateTote.forEach(bytes -> {
      StringBuilder result = new StringBuilder();
      for (Byte aByte : bytes) {
        result.append(tote.charAt(aByte));
      }
      set.add(result.toString());
    });

    return Arrays.asList(set.toArray(new String[0]));
  }

  private List<Byte[]> getPredicateTote() {
    return List.of(
      new Byte[]{0, 1, 2},
      new Byte[]{0, 2, 1},
      new Byte[]{1, 0, 2},
      new Byte[]{1, 2, 0},
      new Byte[]{2, 1, 0},
      new Byte[]{2, 0, 1}
    );
  }

  private List<String> getRun(String num) {
    Set<String> set = new HashSet<>();
    for (int i = 0; i < num.length(); i++) {
      set.add(String.valueOf(num.charAt(i)));
    }
    return Arrays.asList(set.toArray(new String[0]));
  }
}
