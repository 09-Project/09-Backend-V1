package com.example.project09.exception;

import com.example.project09.error.ErrorCode;
import com.example.project09.error.exception.BusinessException;

public class FailedConvertFileException extends BusinessException {
    public FailedConvertFileException() {
        super(ErrorCode.FAILED_CONVERT_FILE);
    }
}
