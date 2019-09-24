package com.github.nut077.lotto.service;

import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.exception.NotFoundException;
import com.github.nut077.lotto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User id: " + id + " -->> Not Found"));
  }

  public List<User> getWinnerLotto(Long periodId, List<String> numberOfWinner) {
    return userRepository.queryWinnerLotto(periodId, numberOfWinner);
  }

  public User update(Long id, User user) {
    findById(id);
    user.setId(id);
    return userRepository.save(user);
  }
}
