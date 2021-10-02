package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class IncorrectTokenException extends BusinessException {
    public IncorrectTokenException() {
        super(ErrorCode.INCORRECT_TOKEN);
    }
}
