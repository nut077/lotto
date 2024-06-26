package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.nutfreedom.excel.ExcelFreedom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelService {

  private final PeriodService periodService;

  public void getFullLotto(Long id, HttpServletResponse response) {
    Period period = periodService.findById(id);

    List<Lotto> lottoOne = new ArrayList<>();
    List<Lotto> lottoTwo = new ArrayList<>();
    List<Lotto> lottoThree = new ArrayList<>();
    setLotto(period, lottoOne, lottoTwo, lottoThree);

    StringBuilder table = new StringBuilder();
    table.append("<table>");
    appendTableHeader(table);

    lottoOne
      .stream()
      .sorted(Comparator.comparing(Lotto::getNumberLotto))
      .collect(Collectors.toList())
      .forEach(lotto -> appendTableDetail(table, lotto));

    lottoTwo
      .stream()
      .sorted(Comparator.comparing(Lotto::getNumberLotto))
      .collect(Collectors.toList())
      .forEach(lotto -> appendTableDetail(table, lotto));

    lottoThree
      .stream()
      .sorted(Comparator.comparing(Lotto::getNumberLotto))
      .collect(Collectors.toList())
      .forEach(lotto -> appendTableDetail(table, lotto));

    table.append("</table>");

    ExcelFreedom excelFreedom = new ExcelFreedom(response, "full", table.toString());
    excelFreedom.write();
  }

  public void getFullSumLotto(Long id, HttpServletResponse response) {
    Period period = periodService.findById(id);

    List<Lotto> lottoOne = new ArrayList<>();
    List<Lotto> lottoTwo = new ArrayList<>();
    List<Lotto> lottoThree = new ArrayList<>();
    setLotto(period, lottoOne, lottoTwo, lottoThree);

    StringBuilder table = new StringBuilder();
    table.append("<table>");
    appendTableHeader(table);

    Map<String, Lotto> mapOne = new LinkedHashMap<>();
    setSumLotto(lottoOne, mapOne);

    Map<String, Lotto> mapTwo = new LinkedHashMap<>();
    setSumLotto(lottoTwo, mapTwo);

    Map<String, Lotto> mapThree = new LinkedHashMap<>();
    setSumLotto(lottoThree, mapThree);

    mapOne.values().forEach(value -> appendTableDetail(table, value));
    mapTwo.values().forEach(value -> appendTableDetail(table, value));
    mapThree.values().forEach(value -> appendTableDetail(table, value));

    table.append("</table>");

    ExcelFreedom excelFreedom = new ExcelFreedom(response, "fullsum", table.toString());
    excelFreedom.write();
  }

  public void getOverLimitLotto(Long id, int overThreeOn, int overThreeDown, int overTwoOn, int overTwoDown,
                                int overTote, int overOneOn, int overOneDown, HttpServletResponse response) {
    Period period = periodService.findById(id);

    List<Lotto> lottoOne = new ArrayList<>();
    List<Lotto> lottoTwo = new ArrayList<>();
    List<Lotto> lottoThree = new ArrayList<>();
    setLotto(period, lottoOne, lottoTwo, lottoThree);

    StringBuilder table = new StringBuilder();
    table.append("<table>");
    appendTableHeader(table);

    Map<String, Lotto> mapOne = new LinkedHashMap<>();
    setSumLotto(lottoOne, mapOne);

    Map<String, Lotto> mapOverOne = new LinkedHashMap<>();
    setOverLimit(mapOne, mapOverOne, overOneOn, overOneDown, overTote);

    Map<String, Lotto> mapTwo = new LinkedHashMap<>();
    setSumLotto(lottoTwo, mapTwo);

    Map<String, Lotto> mapOverTwo = new LinkedHashMap<>();
    setOverLimit(mapTwo, mapOverTwo, overTwoOn, overTwoDown, overTote);

    Map<String, Lotto> mapThree = new LinkedHashMap<>();
    setSumLotto(lottoThree, mapThree);

    Map<String, Lotto> mapOverThree = new LinkedHashMap<>();
    setOverLimit(mapThree, mapOverThree, overThreeOn, overThreeDown, overTote);

    mapOverOne.values().forEach(value -> {
        if (value.getBuyOn() + value.getBuyDown() + value.getBuyTote() != 0) {
          appendTableDetail(table, value);
        }
      }
    );

    mapOverTwo.values().forEach(value -> {
        if (value.getBuyOn() + value.getBuyDown() + value.getBuyTote() != 0) {
          appendTableDetail(table, value);
        }
      }
    );
    mapOverThree.values().forEach(value -> appendTableDetail(table, value));

    table.append("</table>");

    ExcelFreedom excelFreedom = new ExcelFreedom(response, "overLimit", table.toString());
    excelFreedom.write();
  }

  private void setLotto(Period period, List<Lotto> lottoOne, List<Lotto> lottoTwo, List<Lotto> lottoThree) {
    for (User user : period.getUsers()) {
      for (Lotto lotto : user.getLottos()) {
        lotto.setUsers(Collections.singletonList(lotto.getUser().getName()));
        String numberLotto = lotto.getNumberLotto();
        if (numberLotto.length() == 1) {
          lottoOne.add(lotto);
        } else if (numberLotto.length() == 2) {
          lottoTwo.add(lotto);
        } else if (numberLotto.length() == 3) {
          lottoThree.add(lotto);
        }
      }
    }
  }

  private void setSumLotto(List<Lotto> lottos, Map<String, Lotto> mapLotto) {
    lottos
      .stream()
      .sorted(Comparator.comparing(Lotto::getNumberLotto))
      .collect(Collectors.toList())
      .forEach(lotto -> {
        String numberLotto = lotto.getNumberLotto();
        if (mapLotto.containsKey(numberLotto)) {
          Lotto lottoInMap = mapLotto.get(numberLotto);
          lottoInMap.setBuyOn(lotto.getBuyOn() + lottoInMap.getBuyOn());
          lottoInMap.setBuyDown(lotto.getBuyDown() + lottoInMap.getBuyDown());
          lottoInMap.setBuyTote(lotto.getBuyTote() + lottoInMap.getBuyTote());
          List<String> users = lottoInMap.getUsers();
          String name = lotto.getUser().getName();
          if (!users.contains(name)) {
            users.add(name);
          }
          lottoInMap.setUsers(users);
          mapLotto.put(numberLotto, lottoInMap);
        } else {
          List<String> users = new ArrayList<>();
          users.add(lotto.getUser().getName());
          lotto.setUsers(users);
          mapLotto.put(numberLotto, lotto);
        }
      });
  }

  private void setOverLimit(Map<String, Lotto> mapLotto, Map<String, Lotto> mapOver,
                            int overOn, int overDown, int overTote) {
    mapLotto.values().forEach(lotto -> {
      String numberLotto = lotto.getNumberLotto();
      if (lotto.getBuyOn() > overOn) {
        lotto.setBuyOn(lotto.getBuyOn() - overOn);
      } else {
        lotto.setBuyOn(0);
      }
      if (lotto.getBuyDown() > overDown) {
        lotto.setBuyDown(lotto.getBuyDown() - overDown);
      } else {
        lotto.setBuyDown(0);
      }
      if (lotto.getBuyTote() > overTote) {
        lotto.setBuyTote(lotto.getBuyTote() - overTote);
      } else {
        lotto.setBuyTote(0);
      }
      mapOver.put(numberLotto, lotto);
    });
  }

  private void appendTableHeader(StringBuilder table) {
    table.append("<tr>");
    table.append("<td><background-color>grey_25_percent</background-color><width>15</width><format>border-right</format>เลข</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>บน</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>ล่าง</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>โต๊ด</td>");
    table.append("<td><background-color>grey_25_percent</background-color><format>border-center</format>ชื่อ</td>");
    table.append("</tr>");
  }

  private void appendTableDetail(StringBuilder table, Lotto lotto) {
    table.append("<tr>");
    table.append("<td><format>border-right-middle</format>").append(lotto.getNumberLotto()).append("</td>");
    table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyOn()).append("</td>");
    table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyDown()).append("</td>");
    table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyTote()).append("</td>");
    table.append("<td><format>border-left-middle</format>").append(StringUtils.join(lotto.getUsers(), ",")).append("</td>");
    table.append("</tr>");
  }
}
