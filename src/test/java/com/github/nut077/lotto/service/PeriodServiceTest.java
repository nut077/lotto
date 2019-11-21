package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.PeriodCreateDto;
import com.github.nut077.lotto.dto.mapper.PeriodCreateMapper;
import com.github.nut077.lotto.dto.mapper.PeriodCreateMapperImpl;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.repository.PeriodRepository;
import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Test period service")
@ExtendWith({MockitoExtension.class, StopwatchExtension.class})
@Execution(ExecutionMode.CONCURRENT)
class PeriodServiceTest implements WithBDDMockito {

  @Mock
  private PeriodRepository periodRepository;

  private PeriodCreateMapper mapper;

  private PeriodService periodService;

  @BeforeEach
  void setUp() {
    mapper = new PeriodCreateMapperImpl();
    periodService = new PeriodService(periodRepository, mapper);
  }

  @Test
  void should_success_when_findAll() {
    // given
    LocalDate now = LocalDate.now();
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    List<Period> periods = Arrays.asList(
      Period.builder().periodDate(tomorrow).build(),
      Period.builder().periodDate(now).build());

    given(periodRepository.findAll(Sort.by(Sort.Direction.DESC, "periodDate"))).willReturn(periods);

    // when
    List<Period> actual = periodService.findAll();
    // then
    assertThat(actual, hasSize(2));
    assertThat(actual.get(0), hasProperty("periodDate", is(tomorrow)));
  }

  @Test
  void should_success_when_findById() {
    // given
    LocalDate now = LocalDate.now();
    given(periodRepository.findById(anyLong())).willReturn(Optional.of(Period.builder().id(1L).periodDate(now).build()));

    // when
    Period actual = periodService.findById(1L);

    // then
    assertThat(actual, hasProperty("id", is(1L)));
    assertThat(actual, hasProperty("periodDate", is(now)));
  }

  @Test
  void should_success_when_createForm() {
    // given
    LocalDate now = LocalDate.now();
    PeriodCreateDto dto = new PeriodCreateDto();
    dto.setId(1L);
    dto.setPeriodDate(now);

    given(periodRepository.save(any(Period.class))).willReturn(mapper.mapToEntity(dto));

    // when
    Period actual = periodService.createForm(dto);

    // then
    assertThat(actual, hasProperty("id", is(1L)));
  }

  @Test
  void should_success_when_updateUpdateForm() {
    // given
    LocalDate now = LocalDate.now();
    PeriodCreateDto dto = new PeriodCreateDto();
    dto.setId(1L);
    dto.setPeriodDate(now);
    given(periodRepository.findById(anyLong())).willReturn(Optional.of(mapper.mapToEntity(dto)));
    given(periodRepository.save(any(Period.class))).willReturn(mapper.mapToEntity(dto));

    // when
    periodService.updateUpdateForm(1L, dto);

    then(periodRepository).should(times(1)).save(any(Period.class));
  }

  @Test
  void should_success_when_update_by_id() {
    // given
    LocalDate now = LocalDate.now();
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    PeriodCreateDto dto = new PeriodCreateDto();
    dto.setId(1L);
    dto.setPeriodDate(now);
    Period periodSaved = Period.builder().id(1L).periodDate(tomorrow).build();
    given(periodRepository.findById(anyLong())).willReturn(Optional.of(mapper.mapToEntity(dto)));
    given(periodRepository.save(any(Period.class))).willReturn(periodSaved);

    // when
    Period actual = periodService.update(1L, periodSaved);

    // then
    assertThat(actual, hasProperty("periodDate", is(tomorrow)));
  }

  @Test
  void should_success_when_update_by_period() {
    // given
    LocalDate now = LocalDate.now();
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    Period period = Period.builder().id(1L).periodDate(now).build();
    Period periodSaved = Period.builder().id(1L).periodDate(tomorrow).build();
    given(periodRepository.save(any(Period.class))).willReturn(periodSaved);

    // when
    Period actual = periodService.update(periodSaved);

    // then
    assertThat(actual, hasProperty("periodDate", is(tomorrow)));
  }

  @Test
  void should_success_when_delete() {
    // given
    willDoNothing().given(periodRepository).deleteById(anyLong());

    // when
    periodService.delete(anyLong());

    // then
    then(periodRepository).should(times(1)).deleteById(anyLong());
    then(periodRepository).should(never()).deleteAll();
  }
}