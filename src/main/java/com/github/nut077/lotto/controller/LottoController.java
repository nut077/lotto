package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LottoController {

  private final UserService userService;

  @GetMapping("/lotto")
  public String lotto(@RequestParam Long id, Model model) {
    model.addAttribute("user", userService.findById(id));
    return "lotto";
  }

  @GetMapping("/lotto-detail/{userId}")
  public String lottoDetail(@PathVariable Long userId, Model model) {
    User user = userService.findById(userId);
    long maxId = user.getLottos().stream().mapToLong(Lotto::getId).max().orElse(0);
    model.addAttribute("user", userService.findById(userId));
    model.addAttribute("maxId", maxId);
    return "lotto-detail";
  }

  @PostMapping("/lotto-detail/{userId}")
  public String saveLottoDetail(@PathVariable Long userId, @RequestParam String detail, @RequestParam String line) {
    User user = userService.update(userId, detail, line);
    return "redirect:/users-lotto?id=" + user.getPeriod().getId();
  }
}
