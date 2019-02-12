package com.devisv.rest.exception;

public class NotEnoughMoneyException extends BadRequestException {

    private String uuid;

    public NotEnoughMoneyException(String uuid) {
        super(String.format("Account %s don't have enough money", uuid));
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
