package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class MemberNameAlreadyExistsException extends BusinessException {
    public MemberNameAlreadyExistsException() {
        super(ErrorCode.MEMBER_NAME_ALREADY_EXISTS);
    }
}
