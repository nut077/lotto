package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.nutfreedom.excel.ExcelFreedom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class ExcelService {

  private final PeriodService periodService;

  public void getFullLotto(Long id, HttpServletResponse response) {
    StringBuilder table = new StringBuilder();
    table.append("<table>");
    table.append("<tr>");
    table.append("<td><background-color>grey_25_percent</background-color><width>15</width>เลข</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width>บน</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width>ล่าง</td>");
    table.append("<td><background-color>grey_25_percent</background-color><width>10</width>โต๊ด</td>");
    table.append("</tr>");
    Period period = periodService.findById(id);

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
}
