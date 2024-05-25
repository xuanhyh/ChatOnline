package com.chat.controller;

import com.chat.common.result.Result;
import com.chat.pojo.entity.Group;
import com.chat.pojo.vo.GroupSearchVO;
import com.chat.pojo.vo.UserSearchVO;
import com.chat.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController//@RestController 注解是 @Controller 的一个特殊版本，默认将控制器中的方法的返回值直接作为 HTTP 响应的正文内容，Spring MVC 会自动将方法返回的数据对象转换为对应的格式
@RequestMapping("/api/group")
@CrossOrigin(origins = "http://localhost:8081") // 允许来自 http://localhost:8081 的跨域请求
@Slf4j
public class GroupController {
    @Autowired
    GroupService groupService;


    /**
     *
     * 用户根据群名称搜索群，返回一个集合
     */
    @GetMapping("/search")
    public Result<List<GroupSearchVO>> searchGroup(String groupName){
        log.info("搜索群，群名：{}", groupName);
        List<GroupSearchVO> groups = groupService.searchGroup(groupName);
        return Result.success(groups);
    }

    /**
     *
     * 用户创建群
     * 创建之后自动将该用户放到群中
     * 并将其权限设置为0
     * 约定：
     * 0：群主
     * 1：普通群员
     * 为了简化不设置管理员，有需要可以改动
     */
    @GetMapping("/create")
    public Result createGroup(String groupName){
        log.info("创建群，群名：{}", groupName);
        groupService.createGroup(groupName);
        return Result.success();
    }

    /**
     *
     * 根据群id获取群内所有成员信息
     * 成员的信息类型设置为UserSearchVO
     * 如果需要修改返回类型可以随时调整
     *
     */
    @GetMapping("/members")
    public Result<List<UserSearchVO>> getMembers(Long groupId){
        log.info("获取群内所有成员，群id为：{}",groupId);
        List<UserSearchVO> list = groupService.getMembers(groupId);
        return Result.success(list);
    }

    @GetMapping("/getAllGroup")
    public Result<List<Group>> getAllGroup(Long userId){
        log.info("获取用户所有群，用户id为：{}",userId);
        List<Group> groups = groupService.getAllGroupByUserId(userId);
        return Result.success(groups);
    }


    @GetMapping("/dissolve")
    public int dissolve(Long userId,Long groupId){
        log.info("获取群内所有成员，群id为：{}",groupId);
        return groupService.dissolveGroup(userId,groupId);

    }

}
