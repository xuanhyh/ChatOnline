package com.chat.mapper;

import com.chat.pojo.entity.Grouping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupingMapper {

    List<Grouping> selectAllGroupingById(Long userId);
}
