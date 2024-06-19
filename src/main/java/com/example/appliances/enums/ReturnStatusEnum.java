package com.example.appliances.enums;

public enum ReturnStatusEnum {
    SENT(1L),
    ACCEPTED(2L),
    REVOKED(3L),
    REFUSED(4L);

    ReturnStatusEnum(Long  id) {
        this.id = id;
    }

    private Long  id;

    public Long  getId() {
        return this.id;
    }
}