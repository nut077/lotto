package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.dto.PeriodDto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.service.PeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PeriodController {

  private final PeriodService periodService;

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
  public String save(@ModelAttribute("period") PeriodDto dto) {
    Period period = periodService.createForm(dto);
    return "redirect:/users-lotto?id=" + period.getId();
  }

  @PostMapping("/show-update-period")
  public String showUpdate(@RequestParam Long id, Model model) {
    model.addAttribute("period", periodService.findById(id));
    return "update-period";
  }

  @PostMapping("/update-period")
  public String update(@RequestParam Long id, PeriodDto dto) {
    periodService.updateUpdateForm(id, dto);
    return "redirect:/";
  }

  @PostMapping("/delete-period")
  public String delete(@RequestParam Long id) {
    periodService.delete(id);
    return "redirect:/";
  }
}
