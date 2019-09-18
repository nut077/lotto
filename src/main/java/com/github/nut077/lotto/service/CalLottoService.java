package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.utility.NumberUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
      List<Lotto> lotto = user.getLotto();
      Period periodResult = user.getPeriod();
      int pay = 0;
      if (lotto != null) {
        for (Lotto result : lotto) {
          if (numberUtility.getInt(result.getBuyOn()) > 0 && result.getNumberLotto().equals(threeOn)) {
            result.setPayOn(periodResult.getPayThreeOn() * result.getBuyOn());
          }
          if (numberUtility.getInt(result.getBuyOn()) > 0 && result.getNumberLotto().equals(twoOn)) {
            result.setPayOn(periodResult.getPayTwoOnDown() * result.getBuyOn());
          }
          if (numberUtility.getInt(result.getBuyDown()) > 0 && result.getNumberLotto().equals(threeDown1)
                  || result.getNumberLotto().equals(threeDown2) || result.getNumberLotto().equals(threeDown3)
                  || result.getNumberLotto().equals(threeDown4)) {
            result.setPayDown(periodResult.getPayThreeDown() * result.getBuyDown());
          }
          if (numberUtility.getInt(result.getBuyDown()) > 0 && result.getNumberLotto().equals(twoDown)) {
            result.setPayOn(periodResult.getPayTwoOnDown() * result.getBuyDown());
          }
          for (String t : toteList) {
            if (numberUtility.getInt(result.getBuyTote()) > 0 && result.getNumberLotto().equals(t)) {
              result.setPayTote(periodResult.getPayTote() * result.getBuyTote());
              break;
            }
          }
          result.setPayTotal(result.getPayOn() + result.getPayDown() + result.getPayTote());
          pay += result.getPayTotal();
        }
      }
      user.setPay(pay);
      userService.update(user.getId(), user);
    }
    return userService.getWinnerLotto(id, numberOfWinner);
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
    ArrayList<Byte[]> arrayList = new ArrayList<>();
    arrayList.add(new Byte[]{0, 1, 2});
    arrayList.add(new Byte[]{0, 2, 1});
    arrayList.add(new Byte[]{1, 0, 2});
    arrayList.add(new Byte[]{1, 2, 0});
    arrayList.add(new Byte[]{2, 1, 0});
    arrayList.add(new Byte[]{2, 0, 1});
    return arrayList;
  }

}
