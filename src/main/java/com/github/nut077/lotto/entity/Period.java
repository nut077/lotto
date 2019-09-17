package com.github.nut077.lotto.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity(name = "periods")
@Getter
@Setter
@SequenceGenerator(name = "period_seq")
public class Period {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "period_seq")
  private Long id;

  @Column(unique = true)
  private OffsetDateTime periodDate;

  @Column(length = 3)
  private String threeOn;

  @Column(length = 2)
  private String twoOn;

  @Column(length = 3)
  private String threeDown1;

  @Column(length = 3)
  private String threeDown2;

  @Column(length = 3)
  private String threeDown3;

  @Column(length = 3)
  private String threeDown4;

  @Column(length = 2)
  private String twoDown;

  @OneToMany(mappedBy = "period", cascade = CascadeType.ALL)
  private List<User> user;
}
