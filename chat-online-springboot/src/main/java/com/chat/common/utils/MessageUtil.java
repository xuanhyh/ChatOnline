package com.chat.common.utils;

import com.alibaba.fastjson.JSON;
import com.chat.common.result.ResultMessage;

import java.util.Date;

public class MessageUtil {

    public static String getMessage(Boolean isSystemMessage, Long fromId, Object message){
        ResultMessage result = new ResultMessage();
        result.setSystem(isSystemMessage);
        result.setMessage(message);
        if(fromId != null){
            result.setFromId(fromId);
        }
        return JSON.toJSONString(result);

    }

    public static String getMessage(Boolean isSystemMessage, Long fromId, Object message, String name, String time){
        ResultMessage result = new ResultMessage();
        result.setSystem(isSystemMessage);
        result.setMessage(message);
        result.setName(name);
        result.setTime(time);
        if(fromId != null){
            result.setFromId(fromId);
        }
        return JSON.toJSONString(result);
    }

    public static String getMessage(Boolean isSystemMessage, Boolean isGroupMessage, Long fromId, Long toId, Long toGroupId, Object message, String name, String time){
        ResultMessage result = new ResultMessage();
        result.setSystem(isSystemMessage);
        result.setFromGroup(isGroupMessage);//新增
        result.setToId(toId);//新增
        result.setToGroupId(toGroupId);//新增
        result.setMessage(message);
        result.setName(name);
        result.setTime(time);
        if(fromId != null){
            result.setFromId(fromId);
        }
        return JSON.toJSONString(result);
    }
}
