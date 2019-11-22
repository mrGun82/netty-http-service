package com.eding.exceptions;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 14:31
 */
public class MethodParameterNotDefineException extends RuntimeException {
    public MethodParameterNotDefineException(String s) {
        super(s);
    }
}
