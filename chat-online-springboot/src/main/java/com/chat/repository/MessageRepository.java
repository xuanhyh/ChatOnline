package com.chat.repository;

import com.chat.pojo.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long>{//数据库实体类和主键的类型

//    Page<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);
//

//    //会根据名称和库表对应起来
//    @Query("SELECT m FROM com.chat.pojo.entity.Message m WHERE (m.senderId = :senderId AND m.receiverId = :receiverId) OR (m.senderId = :receiverId AND m.receiverId = :senderId)")
//    Page<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);

//    @Query("select m.id,m.chat_flag,m.content_flag,m.text_message,"+
//            "m.picture_message,m.video_message,m.sender_id,m.send_time,"+
//            "m.receiver_id,m.receiver_group_id,m.received from Message m "+
//            "where (m.sender_id=?1 and m.receiver_id=?2) or (m.sender_id=?2 and m.receiver_id=?1)")
//    Page<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);

//    @Query("select new com.chat.pojo.entity.Message(m.id,m.chat_flag,m.content_flag,m.text_message,"+
//            "m.picture_message,m.video_message,m.sender_id,m.send_time," +
//            "m.receiver_id,m.receiver_group_id,m.received) from message m "+
//            "where (m.senderId = :senderId AND m.receiverId = :receiverId) OR (m.senderId = :receiverId AND m.receiverId = :senderId)")
//    Page<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);

//    @Query(value="select * from message where (sender_id=?1 and receiver_id=?2) or (sender_id=?2 and receiver_id=?1)")
//    Page<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);


//    //查询语句中的表名则是对应的项目中实体类的类名
//        @Query(value = "select m from com.chat.pojo.entity.Message m "+
//            "where (m.senderId= :senderId and m.receiverId= :receiverId) or (m.senderId= :receiverId and m.receiverId= :senderId)")
//    Page<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);


    //查询语句中的表名则是对应的项目中实体类的类名
    @Query(value = "select m from com.chat.pojo.entity.Message m where (m.senderId = :senderId and m.receiverId= :receiverId) or (m.senderId= :receiverId and m.receiverId= :senderId)")
    Page<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);

}
