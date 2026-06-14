package org.example.user.presentation.exceptionHandlers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Map;


@Data
@AllArgsConstructor
public class ErrorMessage {
    private Integer statusCode;
    private Date timestamp;
    private String message;
    private Map<String, String> description;
}
