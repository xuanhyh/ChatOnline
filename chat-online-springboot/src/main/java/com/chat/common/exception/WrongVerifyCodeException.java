package com.chat.common.exception;

public class WrongVerifyCodeException extends BaseException {
    public WrongVerifyCodeException () {

    }

    public WrongVerifyCodeException(String msg) {
        super(msg);
    }
}
