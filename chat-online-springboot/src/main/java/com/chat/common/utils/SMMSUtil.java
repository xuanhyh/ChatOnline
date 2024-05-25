package com.chat.common.utils;


import com.alibaba.fastjson.JSONObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Data
@AllArgsConstructor
@Slf4j
public class SMMSUtil {
    private String token;//存储访问 SM.MS 服务器所需的令牌

    /**
     * 图片上传
     * SM.MS 是一个在线图片存储和分享服务。
     * 它允许用户上传图片并获取图片的 URL 地址，
     * 用户可以通过这些 URL 地址分享图片或将图片嵌入到网页中。
     * SM.MS 提供了简单易用的 API，使开发者能够方便地集成图片上传功能到他们的应用程序中。
     * SM.MS 还提供了一些附加功能，如图片管理、图片托管等。
     */
    public String upload(byte[] bytes,String objectName){
        File file = null ;
        FileOutputStream stream = null;
        try {
            file = new File(objectName);
            if (!file.exists()){
                //创建文件
                file.createNewFile();
                stream = new FileOutputStream(file.getAbsoluteFile());
                stream.write(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //这里是向SM.MS服务器发送上传图片请求
        HttpResponse<String> response1 = Unirest.post("https://smms.app/api/v2/upload")
                //这里*****是指上一步获取的token
                .header("Authorization", token)
                .field("smfile", file)
                .asString();
        JSONObject jsonObject = JSONObject.parseObject(response1.getBody());
        //把本地创建的文件删除，不占用本地资源
        file.delete();
        //imageUrl存放上传图片的地址，方便返回前端
        String imageUrl = null;
        //当上传的图片重复时
        if (jsonObject.getString("code").equals("image_repeated")){
            imageUrl=jsonObject.getString("images");
        }
        else {
            imageUrl= JSONObject.parseObject(jsonObject.getString("data")).getString("url");
        }
        return imageUrl;



    }

}
