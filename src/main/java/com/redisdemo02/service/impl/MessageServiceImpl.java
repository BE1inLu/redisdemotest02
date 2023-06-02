package com.redisdemo02.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redisdemo02.service.MessageService;
import com.redisdemo02.util.redisUtil;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    redisUtil redisUtil;

    @Override
    public Long sendMessage(Object obj) {
        return redisUtil.listLeftPush("MessgeList", obj);
    }

    @Override
    public Object getMessage() {
        return redisUtil.lRightPop("MessgeList");
    }

}
