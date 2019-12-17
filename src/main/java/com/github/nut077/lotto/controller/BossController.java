package com.github.nut077.lotto.controller;

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
public class BossController {

  private final PeriodService periodService;

  @GetMapping("/send-boss")
  public String sendBoss(@RequestParam Long id, Model model) {
    model.addAttribute("period", periodService.findById(id));
    return "send-boss";
  }

  @PostMapping("/send-boss")
  public String saveSendBoss(@ModelAttribute("period") Period period) {
    periodService.savedSendBoss(period);
    return "redirect:/";
  }
}
