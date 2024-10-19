package com.servlet_ordering_system.models.vos.enums;

public enum PaymentMethod {

    PIX(1),
    CREDIT_CARD(2),
    DEBIT_CARD(3);

    private final int code;

    private PaymentMethod(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PaymentMethod valueOf(int code) {
        for(PaymentMethod paymentMethod : PaymentMethod.values()) {
            if(paymentMethod.getCode() == code)
                return paymentMethod;
        }

        return null;
    }
}
