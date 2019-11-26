package com.eding.exceptions;

import java.io.IOException;

public class GlobalExceptionHandler {
    public Result exceptionHandler(Exception ex) {
        if (ex instanceof EDException) {
            return Result.error(((EDException) ex).getStatus(), ex.getMessage());
        } else if (ex instanceof NullPointerException) {
            return Result.error(EDResultEnum.ERROR_NULL);
        } else if (ex instanceof ClassCastException) {
            return Result.error(EDResultEnum.ERROR_CLASS_CAST);
        } else if (ex instanceof IOException) {
            return Result.error(EDResultEnum.ERROR_IO);
        } else if (ex instanceof ActionNotDefineException) {
            return Result.error(EDResultEnum.ERROR_MOTHODNOTSUPPORT);
        } else if (ex instanceof HttpMethodNotSupportedException) {
            return Result.error(EDResultEnum.ERROR_HTTP_METHOD_NOT_SUPPORTED);
        } else if (ex instanceof MethodParameterNotMatchException) {
            return Result.error(EDResultEnum.ERROR_PARAMETER_NOT_MATCH);
        } else if (ex instanceof RuntimeException) {
            return Result.error(EDResultEnum.ERROR_RUNTION);
        }
        return Result.error(EDResultEnum.FAILED);
    }

}
