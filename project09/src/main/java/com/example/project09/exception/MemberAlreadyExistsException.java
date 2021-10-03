package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class MemberAlreadyExistsException extends BusinessException {
    public MemberAlreadyExistsException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTS);
    }
}
