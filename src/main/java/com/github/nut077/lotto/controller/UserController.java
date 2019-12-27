package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.dto.UserDto;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/users-lotto")
  public String usersLotto(@RequestParam Long id, Model model) {
    model.addAttribute("users", userService.findByPeriod(id));
    model.addAttribute("period", id);
    return "users-lotto";
  }

  @GetMapping("/users-lotto")
  public String usersLottoGet(@RequestParam Long id, Model model) {
    model.addAttribute("users", userService.findByPeriod(id));
    model.addAttribute("period", id);
    return "users-lotto";
  }

  @GetMapping("/create-user")
  public String createPeriod(@RequestParam Long id, Model model) {
    model.addAttribute("period", id);
    model.addAttribute("user", new User());
    return "create-user";
  }

  @PostMapping("/update-user/{periodId}")
  public String update(@PathVariable Long periodId, @RequestParam Long id, UserDto dto) {
    userService.updateUpdateForm(id, dto);
    return "redirect:/users-lotto?id=" + periodId;
  }

  @PostMapping("/show-update-user")
  public String showUpdate(@RequestParam Long id, Model model) {
    User user = userService.findById(id);
    model.addAttribute("user", user);
    model.addAttribute("period", user.getPeriod().getId());
    return "update-user";
  }

  @PostMapping("/create-user/{periodId}")
  public String save(@PathVariable Long periodId, @ModelAttribute("user") UserDto dto) {
    UserDto user = userService.create(periodId, dto);
    return "redirect:/lotto-detail/" + user.getId();
  }

  @PostMapping("/delete-user/{periodId}")
  public String delete(@PathVariable Long periodId, @RequestParam Long id) {
    userService.delete(id);
    return "redirect:/users-lotto?id=" + periodId;
  }

  @PostMapping("/checkDuplicateName")
  public @ResponseBody String checkDuplicateName(@RequestParam Long periodId, @RequestParam String name) {
    return String.valueOf(userService.checkDuplicateName(periodId, name));
  }

  @GetMapping("/import-lotto")
  public String showImportLotto(@RequestParam Long id, Model model) {
    model.addAttribute("period", id);
    return "import-lotto";
  }

  @PostMapping("/import-lotto/{periodId}")
  public String importLotto(@PathVariable Long periodId, @RequestParam("lottoFile") MultipartFile multipartFile, @RequestParam String name) {
    userService.importLotto(periodId, multipartFile, name);
    return "redirect:/users-lotto?id=" + periodId;
  }

}
