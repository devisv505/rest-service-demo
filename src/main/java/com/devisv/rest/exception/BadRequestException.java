package com.devisv.rest.exception;

import org.eclipse.jetty.http.HttpStatus;

public class BadRequestException extends HttpException {

    private static final int STATUS_CODE = HttpStatus.BAD_REQUEST_400;

    public BadRequestException(String message) {
        this(message, null);
    }

    public BadRequestException(String message, Throwable cause) {
        super(STATUS_CODE, message, cause);
    }
}
