package com.github.nut077.lotto.repository;

import com.github.nut077.lotto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query(nativeQuery = true,
          value = "select u.* from users u where u.user_id in(select l.user_id from lottos l where" +
                  " (l.number_lotto=:threeOn or l.number_lotto=:twoOn or l.number_lotto=:threeDown1 or l.number_lotto=:threeDown2" +
                  " or l.number_lotto=:threeDown3 or l.number_lotto=:threeDown4 or l.number_lotto=:twoDown or l.number_lotto in(:tote))" +
                  " and u.period_id=:periodId)")
  List<User> queryWinnerLotto(@Param("periodId") Long periodId, @Param("threeOn") String threeOn, @Param("twoOn") String twoOn,
                              @Param("threeDown1") String threeDown1, @Param("threeDown2") String threeDown2, @Param("threeDown3") String threeDown3,
                              @Param("threeDown4") String threeDown4, @Param("twoDown") String twoDown, @Param("tote") List<String> tote);
}
