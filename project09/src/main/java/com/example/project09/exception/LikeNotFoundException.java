package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class LikeNotFoundException extends BusinessException {
    public LikeNotFoundException() {
        super(ErrorCode.LIKE_NOT_FOUND);
    }
}
