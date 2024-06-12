package com.chat.service;


import com.chat.pojo.dto.*;
import com.chat.pojo.entity.Group;
import com.chat.pojo.entity.User;
import com.chat.pojo.vo.UserSearchVO;

import java.util.List;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);

    List<User> getFriendById(Long id);

    void signUp(UserSignUpDTO userSignupDTO);

    void sendVerifyCode(MailDTO mailDTO);

    void update(UserUpdateDTO userUpdateDTO);

    UserSearchVO search(String username);

    void deleteFriendById(Long friendId);

    void updateFriend(UpdateFriendDTO updateFriendDTO);

    List<Integer>getAllGroupByUserID(Long userId);


    List<User> getFriendByIdWithGroupingId(Long id);


}
