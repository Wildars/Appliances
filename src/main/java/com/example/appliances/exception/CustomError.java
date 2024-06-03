package com.example.appliances.exception;

import org.springframework.http.HttpStatus;

public enum CustomError {

    FIL_CODE_REQUIRED("Нужен код филиала",HttpStatus.NOT_FOUND),
    //region Template
    TEMPLATE_DUPLICATE_NAME("duplicated KT with this name", HttpStatus.CONFLICT),
    TEMPLATE_NOT_CREATED("template not create", HttpStatus.UNPROCESSABLE_ENTITY),
    TEMPLATE_NOT_UPDATED("template not update", HttpStatus.UNPROCESSABLE_ENTITY),
    TEMPLATE_FORBIDDEN("this template does not beLong this expert", HttpStatus.FORBIDDEN),
    TEMPLATE_NOT_DELETED("template not delete", HttpStatus.UNPROCESSABLE_ENTITY),
    TEMPLATE_NOT_FOUND("template with this id not found", HttpStatus.NOT_FOUND),
    //endregion
    //region KTStatus
    KT_STATUS_NOT_CREATED("KTStatus not create", HttpStatus.UNPROCESSABLE_ENTITY),
    KT_STATUS_NOT_UPDATED("KTStatus not update", HttpStatus.UNPROCESSABLE_ENTITY),
    KT_STATUS_NOT_DELETED("KTStatus not delete", HttpStatus.UNPROCESSABLE_ENTITY),
    KT_STATUS_NOT_FOUND("KTStatus with this id not found", HttpStatus.NOT_FOUND),

    KT_LOCALIZATION_NOT_FOUND("KT Localization with this id not found", HttpStatus.NOT_FOUND),

    KT_LOCALIZATION_NOT_CREATED("KT Localization not create", HttpStatus.UNPROCESSABLE_ENTITY),

    KT_LOCALIZATION_NOT_UPDATED("KT Localization not update", HttpStatus.UNPROCESSABLE_ENTITY),

    KT_LOCALIZATION_NOT_DELETED("KT Localization not delete", HttpStatus.UNPROCESSABLE_ENTITY),


    //endregion
    //region KT
    KT_DUPLICATE_SCREEN_ID("duplicated KT with this screen id", HttpStatus.CONFLICT),
    KT_ALREADY_DELETED("KT with this id already deleted", HttpStatus.UNPROCESSABLE_ENTITY),
    KT_ALREADY_RESTORED("KT with this id already restored", HttpStatus.UNPROCESSABLE_ENTITY),
    KT_DELETED("KT with this id deleted", HttpStatus.BAD_REQUEST),
    KT_NOT_CREATED("KT not create", HttpStatus.UNPROCESSABLE_ENTITY),
    KT_NOT_UPDATED("KT not update", HttpStatus.UNPROCESSABLE_ENTITY),
    KT_NOT_DELETED("KT not delete", HttpStatus.UNPROCESSABLE_ENTITY),
    KT_NOT_FOUND("KT with this id not found", HttpStatus.NOT_FOUND),
    //endregion
    //region User
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    USER_NOT_AUTHENTICATE("Invalid data", HttpStatus.UNAUTHORIZED),
    USER_NOT_HAVE_THIS_ORGANISATION("User not have this organisation", HttpStatus.UNAUTHORIZED),
    USER_NOT_ACTIVE("User not active", HttpStatus.UNAUTHORIZED),
    USER_NOT_HAVE_ANY_ORGANISATION("User not have any organisation", HttpStatus.UNPROCESSABLE_ENTITY),
    //endregion
    ORGANISATION_NOT_FOUND("Organization not found", HttpStatus.NOT_FOUND),
    DCI_NOT_FOUND(".dci file with this id not found", HttpStatus.NOT_FOUND),

    ENTITY_HAVE_LINK("на обьект еще ссылаются, нельзя удалить", HttpStatus.CONFLICT),
    ENTITY_NOT_FOUND("обьект не найден", HttpStatus.NOT_FOUND),
    ;
    private final String message;
    private final HttpStatus status;

    CustomError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
