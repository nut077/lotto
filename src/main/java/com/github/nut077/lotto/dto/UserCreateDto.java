package com.github.nut077.lotto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDto {

  private Long id;
  private String name;
  private PeriodCreateDto period;
}
