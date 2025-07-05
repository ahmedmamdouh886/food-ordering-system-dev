package com.food.ordering.system.domain;

public class DomainConstants {
    // We added private constructor so this class will only be served for static fields.
    // So I cannot create an instant of this class as this private constructor exists.
    private DomainConstants() {
    }

    public static final String UTC = "UTC";
}
