package com.devisv.rest.exception;

import org.eclipse.jetty.http.HttpStatus;

public class NotFoundException extends HttpException {

    private static final int STATUS_CODE = HttpStatus.NOT_FOUND_404;

    public NotFoundException(String message) {
        this(message, null);
    }

    public NotFoundException(String message, Throwable cause) {
        super(STATUS_CODE, message, cause);
    }
}
