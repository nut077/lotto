package com.github.nut077.lotto.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class PeriodCreateDto {

  private Long id;

  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate periodDate;
}
