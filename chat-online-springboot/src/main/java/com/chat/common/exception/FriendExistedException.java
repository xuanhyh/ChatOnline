package com.chat.common.exception;

public class FriendExistedException extends BaseException {
    public FriendExistedException () {
    }
    public FriendExistedException(String msg) {
        super(msg);
    }
}
