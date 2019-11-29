package com.eding.framework.exceptions;

import lombok.Data;

@Data
public class EDException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String status;
    private String message;
    private Object data;
    private Exception exception;

    public EDException() {
        super();
    }

    public EDException(String status, String message, Object data, Exception exception) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.exception = exception;
    }

    public EDException(EDResultEnum apiResultEnum) {
        this(apiResultEnum.getStatus(), apiResultEnum.getMessage(), null, null);
    }

    public EDException(EDResultEnum apiResultEnum, Object data) {
        this(apiResultEnum.getStatus(), apiResultEnum.getMessage(), data, null);
    }

    public EDException(EDResultEnum apiResultEnum, Object data, Exception exception) {
        this(apiResultEnum.getStatus(), apiResultEnum.getMessage(), data, exception);
    }
}
