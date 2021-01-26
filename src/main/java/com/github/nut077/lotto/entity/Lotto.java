package com.github.nut077.lotto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Stream;

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

  private int buyOn;
  private int buyDown;
  private int buyTote;
  private int buyTotal;

  private int payOn;
  private int payDown;
  private int payTote;
  private int payTotal;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private User user;

  @Column(length = 1, columnDefinition = "varchar(1) default 'Y'")
  private Percent percentOn;

  @Column(length = 1, columnDefinition = "varchar(1) default 'Y'")
  private Percent percentDown;

  @Column(length = 1, columnDefinition = "varchar(1) default 'Y'")
  private Percent percentTote;

  @Column(length = 1, columnDefinition = "varchar(1) default 'Y'")
  private Percent chargeOn;

  @Column(length = 1, columnDefinition = "varchar(1) default 'Y'")
  private Percent chargeDown;

  @Column(length = 1, columnDefinition = "varchar(1) default 'Y'")
  private Percent chargeTote;

  @Transient
  private List<String> users;

  @RequiredArgsConstructor
  public enum Percent {
    YES("Y"),
    NO("N");

    @Getter
    private final String code;

    public static Percent codeToPercent(String code) {
      return Stream.of(Percent.values())
        .parallel()
        .filter(status -> status.getCode().equalsIgnoreCase(code))
        .findAny()
        .orElseThrow(
          () -> new IllegalArgumentException("The code : " + code + " is illegal argument."));
    }
  }
}
