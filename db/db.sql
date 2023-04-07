--
-- Table structure for table `admin_user`
--

DROP TABLE IF EXISTS `admin_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_user` (
  `admin_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `login_user_name` varchar(50) NOT NULL COMMENT '管理员登陆名称',
  `login_password` varchar(50) NOT NULL COMMENT '管理员登陆密码',
  `nick_name` varchar(50) NOT NULL COMMENT '管理员显示昵称',
  `locked` tinyint(4) DEFAULT '0' COMMENT '是否锁定 0未锁定 1已锁定无法登陆',
  PRIMARY KEY (`admin_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user`
--

LOCK TABLES `admin_user` WRITE;
/*!40000 ALTER TABLE `admin_user` DISABLE KEYS */;
INSERT INTO `admin_user` VALUES (1,'admin','E10ADC3949BA59ABBE56E057F20F883E','管理员',0);
/*!40000 ALTER TABLE `admin_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog`
--

DROP TABLE IF EXISTS `blog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog` (
  `blog_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '博客表主键id',
  `blog_title` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '博客标题',
  `blog_sub_url` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '博客自定义路径url',
  `blog_cover_image` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '博客封面图',
  `blog_content` mediumtext CHARACTER SET utf8 COMMENT '博客内容',
  `blog_category_id` int(11) DEFAULT NULL COMMENT '博客分类id',
  `blog_category_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '博客分类(冗余字段)',
  `blog_tags` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '博客标签',
  `blog_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0-草稿 1-发布',
  `blog_views` bigint(20) NOT NULL DEFAULT '0' COMMENT '阅读量',
  `enable_comment` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0-不允许评论 1-允许评论',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0=否 1=是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog`
--

LOCK TABLES `blog` WRITE;
/*!40000 ALTER TABLE `blog` DISABLE KEYS */;
INSERT INTO `blog` VALUES (1,'关于我--Fr1ng3','about','https://www.fr1ng3.top/20210930_142900_20.jpg','## About me\n\n我是Fr1ng3，一名Java开发者，技术一般，经历平平，但是也一直渴望进步，同时也努力活着，为了人生不留遗憾，也希望能够一直做着自己喜欢的事情，得闲时分享心得、分享一些浅薄的经验，等以后老得不能再老了，就说故事已经讲完了,不去奢求圆满。\n\n相信浏览这段话的你也知道，学习是一件极其枯燥而无聊的过程，甚至有时候显得很无助，我也想告诉你，成长就是这样一件残酷的事情，任何成功都不是一蹴而就，需要坚持、需要付出、需要你的毅力，短期可能看不到收获，因为破茧成蝶所耗费的时间不是一天。\n\n## Contact\n\n- 我的邮箱：1450526540@qq.com\n- 我的网站：[我的博客](https://www.fr1ng3.top \"我的博客\")',1,'about me','about me',1,356,0,0,'2021-09-30 14:29:05','2021-09-30 14:29:05');
/*!40000 ALTER TABLE `blog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_category`
--

DROP TABLE IF EXISTS `blog_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类表主键',
  `category_name` varchar(50) NOT NULL COMMENT '分类的名称',
  `category_icon` varchar(50) NOT NULL COMMENT '分类的图标',
  `category_rank` int(11) NOT NULL DEFAULT '1' COMMENT '分类的排序值 被使用的越多数值越大',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0=否 1=是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_category`
--

LOCK TABLES `blog_category` WRITE;
/*!40000 ALTER TABLE `blog_category` DISABLE KEYS */;
INSERT INTO `blog_category` VALUES (1,'AboutMe','/admin/dist/img/category/00.png',1,0,'2021-09-30 14:24:01'),(2,'MarkDown','/admin/dist/img/category/18.png',1,0,'2021-10-11 17:17:13'),(3,'Linux','/admin/dist/img/category/13.png',1,0,'2021-10-13 17:32:12'),(4,'Others','/admin/dist/img/category/19.png',1,0,'2021-10-21 15:54:45'),(5,'MySQL','/admin/dist/img/category/12.png',1,0,'2021-10-21 15:58:12'),(6,'Python','/admin/dist/img/category/05.png',1,0,'2021-11-26 14:14:11');
/*!40000 ALTER TABLE `blog_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_comment`
--

DROP TABLE IF EXISTS `blog_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_comment` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `blog_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联的blog主键',
  `commentator` varchar(50) DEFAULT NULL COMMENT '评论者名称',
  `email` varchar(100) DEFAULT NULL COMMENT '评论人的邮箱',
  `website_url` varchar(50) DEFAULT NULL COMMENT '网址',
  `comment_body` varchar(200) DEFAULT NULL COMMENT '评论内容',
  `comment_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论提交时间',
  `commentator_ip` varchar(20) DEFAULT NULL COMMENT '评论时的ip地址',
  `reply_body` varchar(200) DEFAULT NULL COMMENT '回复内容',
  `reply_create_time` datetime DEFAULT NULL COMMENT '回复时间',
  `comment_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否审核通过 0-未审核 1-审核通过',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-未删除 1-已删除',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_comment`
--

LOCK TABLES `blog_comment` WRITE;
/*!40000 ALTER TABLE `blog_comment` DISABLE KEYS */;
INSERT INTO `blog_comment` VALUES (1,5,'testman','admin@myblog.com',NULL,'测试评论','2021-10-13 17:29:51',NULL,'测试回复','2021-10-13 17:30:23',1,0);
/*!40000 ALTER TABLE `blog_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_config`
--

DROP TABLE IF EXISTS `blog_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_config` (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置表主键id',
  `config_name` varchar(100) NOT NULL COMMENT '配置项的名称',
  `config_value` varchar(200) NOT NULL COMMENT '配置项的值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_config`
--

LOCK TABLES `blog_config` WRITE;
/*!40000 ALTER TABLE `blog_config` DISABLE KEYS */;
INSERT INTO `blog_config` VALUES (1,'footerAbout','My Blog #Version 1.0','2018-11-11 20:33:23','2021-10-11 19:06:47'),(2,'footerCopyRight','Copyright @ 2021','2018-11-11 20:33:31','2021-09-30 05:45:24'),(3,'footerICP','鄂ICP备2021xxxxxx号-1','2018-11-11 20:33:27','2021-09-30 05:45:24'),(4,'footerPoweredBy','https://github.com/fr1nge333','2018-11-11 20:33:36','2021-09-30 05:45:24'),(5,'footerPoweredByURL','https://github.com/fr1nge333','2018-11-11 20:33:39','2021-09-30 05:45:24'),(6,'websiteDescription','HELLO,I AM Fr1ng3!','2018-11-11 20:33:04','2021-09-24 08:57:50'),(7,'websiteIcon','/admin/dist/img/favicon2.ico','2018-11-11 20:33:11','2018-11-11 22:05:14'),(8,'websiteLogo','/admin/dist/img/logo2.png','2018-11-11 20:33:08','2021-10-13 14:06:34'),(9,'websiteName','My Blog','2018-11-11 20:33:01','2021-10-01 03:41:49'),(10,'yourAvatar','/admin/dist/img/fr1ng3.jpeg','2018-11-11 20:33:14','2021-10-13 17:16:23'),(11,'yourEmail','1450526540@qq.com','2018-11-11 20:33:17','2019-05-07 21:56:23'),(12,'yourName','Fr1ng3','2018-11-11 20:33:20','2019-05-07 21:56:23'),(13,'yourCareer','Java开发工程师','2018-11-11 20:33:20','2019-05-07 21:56:23'),(14,'backgroudImg','https://www.fr1ng3.top/20211014_000133_52.png','2021-10-12 17:39:52','2021-10-14 00:01:53');
/*!40000 ALTER TABLE `blog_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_file`
--

DROP TABLE IF EXISTS `blog_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_file` (
  `file_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_name` varchar(200) DEFAULT NULL COMMENT '文件显示名称',
  `file_real_name` varchar(200) DEFAULT NULL COMMENT '文件真实名称',
  `file_url` varchar(200) DEFAULT NULL COMMENT '文件路径',
  `file_req_url` varchar(300) DEFAULT NULL COMMENT '文件请求路径',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `file_md5` varchar(200) DEFAULT NULL COMMENT '文件MD5',
  `file_type` varchar(1) DEFAULT NULL COMMENT '文件类型：1-图片，2-其他',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_file`
--

LOCK TABLES `blog_file` WRITE;
/*!40000 ALTER TABLE `blog_file` DISABLE KEYS */;
INSERT INTO `blog_file` VALUES (1,'backiee-43128.jpg','20210930_142900_20.jpg','/opt/blog/uploadfile/20210930_142900_20.jpg',NULL,558340,'5048FC446884B311456F67D62ED7AFD9','1','2021-09-30 06:29:01'),(2,'backiee-194639.jpg','20211011_175203_36.jpg','/opt/blog/uploadfile/20211011_175203_36.jpg',NULL,1400016,'B364B63F800B937B4696D8FAB7E882FA','1','2021-10-11 17:52:04'),(3,'night.png','20211014_000133_52.png','/opt/blog/uploadfile/20211014_000133_52.png',NULL,1746044,'74961132904C587620856C8546484BC4','1','2021-10-14 00:01:33');
/*!40000 ALTER TABLE `blog_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_link`
--

DROP TABLE IF EXISTS `blog_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_link` (
  `link_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '友链表主键id',
  `link_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '友链类别 0-友链 1-推荐 2-个人网站',
  `link_name` varchar(50) NOT NULL COMMENT '网站名称',
  `link_url` varchar(100) NOT NULL COMMENT '网站链接',
  `link_description` varchar(100) NOT NULL COMMENT '网站描述',
  `link_rank` int(11) NOT NULL DEFAULT '0' COMMENT '用于列表排序',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_link`
--

LOCK TABLES `blog_link` WRITE;
/*!40000 ALTER TABLE `blog_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_tag`
--

DROP TABLE IF EXISTS `blog_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_tag` (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '标签表主键id',
  `tag_name` varchar(100) NOT NULL COMMENT '标签名称',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0=否 1=是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_tag`
--

LOCK TABLES `blog_tag` WRITE;
/*!40000 ALTER TABLE `blog_tag` DISABLE KEYS */;
INSERT INTO `blog_tag` VALUES (1,'about me',0,'2021-09-30 16:11:29'),(3,'markdown',0,'2021-10-11 17:32:42'),(5,'Linux命令',0,'2021-10-15 11:12:57'),(6,'crontab',0,'2021-10-15 11:12:57'),(10,'touch',0,'2021-10-21 15:49:25'),(11,'MySQL',0,'2021-10-21 16:06:11'),(12,'mysqldump',0,'2021-10-21 16:06:11'),(13,'vi/vim',0,'2021-10-25 16:29:37'),(14,'cat',0,'2021-10-25 16:30:18'),(15,'grep',0,'2021-10-25 16:31:08'),(16,'Python',0,'2021-11-26 14:25:15'),(17,'Python',0,'2021-11-26 14:25:15'),(18,'Python',0,'2021-11-26 14:25:15'),(19,'Python',0,'2021-11-26 14:25:15'),(20,'Python',0,'2021-11-26 14:25:15'),(21,'Python',0,'2021-11-26 14:25:15');
/*!40000 ALTER TABLE `blog_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_tag_relation`
--

DROP TABLE IF EXISTS `blog_tag_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_tag_relation` (
  `relation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系表id',
  `blog_id` bigint(20) NOT NULL COMMENT '博客id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_tag_relation`
--

LOCK TABLES `blog_tag_relation` WRITE;
/*!40000 ALTER TABLE `blog_tag_relation` DISABLE KEYS */;
INSERT INTO `blog_tag_relation` VALUES (1,1,1,'2021-10-08 11:43:03');
/*!40000 ALTER TABLE `blog_tag_relation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-29 14:29:00
