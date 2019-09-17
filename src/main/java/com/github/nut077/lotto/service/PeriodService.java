package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.repository.PeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodService {

  private final PeriodRepository periodRepository;

  public List<Period> findAll() {
    return periodRepository.findAll();
  }
}
