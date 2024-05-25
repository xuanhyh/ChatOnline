package com.chat.mapper;

import com.chat.pojo.entity.GroupMember;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMemberMapper {

    @Insert("insert into chat_online.group_member (chat_online.group_member.group_id, chat_online.group_member.member_id, chat_online.group_member.member_permission)" +
            "values (#{groupId}, #{memberId}, #{memberPermission})")
    public void insert(GroupMember groupMember);

    @Select("select member_id from group_member where group_id = #{groupId}")
    List<Long> getMembersByGroupId(Long groupId);

    @Select("select * from group_member where member_id = #{userId}")
    List<Long> getAllGroupByUserId(Long userId);

    @Select("select member_permission from group_member where group_id=#{groupId} and member_id=#{userId}")
    public Integer getMemberPermissionByGroupId(Long groupId,Long userId);

    @Delete("delete from group_member where group_id = #{groupId}")
    void deleteGroupMemberById(Long groupId);
}