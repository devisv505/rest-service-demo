package com.devisv.rest.exception;

public class IncorrectCurrencyException extends BadRequestException {

    private String uuid;

    private String currency;

    public IncorrectCurrencyException(String uuid, String currency) {
        super(String.format("Account %s has different currency than %s", uuid, currency));
        this.uuid = uuid;
        this.currency = currency;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCurrency() {
        return currency;
    }
}
