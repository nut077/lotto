package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.repository.PeriodRepository;
import org.springframework.stereotype.Service;

@Service
public class PeriodService extends BaseService<Period, Long> {

  private final PeriodRepository periodRepository;

  public PeriodService(PeriodRepository periodRepository) {
    super(periodRepository);
    this.periodRepository = periodRepository;
  }
}
