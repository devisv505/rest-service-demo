package com.devisv.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferResponseDto {

    private String id;

    private String state;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("completed_at")
    private String completedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
