package com.crm.servicebackend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorDetails {
    private ErrorDetails errorDetails;
    private Map<String, String> validation;
}
