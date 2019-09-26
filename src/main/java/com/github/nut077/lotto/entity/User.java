package com.github.nut077.lotto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "user_seq")
public class User extends Common {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
  private Long id;

  @Column(unique = true)
  private String name;
  private Integer buy;
  private Integer pay;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "period_id")
  @JsonIgnore
  private Period period;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Lotto> lotto;
}
