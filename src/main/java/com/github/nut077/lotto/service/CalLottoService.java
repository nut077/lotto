package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.PeriodResultDto;
import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.utility.NumberUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalLottoService {

  private final PeriodService periodService;
  private final UserService userService;
  private final NumberUtility numberUtility;

  public List<User> calLotto(PeriodResultDto dto) {
    Period period = periodService.findById(dto.getId());
    String threeOn1 = dto.getThreeOn();
    String twoOn1 = threeOn1.substring(1, 3);
    period.setThreeOn(threeOn1);
    period.setTwoOn(twoOn1);
    period.setThreeDown1(dto.getThreeDown1());
    period.setThreeDown2(dto.getThreeDown2());
    period.setThreeDown3(dto.getThreeDown3());
    period.setThreeDown4(dto.getThreeDown4());
    period.setTwoDown(dto.getTwoDown());

    period.setPayThreeOn(dto.getPayThreeOn());
    period.setPayTote(dto.getPayTote());
    period.setPayTwoOn(dto.getPayTwoOn());
    period.setPayThreeDown1(dto.getPayThreeDown1());
    period.setPayThreeDown2(dto.getPayThreeDown2());
    period.setPayThreeDown3(dto.getPayThreeDown3());
    period.setPayThreeDown4(dto.getPayThreeDown4());
    period.setPayTwoDown(dto.getPayTwoDown());

    String threeOn = period.getThreeOn();
    String twoOn = period.getTwoOn();
    String threeDown1 = period.getThreeDown1();
    String threeDown2 = period.getThreeDown2();
    String threeDown3 = period.getThreeDown3();
    String threeDown4 = period.getThreeDown4();
    String twoDown = period.getTwoDown();
    ArrayList<String> toteList = getTote(threeOn);
    ArrayList<String> numberOfWinner = new ArrayList<>(toteList);
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
          int buyDown = numberUtility.getInt(lotto.getBuyOn());
          int buyTote = numberUtility.getInt(lotto.getBuyTote());
          String numberLotto = lotto.getNumberLotto();

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
    updatePeriodBuyAndPay(period, userList);
    userList.forEach(user -> {
      List<Lotto> lottoSuccess = user.getLottos().stream().filter(lotto -> lotto.getPayTotal() > 0).collect(Collectors.toList());
      user.setLottos(lottoSuccess);
    });
    userList = userList.stream().filter(user -> !user.getLottos().isEmpty()).collect(Collectors.toList());
    return userList;
  }

  private void updatePeriodBuyAndPay(Period period, List<User> userList) {
    int pay = 0;
    for (User user : userList) {
      pay += user.getPay();
    }
    period.setPayTotal(pay);
    periodService.update(period);
  }

  private ArrayList<String> getTote(String tote) {
    ArrayList<Byte[]> predicateTote = getPredicateTote();
    HashMap<String, String> hmResult = new HashMap<>();
    ArrayList<String> arrayTote = new ArrayList<>();

    predicateTote.forEach(bytes -> {
      StringBuilder result = new StringBuilder();
      for (Byte aByte : bytes) {
        result.append(tote.charAt(aByte));
      }
      hmResult.put(result.toString(), result.toString());
    });

    hmResult.forEach((s, s2) -> arrayTote.add(s));
    return arrayTote;
  }

  private ArrayList<Byte[]> getPredicateTote() {
    ArrayList<Byte[]> list = new ArrayList<>();
    list.add(new Byte[]{0, 1, 2});
    list.add(new Byte[]{0, 2, 1});
    list.add(new Byte[]{1, 0, 2});
    list.add(new Byte[]{1, 2, 0});
    list.add(new Byte[]{2, 1, 0});
    list.add(new Byte[]{2, 0, 1});
    return list;
  }

}
