package com.github.nut077.lotto.dto.mapper;

import com.github.nut077.lotto.dto.PeriodResultDto;
import com.github.nut077.lotto.entity.Period;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
  injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PeriodResultMapper extends BaseMapper<Period, PeriodResultDto> {
}
