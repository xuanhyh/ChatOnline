package com.chat.service.impl;

import com.chat.common.constant.MessageConstant;
import com.chat.common.exception.ImpressionDeleteException;
import com.chat.mapper.UserImpressionMapper;
import com.chat.pojo.dto.UpdateImpressionDTO;
import com.chat.pojo.entity.UserImpression;
import com.chat.service.UserImpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImpressionServiceImpl implements UserImpressionService {

    @Autowired
    UserImpressionMapper userImpressionMapper;

    @Override
    public List<UserImpression> getImpressionById(Long userId) {

        return userImpressionMapper.select(userId);
    }

    @Override
    public void addImpression(UserImpression userImpression) {

        userImpressionMapper.insert(userImpression);
    }

    @Override
    public void delete(Long nowUserId, Long impressionId) {
        UserImpression impression = userImpressionMapper.getByImpressionId(impressionId);
        if(!impression.getSenderId().equals(nowUserId)){
            throw new ImpressionDeleteException(MessageConstant.UserNotSender);
        }
        userImpressionMapper.delete(impressionId);
    }

    @Override
    public void update(Long nowUserID, UpdateImpressionDTO updateImpressionDTO) {
        UserImpression userImpression = userImpressionMapper.getByImpressionId(updateImpressionDTO.getImpressionId());
        if(!userImpression.getSenderId().equals(nowUserID)){
            throw new ImpressionDeleteException(MessageConstant.UserNotSender);
        }
        userImpressionMapper.update(updateImpressionDTO);
    }


}
