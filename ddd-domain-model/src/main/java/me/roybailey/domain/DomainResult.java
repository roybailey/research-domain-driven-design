package me.roybailey.domain;


import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

@Data
@With
public class DomainResult<T> {

    public static <T> DomainResult<T> result(ResultStatus status, T data, String message, Exception err) {
        return new DomainResult<>(status, data, message, err);
    }
    public static <T> DomainResult<T> ok(T data) {
        return ok(data, ResultStatus.OK.name());
    }
    public static <T> DomainResult<T> ok(T data, String message) {
        return new DomainResult<>(ResultStatus.OK, data, message, null);
    }
    public static <T> DomainResult<T> invalidArgument(String message, Exception err) {
        return new DomainResult<>(ResultStatus.INVALID_ARGUMENT, null, message, err);
    }
    public static <T> DomainResult<T> notFound(String message, Exception err) {
        return new DomainResult<>(ResultStatus.OK, null, message, err);
    }
    public static <T> DomainResult<T> conflict(String message, Exception err) {
        return new DomainResult<>(ResultStatus.OK, null, message, err);
    }
    public static <T> DomainResult<T> notImplemented(Object stub, String method) {
        return new DomainResult<>(
                ResultStatus.NOT_IMPLEMENTED,
                null,
                "NOT IMPLEMENTED: "+stub.getClass().getSimpleName()+"."+method,
                null
        );
    }

    private final ResultStatus status;
    private final T data;
    private final String message;
    private final Exception err;

    @Builder
    private DomainResult(ResultStatus status, T data, String message, Exception err) {
        this.status = (status != null) ? status : ResultStatus.ERROR;
        this.message = (message != null) ? message : "";
        this.data = data;
        this.err = err;
    }

    public T getOrElse(T defaultValue) {
        if (isSuccess() && getData() != null) {
            return getData();
        }
        return defaultValue;
    }

    public boolean isSuccess() {
        return status == ResultStatus.OK;
    }

    public boolean isFailure() {
        return !isSuccess();
    }

}

