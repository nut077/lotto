package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.UserCreateDto;
import com.github.nut077.lotto.dto.mapper.PeriodCreateMapper;
import com.github.nut077.lotto.dto.mapper.UserCreateMapper;
import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.exception.NotFoundException;
import com.github.nut077.lotto.repository.LottoRepository;
import com.github.nut077.lotto.repository.UserRepository;
import com.nutfreedom.utilities.MapFreedom;
import com.nutfreedom.utilities.SplitFreedom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PeriodService periodService;
  private final LottoRepository lottoRepository;
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

  @Transactional
  public User update(Long id, String detail) {
    User user = findById(id);
    lottoRepository.deleteLottoById(id);
    Map<String, String> hm = getDetail(detail);
    MapFreedom map = new MapFreedom(hm);
    int line = map.getInt("line");
    for (int i = 1; i <= line; i++) {
      Lotto lotto = Lotto.builder()
        .numberLotto(map.getString("numberLotto" + i))
        .buyOn(map.getInt("buyOn" + i))
        .buyDown(map.getInt("buyDown" + i))
        .buyTote(map.getInt("buyTote" + i))
        .buyTotal(map.getInt("buyTotal" + i))
        .build();
      user.addLotto(lotto);
    }
    user.setBuy(map.getInt("buyTotal"));
    return userRepository.saveAndFlush(user);
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

  private Map<String, String> getDetail(String detail) {
    SplitFreedom split = new SplitFreedom();
    Map<String, String> hm = new LinkedHashMap<>();
    String[] sp = detail.split("&");
    for (String s : sp) {
      hm.put(split.splitByIndex(s, "=", 0), split.splitByIndex(s, "=", 1));
    }
    return hm;
  }
}
