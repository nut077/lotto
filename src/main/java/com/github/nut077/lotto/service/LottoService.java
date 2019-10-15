package com.github.nut077.lotto.service;

import com.github.nut077.lotto.repository.LottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LottoService {

  private final LottoRepository lottoRepository;
}
