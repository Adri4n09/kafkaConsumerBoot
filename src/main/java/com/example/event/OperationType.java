package com.example.event;

public enum OperationType {

    ADD("Add"), DELETE("DELETE"), UPDATE("UPDATE"), GET("GET");
    private String value;

    OperationType(String value) {
        this.value = value;
    }

    private String getValue() {
        return value;
    }
}
