package com.github.nut077.lotto.utility;

import org.springframework.stereotype.Component;

@Component
public class NumberUtility {

  public int getInt(Integer num) {
    if (num != null) {
      return num;
    }
    return 0;
  }
}
