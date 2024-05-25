package com.chat.common.utils;

import java.util.Random;

public class MailCodeUtil {
    /**
     * 生成邮箱验证码
     *
     * @return 6位邮箱验证码
     */
    public static String GenerateVerifyCode(){
        StringBuilder verifyCode = new StringBuilder();
        Random rand = new Random();
        for(int i = 0; i < 6; i ++ ){
            int range = rand.nextInt(3);
            int character = 0;
            switch(range) {
                case 0:
                    character = rand.nextInt(10) + 48;
                    break;
                case 1:
                    character = rand.nextInt(26) + 65;
                    break;
                case 2:
                    character = rand.nextInt(26) + 97;
                    break;
                default:
                    break;
            }
            verifyCode.append((char)character);
        }
        return verifyCode.toString();
    }
}
