package com.github.nut077.lotto.entity.attribute;

import com.github.nut077.lotto.entity.Lotto;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply = true)
public class LottPercentConverter implements AttributeConverter<Lotto.Percent, String> {

  // convert จาก enum ให้เปลี่ยนเป็นค่าที่จะ save ลงใน database
  @Override
  public String convertToDatabaseColumn(Lotto.Percent percent) {
    return Objects.isNull(percent) ? null : percent.getCode();
  }

  // ของจาก database ให้ convert เป้น enum อะไรที่ต้องการ
  @Override
  public Lotto.Percent convertToEntityAttribute(String code) {
    return Objects.isNull(code) ? null : Lotto.Percent.codeToPercent(code);
  }
}
