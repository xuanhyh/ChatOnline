package com.chat.controller;


import com.chat.common.context.BaseContext;
import com.chat.common.result.Result;
import com.chat.pojo.dto.UpdateImpressionDTO;
import com.chat.pojo.entity.UserImpression;
import com.chat.service.UserImpressionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController 注解是 @Controller 的一个特殊版本，默认将控制器中的方法的返回值直接作为 HTTP 响应的正文内容，Spring MVC 会自动将方法返回的数据对象转换为对应的格式
@RequestMapping("/api/impression")
@CrossOrigin(origins = "http://localhost:8081") // 允许来自 http://localhost:8081 的跨域请求
@Slf4j
public class UserImpressionController {
    @Autowired
    UserImpressionService userImpressionService;

    @GetMapping("/getById")
    public Result<List<UserImpression>> getImpression(Long userId){
        log.info("获取好友对用户的印象，用户id为：{}", userId);
        List<UserImpression> list = userImpressionService.getImpressionById(userId);
        return Result.success(list);
    }

    @PostMapping("/addImpression")
    public Result addImpression(@RequestBody UserImpression userImpression){
        Long senderId = BaseContext.getCurrentId();
        userImpression.setSenderId(senderId);
        log.info("id为{}的用户对id为{}的用户的文本印象为：{}，图片印象为：{}",
                userImpression.getSenderId(), userImpression.getUserId(),
                userImpression.getImpressionText(), userImpression.getImpressionPicture());
        userImpressionService.addImpression(userImpression);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(Long impressionId){
        Long nowUserId = BaseContext.getCurrentId();
        log.info("id为{}的用户想要删除id为{}的印象",
                nowUserId, impressionId);
        userImpressionService.delete(nowUserId, impressionId);
        return Result.success();
    }

    /**
     * 修改印象接口
     * 使用场景：
     * A对B做出了一个印象，然后A想修改这个印象
     *
     * @param updateImpressionDTO
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody UpdateImpressionDTO updateImpressionDTO){
        Long nowUserId = BaseContext.getCurrentId();
        log.info("id为{}的用户想要修改id为{}的印象",
                nowUserId, updateImpressionDTO.getImpressionId());
        userImpressionService.update(nowUserId, updateImpressionDTO);
        return Result.success();
    }

}
