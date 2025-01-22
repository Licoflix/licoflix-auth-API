package com.auth.licoflix.core.domain.common;

public enum DomainReturnCode {

    /*        Operations     */
    SUCCESSFUL_OPERATION("Operation concluded with success"),
    USER_EXISTS("User with same params already exists!"),
    DELETED_USER("This user are deleted, contact an administrator!"),
    INVALID_TOKEN("Invalid Token"),
    NAME_EXISTS("Already exists a user with same name!"),
    FAILED_LOGIN("Invalid email or password !"),
    XLS_ERROR("Error downloading Excel file");

    private final String description;

    DomainReturnCode(String value) {
        description = value;
    }

    public String getDesc() {
        return description;
    }
}