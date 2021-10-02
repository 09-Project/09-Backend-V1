package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class ExpiredAccessTokenException extends BusinessException {
    public ExpiredAccessTokenException() {
        super(ErrorCode.EXPIRED_ACCESS_TOKEN);
    }
}
