package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalLottoService {

  private final PeriodService periodService;
  private final UserService userService;

  public void calLotto(Long id) {
    Period period = periodService.findById(id);
    String threeOn = period.getThreeOn();
    String twoOn = period.getTwoOn();
    String threeDown1 = period.getThreeDown1();
    String threeDown2 = period.getThreeDown2();
    String threeDown3 = period.getThreeDown3();
    String threeDown4 = period.getThreeDown4();
    String twoDown = period.getTwoDown();
    ArrayList<String> toteList = getTote(threeOn);
    List<User> winnerLotto = userService.getWinnerLotto(id, threeOn, twoOn, threeDown1, threeDown2, threeDown3, threeDown4, twoDown, toteList);
    for (User lotto : winnerLotto) {

    }
  }

  private ArrayList<String> getTote(String tote) {
    LinkedHashMap<Integer, Byte[]> hmPredicate = getPredicateTote();
    HashMap<String, String> hmResult = new HashMap<>();
    ArrayList<String> arrayTote = new ArrayList<>();
    for (Integer key : hmPredicate.keySet()) {
      Byte[] bytes = hmPredicate.get(key);
      StringBuilder result = new StringBuilder();
      for (Byte aByte : bytes) {
        result.append(tote.charAt(aByte));
      }
      hmResult.put(result.toString(), result.toString());
    }
    hmResult.forEach((s, s2) -> arrayTote.add(s));
    return arrayTote;
  }

  private LinkedHashMap<Integer, Byte[]> getPredicateTote() {
    LinkedHashMap<Integer, Byte[]> hmPredicate = new LinkedHashMap<>();
    hmPredicate.put(1, new Byte[]{0, 1, 2});
    hmPredicate.put(2, new Byte[]{0, 2, 1});
    hmPredicate.put(3, new Byte[]{1, 0, 2});
    hmPredicate.put(4, new Byte[]{1, 2, 0});
    hmPredicate.put(5, new Byte[]{2, 1, 0});
    hmPredicate.put(6, new Byte[]{2, 0, 1});
    return hmPredicate;
  }
}
