package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.service.CalLottoService;
import com.github.nut077.lotto.service.PeriodService;
import com.github.nut077.lotto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LottoController {

  private final UserService userService;
  private final PeriodService periodService;
  private final CalLottoService calLottoService;

  @GetMapping("/lotto-detail/{userId}")
  public String lottoDetail(@PathVariable Long userId, Model model) {
    User user = userService.findById(userId);
    long maxId = user.getLottos().stream().mapToLong(Lotto::getId).max().orElse(0);
    model.addAttribute("user", user);
    model.addAttribute("maxId", maxId);
    return "lotto-detail";
  }

  @PostMapping("/lotto-detail/{userId}")
  public String saveLottoDetail(@PathVariable Long userId, HttpServletRequest request) {
    User user = userService.createLotto(userId, request);
    return "redirect:/users-lotto?id=" + user.getPeriod().getId();
  }

  @PostMapping("/check-lotto")
  public String checkLotto(@RequestParam("id") Long periodId, Model model) {
    model.addAttribute("period", periodService.findById(periodId));
    return "lotto";
  }

  @PostMapping("/show-check-lotto")
  public String showCheckLotto(@RequestParam("id") Long periodId, Model model) {
    model.addAttribute("user", calLottoService.calLotto(periodId));
    return "show-lotto";
  }
}
