package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class TooLongNameException extends BusinessException {
    public TooLongNameException() {
        super(ErrorCode.TOO_LONG_NAME);
    }
}
