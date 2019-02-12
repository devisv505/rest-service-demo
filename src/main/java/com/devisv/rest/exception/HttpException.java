package com.devisv.rest.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"message", "localizedMessage", "stackTrace", "suppressed", "cause", "args", "messageKey", "stackTrace"})
public class HttpException extends RuntimeException {

    public HttpException(int httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.localizedMessage = message;
    }

    @JsonProperty("status")
    private int httpStatus;

    @JsonProperty("exception")
    private String localizedMessage;

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }
}
