package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.nutfreedom.excel.ExcelFreedom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelService {

  private final PeriodService periodService;

  public void getFullLotto(Long id, HttpServletResponse response) {
    Period period = periodService.findById(id);

    StringBuilder table = new StringBuilder();
    table.append("<table>");
    table.append("<tr>");
    table.append("<td><background-color>grey_25_percent</background-color><width>15</width><format>border-right</format>เลข</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>บน</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>ล่าง</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>โต๊ด</td>");
    table.append("</tr>");

    for (User user : period.getUsers()) {
      for (Lotto lotto : user.getLottos()) {
        table.append("<tr>");
        table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getNumberLotto()).append("</td>");
        table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyOn()).append("</td>");
        table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyDown()).append("</td>");
        table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyTote()).append("</td>");
        table.append("</tr>");
      }
    }
    table.append("<tr>");
    table.append("<td><background-color>grey_25_percent</background-color><format>border-center-middle</format><height>600</height>รวมทั้งหมดที่ซื้อหักเปอร์เซ็นต์แล้ว</td>");
    table.append("<td><colspan>3</colspan><format>border-center-middle</format><type>number</type>").append(period.getBuyTotal()).append("</td>");
    table.append("</tr>");
    table.append("</table>");

    ExcelFreedom excelFreedom = new ExcelFreedom(response, null, "full", table.toString());
    excelFreedom.write();
  }

  public void getOptionLotto(Long id, int money, HttpServletResponse response) {
    List<Lotto> listMin = new ArrayList<>();
    List<Lotto> listMax = new ArrayList<>();
    Map<String, Integer> map = new LinkedHashMap<>();

    Period period = periodService.findById(id);
    for (User user : period.getUsers()) {
      for (Lotto lotto : user.getLottos()) {
        int buyTotal = lotto.getBuyTotal();
        String numberLotto = lotto.getNumberLotto();
        if (map.containsKey(numberLotto)) {
          map.put(numberLotto, map.get(numberLotto) + buyTotal);
        } else {
          map.put(numberLotto, buyTotal);
        }
      }
    }

    for (User user : period.getUsers()) {
      for (Lotto lotto : user.getLottos()) {
        String numberLotto = lotto.getNumberLotto();
        if (map.get(numberLotto) >= money) {
          listMax.add(lotto);
        } else {
          listMin.add(lotto);
        }
      }
    }

    StringBuilder table = new StringBuilder();
    table.append("<table>");
    table.append("<sheet>ขั้นต่ำ ").append(money).append("</sheet>");
    table.append("<tr>");
    table.append("<td><background-color>grey_25_percent</background-color><width>15</width><format>border-right</format>เลข</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>บน</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>ล่าง</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>โต๊ด</td>");
    table.append("</tr>");
    int sumMax = listMax.stream().mapToInt(Lotto::getBuyTotal).sum();
    for (Lotto lotto : listMax) {
      table.append("<tr>");
      table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getNumberLotto()).append("</td>");
      table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyOn()).append("</td>");
      table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyDown()).append("</td>");
      table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyTote()).append("</td>");
      table.append("</tr>");
    }
    table.append("<tr>");
    table.append("<td><background-color>grey_25_percent</background-color><format>border-center-middle</format><height>600</height>รวมทั้งหมดที่ซื้อหักเปอร์เซ็นต์แล้ว</td>");
    table.append("<td><colspan>3</colspan><format>border-center-middle</format><type>number</type>").append(sumMax).append("</td>");
    table.append("</tr>");
    table.append("</table>");

    table.append("<table>");
    table.append("<sheet>ต่ำกว่า ").append(money).append("</sheet>");
    table.append("<tr>");
    table.append("<td><background-color>grey_25_percent</background-color><width>15</width><format>border-right</format>เลข</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>บน</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>ล่าง</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width><format>border-right</format>โต๊ด</td>");
    table.append("</tr>");
    int sumMin = listMin.stream().mapToInt(Lotto::getBuyTotal).sum();
    for (Lotto lotto : listMin) {
      table.append("<tr>");
      table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getNumberLotto()).append("</td>");
      table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyOn()).append("</td>");
      table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyDown()).append("</td>");
      table.append("<td><format>border-right-middle</format><type>number</type>").append(lotto.getBuyTote()).append("</td>");
      table.append("</tr>");
    }
    table.append("<tr>");
    table.append("<td><background-color>grey_25_percent</background-color><format>border-center-middle</format><height>600</height>รวมทั้งหมดที่ซื้อหักเปอร์เซ็นต์แล้ว</td>");
    table.append("<td><colspan>3</colspan><format>border-center-middle</format><type>number</type>").append(sumMin).append("</td>");
    table.append("</tr>");
    table.append("</table>");
    ExcelFreedom excelFreedom = new ExcelFreedom(response, null, "option", table.toString());
    excelFreedom.write();
  }
}
