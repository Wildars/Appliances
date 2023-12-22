package com.example.appliances.enums;

public enum SaleStatusEnum {
    ACCEPTED(1L),
    SENDET(2L),
    REJECTED(3L),
    DONE(4L);

    SaleStatusEnum(Long  id) {
        this.id = id;
    }

    private Long  id;

    public Long  getId() {
        return this.id;
    }
}
