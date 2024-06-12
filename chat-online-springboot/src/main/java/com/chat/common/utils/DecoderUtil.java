package com.chat.common.utils;


import com.alibaba.fastjson.JSON;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;
import com.chat.pojo.dto.SignalDTO;

/**
 * @Author: wu.shaoya
 * @Description: 解码器
 * @Date: 10:14 2019/10/31
 */
public class DecoderUtil implements Decoder.Text<SignalDTO> {

    @Override
    public SignalDTO decode(String jsonMessage) throws DecodeException {
        return JSON.parseObject(jsonMessage, SignalDTO.class);

    }

    @Override
    public boolean willDecode(String jsonMessage) {
       /* try {
            // Check if incoming message is valid JSON
           // JSON.createReader(new StringReader(jsonMessage)).readObject();
            //检查是否是合法的json字符串
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonMessage);
            return true;
        } catch (Exception e) {
            return false;
        }*/
       return true;
    }

    @Override
    public void init(EndpointConfig ec) {
        //System.out.println("MessageDecoder -init method called");
    }

    @Override
    public void destroy() {
        //System.out.println("MessageDecoder - destroy method called");
    }

}

