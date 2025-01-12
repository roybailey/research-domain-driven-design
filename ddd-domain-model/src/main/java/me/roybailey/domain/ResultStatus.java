package me.roybailey.domain;

public enum ResultStatus {

    OK(true),
    CREATED(true),

    ERROR,
    INVALID_ARGUMENT,
    UNAUTHORIZED,
    NOT_FOUND,
    CONFLICT,
    NOT_IMPLEMENTED;

    private Boolean success;

    ResultStatus() {
        this.success = false;
    }
    ResultStatus(Boolean success) {
        this.success = success;
    }

    public Boolean isSuccess() {
        return success;
    }

    public Boolean isFailure() {
        return !success;
    }
}

