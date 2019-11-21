package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.dto.PeriodCreateDto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.service.ExcelService;
import com.github.nut077.lotto.service.PeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class PeriodController {

  private final PeriodService periodService;
  private final ExcelService excelService;

  @GetMapping("/")
  public String period(Model model) {
    model.addAttribute("periods", periodService.findAll());
    return "index";
  }

  @GetMapping("/create-period")
  public String createPeriod(Model model) {
    model.addAttribute("period", new Period());
    return "create-period";
  }

  @PostMapping("/create-period")
  public String save(@ModelAttribute("period") PeriodCreateDto dto) {
    Period period = periodService.createForm(dto);
    return "redirect:/users-lotto?id=" + period.getId();
  }

  @PostMapping("/show-update-period")
  public String showUpdate(@RequestParam Long id, Model model) {
    model.addAttribute("period", periodService.findById(id));
    return "update-period";
  }

  @PostMapping("/update-period")
  public String update(@RequestParam Long id, PeriodCreateDto dto) {
    periodService.updateUpdateForm(id, dto);
    return "redirect:/";
  }

  @PostMapping("/delete-period")
  public String delete(@RequestParam Long id) {
    periodService.delete(id);
    return "redirect:/";
  }

  @PostMapping("/download-excel")
  public String download(@RequestParam Long id, Model model) {
    model.addAttribute("periodId", id);
    return "download-excel";
  }

  @GetMapping("/download/full/{id}")
  public String download(@PathVariable Long id, HttpServletResponse response) {
    excelService.getFullLotto(id, response);
    return "redirect:/download-excel?id=" + id;
  }
}
