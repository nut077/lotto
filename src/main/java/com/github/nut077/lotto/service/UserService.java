package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.PeriodCreateDto;
import com.github.nut077.lotto.dto.UserCreateDto;
import com.github.nut077.lotto.dto.mapper.PeriodCreateMapper;
import com.github.nut077.lotto.dto.mapper.UserCreateMapper;
import com.github.nut077.lotto.entity.Period;
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
  private final PeriodService periodService;
  private final UserCreateMapper userCreateMapper;
  private final PeriodCreateMapper periodCreateMapper;

  public List<User> findByPeriod(Long id) {
    return userRepository.findByPeriod(periodService.findById(id));
  }

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

  public void createForm(Long periodId, UserCreateDto dto) {
    dto.setPeriod(periodCreateMapper.mapToDto(periodService.findById(periodId)));
    userRepository.save(userCreateMapper.mapToEntity(dto));
  }

  public void delete(Long id) {
    findById(id);
    userRepository.deleteById(id);
  }

  public void updateUpdateForm(Long id, UserCreateDto dto) {
    User user = findById(id);
    user.setName(dto.getName());
    userRepository.save(user);
  }
}
