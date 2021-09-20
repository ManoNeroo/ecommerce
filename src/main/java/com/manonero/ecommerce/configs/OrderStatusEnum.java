package com.manonero.ecommerce.configs;

public enum OrderStatusEnum {
    UNCONFIRM(0), PREPARESHIP(1), SHIPPING(2), SHIPPED(3);

    private final int value;

    private OrderStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
