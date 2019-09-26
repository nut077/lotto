package com.github.nut077.lotto.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.OffsetDateTime;

@Getter
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(value = Listeners.class)
public class Common {

  private OffsetDateTime createDate;
  private OffsetDateTime updatedDate;

  @Version
  private int version;
}
