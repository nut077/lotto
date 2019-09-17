package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.service.PeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PeriodController {

  private final PeriodService periodService;

  @GetMapping("/")
  public String period() {
    return "index";
  }
}
