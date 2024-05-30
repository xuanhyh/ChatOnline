package com.chat.service.impl;

import com.chat.mapper.GroupingMapper;
import com.chat.pojo.entity.Grouping;
import com.chat.service.GroupingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GroupingServiceImpl implements GroupingService {

    @Autowired
    GroupingMapper groupingMapper;

    @Override
    public List<Grouping> selectAllGroupingById(Long userId){
        List<Grouping> groupingList = groupingMapper.selectAllGroupingById(userId);
        if(groupingList.isEmpty())
        {
            log.info("为空");
        }
        log.info("GroupingServiceImpl内"+groupingList.toString());
        return groupingList;
    }
}
