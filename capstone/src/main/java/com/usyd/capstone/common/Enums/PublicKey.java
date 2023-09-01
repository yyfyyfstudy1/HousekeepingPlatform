package com.usyd.capstone.common.Enums;

public enum PublicKey {
    firstKey("elec5619");

    private final String value;

    PublicKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
