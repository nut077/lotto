package com.github.nut077.lotto.controller;

import com.github.nut077.lotto.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class ExcelController {

  private final ExcelService excelService;

  @PostMapping("/download-excel")
  public String download(@RequestParam Long id, Model model) {
    model.addAttribute("periodId", id);
    return "download-excel";
  }

  @GetMapping("/download/full/{id}")
  public String download(@PathVariable Long id, HttpServletResponse response) {
    excelService.getFullLotto(id, response);
    return "redirect:/download-excel?id=" + id;
  }

  @GetMapping("/download/option/{id}/{money}")
  public String downloadOption(@PathVariable Long id, @PathVariable int money, HttpServletResponse response) {
    excelService.getOptionLotto(id, money, response);
    return "redirect:/download-excel?id=" + id;
  }
}
