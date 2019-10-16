package com.github.nut077.lotto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity(name = "lottos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lotto extends Common {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 3)
  private String numberLotto;

  private Integer buyOn;
  private Integer buyDown;
  private Integer buyTote;
  private Integer buyTotal;

  private Integer payOn;
  private Integer payDown;
  private Integer payTote;
  private Integer payTotal;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private User user;
}
