package com.chat.controller;

import com.chat.common.result.Result;
import com.chat.mapper.GroupingMapper;
import com.chat.pojo.entity.Grouping;
import com.chat.service.GroupingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RestController 注解是 @Controller 的一个特殊版本，默认将控制器中的方法的返回值直接作为 HTTP 响应的正文内容，Spring MVC 会自动将方法返回的数据对象转换为对应的格式
@RequestMapping("/api/grouping")
@CrossOrigin(origins = "http://localhost:8081") // 允许来自 http://localhost:8081 的跨域请求
@Slf4j
public class GroupingController {

    @Autowired
    GroupingService groupingService;

    @GetMapping("/getAllGrouping")
    public Result getAllGrouping(Long userId){
        List<Grouping> groupingList = groupingService.selectAllGroupingById(userId);
        log.info("GroupingController内"+groupingList.toString());
        return Result.success(groupingList);
    }
}
