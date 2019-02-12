package com.devisv.rest.exception;

public class IdenticalAccountsException extends BadRequestException {

    private String sourceAccountId;

    private String targetAccountId;

    public IdenticalAccountsException(String sourceAccountId, String targetAccountId) {
        super("You can't transfer amount between one account");

        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }
}
