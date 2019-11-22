package com.eding.exceptions;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 14:35
 */
public class ActionNotDefineException extends RuntimeException {
    public ActionNotDefineException(String s) {
        super(s);
    }
}
