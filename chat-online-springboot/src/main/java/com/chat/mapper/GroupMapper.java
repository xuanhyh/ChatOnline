package com.chat.mapper;


import com.chat.annotation.AutoFill;
import com.chat.common.enumeration.OperationType;
import com.chat.pojo.entity.Group;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMapper {
    // group是mysql中的关键字，所以这里用``包裹
    @Select("select * from `group` where group_name = #{groupName}")
    List<Group> getByName(String groupName);


    @Select("select * from `group` where group_id = #{groupID}")
    Group getById(Long groupID);

    @AutoFill(OperationType.INSERT)
    void insert(Group newGroup);

    @Delete("delete from `group` where group_id = #{groupId}")
    void deleteGroupById(Long groupId);
}
