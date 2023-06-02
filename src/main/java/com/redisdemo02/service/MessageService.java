package com.redisdemo02.service;

public interface MessageService {

    // 消息队列，进出

    Long sendMessage(Object obj);

    Object getMessage();

}
