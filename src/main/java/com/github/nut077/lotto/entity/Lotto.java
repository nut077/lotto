package com.github.nut077.lotto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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

  @Column(length = 1)
  private Percent percent;

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
