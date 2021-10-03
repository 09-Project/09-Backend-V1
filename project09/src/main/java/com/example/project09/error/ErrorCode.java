package com.example.project09.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 토큰
    INCORRECT_TOKEN(400, "Incorrect Token"),
    INVALID_TOKEN(401, "Invalid Token"),
    EXPIRED_ACCESS_TOKEN(401, "Expired Access Token"),
    EXPIRED_REFRESH_TOKEN(401, "Expired Refresh Token"),
    REFRESH_TOKEN_NOT_FOUND(404, "Refresh Token Not Found"),

    // 회원
    INVALID_PASSWORD(401, "Invalid Password"),
    MEMBER_NOT_FOUND(404, "Member Not Found"),
    MEMBER_NAME_ALREADY_EXISTS(409, "Member Name Already Exists"),
    MEMBER_ALREADY_EXISTS(409, "Member Already Exists"),

    // 상품
    POST_NOT_FOUND(404, "Post Not Found"),

    // 이미지
    IMAGE_NOT_FOUND(404, "Image Not Found"),

    // S3
    FAILED_CONVERT_FILE(400,"Failed Convert File"),
    S3_CONNECTION_FAILED(500, "S3 Connection Failed"),

    // 찜
    LIKE_NOT_FOUND(404, "Like Not Found"),
    LIKE_ALREADY_EXISTS(409, "Like Already Exists");


    private final int status;
    private final String message;

}
