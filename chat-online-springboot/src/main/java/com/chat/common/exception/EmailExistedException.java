package com.chat.common.exception;

/**
 * 注册邮箱重复异常
 */
public class EmailExistedException extends BaseException {
    public EmailExistedException() {
    }
    public EmailExistedException(String msg) {
        super(msg);
    }
}
