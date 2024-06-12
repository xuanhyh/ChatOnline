package com.chat.controller;

import com.chat.common.context.BaseContext;
import com.chat.common.result.Result;
import com.chat.pojo.dto.GroupRequestDTO;
import com.chat.pojo.entity.Group;
import com.chat.pojo.vo.GroupRequestVO;
import com.chat.pojo.vo.GroupSearchVO;
import com.chat.pojo.vo.UserGroupRequestVO;
import com.chat.pojo.vo.UserSearchVO;
import com.chat.service.GroupRequestService;
import com.chat.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController//@RestController 注解是 @Controller 的一个特殊版本，默认将控制器中的方法的返回值直接作为 HTTP 响应的正文内容，Spring MVC 会自动将方法返回的数据对象转换为对应的格式
@RequestMapping("/api/group")
@CrossOrigin(origins = "*") // 允许来自 http://localhost:8081 的跨域请求
@Slf4j
public class GroupController {
    @Autowired
    GroupService groupService;

    @Autowired
    GroupRequestService groupRequestService;

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


    /*
    解散群
     */
    @GetMapping("/dissolve")
    public int dissolve(Long userId,Long groupId){
        log.info("获取群内所有成员，群id为：{}",groupId);
        return groupService.dissolveGroup(userId,groupId);

    }

    @GetMapping("/getGroupInfoByGroupId")
    public Result<Group> getGroupInfoByGroupId(Long groupId){
        log.info("获取群id为{}的群信息：",groupId);
        return Result.success(groupService.getGroupInfoByGroupId(groupId));
    }

    /**
     *
     *
     * 用户申请加群
     */
    @PostMapping("/joinGroup")
    public Result joinGroup(Long groupId){
        Long userId = BaseContext.getCurrentId();
        log.info("id为{}的用户申请加入id为{}的群", userId, groupId);
        groupService.joinGroup(userId, groupId);
        return Result.success();
    }

    /**
     *
     * status分为
     * PENDING 请求中(也就是未处理)
     * ACCEPT 接受请求
     * REJECT 拒绝请求（模仿的加好友请求⌓‿⌓）
     * 群主查询status为PENDING的加群申请
     * 需要前端每隔一会儿就发请求查询
     *
     * 返回：申请记录id、申请人id、申请加入的群的id
     */
    @GetMapping("/getJoinRequest")
    public Result<List<GroupRequestVO>> getJoinRequest(){
        Long userId = BaseContext.getCurrentId();
        log.info("id为{}的群主查询未处理的加群申请", userId);
        List<GroupRequestVO> list = groupRequestService.getJoinRequest(userId);
        return Result.success(list);
    }


    /**
     *
     * 群主处理加群申请
     * 前端传过来申请记录的id，以及处理的结果：ACCEPT或者REJECT
     */
    @PostMapping("/handleGroupRequest")
    public Result handleGroupRequest(@RequestBody GroupRequestDTO groupRequestDTO){
        Long userId = BaseContext.getCurrentId();
        log.info("id为{}的群主处理记录id为{}的请求，处理结果是{}",
                userId,
                groupRequestDTO.getGroupRequestId(),
                groupRequestDTO.getStatus());
        groupRequestService.handleGroupRequest(groupRequestDTO);
        return Result.success();

    }



    /**
     *
     * 用户查看自己发出的所有加群申请以及对应的处理状态
     * 可以让用户知道自己是被拒绝加群了还是被同意加群了还是未处理
     */
    @GetMapping("/getUserGroupRequest")
    public Result<List<UserGroupRequestVO>> getUserGroupRequest(){
        Long userId = BaseContext.getCurrentId();
        log.info("id为{}的用户查询自己发出的所有加群申请", userId);
        List<UserGroupRequestVO> list = groupRequestService.getByFromUserId(userId);
        return Result.success(list);
    }


    /**
     *
     * 用户退出某个群
     *
     */
    @GetMapping("/exitGroup")
    public Result exitGroup(Long groupId){
        Long userId = BaseContext.getCurrentId();
        log.info("id为{}的用户退出id为{}的群", userId, groupId);
        groupService.exitGroup(userId, groupId);
        return Result.success();
    }

    /**
     *
     * 群主把群主的位置交给别人
     * 原群主会成为普通群员
     */
    @GetMapping("/changeCreator")
    public Result changeCreator(Long groupId, String username){
        Long userId = BaseContext.getCurrentId();
        log.info("id为{}的群的群主（id为{}）想要把群主的位置交给用户名为{}的群员", groupId, userId, username);
        groupService.changeCreator(groupId, userId, username);
        return Result.success();

    }

}
