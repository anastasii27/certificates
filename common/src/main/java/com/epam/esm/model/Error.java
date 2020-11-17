package com.epam.esm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
    private long errorCode;
    private String errorMessage;
}