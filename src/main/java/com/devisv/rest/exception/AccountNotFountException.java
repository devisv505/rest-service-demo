package com.devisv.rest.exception;

import java.util.UUID;

public class AccountNotFountException extends NotFoundException {

    private String uuid;

    public AccountNotFountException(UUID uuid) {
        super(String.format("Account with uuid %s not found", uuid.toString()));

        this.uuid = uuid.toString();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
