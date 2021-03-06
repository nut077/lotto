package com.github.nut077.lotto.repository;

import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query(nativeQuery = true,
          value = "select u.* from users u where u.id in(select l.user_id from lottos l where" +
                  " (l.number_lotto in(:numberOfWinner))" +
                  " and u.period_id=:periodId)")
  List<User> queryWinnerLotto(@Param("periodId") Long periodId, @Param("numberOfWinner") List<String> numberOfWinner);

  List<User> findByPeriod(Period period);
}
