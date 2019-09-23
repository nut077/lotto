package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.service.PeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PeriodController {

  private final PeriodService periodService;

  @GetMapping("/")
  public String period(Model model) {
    model.addAttribute("periods", periodService.findAll());
    return "index";
  }

  @GetMapping("/createPeriod")
  public String createPeriod(Model model) {
    model.addAttribute("period", new Period());
    return "create-period";
  }
}
