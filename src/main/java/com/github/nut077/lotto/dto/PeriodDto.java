package com.github.nut077.lotto.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class PeriodDto {

  private Long id;

  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate periodDate;

  private String threeOn;
  private String twoOn;
  private String threeDown1;
  private String threeDown2;
  private String threeDown3;
  private String threeDown4;
  private String twoDown;

  private int payThreeOn;
  private int payTwoOn;
  private int payThreeDown1;
  private int payThreeDown2;
  private int payThreeDown3;
  private int payThreeDown4;
  private int payTwoDown;
  private int payTote;
  private int payRunOn;
  private int payRunDown;
}
