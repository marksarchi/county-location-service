package com.marksarchi.countylocationservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseWrapper<T> {
    private final int code;
    private final String message;
    private T data;


}
