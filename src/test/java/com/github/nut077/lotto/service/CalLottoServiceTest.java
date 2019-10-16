package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.mapper.PeriodCreateMapper;
import com.github.nut077.lotto.dto.mapper.UserCreateMapper;
import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.repository.LottoRepository;
import com.github.nut077.lotto.repository.PeriodRepository;
import com.github.nut077.lotto.repository.UserRepository;
import com.github.nut077.lotto.utility.NumberUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("Test cal lotto service")
@ExtendWith({MockitoExtension.class, StopwatchExtension.class})
@Execution(ExecutionMode.CONCURRENT)
class CalLottoServiceTest {

  private CalLottoService service;

  @Mock private PeriodRepository periodRepository;
  @Mock private UserRepository userRepository;
  @Mock private LottoRepository lottoRepository;

  private PeriodCreateMapper periodCreateMapper;
  private UserCreateMapper userCreateMapper;

  @BeforeEach
  void setUp() {
    PeriodService periodService = new PeriodService(periodRepository, periodCreateMapper);
    UserService userService = new UserService(userRepository, periodService, lottoRepository, userCreateMapper, periodCreateMapper);
    NumberUtility numberUtility = new NumberUtility();
    service = new CalLottoService(periodService, userService, numberUtility);
  }

  @Test
  void should_success_when_valid_value_call_calLotto() {
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

    User user = User.builder()
            .id(1L)
            .name("freedom")
            .buy(150)
            .period(period)
            .build();

    User user2 = User.builder()
            .id(2L)
            .name("eiei")
            .buy(300)
            .period(period)
            .lottos(Collections.singletonList(Lotto.builder()
                    .id(1L).numberLotto("356").buyOn(100).buyDown(100).buyTote(100).buyTotal(300)
                    .build()))
            .build();

    List<Lotto> lottoList = Arrays.asList(
            Lotto.builder().id(1L).numberLotto("365")
                    .buyOn(10)
                    .buyDown(10)
                    .buyTote(10)
                    .buyTotal(30)
                    .user(user)
                    .build(),
            Lotto.builder().id(2L).numberLotto("113")
                    .buyOn(10)
                    .buyDown(10)
                    .buyTote(10)
                    .buyTotal(30)
                    .user(user)
                    .build(),
            Lotto.builder().id(3L).numberLotto("974")
                    .buyOn(10)
                    .buyDown(10)
                    .buyTote(10)
                    .buyTotal(30)
                    .user(user)
                    .build(),
            Lotto.builder().id(4L).numberLotto("251")
                    .buyOn(10)
                    .buyDown(10)
                    .buyTote(10)
                    .buyTotal(30)
                    .user(user)
                    .build(),
            Lotto.builder().id(5L).numberLotto("776")
                    .buyOn(10)
                    .buyDown(10)
                    .buyTote(10)
                    .buyTotal(30)
                    .user(user)
                    .build(),
            Lotto.builder().id(6L).numberLotto("998")
                    .buyOn(10)
                    .buyDown(10)
                    .buyTote(10)
                    .buyTotal(30)
                    .user(user)
                    .build(),
            Lotto.builder().id(7L).numberLotto("65")
                    .buyOn(10)
                    .buyDown(10)
                    .buyTotal(20)
                    .user(user)
                    .build(),
            Lotto.builder().id(8L).numberLotto("74")
                    .buyOn(10)
                    .buyDown(10)
                    .buyTotal(20)
                    .user(user)
                    .build(),
            Lotto.builder().id(9L).numberLotto("32")
                    .buyOn(10)
                    .buyDown(10)
                    .buyTotal(20)
                    .user(user)
                    .build()
    );

    user.setLottos(lottoList);
    List<User> userList = Arrays.asList(user, user2);
    period.setUsers(userList);

    given(periodRepository.findById(anyLong())).willReturn(Optional.of(period));
    given(userRepository.queryWinnerLotto(anyLong(), anyList())).willReturn(userList);
    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
    given(userRepository.findById(anyLong())).willReturn(Optional.of(user2));

    // when
    List<User> actual = service.calLotto(1L);

    // then
    assertThat(actual, hasSize(2));
    assertThat(actual.get(0), hasProperty("buy", is(240)));
    assertThat(actual.get(0), hasProperty("pay", is(11100)));
    assertThat(actual.get(1), hasProperty("buy", is(300)));
    assertThat(actual.get(1), hasProperty("pay", is(9000)));
  }

  @Test
  void should_success_and_return_value_is_zero_when_not_lotto_value_call_calLotto() {
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

    User user = User.builder()
            .id(1L)
            .name("freedom")
            .buy(150)
            .period(period)
            .build();

    user.setLottos(null);
    List<User> userList = Collections.singletonList(user);
    period.setUsers(userList);

    given(periodRepository.findById(anyLong())).willReturn(Optional.of(period));
    given(userRepository.queryWinnerLotto(anyLong(), anyList())).willReturn(userList);
    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

    // when
    List<User> actual = service.calLotto(1L);

    // then
    assertThat(actual, hasSize(1));
    assertThat(actual.get(0), hasProperty("buy", is(0)));
    assertThat(actual.get(0), hasProperty("pay", is(0)));
  }
}