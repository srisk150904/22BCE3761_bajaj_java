package com.healthrx.webhookapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinalQueryRequest {

    @JsonProperty("finalQuery")
    private String finalQuery;

    public FinalQueryRequest() {}

    public FinalQueryRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    @Override
    public String toString() {
        return "SqlQueryRequest{finalQuery='" + finalQuery + "'}";
    }
}
