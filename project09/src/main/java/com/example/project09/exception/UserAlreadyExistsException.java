package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException() {
        super(ErrorCode.USER_ALREADY_EXISTS);
    }
}
