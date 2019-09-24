package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.dto.PeriodCreateDto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.service.PeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
  public String save(@ModelAttribute("period") PeriodCreateDto period) {
    periodService.createForm(period);
    return "redirect:/";
  }
}
