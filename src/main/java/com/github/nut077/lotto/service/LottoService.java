package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.repository.LottoRepository;
import org.springframework.stereotype.Service;

@Service
public class LottoService extends BaseService<Lotto, Long> {

  private final LottoRepository lottoRepository;

  public LottoService(LottoRepository lottoRepository) {
    super(lottoRepository);
    this.lottoRepository = lottoRepository;
  }
}
