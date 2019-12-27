package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.PeriodDto;
import com.github.nut077.lotto.dto.mapper.PeriodMapper;
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
  private final PeriodMapper mapper;

  public List<Period> findAll() {
    return periodRepository.findAll(Sort.by(Sort.Direction.DESC, "periodDate"));
  }

  public Period findById(Long id) {
    return periodRepository.findById(id).orElseThrow(() -> new NotFoundException("Period id: " + id + " -->> Not Found"));
  }

  public Period createForm(PeriodDto dto) {
    return periodRepository.save(mapper.mapToEntity(dto));
  }

  public void updateUpdateForm(Long id, PeriodDto dto) {
    Period period = findById(id);
    periodRepository.save(mapper.mapToEntity(dto, period));
  }

  public Period update(Long id, Period period) {
    findById(id);
    period.setId(id);
    return periodRepository.save(period);
  }

  public Period update(Period period) {
    return periodRepository.save(period);
  }

  public void delete(Long id) {
    periodRepository.deleteById(id);
  }

  public void savedSendBoss(Period period) {
    Period p = findById(period.getId());
    p.setPayBoss(period.getPayBoss());
    p.setPayReal(p.getBuyTotal() - p.getPayTotal() - period.getPayBoss());
    update(period.getId(), p);
  }
}
