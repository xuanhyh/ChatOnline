package com.chat.controller;


import com.chat.common.constant.JwtClaimsConstant;
import com.chat.common.result.Result;
import com.chat.common.utils.JwtUtil;
import com.chat.common.properties.JwtProperties;
import com.chat.pojo.dto.*;
import com.chat.pojo.entity.FriendRequest;
import com.chat.pojo.entity.User;
import com.chat.pojo.vo.UserLoginVO;
import com.chat.pojo.vo.UserSearchVO;
import com.chat.service.UserFriendService;
import com.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController//@RestController 注解是 @Controller 的一个特殊版本，默认将控制器中的方法的返回值直接作为 HTTP 响应的正文内容，Spring MVC 会自动将方法返回的数据对象转换为对应的格式
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:8081") // 允许来自 http://localhost:8081 的跨域请求
@Slf4j
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserFriendService userFriendService;
    @Autowired
    JwtProperties jwtProperties;

    /**
     * 登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登录");
        User user = userService.login(userLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        //这名字(。・ω・。)
        //咳咳。。。。复制的时候忘记改了，已纠正
        UserLoginVO userLoginVO = UserLoginVO.builder() //这就是@Builder的好处
                .userId(user.getUserId())
                .userName(user.getUsername())
                .name(user.getName())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * 根据用户id查找用户所有的好友
     * @param id
     * @return
     */
    @GetMapping("/friend")
    public Result test(Long id){
        log.info("根据用户id查找用户的好友：{}", id);
        List<User> list = userService.getFriendById(id);
        return Result.success(list);
    }


    /**
     * 用户注册
     *
     * @param
     * @return
     */
    @PostMapping("/signup")
    public Result signup(@RequestBody UserSignUpDTO userSignUpDTO){
        log.info("用户注册");
        userService.signUp(userSignUpDTO);
        return Result.success();
    }

    /**
     *
     */
    @PostMapping("/sendVerifyCode")
    public Result sendVerifyCode(@RequestBody MailDTO mailDTO){
        log.info("发送验证码");
        userService.sendVerifyCode(mailDTO);
        return Result.success();
    }

    /**
     * 根据id修改用户信息
     * 这个接口只是用来测试的
     *
     */
    @PostMapping("/update")
    public Result update(@RequestBody UserUpdateDTO userUpdateDTO){
        userService.update(userUpdateDTO);
        return Result.success();
    }


    /**
     * 搜索用户，根据username
     * @return
     */
    @GetMapping("/searchFriend")
    public Result<UserSearchVO> search(String username){
        log.info("搜索用户，根据username：{}", username);
        UserSearchVO userSearchVO = userService.search(username);
        return Result.success(userSearchVO);
    }


    /**
     * 添加好友，目前是根据用户id，有需要的话可以修改为根据用户名
     * 我测，突然想起来还得对方同意，有点复杂了
     * @return
     */
    @PostMapping("/sendFriendRequest")
    public Result sendFriendRequest(@RequestBody FriendRequestDTO friendRequestDTO){
        log.info("发送好友请求");
        userFriendService.sendFriendRequest(friendRequestDTO);
        return Result.success();
    }
    @GetMapping("/getFriendRequests")
    public Result<?> getFriendRequests(){
        log.info("获取好友请求");
        List<FriendRequest> list = userFriendService.getFriendRequest();
        return Result.success(list);
    }
    @PostMapping("/respondToFriendRequest")
    public Result respongToFriendRequest(@RequestBody Map<String, String> requestBody){
        Long fromUserId = Long.parseLong(requestBody.get("fromUserId"));
        String status = requestBody.get("status");
        userFriendService.respongToFriendRequest(fromUserId, status);
        return Result.success();
    }
    /**
     * 根据id删除好友，双向删除
     *
     */
    @DeleteMapping("/delete")
    public Result deleteFriendById(Long FriendId){
        log.info("删除好友，id为{}", FriendId);
        userService.deleteFriendById(FriendId);
        return Result.success();
    }


    /**
     *
     * 修改对好友的备注
     * 如果不修改备注，备注就传过来null
     */
    @PutMapping("/updateFriend")
    public Result updateFriend(@RequestBody UpdateFriendDTO updateFriendDTO){
        log.info("修改对好友的备注{}", updateFriendDTO);
        userService.updateFriend(updateFriendDTO);
        return Result.success();
    }

    /**
     *
     * 修改对好友的分组
     */
    @PutMapping("/updateFriendGroup")
    public Result updateFriendGroup(@RequestBody FriendGroupChangeDTO friendGroupChangeDTO){
        log.info("修改对好友的分组{}", friendGroupChangeDTO);
        userFriendService.changeFriendGroup(friendGroupChangeDTO);
        return Result.success();
    }
    @PostMapping("/createrNewGrouping")
    public Result createNewGrouping(String groupingName){
        log.info("新建好友分组：{}",groupingName);
        userFriendService.createANewGrouping(groupingName);
        return Result.success();
    }

    @GetMapping("/getFriendByIdWithGroupingId")
    public Result getFriendByIdWithGroupingId(Long id){
        log.info("获取id{}用户的所有好友（带分组id）",id);
        return Result.success(userService.getFriendByIdWithGroupingId(id));
    }
}
