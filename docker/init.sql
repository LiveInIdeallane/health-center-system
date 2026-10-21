-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: db_service
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `backdown`
--

DROP TABLE IF EXISTS `backdown`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backdown` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `remarks` varchar(100) DEFAULT NULL COMMENT '备注',
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `customer_id` int NOT NULL COMMENT '客户ID',
  `retreattime` date NOT NULL COMMENT '退住时间',
  `retreattype` int NOT NULL COMMENT '退住类型 0-正常退住 1-死亡退住 2-保留床位',
  `retreatreason` varchar(100) DEFAULT NULL COMMENT '退住原因',
  `auditstatus` int NOT NULL DEFAULT '0' COMMENT '审批状态 0-已提交 1-同意 2-拒绝',
  `auditperson` varchar(100) DEFAULT NULL COMMENT '审批人',
  `audittime` date DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退住表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backdown`
--

LOCK TABLES `backdown` WRITE;
/*!40000 ALTER TABLE `backdown` DISABLE KEYS */;
INSERT INTO `backdown` VALUES (1,NULL,0,2,'2026-10-20',1,'合同到期，家属接回',1,'张大明','2026-10-20'),(2,NULL,0,4,'2026-10-22',3,'暂时保留床位',0,NULL,NULL),(3,NULL,0,6,'2026-10-25',2,'因病离世',0,NULL,NULL),(4,NULL,0,8,'2026-09-30',1,'转去其他养老机构',1,'张大明','2026-09-28'),(5,NULL,0,11,'2026-09-20',1,'家属要求退住',2,'张大明','2026-09-19');
/*!40000 ALTER TABLE `backdown` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bed`
--

DROP TABLE IF EXISTS `bed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bed` (
  `id` int NOT NULL,
  `room_no` int NOT NULL,
  `bed_status` int NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `bed_no` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bed`
--

LOCK TABLES `bed` WRITE;
/*!40000 ALTER TABLE `bed` DISABLE KEYS */;
INSERT INTO `bed` VALUES (1,101,2,'在住','101-1'),(2,102,1,'在住','102-1'),(3,103,2,'在住','103-1'),(4,104,2,'在住','104-1'),(5,105,2,'在住','105-1'),(6,201,2,'在住','201-1'),(7,202,2,'在住','202-1'),(8,203,2,'在住','203-1'),(9,204,2,'在住','204-1'),(10,205,2,'在住','205-1'),(11,301,2,'在住','301-1'),(12,302,2,'在住','302-1'),(13,303,2,'在住','303-1'),(14,304,2,'在住','304-1'),(15,305,2,'在住','305-1'),(16,401,2,'在住','401-1'),(17,402,2,'在住','402-1'),(18,403,2,'在住','403-1'),(19,404,2,'在住','404-1'),(20,405,2,'在住','405-1'),(21,101,1,'空闲','101-2'),(22,102,1,'空闲','102-2'),(23,103,1,'空闲','103-2'),(24,104,1,'空闲','104-2'),(25,105,1,'空闲','105-2'),(26,201,1,'空闲','201-2'),(27,202,1,'空闲','202-2'),(28,203,1,'空闲','203-2'),(29,204,1,'空闲','204-2'),(30,205,1,'空闲','205-2'),(102,1003,2,'测试床位','1003-1'),(103,1003,3,'测试','1003-3');
/*!40000 ALTER TABLE `bed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `beddetails`
--

DROP TABLE IF EXISTS `beddetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beddetails` (
  `id` int NOT NULL AUTO_INCREMENT,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `bed_details` varchar(255) DEFAULT NULL,
  `customer_id` int DEFAULT NULL,
  `bed_id` int DEFAULT NULL,
  `is_deleted` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=265 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beddetails`
--

LOCK TABLES `beddetails` WRITE;
/*!40000 ALTER TABLE `beddetails` DISABLE KEYS */;
INSERT INTO `beddetails` VALUES (245,'2024-03-01',NULL,'入住使用',1,1,0),(246,'2024-05-15',NULL,'入住使用',2,2,0),(247,'2024-01-10',NULL,'入住使用',3,3,0),(248,'2024-06-01',NULL,'入住使用',4,4,0),(249,'2024-02-20',NULL,'入住使用',5,5,0),(250,'2024-04-12',NULL,'入住使用',6,6,0),(251,'2024-07-08',NULL,'入住使用',7,7,0),(252,'2024-08-16',NULL,'入住使用',8,8,0),(253,'2024-09-01',NULL,'入住使用',9,9,0),(254,'2024-03-25',NULL,'入住使用',10,10,0),(255,'2024-05-05',NULL,'入住使用',11,11,0),(256,'2024-06-18',NULL,'入住使用',12,12,0),(257,'2024-07-22',NULL,'入住使用',13,13,0),(258,'2024-08-30',NULL,'入住使用',14,14,0),(259,'2024-09-14',NULL,'入住使用',15,15,0),(260,'2024-04-02',NULL,'入住使用',16,16,0),(261,'2024-05-28',NULL,'入住使用',17,17,0),(262,'2024-06-26',NULL,'入住使用',18,18,0),(263,'2024-07-15',NULL,'入住使用',19,19,0),(264,'2024-08-09',NULL,'入住使用',20,20,0);
/*!40000 ALTER TABLE `beddetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `customer_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户姓名',
  `customer_age` int NOT NULL COMMENT '年龄',
  `customer_sex` int NOT NULL COMMENT '性别 0：男 1：女',
  `idcard` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
  `room_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '房间号',
  `building_no` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属楼房',
  `checkin_date` date NOT NULL COMMENT '入住时间',
  `expiration_date` date NOT NULL COMMENT '合同到期时间',
  `contact_tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系电话',
  `bed_id` int NOT NULL COMMENT '床号',
  `psychosomatic_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身心状况',
  `attention` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '注意事项',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `height` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身高',
  `weight` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '体重',
  `blood_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '血型',
  `filepath` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像路径',
  `user_id` int DEFAULT NULL COMMENT '关联健康管家(护工)，无管家设置-1',
  `level_id` int DEFAULT NULL COMMENT '护理等级',
  `family_member` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '家属',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='客户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,0,'张伟',72,1,'110101195203051234','101','1号楼','2024-03-01','2026-03-01','13800001234',1,'良好','高血压，需按时服药','1952-03-05','172','68','A','',2,1,'张建国'),(2,0,'张丽',68,0,'110101195607125678','102','1号楼','2024-05-15','2026-05-15','13800005678',2,'良好',NULL,'1956-07-12','158','55','B','',2,1,'张晓明'),(3,0,'张敏',75,0,'320101194911234321','103','1号楼','2024-01-10','2026-01-10','13900001122',3,'一般','糖尿病','1949-11-23','155','58','O','',3,1,'张红星'),(4,0,'王芳',70,0,'320101195409186789','104','1号楼','2024-06-01','2026-06-01','13900003344',4,'良好',NULL,'1954-09-18','160','57','A','',3,2,'王大军'),(5,0,'王强',78,1,'320101194612053217','105','1号楼','2024-02-20','2026-02-20','15500008899',5,'一般','腿脚不便，需轮椅','1946-12-05','175','72','AB','',6,2,'王丽娟'),(6,0,'李明',66,1,'330101195805061129','201','2号楼','2024-04-12','2026-04-12','15500006677',6,'良好',NULL,'1958-05-06','170','65','O','',6,3,'李国强'),(7,0,'李娜',71,0,'330101195307194456','202','2号楼','2024-07-08','2026-07-08','18600001234',7,'良好','轻度听力障碍','1953-07-19','159','54','B','',7,1,'李伟'),(8,0,'陈静',69,0,'440101195505247788','203','2号楼','2024-08-16','2026-08-16','18600005566',8,'良好',NULL,'1955-05-24','161','56','A','',NULL,1,'陈志强'),(9,0,'陈晓',63,1,'440101196112301357','204','2号楼','2024-09-01','2026-09-01','18600009900',9,'良好',NULL,'1961-12-30','174','70','O','',7,2,'陈小花'),(10,0,'刘洋',74,1,'110101195004082468','205','2号楼','2024-03-25','2026-03-25','17700001111',10,'一般','冠心病，随身带药','1950-04-08','171','69','A','',8,3,'刘海涛'),(11,0,'杨光',76,1,'110101194708163579','301','3号楼','2024-05-05','2026-05-05','17700002222',11,'良好',NULL,'1947-08-16','176','73','B','',2,2,'杨建华'),(12,0,'赵磊',65,1,'320101195909014680','302','3号楼','2024-06-18','2026-06-18','17700003333',12,'良好',NULL,'1959-09-01','173','67','O','',NULL,1,'赵春燕'),(13,0,'黄敏',73,0,'330101195107131234','303','3号楼','2024-07-22','2026-07-22','13800004444',13,'一般','失眠，睡前需巡视','1951-07-13','157','53','AB','',NULL,2,'黄志明'),(14,0,'周涛',79,1,'440101194503067890','304','3号楼','2024-08-30','2026-08-30','13800005555',14,'良好',NULL,'1945-03-06','177','75','A','',NULL,3,'周雪梅'),(15,0,'吴霞',67,0,'110101195706142345','305','3号楼','2024-09-14','2026-09-14','13900006666',15,'良好',NULL,'1957-06-14','156','52','B','',NULL,1,'吴建军'),(16,0,'徐军',76,1,'320101194808197766','401','4号楼','2024-04-02','2026-04-02','13900007777',16,'一般','帕金森前期，手抖','1948-08-19','174','71','O','',NULL,4,'徐婷'),(17,0,'孙倩',64,0,'330101196009203456','402','4号楼','2024-05-28','2026-05-28','15500008888',17,'良好',NULL,'1960-09-20','162','59','A','',NULL,2,'孙立'),(18,0,'马超',71,1,'440101195301158899','403','4号楼','2024-06-26','2026-06-26','15500009999',18,'良好',NULL,'1953-01-15','175','68','AB','',NULL,3,'马丽丽'),(19,0,'朱婷',62,0,'110101196208041122','404','4号楼','2024-07-15','2026-07-15','18600001111',19,'良好','低盐饮食','1962-08-04','158','56','B','',NULL,1,'朱建华'),(20,0,'胡兵',70,1,'320101195407173344','405','4号楼','2024-08-09','2026-08-09','18600002222',20,'一般','高血脂','1954-07-17','172','74','O','',NULL,5,'胡晓东');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customernurseitem`
--

DROP TABLE IF EXISTS `customernurseitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customernurseitem` (
  `id` int NOT NULL,
  `item_id` int NOT NULL,
  `customer_id` int NOT NULL,
  `level_id` int DEFAULT NULL,
  `nurse_number` int NOT NULL DEFAULT '1',
  `is_deleted` int NOT NULL DEFAULT '0',
  `buy_time` date DEFAULT NULL,
  `maturity_time` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customernurseitem`
--

LOCK TABLES `customernurseitem` WRITE;
/*!40000 ALTER TABLE `customernurseitem` DISABLE KEYS */;
INSERT INTO `customernurseitem` VALUES (1,1,3,1,4,0,'2026-07-01','2027-01-01'),(2,2,3,1,30,0,'2026-07-01','2027-01-01'),(3,2,5,2,20,0,'2026-08-01','2026-12-31'),(4,3,5,2,15,0,'2026-08-01','2026-12-31'),(5,4,8,1,10,0,'2026-06-15','2026-12-15'),(6,2,10,3,25,0,'2026-09-01','2027-03-01'),(7,5,13,2,12,0,'2026-05-20','2026-11-20'),(8,6,16,4,8,0,'2026-04-01','2027-04-01'),(9,7,19,1,6,0,'2026-09-10','2026-12-10'),(10,2,2,1,18,0,'2026-08-18','2026-11-18'),(11,1,20,5,5,0,'2026-03-01','2027-03-01'),(12,1,2,1,1,0,'2026-10-20','2026-10-20');
/*!40000 ALTER TABLE `customernurseitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food`
--

DROP TABLE IF EXISTS `food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food` (
  `id` int NOT NULL AUTO_INCREMENT,
  `food_name` varchar(100) NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `food_type` varchar(50) DEFAULT NULL,
  `is_halal` int DEFAULT '0',
  `food_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food`
--

LOCK TABLES `food` WRITE;
/*!40000 ALTER TABLE `food` DISABLE KEYS */;
INSERT INTO `food` VALUES (1,'小米粥',3.00,'主食',1,''),(2,'软米饭',2.00,'主食',1,''),(3,'清蒸鲈鱼',18.00,'荤菜',1,''),(4,'西红柿炒蛋',10.00,'素菜',1,''),(5,'青菜豆腐',8.00,'素菜',1,''),(6,'红烧鸡腿',15.00,'荤菜',1,''),(7,'南瓜粥',4.00,'主食',1,''),(8,'水果拼盘',6.00,'加餐',1,'');
/*!40000 ALTER TABLE `food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meal`
--

DROP TABLE IF EXISTS `meal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal` (
  `id` int NOT NULL AUTO_INCREMENT,
  `week_day` varchar(20) NOT NULL,
  `food_id` int NOT NULL,
  `meal_type` int NOT NULL,
  `taste` varchar(50) DEFAULT NULL,
  `is_deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meal`
--

LOCK TABLES `meal` WRITE;
/*!40000 ALTER TABLE `meal` DISABLE KEYS */;
INSERT INTO `meal` VALUES (1,'星期一',1,1,'清淡',0),(2,'星期一',3,2,'咸鲜',0),(3,'星期一',5,3,'清淡',0),(4,'星期二',2,1,'清淡',0),(5,'星期二',6,2,'酱香',0),(6,'星期三',7,1,'微甜',0),(7,'星期三',4,2,'家常',0),(8,'星期四',8,3,'甜',0);
/*!40000 ALTER TABLE `meal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` int NOT NULL,
  `menus_index` varchar(20) DEFAULT NULL,
  `title` varchar(50) NOT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `path` varchar(100) DEFAULT NULL,
  `parent_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'1','客户管理','user','/customer',0),(2,'2','用户管理','setting','/user',0),(3,'3','床位管理','bed','/bed',0),(4,'4','护理管理','nurse','/nurse',0),(5,'5','健康管家','manager','/manager',0),(6,'1-1','入住登记','','/customer/checkin',1),(7,'1-2','退住登记','','/customer/backdown',1),(8,'1-3','外出登记','','/customer/outward',1),(9,'4-1','护理级别','','/nurse/level',4),(10,'4-2','护理项目','','/nurse/item',4);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nursecontent`
--

DROP TABLE IF EXISTS `nursecontent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nursecontent` (
  `id` int NOT NULL AUTO_INCREMENT,
  `serial_number` varchar(50) NOT NULL,
  `nursing_name` varchar(100) NOT NULL,
  `service_price` varchar(20) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `status` int NOT NULL DEFAULT '1',
  `execution_cycle` varchar(50) DEFAULT NULL,
  `execution_times` varchar(50) DEFAULT NULL,
  `is_deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nursecontent`
--

LOCK TABLES `nursecontent` WRITE;
/*!40000 ALTER TABLE `nursecontent` DISABLE KEYS */;
INSERT INTO `nursecontent` VALUES (1,'HL001','助浴服务','80.00','协助老人完成洗浴',1,'每周','2',0),(2,'HL002','测量血压','20.00','每日测量并记录血压',1,'每日','1',0),(3,'HL003','协助进食','30.00','帮助行动不便老人进餐',1,'每日','3',0),(4,'HL004','翻身拍背','40.00','预防褥疮，定时翻身',1,'每日','4',0),(5,'HL005','口腔护理','25.00','早晚口腔清洁',1,'每日','2',0),(6,'HL006','康复训练','100.00','肢体功能康复训练',1,'每周','3',0),(7,'HL007','心理疏导','80.00','陪伴聊天、情绪疏导',1,'每周','1',0),(8,'HL008','晚间巡视','15.00','夜间定时巡视',2,'每日','2',0);
/*!40000 ALTER TABLE `nursecontent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nurselevel`
--

DROP TABLE IF EXISTS `nurselevel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nurselevel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `level_name` varchar(50) NOT NULL,
  `level_status` int NOT NULL DEFAULT '1',
  `is_deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nurselevel`
--

LOCK TABLES `nurselevel` WRITE;
/*!40000 ALTER TABLE `nurselevel` DISABLE KEYS */;
INSERT INTO `nurselevel` VALUES (1,'一级护理',1,0),(2,'二级护理',1,0),(3,'三级护理',1,0),(4,'特级护理',2,0),(5,'专护',1,0);
/*!40000 ALTER TABLE `nurselevel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nurselevelitem`
--

DROP TABLE IF EXISTS `nurselevelitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nurselevelitem` (
  `id` int NOT NULL AUTO_INCREMENT,
  `level_id` int NOT NULL,
  `item_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nurselevelitem`
--

LOCK TABLES `nurselevelitem` WRITE;
/*!40000 ALTER TABLE `nurselevelitem` DISABLE KEYS */;
INSERT INTO `nurselevelitem` VALUES (1,1,1),(2,1,2),(3,1,3),(4,2,2),(5,2,3),(6,3,2),(7,4,1),(8,4,2),(9,4,3),(10,4,4),(11,4,5),(12,5,1),(13,5,2),(14,5,3),(15,5,4),(16,5,5),(17,5,6),(18,5,7);
/*!40000 ALTER TABLE `nurselevelitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nurserecord`
--

DROP TABLE IF EXISTS `nurserecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nurserecord` (
  `id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` int NOT NULL DEFAULT '0',
  `customer_id` int NOT NULL,
  `item_id` int NOT NULL,
  `nursing_time` datetime DEFAULT NULL,
  `nursing_content` varchar(255) DEFAULT NULL,
  `nursing_count` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nurserecord`
--

LOCK TABLES `nurserecord` WRITE;
/*!40000 ALTER TABLE `nurserecord` DISABLE KEYS */;
INSERT INTO `nurserecord` VALUES (1,0,3,1,'2026-10-15 09:20:00','协助完成洗浴，状态良好',1,2),(2,0,3,2,'2026-10-16 08:10:00','血压 135/85，记录正常',1,2),(3,0,5,2,'2026-10-16 08:30:00','血压 142/90，已提醒按时服药',1,3),(4,0,5,3,'2026-10-16 11:40:00','午餐进食正常',1,3),(5,0,8,4,'2026-10-15 15:00:00','完成翻身拍背，皮肤无异常',2,6),(6,0,10,2,'2026-10-17 08:05:00','血压 128/80',1,6),(7,0,13,5,'2026-10-17 20:30:00','晚间口腔护理完成',1,3),(8,0,16,6,'2026-10-14 10:00:00','康复训练 30 分钟',1,6),(9,0,19,7,'2026-10-13 16:20:00','心理疏导 20 分钟',1,2),(10,0,20,1,'2026-10-12 09:00:00','助浴服务完成',1,3);
/*!40000 ALTER TABLE `nurserecord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `outward`
--

DROP TABLE IF EXISTS `outward`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `outward` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `remarks` varchar(100) DEFAULT NULL COMMENT '备注',
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `customer_id` int NOT NULL COMMENT '客户ID',
  `outgoingreason` varchar(100) NOT NULL COMMENT '外出事由',
  `outgoingtime` date NOT NULL COMMENT '外出时间',
  `expectedreturntime` date NOT NULL COMMENT '预计回院时间',
  `actualreturntime` date DEFAULT NULL COMMENT '实际回院时间',
  `escorted` varchar(50) DEFAULT NULL COMMENT '陪同人',
  `relation` varchar(50) DEFAULT NULL COMMENT '与老人关系',
  `escortedtel` varchar(50) DEFAULT NULL COMMENT '陪同人电话',
  `auditstatus` int NOT NULL DEFAULT '0' COMMENT '审批状态 0-已提交 1-同意 2-拒绝',
  `auditperson` varchar(50) DEFAULT NULL COMMENT '审批人',
  `audittime` date DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='外出记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `outward`
--

LOCK TABLES `outward` WRITE;
/*!40000 ALTER TABLE `outward` DISABLE KEYS */;
INSERT INTO `outward` VALUES (1,'家属接回',0,1,'回家探亲','2026-10-15','2026-10-18',NULL,'张建国','儿子','13800001234',0,NULL,NULL),(2,NULL,0,3,'医院复查','2026-10-16','2026-10-17',NULL,'张红星','儿子','13900001122',0,NULL,NULL),(3,'需按时返回',0,5,'参加家庭聚会','2026-10-17','2026-10-19',NULL,'王丽娟','女儿','15500008899',0,NULL,NULL),(4,NULL,0,7,'购买生活用品','2026-10-10','2026-10-10','2026-10-10','李伟','儿子','18600001234',1,'张大明','2026-10-09'),(5,NULL,0,9,'朋友聚会','2026-10-08','2026-10-09',NULL,'陈小花','女儿','18600009900',2,'张大明','2026-10-08');
/*!40000 ALTER TABLE `outward` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preference`
--

DROP TABLE IF EXISTS `preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preference` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` int DEFAULT NULL COMMENT '顾客ID',
  `preferences` varchar(30) DEFAULT NULL COMMENT '饮食喜好',
  `attention` varchar(30) DEFAULT NULL COMMENT '注意事项',
  `remark` varchar(30) DEFAULT NULL COMMENT '备注',
  `is_deleted` int DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='偏好表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preference`
--

LOCK TABLES `preference` WRITE;
/*!40000 ALTER TABLE `preference` DISABLE KEYS */;
INSERT INTO `preference` VALUES (1,1,'清淡饮食，喜食鱼类','低盐低油','早餐喝小米粥',0),(2,2,'喜食甜食','糖尿病，少糖','水果控制在少量',0),(3,3,'爱吃面食','忌辛辣','晚餐少量',0),(4,4,'喜食蔬菜','无特殊忌口','可增加粗粮',0),(5,5,'偏爱软烂食物','腿脚不便，需送餐','午餐需人协助',0);
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `is_deleted` int NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'2026-01-01 09:00:00',NULL,1,0,'系统管理员'),(2,'2026-01-01 09:10:00',NULL,1,0,'健康管家'),(3,'2026-01-02 10:00:00','2026-02-01 10:00:00',1,0,'护理员'),(4,'2026-01-03 11:00:00',NULL,1,0,'数据维护员');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rolemenu`
--

DROP TABLE IF EXISTS `rolemenu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rolemenu` (
  `id` int NOT NULL,
  `role_id` int NOT NULL,
  `menu` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rolemenu`
--

LOCK TABLES `rolemenu` WRITE;
/*!40000 ALTER TABLE `rolemenu` DISABLE KEYS */;
INSERT INTO `rolemenu` VALUES (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,1,5),(6,1,6),(7,1,7),(8,1,8),(9,1,9),(10,1,10),(16,3,1),(17,3,4),(18,3,9),(19,3,10),(20,2,9);
/*!40000 ALTER TABLE `rolemenu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` int NOT NULL,
  `room_floor` varchar(10) NOT NULL,
  `room_no` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'1号楼',101),(2,'1号楼',102),(3,'1号楼',103),(4,'1号楼',104),(5,'1号楼',105),(6,'2号楼',201),(7,'2号楼',202),(8,'2号楼',203),(9,'2号楼',204),(10,'2号楼',205),(11,'3号楼',301),(12,'3号楼',302),(13,'3号楼',303),(14,'3号楼',304),(15,'3号楼',305),(16,'4号楼',401),(17,'4号楼',402),(18,'4号楼',403),(19,'4号楼',404),(20,'4号楼',405),(101,'三层',1001),(102,'十层',1002),(103,'十层',1003),(104,'十层',1004);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_time` date NOT NULL COMMENT '创建时间',
  `create_by` int NOT NULL COMMENT '创建者',
  `update_time` date DEFAULT NULL COMMENT '更新时间',
  `update_by` int DEFAULT NULL COMMENT '更新者',
  `is_deleted` int NOT NULL COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统账号',
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `sex` int NOT NULL COMMENT '性别（0：女，1：男）',
  `email` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话号码',
  `role_id` int NOT NULL COMMENT '系统角色编号（1-管理员，2-健康管家）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'2025-01-10',1,'2025-02-15',1,0,'张大明','admin01','123456',1,'admin@health.com','13500112233',1),(2,'2025-01-12',1,NULL,NULL,0,'李小红','health01','123456',0,'li@health.com','13500112244',2),(3,'2025-01-15',1,'2025-03-01',2,0,'王海涛','health02','654321',1,'wang@health.com','13500112255',2),(4,'2025-01-18',1,NULL,NULL,0,'赵美玲','health03','111111',0,NULL,'13500112266',2),(5,'2025-01-20',1,'2025-04-10',1,1,'陈志强','health04','222222',1,'chen@health.com','13500112277',2),(6,'2025-02-01',1,NULL,NULL,0,'刘雅静','health05','333333',0,'liu@health.com','13500112288',2),(7,'2025-02-05',1,NULL,NULL,0,'周建峰','health06','444444',1,NULL,'13500112299',2),(8,'2025-02-08',1,'2025-05-06',2,0,'吴佳琪','health07','555555',0,'wu@health.com','13500112300',2);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'db_service'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-21 10:41:53
