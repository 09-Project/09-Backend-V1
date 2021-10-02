package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class MemberUsernameAlreadyExistsException extends BusinessException {
    public MemberUsernameAlreadyExistsException() {
        super(ErrorCode.MEMBER_USERNAME_ALREADY_EXISTS);
    }
}
