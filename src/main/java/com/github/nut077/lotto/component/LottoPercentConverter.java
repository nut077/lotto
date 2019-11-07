package com.github.nut077.lotto.component;

import com.github.nut077.lotto.entity.Lotto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LottoPercentConverter implements Converter<String, Lotto.Percent> {

  @Override
  public Lotto.Percent convert(String code) {
    return Lotto.Percent.codeToPercent(code);
  }
}
