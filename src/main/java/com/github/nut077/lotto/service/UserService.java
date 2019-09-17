package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService<User, Long> {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    super(userRepository);
    this.userRepository = userRepository;
  }

  public List<User> getWinnerLotto(Long periodId, String threeOn, String twoOn, String threeDown1, String threeDown2,
                                   String threeDown3, String threeDown4, String twoDown, List<String> tote) {
    return userRepository.queryWinnerLotto(periodId, threeOn, twoOn, threeDown1, threeDown2, threeDown3, threeDown4, twoDown, tote);
  }
}
