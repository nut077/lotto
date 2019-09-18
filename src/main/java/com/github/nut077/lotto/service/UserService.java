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

  public List<User> getWinnerLotto(Long periodId, List<String> numberOfWinner) {
    return userRepository.queryWinnerLotto(periodId, numberOfWinner);
  }
}
