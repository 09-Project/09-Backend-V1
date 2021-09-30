package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class S3ConnectionFailedException extends BusinessException {
    public S3ConnectionFailedException() {
        super(ErrorCode.S3_CONNECTION_FAILED);
    }
}
