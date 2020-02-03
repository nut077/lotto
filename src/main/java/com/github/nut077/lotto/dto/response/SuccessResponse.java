package com.github.nut077.lotto.dto.response;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
public class SuccessResponse<T> {

  private String code;
  private String message;
  private OffsetDateTime timestamp;
  private T data;

  public static <T> SuccessResponseBuilder builder(T data) {
    return SuccessResponseBuilder.builder().data(data)
            .code("xxx-200")
            .message("success")
            .timestamp(OffsetDateTime.now()).build();
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SuccessResponseBuilder<T> {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private T data;
  }
}
