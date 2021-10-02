package com.example.project09.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    TOO_LONG_NAME(400, "Too Long Name"),
    INVALID_PASSWORD(401, "Invalid Password"),
    INVALID_TOKEN(401, "Invalid Token"),

    USER_ALREADY_EXISTS(409, "User Already Exists"),
    USER_NOT_FOUND(404, "User Not Found"),

    POST_NOT_FOUND(404, "Post Not Found"),

    IMAGE_NOT_FOUND(404, "Image Not Found"),
    FAILED_CONVERT_FILE(400,"Failed Convert File"),

    LIKE_NOT_FOUND(404, "Like Not Found"),
    LIKE_ALREADY_EXISTS(409, "Like Already Exists"),

    S3_CONNECTION_FAILED(500, "S3 Connection Failed");

    private final int status;
    private final String message;

}
