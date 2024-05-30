package com.chat.handler;

import com.chat.common.constant.MessageConstant;
import com.chat.common.constant.MessageConstant;
import com.chat.common.exception.BaseException;
import com.chat.common.result.Result;
import com.chat.common.exception.BaseException;
import com.chat.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 * 异常处理器的一个重要作用就是在Impl判断要返回错误结果时可以直接抛出错误，
 * 然后就可以直接向客户端发错误消息了，因为Impl不return
 */
@RestControllerAdvice//在 @RestControllerAdvice 注解的类中定义全局异常处理方法
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        ex.printStackTrace();
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */

    //它设计出来的作用应该是当username重复的时候，给前端提醒一下，先留着用不到就删除
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //Duplicate entry 'zhangsan' for key 'employee.idx_username'
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else{
            log.info(message);
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
