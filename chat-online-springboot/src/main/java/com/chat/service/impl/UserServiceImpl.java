package com.chat.service.impl;

import com.chat.common.constant.MessageConstant;
import com.chat.common.context.BaseContext;
import com.chat.common.exception.EmailExistedException;
import com.chat.common.exception.PasswordErrorException;
import com.chat.common.exception.UserNameAlreadyExistException;
import com.chat.common.exception.WrongVerifyCodeException;
import com.chat.common.exception.AccountNotFoundException;
import com.chat.common.utils.MailUtil;
import com.chat.mapper.UserFriendMapper;
import com.chat.mapper.UserMapper;
import com.chat.pojo.dto.*;
import com.chat.pojo.entity.EmailAndVerifyCode;
import com.chat.pojo.entity.User;
import com.chat.pojo.entity.UserFriend;
import com.chat.pojo.vo.UserSearchVO;
import com.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserFriendMapper userFriendMapper;

    private String verifyCode;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        //1、根据用户名查询数据库中的数据
        User user = userMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对象
        return user;

    }

    @Override
    public List<User> getFriendById(Long id) {
        List<User> list = userMapper.getFriendById(id);
        return list;
    }

    @Override
    public void signUp(UserSignUpDTO userSignUpDTO) {
        String verifyCodeReceive = userSignUpDTO.getVerifyCode();
        String email = userSignUpDTO.getEmail();
        String username = userSignUpDTO.getUsername();

        // 判断username有没有重复
        User userGetByVerify = userMapper.getByUsername(username);
        if(userGetByVerify!=null)
        {
            throw new UserNameAlreadyExistException(MessageConstant.USER_ALREADY_EXISTS);
        }

        // 判断验证码对不对
        EmailAndVerifyCode emailAndVerifyCode = userMapper.checkVerifyCode(email);
        if(emailAndVerifyCode==null){
            throw new WrongVerifyCodeException(MessageConstant.VERIFY_CODE_WRONG);
        }
        System.out.println("从数据库获取到的邮箱为："+emailAndVerifyCode.getEmail());
        System.out.println("从数据库获取到的验证码为："+emailAndVerifyCode.getVerifyCode());
        System.out.println("从前端获取到的验证码为："+verifyCodeReceive);
        if(!emailAndVerifyCode.getVerifyCode().equals(verifyCodeReceive))
        {
            throw new WrongVerifyCodeException(MessageConstant.VERIFY_CODE_WRONG);
        }

        User user = new User();
        BeanUtils.copyProperties(userSignUpDTO, user);
        String password = userSignUpDTO.getPassword();

        // 将密码加密之后存储
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));

        user.setEmail(userSignUpDTO.getEmail());
        user.setName(userSignUpDTO.getName());
        user.setUsername(userSignUpDTO.getUsername());
        // id不需要设置，在表中自动增长
        // 创建者、创建时间、修改者、修改时间不要手动设置，通过注解、切面完成

        System.out.println("user内容为："+user.toString());
        userMapper.insert(user);
    }

    @Override
    public void sendVerifyCode(MailDTO mailDTO){
        String mail = mailDTO.getEmail();
        User forCheckEmail = userMapper.checkEmail(mail);
        if(forCheckEmail != null){
            // 注册邮箱已存在
            throw new EmailExistedException(MessageConstant.EMAILL_EXISTED_FAILED);
        }
        MailUtil mailUtil = new MailUtil();
        verifyCode = mailUtil.getVerifyCode();
        try {
            mailUtil.sendEmail(mail);
        } catch (Exception e){
            e.printStackTrace();
        }
        userMapper.updateVerifyCode(mail,verifyCode);// 这里插入对应邮箱与验证码
        System.out.println(verifyCode);
    }

    @Override
    public void update(UserUpdateDTO userUpdateDTO) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        String password = userUpdateDTO.getPassword();
        if(password!=null){
            user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        }

        userMapper.update(user);
    }

    @Override
    public UserSearchVO search(String username) {
        User user = userMapper.getByUsername(username);
        UserSearchVO userSearchVO = new UserSearchVO();
        BeanUtils.copyProperties(user, userSearchVO);
        return userSearchVO;
    }

    @Override
    public void deleteFriendById(Long friendId) {
        Long userId = BaseContext.getCurrentId();
        log.info("deleteFriendById里的userId为：{}",userId);
        userFriendMapper.delete(userId, friendId);
    }

    @Override
    public void updateFriend(UpdateFriendDTO updateFriendDTO) {
        Long userId = BaseContext.getCurrentId();
        UserFriend userFriend = new UserFriend();
        BeanUtils.copyProperties(updateFriendDTO, userFriend);
        userFriend.setUserId(userId);
        log.info("{}", userFriend);
        userFriendMapper.update(userFriend);
    }



    @Override
    public List<User> getFriendByIdWithGroupingId(Long id){
        return userMapper.getFriendByIdWithGroupingId(id);
    }
}
