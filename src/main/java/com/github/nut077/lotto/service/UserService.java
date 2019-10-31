package com.github.nut077.lotto.service;

import com.github.nut077.lotto.dto.UserCreateDto;
import com.github.nut077.lotto.dto.mapper.PeriodCreateMapper;
import com.github.nut077.lotto.dto.mapper.UserCreateMapper;
import com.github.nut077.lotto.entity.Lotto;
import com.github.nut077.lotto.entity.User;
import com.github.nut077.lotto.exception.NotFoundException;
import com.github.nut077.lotto.repository.LottoRepository;
import com.github.nut077.lotto.repository.UserRepository;
import com.nutfreedom.utilities.ParseNumberFreedom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

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

  public User createLotto(Long id, User user) {
    findById(id);
    user.setId(id);
    return userRepository.save(user);
  }

  @Transactional
  public User createLotto(Long id, HttpServletRequest request) {
    ParseNumberFreedom parse = new ParseNumberFreedom();
    User user = findById(id);
    lottoRepository.deleteLottoById(id);
    String line = request.getParameter("line");
    String[] spLine = line.split(",");
    for (String i : spLine) {
      Lotto lotto = Lotto.builder()
        .numberLotto(request.getParameter("numberLotto" + i))
        .buyOn(parse.parseInt(request.getParameter("buyOn" + i)))
        .buyDown(parse.parseInt(request.getParameter("buyDown" + i)))
        .buyTote(parse.parseInt(request.getParameter("buyTote" + i)))
        .buyTotal(parse.parseInt(request.getParameter("buyTotal" + i)))
        .build();
      user.addLotto(lotto);
    }
    user.setBuy(parse.parseInt(request.getParameter("buyAll")));
    return userRepository.saveAndFlush(user);
  }

  public UserCreateDto create(Long periodId, UserCreateDto dto) {
    dto.setPeriod(periodCreateMapper.mapToDto(periodService.findById(periodId)));
    User user = userRepository.save(userCreateMapper.mapToEntity(dto));
    return userCreateMapper.mapToDto(user);
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
