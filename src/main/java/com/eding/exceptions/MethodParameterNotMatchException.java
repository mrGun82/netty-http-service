package com.eding.exceptions;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 14:31
 */
public class MethodParameterNotMatchException extends RuntimeException {

    public MethodParameterNotMatchException() {
        super(EDResultEnum.ERROR_PARAMETER_NOT_MATCH.getMessage());
    }

    public MethodParameterNotMatchException(String s) {
        super(s);
    }


}
