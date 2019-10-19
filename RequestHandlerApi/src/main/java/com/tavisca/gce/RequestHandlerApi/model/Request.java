package com.tavisca.gce.RequestHandlerApi.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "request")
public class Request {
    @Id
    private String transactionId;
    private boolean isValid;
    private String timestamp;
    private String fromService;
    private String toService;

    public Request() {
    }

    public Request(String transactionId, boolean isValid, String timestamp, String fromService, String toService) {
        this.transactionId = transactionId;
        this.isValid = isValid;
        this.timestamp = timestamp;
        this.fromService = fromService;
        this.toService = toService;
    }

    public String getFromService() {
        return fromService;
    }

    public void setFromService(String fromService) {
        this.fromService = fromService;
    }

    public String getToService() {
        return toService;
    }

    public void setToService(String toService) {
        this.toService = toService;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}