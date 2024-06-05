/*
    --error response best practice info--
    https://www.baeldung.com/rest-api-error-handling-best-practices
 */
package com.codeacademy.showcase.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorBeanModel {

    private String type; // user request path
    private String timestamp;
    private List<ErrorBean> errors;

}
