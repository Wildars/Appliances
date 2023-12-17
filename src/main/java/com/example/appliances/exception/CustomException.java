package com.example.appliances.exception;

import com.example.appliances.model.response.ExceptionResponse;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final CustomError error;
    private final Exception exception;

    private ExceptionResponse response;

    public CustomException(CustomError error) {
        this.error = error;
        this.exception = null;
    }

    public CustomException(CustomError error, Exception exception) {
        this.error = error;
        this.exception = exception;
    }


    public String getReason() {
        return response.getReason();
    }
}
