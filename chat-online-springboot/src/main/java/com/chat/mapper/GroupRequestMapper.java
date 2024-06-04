package com.chat.mapper;

import com.chat.pojo.dto.GroupRequestDTO;
import com.chat.pojo.entity.GroupRequest;
import com.chat.pojo.vo.GroupRequestVO;
import com.chat.pojo.vo.UserGroupRequestVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface GroupRequestMapper {
    @Insert("insert into group_request (from_user_id, group_id, group_creator_id, status) " +
            "values (#{fromUserId}, #{groupId}, #{groupCreatorId}, #{status})")
    void insert(GroupRequest groupRequest);

    @Select("select * from group_request where group_creator_id = #{userId} and status = 'PENDING'")
    List<GroupRequest> getJoinRequest(Long userId);

    @Update("update group_request set status = #{status} where id = #{groupRequestId}")
    void update(GroupRequestDTO groupRequestDTO);

    @Select("select * from group_request where id = #{groupRequestId}")
    GroupRequest getById(Long groupRequestId);

    @Select("select * from group_request where from_user_id = #{userId};")
    List<GroupRequest> getByFromUserId(Long userId);
}
