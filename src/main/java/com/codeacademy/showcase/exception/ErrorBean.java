package com.codeacademy.showcase.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorBean {

    private String rejectedField;
//    private String errorCode;
    private String errorMessage;

}
