package com.github.nut077.lotto.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PeriodCreateDto {

  private Long id;
  private LocalDate periodDate;
}
