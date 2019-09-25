package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.PeriodCreateDto;
import com.github.nut077.lotto.dto.mapper.PeriodCreateMapper;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.exception.NotFoundException;
import com.github.nut077.lotto.repository.PeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodService {

  private final PeriodRepository periodRepository;
  private final PeriodCreateMapper mapper;

  public List<Period> findAll() {
    return periodRepository.findAll(Sort.by(Sort.Direction.DESC, "periodDate"));
  }

  public Period findById(Long id) {
    return periodRepository.findById(id).orElseThrow(() -> new NotFoundException("Period id: " + id + " -->> Not Found"));
  }

  public PeriodCreateDto createForm(PeriodCreateDto dto) {
    return mapper.mapToDto(periodRepository.saveAndFlush(mapper.mapToEntity(dto)));
  }

  public PeriodCreateDto updateUpdateForm(Long id, PeriodCreateDto dto) {
    findById(id);
    dto.setId(id);
    return createForm(dto);
  }

  public Period update(Long id, Period period) {
    findById(id);
    period.setId(id);
    return periodRepository.save(period);
  }
}
