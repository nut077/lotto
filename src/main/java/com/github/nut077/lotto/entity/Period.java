package com.github.nut077.lotto.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity(name = "periods")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "period_seq")
public class Period {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "period_seq")
  private Long id;

  @Column(unique = true)
  private LocalDate periodDate;

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

  private int payThreeOn;
  private int payTwoOn;
  private int payThreeDown1;
  private int payThreeDown2;
  private int payThreeDown3;
  private int payThreeDown4;
  private int payTwoDown;
  private int payTote;

  private int buyTotal;
  private int payTotal;

  @OneToMany(mappedBy = "period", cascade = CascadeType.ALL)
  private List<User> user;
}
