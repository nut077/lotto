package com.github.nut077.lotto.repository;

import com.github.nut077.lotto.entity.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LottoRepository extends JpaRepository<Lotto, Long> {
}
