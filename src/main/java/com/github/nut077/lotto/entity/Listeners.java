package com.github.nut077.lotto.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.OffsetDateTime;

public class Listeners<T extends Common> {

  @PrePersist
  private void prePersist(T e) {
    e.setCreateDate(OffsetDateTime.now());
  }

  @PreUpdate
  private void perUpdate(T e) {
    e.setUpdatedDate(OffsetDateTime.now());
  }
}
