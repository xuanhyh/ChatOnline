package com.chat.service;

import com.chat.pojo.entity.Grouping;

import java.util.List;

public interface GroupingService {

    List<Grouping> selectAllGroupingById(Long userId);
}
