package com.github.nut077.lotto.repository;

import com.github.nut077.lotto.entity.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LottoRepository extends JpaRepository<Lotto, Long> {

  @Modifying
  @Query(nativeQuery = true, value = "delete from lottos l where l.user_id=:userId")
  void deleteLottoByUserId(@Param("userId") Long userId);
}
