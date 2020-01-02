package com.github.nut077.lotto.dto.mapper;

import com.github.nut077.lotto.dto.PeriodDto;
import com.github.nut077.lotto.entity.Period;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
  injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PeriodMapper extends BaseMapper<Period, PeriodDto> {
}
