-- MySQL dump 10.13  Distrib 8.0.37, for Linux (x86_64)
--
-- Host: localhost    Database: chat_online
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `email_verification`
--

DROP TABLE IF EXISTS `email_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_verification` (
  `email` varchar(255) NOT NULL,
  `verification_code` varchar(10) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_verification`
--

LOCK TABLES `email_verification` WRITE;
/*!40000 ALTER TABLE `email_verification` DISABLE KEYS */;
INSERT INTO `email_verification` VALUES ('12324444433@qq.com','NtNgP9','2024-05-29 01:14:21'),('1234@qq.com','gEjK5P','2024-05-21 10:51:29'),('123546@qq.com','P3u3cm','2024-05-30 11:11:43'),('123588@qq.com','0oPYxx','2024-05-30 11:14:06'),('124234333@qq.com','9ga615','2024-05-29 01:15:24'),('158@qq.com','cZlhN6','2024-05-28 02:56:05'),('1742661245@qq.com','Yo6Jn4','2024-05-29 01:09:09'),('1774078982@qq.com','GCPMDE','2024-05-21 10:28:15'),('188@qq.com','6sUE7J','2024-05-28 02:53:24'),('192@qq.com','Wsjo1z','2024-05-29 00:48:57'),('1988@qq.com','irF3C2','2024-05-29 01:00:23'),('34231111@qq.com','s30gzA','2024-05-29 01:04:34'),('888@qq.com','IrG876','2024-06-04 03:46:08');
/*!40000 ALTER TABLE `email_verification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_request`
--

DROP TABLE IF EXISTS `friend_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `from_user_id` bigint DEFAULT NULL,
  `to_user_id` bigint DEFAULT NULL,
  `from_user_username` varchar(45) DEFAULT NULL,
  `from_user_name` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `send_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_request`
--

LOCK TABLES `friend_request` WRITE;
/*!40000 ALTER TABLE `friend_request` DISABLE KEYS */;
INSERT INTO `friend_request` VALUES (1,1,2,NULL,NULL,'REJECT','2024-05-23 11:58:25'),(2,1,2,NULL,NULL,'REJECT','2024-05-23 11:58:25'),(3,1,2,NULL,NULL,'REJECT','2024-05-23 11:58:25'),(4,1,2,NULL,NULL,'REJECT','2024-05-23 11:58:25'),(5,1,2,NULL,NULL,'REJECT','2024-05-27 16:14:12'),(6,1,2,NULL,NULL,'REJECT','2024-05-27 16:28:56'),(7,1,2,NULL,NULL,'REJECT','2024-05-27 16:29:31'),(8,1,2,NULL,NULL,'REJECT','2024-05-27 16:29:43'),(9,1,2,NULL,NULL,'REJECT','2024-05-27 16:31:55'),(10,1,2,NULL,NULL,'REJECT','2024-05-27 16:32:01'),(11,1,3,NULL,NULL,'PENDING','2024-05-27 16:32:25'),(12,1,2,NULL,NULL,'REJECT','2024-05-23 11:58:25'),(13,1,2,NULL,NULL,'REJECT','2024-05-27 16:34:18'),(14,1,3,NULL,NULL,'PENDING','2024-05-30 21:33:37'),(15,1,2,NULL,NULL,'REJECT','2024-06-02 11:00:19'),(16,1,14,NULL,NULL,'ACCEPT','2024-06-02 11:08:42'),(19,14,1,'xiaoming','小明','ACCEPT','2024-06-02 17:31:25'),(20,1,7,'zhangsan','张三','ACCEPT','2024-06-07 08:41:24'),(21,1,7,'zhangsan','张三','REJECT','2024-06-07 08:42:42'),(22,1,7,'zhangsan','张三','REJECT','2024-06-07 08:46:35'),(23,1,7,'zhangsan','张三','ACCEPT','2024-06-07 08:47:01'),(24,1,7,'zhangsan','张三','ACCEPT','2024-06-07 08:47:38'),(25,1,7,'zhangsan','张三','ACCEPT','2024-06-07 08:59:59'),(26,1,7,'zhangsan','张三','ACCEPT','2024-06-07 09:00:14'),(27,1,7,'zhangsan','张三','ACCEPT','2024-06-07 09:00:20'),(28,1,2,'zhangsan','张三','REJECT','2024-06-07 12:23:54'),(29,1,2,'zhangsan','张三','ACCEPT','2024-06-07 12:24:54'),(30,1,14,'zhangsan','张三','ACCEPT','2024-06-07 12:31:05');
/*!40000 ALTER TABLE `friend_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group` (
  `group_id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `group_group_id_uindex` (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group`
--

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
INSERT INTO `group` VALUES (1,'聊天交友群','2024-05-22 14:07:17','2024-05-22 14:48:04',NULL),(2,'聊天交友群','2024-05-22 14:07:31','2024-05-22 14:48:07',NULL),(3,'学习交流群','2024-05-22 14:07:46','2024-05-22 14:48:09',NULL);
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_member`
--

DROP TABLE IF EXISTS `group_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_member` (
  `group_id` bigint NOT NULL,
  `member_id` bigint DEFAULT NULL,
  `member_permission` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_member`
--

LOCK TABLES `group_member` WRITE;
/*!40000 ALTER TABLE `group_member` DISABLE KEYS */;
INSERT INTO `group_member` VALUES (1,1,0),(3,2,0),(2,4,0),(1,2,1),(3,1,1);
/*!40000 ALTER TABLE `group_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_request`
--

DROP TABLE IF EXISTS `group_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `from_user_id` bigint NOT NULL,
  `group_id` bigint NOT NULL,
  `group_creator_id` bigint NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_request`
--

LOCK TABLES `group_request` WRITE;
/*!40000 ALTER TABLE `group_request` DISABLE KEYS */;
INSERT INTO `group_request` VALUES (3,1,3,2,'PENDING'),(4,1,2,4,'PENDING'),(5,1,2,4,'PENDING');
/*!40000 ALTER TABLE `group_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_unread_num`
--

DROP TABLE IF EXISTS `group_unread_num`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_unread_num` (
  `group_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `unread_num` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`group_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_unread_num`
--

LOCK TABLES `group_unread_num` WRITE;
/*!40000 ALTER TABLE `group_unread_num` DISABLE KEYS */;
INSERT INTO `group_unread_num` VALUES (1,1,0),(1,2,0),(1,3,21);
/*!40000 ALTER TABLE `group_unread_num` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chat_flag` int NOT NULL,
  `content_flag` int NOT NULL,
  `text_message` varchar(255) DEFAULT NULL,
  `picture_message` varchar(255) DEFAULT NULL,
  `video_message` varchar(255) DEFAULT NULL,
  `sender_id` bigint NOT NULL,
  `send_time` datetime NOT NULL,
  `receiver_id` bigint DEFAULT NULL,
  `receive_group_id` bigint DEFAULT NULL,
  `received` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `message_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (28,0,0,'发给张三看一下',NULL,NULL,2,'2024-05-24 11:17:02',1,NULL,1),(29,0,0,'给李四发消息',NULL,NULL,1,'2024-05-24 12:38:39',2,NULL,1),(30,0,0,'给李四再发一下消息',NULL,NULL,1,'2024-05-24 16:47:36',2,NULL,1),(31,1,0,'啊啊啊啊啊啊啊啊',NULL,NULL,1,'2024-05-24 17:00:21',NULL,1,0),(32,1,0,'再发个群聊消息',NULL,NULL,1,'2024-05-24 17:02:05',NULL,1,0),(33,1,0,'我要这铁棒有何用',NULL,NULL,1,'2024-05-24 17:03:45',NULL,1,0),(34,1,0,'我要 这金箍又如何',NULL,NULL,2,'2024-05-24 17:04:19',NULL,1,0),(35,0,0,'再再发一下',NULL,NULL,1,'2024-05-24 17:11:35',2,NULL,1),(36,1,0,'师傅别念了',NULL,NULL,1,'2024-05-24 17:11:52',NULL,1,0),(37,1,0,'测试收在群聊还是私聊',NULL,NULL,2,'2024-05-24 18:18:04',NULL,1,0),(38,1,0,'测试收在群聊还是私聊',NULL,NULL,2,'2024-05-24 18:18:31',NULL,1,0),(39,1,0,'测试收在群聊还是私聊',NULL,NULL,1,'2024-05-24 18:18:56',NULL,1,0),(40,1,0,'ttt5',NULL,NULL,2,'2024-05-24 21:57:27',NULL,1,0),(41,1,0,'发个试试',NULL,NULL,2,'2024-05-25 10:32:59',NULL,1,0),(42,1,0,'再发个试试',NULL,NULL,2,'2024-05-25 10:33:28',NULL,1,0),(43,1,0,'测试群新消息显示',NULL,NULL,2,'2024-05-25 10:48:06',NULL,1,0),(44,1,0,'再测试一下',NULL,NULL,2,'2024-05-25 10:50:59',NULL,1,0),(45,1,0,'再发个试试',NULL,NULL,1,'2024-05-25 11:37:51',NULL,1,0),(46,1,0,'再试试',NULL,NULL,1,'2024-05-25 11:41:26',NULL,1,0),(47,1,0,'测试测试',NULL,NULL,1,'2024-05-25 11:45:42',NULL,1,0),(48,1,0,'哇哈哈哈哈好测',NULL,NULL,2,'2024-05-25 11:45:56',NULL,1,0),(49,1,0,'我也试试',NULL,NULL,2,'2024-05-25 11:46:11',NULL,1,0),(50,0,0,'嗨嗨嗨',NULL,NULL,2,'2024-05-25 11:53:15',1,NULL,1),(51,0,0,'嗯嗯',NULL,NULL,1,'2024-05-25 11:53:58',2,NULL,1),(52,0,0,'你在呀',NULL,NULL,2,'2024-05-25 11:54:05',1,NULL,1),(53,0,0,'发个消息看看',NULL,NULL,2,'2024-05-25 11:59:00',1,NULL,1),(54,0,0,'好好好',NULL,NULL,1,'2024-05-25 11:59:09',2,NULL,1),(55,0,0,'我也来',NULL,NULL,2,'2024-05-25 11:59:30',1,NULL,1),(56,0,0,'再试试',NULL,NULL,1,'2024-05-25 11:59:41',2,NULL,1),(57,1,0,'来了来了',NULL,NULL,1,'2024-05-25 12:00:13',NULL,1,0),(58,1,0,'试试就试试',NULL,NULL,2,'2024-05-25 12:00:20',NULL,1,0),(59,1,0,'新消息',NULL,NULL,1,'2024-05-25 12:02:41',NULL,1,0),(60,1,0,'在线接收测试',NULL,NULL,2,'2024-05-25 12:02:50',NULL,1,0),(61,1,0,'在线接收测试',NULL,NULL,2,'2024-05-25 12:03:09',NULL,1,0),(62,1,0,'在线接收测试2',NULL,NULL,2,'2024-05-25 12:03:36',NULL,1,0),(63,1,0,'交友群消息',NULL,NULL,2,'2024-05-25 12:05:18',NULL,1,0),(64,1,0,'嗨嗨嗨，测试来啦',NULL,NULL,2,'2024-05-25 12:13:34',NULL,1,0),(65,1,0,'来来来，在线聊天',NULL,NULL,2,'2024-05-25 12:15:21',NULL,1,0),(66,0,0,'在线聊天',NULL,NULL,2,'2024-05-25 12:15:43',1,NULL,1),(67,0,0,'来来来',NULL,NULL,1,'2024-05-25 12:24:00',2,NULL,1),(68,1,0,'好家伙',NULL,NULL,1,'2024-05-25 12:24:14',NULL,1,0),(69,0,0,'你好',NULL,NULL,1,'2024-05-25 14:18:21',2,NULL,1),(70,0,0,'lalalala',NULL,NULL,1,'2024-05-25 14:19:02',2,NULL,1),(71,0,0,'dadadadadada',NULL,NULL,1,'2024-05-25 14:19:10',2,NULL,1),(72,1,0,'dadadadadadadada',NULL,NULL,2,'2024-05-25 14:19:25',NULL,1,0),(73,0,0,'wuwuwuw',NULL,NULL,1,'2024-05-25 14:22:06',2,NULL,1),(74,0,0,'ww',NULL,NULL,1,'2024-05-25 14:22:23',2,NULL,1),(75,0,0,'ww',NULL,NULL,1,'2024-05-25 14:22:35',2,NULL,1),(76,0,0,'111111',NULL,NULL,1,'2024-05-25 14:26:56',2,NULL,1),(77,0,0,'11111111',NULL,NULL,2,'2024-05-25 14:27:14',1,NULL,1),(78,1,0,'张三发言：01230192041',NULL,NULL,1,'2024-05-25 14:28:24',NULL,1,0),(79,1,0,'太强了',NULL,NULL,2,'2024-05-25 14:29:50',NULL,1,0),(80,1,0,'大佬们',NULL,NULL,2,'2024-05-25 14:29:57',NULL,1,0),(81,0,0,'阿巴阿巴',NULL,NULL,2,'2024-05-30 21:51:13',3,NULL,1),(82,0,0,'哈皮',NULL,NULL,2,'2024-05-30 21:51:43',3,NULL,1),(83,0,0,'快来呀',NULL,NULL,2,'2024-05-30 21:52:03',3,NULL,1),(84,0,0,'来呀快活呀',NULL,NULL,3,'2024-05-30 21:53:42',2,NULL,1),(85,0,0,'好好好',NULL,NULL,2,'2024-05-30 21:53:56',3,NULL,1),(86,0,0,'111',NULL,NULL,7,'2024-06-07 09:15:58',1,NULL,1),(87,0,0,'333',NULL,NULL,7,'2024-06-07 09:16:02',1,NULL,1),(88,0,0,'嗨嗨嗨',NULL,NULL,2,'2024-06-10 16:19:19',1,NULL,1),(89,0,0,'嗨嗨嗨',NULL,NULL,1,'2024-06-10 16:20:41',2,NULL,1),(90,0,0,'1',NULL,NULL,2,'2024-06-10 16:41:09',1,NULL,1);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(64) NOT NULL,
  `sex` varchar(8) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `state` int DEFAULT NULL,
  `email` varchar(64) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_email_uindex` (`email`),
  UNIQUE KEY `user_username_uindex` (`username`),
  UNIQUE KEY `user_user_id_uindex` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'张三','zhangsan','e10adc3949ba59abbe56e057f20f883e','男','https://s2.loli.net/2024/05/20/Fcaygip6dUtmJCq.jpg',0,'123@qq.com','2024-05-20 22:13:47','2024-05-20 22:13:50'),(2,'李四','lisi','e10adc3949ba59abbe56e057f20f883e','女','https://s2.loli.net/2024/05/20/YuRryljA9KtLQBF.jpg',0,'234@qq.com','2024-05-20 22:14:43','2024-05-20 22:14:46'),(3,'王五','wangwu','e10adc3949ba59abbe56e057f20f883e','男','https://s2.loli.net/2024/05/20/YuRryljA9KtLQBF.jpg',0,'345@qq.com','2024-05-21 14:36:08','2024-05-21 14:36:09'),(4,'曾锐','zr','fc91844b08b280318b27c15b55144361',NULL,NULL,NULL,'1774078982@qq.com','2024-05-21 18:32:20','2024-05-21 18:32:20'),(6,'老六','laoliu','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,'188@qq.com','2024-05-28 10:53:30','2024-05-28 10:53:30'),(7,'刘七','liuqi','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,'158@qq.com','2024-05-28 10:56:10','2024-05-28 10:56:10'),(9,'哈哈','hh','123456',NULL,NULL,NULL,'182@qq.com','2024-05-29 08:59:09','2024-05-29 08:59:09'),(11,'张七','zhangqi','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,'34231111@qq.com','2024-05-29 09:05:06','2024-05-29 09:05:06'),(12,'王八','wangba','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,'1742661245@qq.com','2024-05-29 09:09:17','2024-05-29 09:09:17'),(13,'小红','xiaohong','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,'12324444433@qq.com','2024-05-29 09:14:30','2024-05-29 09:14:30'),(14,'小明','xiaoming','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,'124234333@qq.com','2024-05-29 09:15:33','2024-05-29 09:15:33'),(18,'测试','test4','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,'123588@qq.com','2024-05-30 19:21:45','2024-05-30 19:21:45'),(19,'这是一个很长长长长长长长长长长长长长长长长长长长长长长长的名字','alongname','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,'888@qq.com','2024-06-04 11:48:06','2024-06-04 11:48:06');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_friend`
--

DROP TABLE IF EXISTS `user_friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_friend` (
  `user_id` bigint NOT NULL,
  `friend_id` bigint NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `grouping_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_friend`
--

LOCK TABLES `user_friend` WRITE;
/*!40000 ALTER TABLE `user_friend` DISABLE KEYS */;
INSERT INTO `user_friend` VALUES (1,3,NULL,2),(3,1,NULL,NULL),(2,3,NULL,NULL),(3,2,NULL,NULL),(1,4,NULL,2),(1,9,NULL,NULL),(1,19,NULL,NULL),(1,12,NULL,NULL),(1,13,NULL,NULL),(1,18,NULL,NULL),(1,7,NULL,NULL),(7,1,NULL,NULL),(1,2,NULL,NULL),(2,1,NULL,NULL),(1,14,NULL,NULL),(14,1,NULL,NULL);
/*!40000 ALTER TABLE `user_friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_grouping`
--

DROP TABLE IF EXISTS `user_grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_grouping` (
  `grouping_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `grouping_name` varchar(255) NOT NULL,
  PRIMARY KEY (`grouping_id`),
  KEY `userId_idx` (`user_id`),
  CONSTRAINT `userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_grouping`
--

LOCK TABLES `user_grouping` WRITE;
/*!40000 ALTER TABLE `user_grouping` DISABLE KEYS */;
INSERT INTO `user_grouping` VALUES (1,1,'好基友'),(2,1,'同学'),(4,1,'大学'),(5,1,'家人');
/*!40000 ALTER TABLE `user_grouping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_impression`
--

DROP TABLE IF EXISTS `user_impression`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_impression` (
  `impression_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  `content_flag` int NOT NULL,
  `impression_text` varchar(255) DEFAULT NULL,
  `impression_picture` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`impression_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_impression`
--

LOCK TABLES `user_impression` WRITE;
/*!40000 ALTER TABLE `user_impression` DISABLE KEYS */;
INSERT INTO `user_impression` VALUES (1,1,2,0,'为人正直',NULL),(2,2,1,0,'哈哈哈哈哈哈哈',NULL);
/*!40000 ALTER TABLE `user_impression` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-10 19:43:39
