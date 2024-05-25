package com.chat.service;

import com.chat.pojo.dto.UpdateImpressionDTO;
import com.chat.pojo.entity.UserImpression;

import java.util.List;

public interface UserImpressionService {
    List<UserImpression> getImpressionById(Long userId);

    void addImpression(UserImpression userImpression);

    void delete(Long nowUserId, Long impressionId);

    void update(Long nowUserID, UpdateImpressionDTO updateImpressionDTO);

}
