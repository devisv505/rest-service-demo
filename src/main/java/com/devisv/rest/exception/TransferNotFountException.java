package com.devisv.rest.exception;

import java.util.UUID;

public class TransferNotFountException extends NotFoundException {

    private String uuid;

    public TransferNotFountException(UUID uuid) {
        super(String.format("Transfer with uuid %s not found", uuid.toString()));

        this.uuid = uuid.toString();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
