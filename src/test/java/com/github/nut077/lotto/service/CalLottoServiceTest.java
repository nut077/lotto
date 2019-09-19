package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.repository.PeriodRepository;
import com.github.nut077.lotto.repository.UserRepository;
import com.github.nut077.lotto.utility.NumberUtility;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test cal lotto service")
@ExtendWith({MockitoExtension.class})
@Execution(ExecutionMode.CONCURRENT)
class CalLottoServiceTest {

  private PeriodService periodService;
  private UserService userService;
  private NumberUtility numberUtility;

  @Mock
  private PeriodRepository periodRepository;

  @Mock
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    this.periodService = new PeriodService(periodRepository);
    this.userService = new UserService(userRepository);
    this.numberUtility = new NumberUtility();
  }

  @Test
  void calLotto() {
    // given
    Period period = Period.builder()
            .id(1L)
            .periodDate(LocalDate.now())
            .threeOn("365")
            .twoOn("65")
            .threeDown1("113")
            .threeDown2("974")
            .threeDown3("251")
            .threeDown4("776")
            .twoDown("74")
            .payThreeOn(500)
            .payTwoOn(60)
            .payThreeDown1(100)
            .payThreeDown2(100)
            .payThreeDown3(100)
            .payThreeDown4(100)
            .payTwoDown(60)
            .payTote(90)
            .build();
    // when

    // then
  }
}