package com.github.nut077.lotto.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test product service")
@ExtendWith({MockitoExtension.class})
@Execution(ExecutionMode.CONCURRENT)
class CalLottoServiceTest {

  @Test
  void calLotto() {
  }
}