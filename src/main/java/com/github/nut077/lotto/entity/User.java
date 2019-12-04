package com.github.nut077.lotto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends Common {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private int buy;
  private int pay;
  private int buyPercent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "period_id")
  @JsonIgnore
  private Period period;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Lotto> lottos = new ArrayList<>();

  public void setLottos(List<Lotto> lottos) {
    if (lottos != null) {
      lottos.forEach(lotto -> lotto.setUser(this));
    }
    this.lottos = lottos;
  }

  public void addLotto(Lotto lotto) {
    lotto.setUser(this);
    this.lottos.add(lotto);
  }
}
