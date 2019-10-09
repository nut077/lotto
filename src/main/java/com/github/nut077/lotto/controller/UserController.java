package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.dto.PeriodCreateDto;
import com.github.nut077.lotto.dto.UserCreateDto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/users-lotto")
  public String usersLotto(@RequestParam Long id, Model model) {
    model.addAttribute("users", userService.findByPeriod(id));
    return "users-lotto";
  }

  @GetMapping("/create-user")
  public String createPeriod(@RequestParam Long id, Model model) {
    model.addAttribute("periodId", id);
    model.addAttribute("user", new User());
    return "create-user";
  }

  @PostMapping("/create-user")
  public String save(@ModelAttribute("user") UserCreateDto dto) {
    userService.createForm(dto);
    return "redirect:/";
  }

}
