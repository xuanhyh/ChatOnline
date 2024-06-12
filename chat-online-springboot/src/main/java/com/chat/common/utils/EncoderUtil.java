package com.chat.common.utils;

import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import com.chat.pojo.dto.SignalDTO;

/**
 * @Author: wu.shaoya
 * @Description: 编码器
 * @Date: 10:14 2019/10/31
 */
public class EncoderUtil implements Encoder.Text<SignalDTO> {

    @Override
    public String encode(SignalDTO message) throws EncodeException {
        return JSONObject.toJSONString(message);

    }

    @Override
    public void init(EndpointConfig ec) {
        //System.out.println("MessageEncoder - init method called");
    }



    @Override
    public void destroy() {
        //System.out.println("MessageEncoder - destroy method called");
    }

}

