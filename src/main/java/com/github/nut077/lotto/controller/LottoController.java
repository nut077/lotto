package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.service.LottoService;
import com.github.nut077.lotto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LottoController {

  private final LottoService lottoService;
  private final UserService userService;

  @GetMapping("/lotto")
  public String lotto(@RequestParam Long id, Model model) {
    model.addAttribute("user", userService.findById(id));
    return "lotto";
  }
}
