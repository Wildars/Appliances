package com.example.appliances.enums;

public enum WishListStatusEnum {
    CREATED(1L),
    RETURNED(2L),
    ACCEPTED(3L),
    REJECTED(4L),
    RECIEVED(5L),
    REFUSE(6L);

    WishListStatusEnum(Long  id) {
        this.id = id;
    }

    private Long  id;

    public Long  getId() {
        return this.id;
    }
}
