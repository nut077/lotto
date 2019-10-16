package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.utility.NumberUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CalLottoService {

  private final PeriodService periodService;
  private final UserService userService;
  private final NumberUtility numberUtility;

  public List<User> calLotto(Long id) {
    Period period = periodService.findById(id);
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
    List<User> userWinnerLotto = userService.getWinnerLotto(id, numberOfWinner);
    for (User user : userWinnerLotto) {
      List<Lotto> lottoList = user.getLottos();
      Period periodResult = user.getPeriod();
      int pay = 0;
      int buy = 0;
      if (lottoList != null) {
        for (Lotto lotto : lottoList) {
          int buyOn = numberUtility.getInt(lotto.getBuyOn());
          int buyDown = numberUtility.getInt(lotto.getBuyOn());
          int buyTote = numberUtility.getInt(lotto.getBuyTote());
          String numberLotto = lotto.getNumberLotto();

          if (numberLotto.equals(threeOn)) {
            lotto.setPayOn(periodResult.getPayThreeOn() * buyOn);
          }
          if (numberLotto.equals(twoOn)) {
            lotto.setPayOn(periodResult.getPayTwoOn() * buyOn);
          }
          if (numberLotto.equals(threeDown1)) {
            lotto.setPayDown(periodResult.getPayThreeDown1() * buyDown);
          }
          if (numberLotto.equals(threeDown2)) {
            lotto.setPayDown(periodResult.getPayThreeDown2() * buyDown);
          }
          if (numberLotto.equals(threeDown3)) {
            lotto.setPayDown(periodResult.getPayThreeDown3() * buyDown);
          }
          if (numberLotto.equals(threeDown4)) {
            lotto.setPayDown(periodResult.getPayThreeDown4() * buyDown);
          }
          if (numberLotto.equals(twoDown)) {
            lotto.setPayDown(periodResult.getPayTwoDown() * buyDown);
          }
          if (toteList.contains(numberLotto)) {
            lotto.setPayTote(periodResult.getPayTote() * buyTote);
          }

          buy += lotto.getBuyTotal();
          lotto.setPayTotal(numberUtility.getInt(lotto.getPayOn()) + numberUtility.getInt(lotto.getPayDown()) + numberUtility.getInt(lotto.getPayTote()));
          pay += lotto.getPayTotal();
        }
      }
      user.setBuy(buy);
      user.setPay(pay);
      userService.update(user.getId(), user);
    }
    List<User> userList = userService.getWinnerLotto(id, numberOfWinner);
    updatePeriodBuyAndPay(period, userList);
    return userList;
  }

  private void updatePeriodBuyAndPay(Period period, List<User> userList) {
    int buy = 0;
    int pay = 0;
    for (User user : userList) {
      buy += user.getBuy();
      pay += user.getPay();
    }
    period.setBuyTotal(buy);
    period.setPayTotal(pay);
    periodService.update(period.getId(), period);
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
