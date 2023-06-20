package com.jgdabc.common.ExceptionHander;

/**
 * @author 兰舟千帆
 * @version 1.0
 * @date 2022/11/12 18:03
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
