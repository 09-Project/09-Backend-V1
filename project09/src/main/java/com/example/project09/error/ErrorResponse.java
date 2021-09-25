package com.example.project09.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String message;

    public String convertToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}
