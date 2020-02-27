CREATE DATABASE  IF NOT EXISTS `cshare` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cshare`;
-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cshare
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account_emailaddress`
--

DROP TABLE IF EXISTS `account_emailaddress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_emailaddress` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(254) NOT NULL,
  `verified` tinyint(1) NOT NULL,
  `primary` tinyint(1) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `account_emailaddress_user_id_2c513194_fk_api_user_id` (`user_id`),
  CONSTRAINT `account_emailaddress_user_id_2c513194_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_emailaddress`
--

LOCK TABLES `account_emailaddress` WRITE;
/*!40000 ALTER TABLE `account_emailaddress` DISABLE KEYS */;
INSERT INTO `account_emailaddress` VALUES (1,'gros.clara@hotmail.fr',0,1,2),(2,'toure.babacar@hotmail.fr',0,1,3),(3,'yoann.roussel@hotmail.fr',0,1,4),(4,'remi.cambie@hotmail.fr',0,1,5),(5,'xavier.vaucorbeil@hotmail.fr',0,1,6),(6,'theotime.macrez@hotmail.fr',0,1,7);
/*!40000 ALTER TABLE `account_emailaddress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_emailconfirmation`
--

DROP TABLE IF EXISTS `account_emailconfirmation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_emailconfirmation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime(6) NOT NULL,
  `sent` datetime(6) DEFAULT NULL,
  `key` varchar(64) NOT NULL,
  `email_address_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`),
  KEY `account_emailconfirm_email_address_id_5b7f8c58_fk_account_e` (`email_address_id`),
  CONSTRAINT `account_emailconfirm_email_address_id_5b7f8c58_fk_account_e` FOREIGN KEY (`email_address_id`) REFERENCES `account_emailaddress` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_emailconfirmation`
--

LOCK TABLES `account_emailconfirmation` WRITE;
/*!40000 ALTER TABLE `account_emailconfirmation` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_emailconfirmation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_order`
--

DROP TABLE IF EXISTS `api_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `client_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `api_order_client_id_e102c509_fk_api_user_id` (`client_id`),
  KEY `api_order_product_id_e3e75af3_fk_api_product_id` (`product_id`),
  CONSTRAINT `api_order_client_id_e102c509_fk_api_user_id` FOREIGN KEY (`client_id`) REFERENCES `api_user` (`id`),
  CONSTRAINT `api_order_product_id_e3e75af3_fk_api_product_id` FOREIGN KEY (`product_id`) REFERENCES `api_product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_order`
--

LOCK TABLES `api_order` WRITE;
/*!40000 ALTER TABLE `api_order` DISABLE KEYS */;
INSERT INTO `api_order` VALUES (1,'2020-02-12 10:31:55.362303','2020-02-12 10:31:55.362303',3,2),(2,'2020-02-12 10:38:29.775999','2020-02-12 10:38:29.775999',2,1),(3,'2020-02-12 10:40:41.584801','2020-02-12 10:40:41.584801',2,4),(4,'2020-02-12 10:43:14.476532','2020-02-12 10:43:14.476532',2,7);
/*!40000 ALTER TABLE `api_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_product`
--

DROP TABLE IF EXISTS `api_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `category` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  `product_picture` varchar(100) NOT NULL,
  `quantity` varchar(50) NOT NULL,
  `expiration_date` date NOT NULL,
  `supplier_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `api_product_supplier_id_6b83e5ee_fk_api_user_id` (`supplier_id`),
  CONSTRAINT `api_product_supplier_id_6b83e5ee_fk_api_user_id` FOREIGN KEY (`supplier_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_product`
--

LOCK TABLES `api_product` WRITE;
/*!40000 ALTER TABLE `api_product` DISABLE KEYS */;
INSERT INTO `api_product` VALUES (1,'Saucisse','2020-02-12 10:17:20.640888','2020-02-12 10:38:29.714758','Viandes/Oeufs','Collected','media/product/apple.jpg','3','2020-12-12',5),(2,'Pain','2020-02-12 10:17:51.786364','2020-02-12 10:31:55.241515','Desserts/Pain','Collected','media/product/apple.jpg','5','2020-03-12',5),(3,'Tomates','2020-02-12 10:18:09.446478','2020-02-12 10:18:09.446478','Fruits/Légumes','Available','media/product/apple.jpg','500g','2020-02-12',2),(4,'Blanquette','2020-02-12 10:18:35.633523','2020-02-12 10:40:41.488166','Conserves/Plats cuisinés','Collected','media/product/apple.jpg','250g','2020-03-12',6),(5,'Poulet','2020-02-12 10:25:58.955174','2020-02-12 10:25:58.955174','Viandes/Oeufs','Available','media/product/IMG_20200212_102546_6478262674993896886.jpg','3 cuisses','2020-02-16',3),(7,'Patates','2020-02-12 10:41:24.436832','2020-02-12 10:43:14.402385','Féculents','Collected','media/product/apple.jpg','3','2020-02-12',6),(8,'Banane','2020-02-12 10:47:05.887588','2020-02-12 10:47:05.887588','Fruits/Légumes','Available','media/product/IMG_20200212_104657_7488569173485934057.jpg','1','2020-02-14',7);
/*!40000 ALTER TABLE `api_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_user`
--

DROP TABLE IF EXISTS `api_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(128) NOT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `email` varchar(254) NOT NULL,
  `date_joined` datetime(6) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `is_staff` tinyint(1) NOT NULL,
  `is_superuser` tinyint(1) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `profile_picture` varchar(100) NOT NULL,
  `room_number` varchar(50) NOT NULL,
  `campus` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user`
--

LOCK TABLES `api_user` WRITE;
/*!40000 ALTER TABLE `api_user` DISABLE KEYS */;
INSERT INTO `api_user` VALUES (1,'pbkdf2_sha256$180000$LjCpzlzmazAN$77PAxDkncbvZV4wn9dKSJd5dLfS7q48uz8zYxZQe1Bg=','2020-02-12 09:57:20.443677','gros@hotmail.fr','2020-02-12 09:56:45.069291',1,1,1,' ',' ','',' ','Gif'),(2,'pbkdf2_sha256$180000$81u77jFQn8Bp$r44S59EVFk+w//cTLPRxsB4OJROHp40WXLeE3F3hy8A=','2020-02-12 10:43:09.548391','gros.clara@hotmail.fr','2020-02-12 10:05:44.160274',1,0,0,'Clara','Gros','media/user/IMG_20200208_164938_kaCHvsz.jpg','A001.4','Metz'),(3,'pbkdf2_sha256$180000$vcVpuVUhNjM8$e/MbkK+1HQQ241jBohN1T6MGUjKpbt1gYHva0187n98=','2020-02-12 10:24:46.969058','toure.babacar@hotmail.fr','2020-02-12 10:06:58.001603',1,0,0,'Babacar','Toure','media/user/IMG_20200208_164938_ywJixUK.jpg','A001.2','Metz'),(4,'pbkdf2_sha256$180000$FENDBeDw7MqS$/DAuGoo2rT89osLftR814ONOzMl0skCtb3/lx6mHzQ4=','2020-02-12 10:08:34.865674','yoann.roussel@hotmail.fr','2020-02-12 10:08:34.508095',1,0,0,'Yoann','Roussel','media/user/IMG_20200208_164938_wj1WZfL.jpg','A001.5','Metz'),(5,'pbkdf2_sha256$180000$sa9lWCIAc1uD$VOZA4U/NykopEyrCzsQTDDuaFpUx49e+ibrQHiMbUpI=','2020-02-12 10:12:20.654613','remi.cambie@hotmail.fr','2020-02-12 10:12:20.259901',1,0,0,'Rémi','Cambie','media/user/IMG_20200208_164938_4lO1e75.jpg','A001.1','Metz'),(6,'pbkdf2_sha256$180000$UruGXywvfcvc$oRJhRL7Ik+wKwWYMclM/+HHLcYMk/ZqJQRsaWLUv3Hs=','2020-02-12 10:13:19.215278','xavier.vaucorbeil@hotmail.fr','2020-02-12 10:13:18.872172',1,0,0,'Xavier','De Vaucorbeil','media/user/IMG_20200208_164938_Q1GcORR.jpg','A001.3','Metz'),(7,'pbkdf2_sha256$180000$ByMEZ9YOWcDm$GRJB1UUwitF7WvYpI7SmCn80FfHzHeFdj4zjuhQ+rn0=','2020-02-12 10:46:07.428015','theotime.macrez@hotmail.fr','2020-02-12 10:45:44.658405',1,0,0,'Théotime','Macrez','media/user/IMG_20200208_164938_nbHQNZ7.jpg','4F306','Gif');
/*!40000 ALTER TABLE `api_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_user_groups`
--

DROP TABLE IF EXISTS `api_user_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_user_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `api_user_groups_user_id_group_id_9c7ddfb5_uniq` (`user_id`,`group_id`),
  KEY `api_user_groups_group_id_3af85785_fk_auth_group_id` (`group_id`),
  CONSTRAINT `api_user_groups_group_id_3af85785_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`),
  CONSTRAINT `api_user_groups_user_id_a5ff39fa_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user_groups`
--

LOCK TABLES `api_user_groups` WRITE;
/*!40000 ALTER TABLE `api_user_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_user_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_user_user_permissions`
--

DROP TABLE IF EXISTS `api_user_user_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_user_user_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `api_user_user_permissions_user_id_permission_id_a06dd704_uniq` (`user_id`,`permission_id`),
  KEY `api_user_user_permis_permission_id_305b7fea_fk_auth_perm` (`permission_id`),
  CONSTRAINT `api_user_user_permis_permission_id_305b7fea_fk_auth_perm` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`),
  CONSTRAINT `api_user_user_permissions_user_id_f3945d65_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user_user_permissions`
--

LOCK TABLES `api_user_user_permissions` WRITE;
/*!40000 ALTER TABLE `api_user_user_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_user_user_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_group`
--

DROP TABLE IF EXISTS `auth_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_group`
--

LOCK TABLES `auth_group` WRITE;
/*!40000 ALTER TABLE `auth_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_group_permissions`
--

DROP TABLE IF EXISTS `auth_group_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_group_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `auth_group_permissions_group_id_permission_id_0cd325b0_uniq` (`group_id`,`permission_id`),
  KEY `auth_group_permissio_permission_id_84c5c92e_fk_auth_perm` (`permission_id`),
  CONSTRAINT `auth_group_permissio_permission_id_84c5c92e_fk_auth_perm` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`),
  CONSTRAINT `auth_group_permissions_group_id_b120cbf9_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_group_permissions`
--

LOCK TABLES `auth_group_permissions` WRITE;
/*!40000 ALTER TABLE `auth_group_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_group_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_permission`
--

DROP TABLE IF EXISTS `auth_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `content_type_id` int(11) NOT NULL,
  `codename` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `auth_permission_content_type_id_codename_01ab375a_uniq` (`content_type_id`,`codename`),
  CONSTRAINT `auth_permission_content_type_id_2f476e4b_fk_django_co` FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_permission`
--

LOCK TABLES `auth_permission` WRITE;
/*!40000 ALTER TABLE `auth_permission` DISABLE KEYS */;
INSERT INTO `auth_permission` VALUES (1,'Can add user',1,'add_user'),(2,'Can change user',1,'change_user'),(3,'Can delete user',1,'delete_user'),(4,'Can view user',1,'view_user'),(5,'Can add product',2,'add_product'),(6,'Can change product',2,'change_product'),(7,'Can delete product',2,'delete_product'),(8,'Can view product',2,'view_product'),(9,'Can add order',3,'add_order'),(10,'Can change order',3,'change_order'),(11,'Can delete order',3,'delete_order'),(12,'Can view order',3,'view_order'),(13,'Can add log entry',4,'add_logentry'),(14,'Can change log entry',4,'change_logentry'),(15,'Can delete log entry',4,'delete_logentry'),(16,'Can view log entry',4,'view_logentry'),(17,'Can add permission',5,'add_permission'),(18,'Can change permission',5,'change_permission'),(19,'Can delete permission',5,'delete_permission'),(20,'Can view permission',5,'view_permission'),(21,'Can add group',6,'add_group'),(22,'Can change group',6,'change_group'),(23,'Can delete group',6,'delete_group'),(24,'Can view group',6,'view_group'),(25,'Can add content type',7,'add_contenttype'),(26,'Can change content type',7,'change_contenttype'),(27,'Can delete content type',7,'delete_contenttype'),(28,'Can view content type',7,'view_contenttype'),(29,'Can add session',8,'add_session'),(30,'Can change session',8,'change_session'),(31,'Can delete session',8,'delete_session'),(32,'Can view session',8,'view_session'),(33,'Can add Token',9,'add_token'),(34,'Can change Token',9,'change_token'),(35,'Can delete Token',9,'delete_token'),(36,'Can view Token',9,'view_token'),(37,'Can add site',10,'add_site'),(38,'Can change site',10,'change_site'),(39,'Can delete site',10,'delete_site'),(40,'Can view site',10,'view_site'),(41,'Can add email address',11,'add_emailaddress'),(42,'Can change email address',11,'change_emailaddress'),(43,'Can delete email address',11,'delete_emailaddress'),(44,'Can view email address',11,'view_emailaddress'),(45,'Can add email confirmation',12,'add_emailconfirmation'),(46,'Can change email confirmation',12,'change_emailconfirmation'),(47,'Can delete email confirmation',12,'delete_emailconfirmation'),(48,'Can view email confirmation',12,'view_emailconfirmation'),(49,'Can add social account',13,'add_socialaccount'),(50,'Can change social account',13,'change_socialaccount'),(51,'Can delete social account',13,'delete_socialaccount'),(52,'Can view social account',13,'view_socialaccount'),(53,'Can add social application',14,'add_socialapp'),(54,'Can change social application',14,'change_socialapp'),(55,'Can delete social application',14,'delete_socialapp'),(56,'Can view social application',14,'view_socialapp'),(57,'Can add social application token',15,'add_socialtoken'),(58,'Can change social application token',15,'change_socialtoken'),(59,'Can delete social application token',15,'delete_socialtoken'),(60,'Can view social application token',15,'view_socialtoken');
/*!40000 ALTER TABLE `auth_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authtoken_token`
--

DROP TABLE IF EXISTS `authtoken_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authtoken_token` (
  `key` varchar(40) NOT NULL,
  `created` datetime(6) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`key`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `authtoken_token_user_id_35299eff_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authtoken_token`
--

LOCK TABLES `authtoken_token` WRITE;
/*!40000 ALTER TABLE `authtoken_token` DISABLE KEYS */;
INSERT INTO `authtoken_token` VALUES ('8cf6154fbd853d69c83383f57ada5d3224f1f3fc','2020-02-12 10:08:34.781276',4),('910a3036062ac6c77903c1d0f97927e39890bd01','2020-02-12 10:12:20.601348',5),('fb63f8005f989fd3440c0905498d7f8476714e34','2020-02-12 10:13:19.150375',6);
/*!40000 ALTER TABLE `authtoken_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_admin_log`
--

DROP TABLE IF EXISTS `django_admin_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `django_admin_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action_time` datetime(6) NOT NULL,
  `object_id` longtext,
  `object_repr` varchar(200) NOT NULL,
  `action_flag` smallint(5) unsigned NOT NULL,
  `change_message` longtext NOT NULL,
  `content_type_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `django_admin_log_content_type_id_c4bce8eb_fk_django_co` (`content_type_id`),
  KEY `django_admin_log_user_id_c564eba6_fk_api_user_id` (`user_id`),
  CONSTRAINT `django_admin_log_content_type_id_c4bce8eb_fk_django_co` FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`),
  CONSTRAINT `django_admin_log_user_id_c564eba6_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`),
  CONSTRAINT `django_admin_log_chk_1` CHECK ((`action_flag` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `django_admin_log`
--

LOCK TABLES `django_admin_log` WRITE;
/*!40000 ALTER TABLE `django_admin_log` DISABLE KEYS */;
INSERT INTO `django_admin_log` VALUES (1,'2020-02-12 10:17:20.677556','1','Product object (1)',1,'[{\"added\": {}}]',2,1),(2,'2020-02-12 10:17:51.788470','2','Product object (2)',1,'[{\"added\": {}}]',2,1),(3,'2020-02-12 10:18:09.448473','3','Product object (3)',1,'[{\"added\": {}}]',2,1),(4,'2020-02-12 10:18:35.634736','4','Product object (4)',1,'[{\"added\": {}}]',2,1),(5,'2020-02-12 10:41:24.439996','7','Product object (7)',1,'[{\"added\": {}}]',2,1);
/*!40000 ALTER TABLE `django_admin_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_content_type`
--

DROP TABLE IF EXISTS `django_content_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `django_content_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_label` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `django_content_type_app_label_model_76bd3d3b_uniq` (`app_label`,`model`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `django_content_type`
--

LOCK TABLES `django_content_type` WRITE;
/*!40000 ALTER TABLE `django_content_type` DISABLE KEYS */;
INSERT INTO `django_content_type` VALUES (11,'account','emailaddress'),(12,'account','emailconfirmation'),(4,'admin','logentry'),(3,'api','order'),(2,'api','product'),(1,'api','user'),(6,'auth','group'),(5,'auth','permission'),(9,'authtoken','token'),(7,'contenttypes','contenttype'),(8,'sessions','session'),(10,'sites','site'),(13,'socialaccount','socialaccount'),(14,'socialaccount','socialapp'),(15,'socialaccount','socialtoken');
/*!40000 ALTER TABLE `django_content_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_migrations`
--

DROP TABLE IF EXISTS `django_migrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `django_migrations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `applied` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `django_migrations`
--

LOCK TABLES `django_migrations` WRITE;
/*!40000 ALTER TABLE `django_migrations` DISABLE KEYS */;
INSERT INTO `django_migrations` VALUES (1,'contenttypes','0001_initial','2020-02-12 09:56:29.573835'),(2,'contenttypes','0002_remove_content_type_name','2020-02-12 09:56:29.944150'),(3,'auth','0001_initial','2020-02-12 09:56:30.233710'),(4,'auth','0002_alter_permission_name_max_length','2020-02-12 09:56:30.879125'),(5,'auth','0003_alter_user_email_max_length','2020-02-12 09:56:30.889820'),(6,'auth','0004_alter_user_username_opts','2020-02-12 09:56:30.902833'),(7,'auth','0005_alter_user_last_login_null','2020-02-12 09:56:30.915512'),(8,'auth','0006_require_contenttypes_0002','2020-02-12 09:56:30.924759'),(9,'auth','0007_alter_validators_add_error_messages','2020-02-12 09:56:30.939094'),(10,'auth','0008_alter_user_username_max_length','2020-02-12 09:56:30.957910'),(11,'auth','0009_alter_user_last_name_max_length','2020-02-12 09:56:30.967719'),(12,'auth','0010_alter_group_name_max_length','2020-02-12 09:56:31.002234'),(13,'auth','0011_update_proxy_permissions','2020-02-12 09:56:31.025205'),(14,'api','0001_initial','2020-02-12 09:56:31.555801'),(15,'account','0001_initial','2020-02-12 09:56:33.358818'),(16,'account','0002_email_max_length','2020-02-12 09:56:33.878920'),(17,'admin','0001_initial','2020-02-12 09:56:34.005828'),(18,'admin','0002_logentry_remove_auto_add','2020-02-12 09:56:34.472715'),(19,'admin','0003_logentry_add_action_flag_choices','2020-02-12 09:56:34.500284'),(20,'authtoken','0001_initial','2020-02-12 09:56:34.633892'),(21,'authtoken','0002_auto_20160226_1747','2020-02-12 09:56:35.288125'),(22,'sessions','0001_initial','2020-02-12 09:56:35.388659'),(23,'sites','0001_initial','2020-02-12 09:56:35.538899'),(24,'sites','0002_alter_domain_unique','2020-02-12 09:56:35.587254'),(25,'socialaccount','0001_initial','2020-02-12 09:56:36.074651'),(26,'socialaccount','0002_token_max_lengths','2020-02-12 09:56:37.324361'),(27,'socialaccount','0003_extra_data_default_dict','2020-02-12 09:56:37.357611');
/*!40000 ALTER TABLE `django_migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_session`
--

DROP TABLE IF EXISTS `django_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `django_session` (
  `session_key` varchar(40) NOT NULL,
  `session_data` longtext NOT NULL,
  `expire_date` datetime(6) NOT NULL,
  PRIMARY KEY (`session_key`),
  KEY `django_session_expire_date_a5c62663` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `django_session`
--

LOCK TABLES `django_session` WRITE;
/*!40000 ALTER TABLE `django_session` DISABLE KEYS */;
INSERT INTO `django_session` VALUES ('0f5fieqc6apocvcsy22q6uhyq4x59em6','ZmQ3ZDRlMGQxMzBjYzE5Y2M3Nzc1MWIzMzg2NGJhMzM3YTUyMjE4Yzp7ImFjY291bnRfdmVyaWZpZWRfZW1haWwiOm51bGwsImFjY291bnRfdXNlciI6IjMiLCJfYXV0aF91c2VyX2lkIjoiMyIsIl9hdXRoX3VzZXJfYmFja2VuZCI6ImRqYW5nby5jb250cmliLmF1dGguYmFja2VuZHMuTW9kZWxCYWNrZW5kIiwiX2F1dGhfdXNlcl9oYXNoIjoiM2U4ZTJkOWE5ODUwZWZkNGQzMjU0ZjkwMDc1Y2VlNGI0N2JjN2MwYiJ9','2020-02-26 10:06:58.393489'),('0wq15g1fgujubmpyrlcsmudhm0k6g8tw','ZDUxZDEyMTNkZDFkZDQ2YjM3Y2M3MDU3NGM0ZWM1ZDFmZWEzODBiZDp7Il9hdXRoX3VzZXJfaWQiOiIzIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiIzZThlMmQ5YTk4NTBlZmQ0ZDMyNTRmOTAwNzVjZWU0YjQ3YmM3YzBiIn0=','2020-02-26 10:19:05.192442'),('165q1bksxywel48h3jh2070vdb75x3ka','MmRhMjY4OTdjYzY5Y2QyODE2ZDIzY2M4NmM5OWZlOGRjYTFhMmE1Mjp7Il9hdXRoX3VzZXJfaWQiOiIyIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiI1ZjU1OTUwMThiMjI0NTJhMzdiYWQ4ZTI3ZDVkOGYwNGE1M2E0MjQ2In0=','2020-02-26 10:37:45.399821'),('1i58z4nwsyaz7n9fhqlutte2m48tfedw','NTBmZTNmNWRjYzMyMjQwMGExNzYwN2Q5MjY0ZmI5NWY3YWM3MWIwYjp7ImFjY291bnRfdmVyaWZpZWRfZW1haWwiOm51bGwsImFjY291bnRfdXNlciI6IjYiLCJfYXV0aF91c2VyX2lkIjoiNiIsIl9hdXRoX3VzZXJfYmFja2VuZCI6ImRqYW5nby5jb250cmliLmF1dGguYmFja2VuZHMuTW9kZWxCYWNrZW5kIiwiX2F1dGhfdXNlcl9oYXNoIjoiN2Y5YWM3NTU1NDBkYmE2ZjkxYTY4NTc3MWRmZDdiZGVlMWE4YTAxZSJ9','2020-02-26 10:13:19.231234'),('323behhe4cvraek60cpvzko7mfnspsj8','ZGU3YTA3OTc0ZjJmNDg1NzJjYTg3YTUzM2M0ODdmYWM4NDZlMjJlNzp7Il9hdXRoX3VzZXJfaWQiOiIxIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiJlZTIwMGE1MWVlZTMxZTdiMGFhOTdjZjlkMzFiYzc0YTRhYmU5NTE1In0=','2020-02-26 09:57:20.451995'),('665tsw8trwjrvkwvf8vka2n2474do1nc','MDQ5MWNjOWM3MjZjOTlhMmNhMmRkYmE0NTU2ZGQ0OWY1YjU1NjY2OTp7ImFjY291bnRfdmVyaWZpZWRfZW1haWwiOm51bGwsImFjY291bnRfdXNlciI6IjIiLCJfYXV0aF91c2VyX2lkIjoiMiIsIl9hdXRoX3VzZXJfYmFja2VuZCI6ImRqYW5nby5jb250cmliLmF1dGguYmFja2VuZHMuTW9kZWxCYWNrZW5kIiwiX2F1dGhfdXNlcl9oYXNoIjoiNWY1NTk1MDE4YjIyNDUyYTM3YmFkOGUyN2Q1ZDhmMDRhNTNhNDI0NiJ9','2020-02-26 10:05:44.620784'),('75sq64828dawf39bp8jhx9tm5higp9o9','N2YzZDM5MTJjOTkzZTYxYmQ0OTIyNGI1ODAwYjBmNzBiNjIwYzEyNTp7ImFjY291bnRfdmVyaWZpZWRfZW1haWwiOm51bGwsImFjY291bnRfdXNlciI6IjgiLCJfYXV0aF91c2VyX2lkIjoiOCIsIl9hdXRoX3VzZXJfYmFja2VuZCI6ImRqYW5nby5jb250cmliLmF1dGguYmFja2VuZHMuTW9kZWxCYWNrZW5kIiwiX2F1dGhfdXNlcl9oYXNoIjoiY2Q4YWEyYTEwMWY0YWNmY2Y2NWIxNzY5N2E5ODYxYTkwYmNhNzcxNyJ9','2020-02-26 10:47:59.999198'),('9x5middu382tccw1sib67y2tq5kwjgzv','YTJmMjJlMmM5NjBiNWNhYTI4NzEyYTFkNWEyNGY3N2U2NjI4NmE1NDp7Il9hdXRoX3VzZXJfaWQiOiI3IiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiI3YThjOTc4MDc0NWE5MDE1MDQxYjEwMzgzYTI3MDY4MTJkODk2NjZkIn0=','2020-02-26 10:46:07.445911'),('bjgito2rfvresxco3iwd85b95556gyil','NzA4NmEzZjM4ZGQ2NmFlMWUyYWRiNTg5YTBjMDc5MTI0ZWEzYWRkMzp7ImFjY291bnRfdmVyaWZpZWRfZW1haWwiOm51bGwsImFjY291bnRfdXNlciI6IjciLCJfYXV0aF91c2VyX2lkIjoiNyIsIl9hdXRoX3VzZXJfYmFja2VuZCI6ImRqYW5nby5jb250cmliLmF1dGguYmFja2VuZHMuTW9kZWxCYWNrZW5kIiwiX2F1dGhfdXNlcl9oYXNoIjoiN2E4Yzk3ODA3NDVhOTAxNTA0MWIxMDM4M2EyNzA2ODEyZDg5NjY2ZCJ9','2020-02-26 10:45:45.102603'),('bk98y0ehbz4xozjjimrqre8645odw5dn','MmRhMjY4OTdjYzY5Y2QyODE2ZDIzY2M4NmM5OWZlOGRjYTFhMmE1Mjp7Il9hdXRoX3VzZXJfaWQiOiIyIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiI1ZjU1OTUwMThiMjI0NTJhMzdiYWQ4ZTI3ZDVkOGYwNGE1M2E0MjQ2In0=','2020-02-26 10:43:09.563357'),('croneu6zcd6m0z9k0xh5b8vy3bb5r15c','ZWUxNjVhNGIwZWU1ZTAzYTcxMmYzNTM0YWVmOGQxNWQwODE5MjUzYTp7ImFjY291bnRfdmVyaWZpZWRfZW1haWwiOm51bGwsImFjY291bnRfdXNlciI6IjQiLCJfYXV0aF91c2VyX2lkIjoiNCIsIl9hdXRoX3VzZXJfYmFja2VuZCI6ImRqYW5nby5jb250cmliLmF1dGguYmFja2VuZHMuTW9kZWxCYWNrZW5kIiwiX2F1dGhfdXNlcl9oYXNoIjoiOGFkNTE4YTRlNDI1NzNiMGQxNzIzMTVlZGEwNjYwOTYyOTYyMmI1ZSJ9','2020-02-26 10:08:34.880021'),('czatxaw7cqtvv4z5qihb1r6gqj8xhycc','ZDUxZDEyMTNkZDFkZDQ2YjM3Y2M3MDU3NGM0ZWM1ZDFmZWEzODBiZDp7Il9hdXRoX3VzZXJfaWQiOiIzIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiIzZThlMmQ5YTk4NTBlZmQ0ZDMyNTRmOTAwNzVjZWU0YjQ3YmM3YzBiIn0=','2020-02-26 10:24:46.984485'),('fbxqvi1rprcl30v2ogdedrxxoiip04f1','MmRhMjY4OTdjYzY5Y2QyODE2ZDIzY2M4NmM5OWZlOGRjYTFhMmE1Mjp7Il9hdXRoX3VzZXJfaWQiOiIyIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiI1ZjU1OTUwMThiMjI0NTJhMzdiYWQ4ZTI3ZDVkOGYwNGE1M2E0MjQ2In0=','2020-02-26 10:38:09.080841'),('fqee265xwi58k9at5nc1sui493hg697b','MmRhMjY4OTdjYzY5Y2QyODE2ZDIzY2M4NmM5OWZlOGRjYTFhMmE1Mjp7Il9hdXRoX3VzZXJfaWQiOiIyIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiI1ZjU1OTUwMThiMjI0NTJhMzdiYWQ4ZTI3ZDVkOGYwNGE1M2E0MjQ2In0=','2020-02-26 10:37:45.424538'),('mxjuhpgb252xycme73rds2w10xh9fi6k','MTJkNDM4Yzg2N2E3ZjM4MTAyYjI1MGI4MjkwNmVhMzc1MjYwMjkwMjp7Il9hdXRoX3VzZXJfaWQiOiI4IiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiJjZDhhYTJhMTAxZjRhY2ZjZjY1YjE3Njk3YTk4NjFhOTBiY2E3NzE3In0=','2020-02-26 10:48:43.743502'),('nl9wtcevri1efhpw9pew5rilz0t2gr92','ZDUxZDEyMTNkZDFkZDQ2YjM3Y2M3MDU3NGM0ZWM1ZDFmZWEzODBiZDp7Il9hdXRoX3VzZXJfaWQiOiIzIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiIzZThlMmQ5YTk4NTBlZmQ0ZDMyNTRmOTAwNzVjZWU0YjQ3YmM3YzBiIn0=','2020-02-26 10:22:02.368512'),('nykbrcx301eehg7mm6v8n8n57em7io2b','MTJkNDM4Yzg2N2E3ZjM4MTAyYjI1MGI4MjkwNmVhMzc1MjYwMjkwMjp7Il9hdXRoX3VzZXJfaWQiOiI4IiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiJjZDhhYTJhMTAxZjRhY2ZjZjY1YjE3Njk3YTk4NjFhOTBiY2E3NzE3In0=','2020-02-26 10:48:43.715454'),('qobqy39cn06ly2wt0mvwqaklozudah3m','MmRhMjY4OTdjYzY5Y2QyODE2ZDIzY2M4NmM5OWZlOGRjYTFhMmE1Mjp7Il9hdXRoX3VzZXJfaWQiOiIyIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiI1ZjU1OTUwMThiMjI0NTJhMzdiYWQ4ZTI3ZDVkOGYwNGE1M2E0MjQ2In0=','2020-02-26 10:40:44.890146'),('scnanax986svy1d5rptw27vaze7f1y7n','MzExNzdjYzYzMzEwMzk3NTkxNjdhNTI0N2ZkMWY1M2E1OGQ1OGU1Mzp7Il9hdXRoX3VzZXJfaWQiOiI4IiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiIzYTIxMGJlOTBiZTg2YjZkMzQzZTJkOTliODRiNzdmNTU5YWIwZmQ4In0=','2020-02-26 10:54:23.387965'),('sykdkx7ff4uky27x6gtt87lvz7xz3d5t','MmRhMjY4OTdjYzY5Y2QyODE2ZDIzY2M4NmM5OWZlOGRjYTFhMmE1Mjp7Il9hdXRoX3VzZXJfaWQiOiIyIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiI1ZjU1OTUwMThiMjI0NTJhMzdiYWQ4ZTI3ZDVkOGYwNGE1M2E0MjQ2In0=','2020-02-26 10:40:20.443468'),('v7cr8satwg3izped0f0wbhbpxfvg97yi','NWE5M2IxYWIzN2E4ZGRiNWE3ZTlhMTdiM2RjMmYwNzdhY2EwNjBjMzp7ImFjY291bnRfdmVyaWZpZWRfZW1haWwiOm51bGwsImFjY291bnRfdXNlciI6IjUiLCJfYXV0aF91c2VyX2lkIjoiNSIsIl9hdXRoX3VzZXJfYmFja2VuZCI6ImRqYW5nby5jb250cmliLmF1dGguYmFja2VuZHMuTW9kZWxCYWNrZW5kIiwiX2F1dGhfdXNlcl9oYXNoIjoiYzczYjA4ODBkYmY2ODlmZGIwM2YxMzY1ZDhiOWE3MWQwMDg5N2ZmZSJ9','2020-02-26 10:12:20.666243'),('xdy9x1ql87l3w5aoc74yvrvcb2hyjspl','ZDUxZDEyMTNkZDFkZDQ2YjM3Y2M3MDU3NGM0ZWM1ZDFmZWEzODBiZDp7Il9hdXRoX3VzZXJfaWQiOiIzIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiIzZThlMmQ5YTk4NTBlZmQ0ZDMyNTRmOTAwNzVjZWU0YjQ3YmM3YzBiIn0=','2020-02-26 10:22:36.649591'),('you600fzeejc6uheyo3ygrh6n1xxzslv','Yzc0ZjA4YjA2ZDBkYTEyYzdkZmIwZWFjZjdlN2Q1YjIzOTlmOGMwMDp7Il9hdXRoX3VzZXJfaGFzaCI6IjNhMjEwYmU5MGJlODZiNmQzNDNlMmQ5OWI4NGI3N2Y1NTlhYjBmZDgifQ==','2020-02-26 10:53:47.961640');
/*!40000 ALTER TABLE `django_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_site`
--

DROP TABLE IF EXISTS `django_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `django_site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `domain` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `django_site_domain_a2e37b91_uniq` (`domain`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `django_site`
--

LOCK TABLES `django_site` WRITE;
/*!40000 ALTER TABLE `django_site` DISABLE KEYS */;
INSERT INTO `django_site` VALUES (1,'example.com','example.com');
/*!40000 ALTER TABLE `django_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socialaccount_socialaccount`
--

DROP TABLE IF EXISTS `socialaccount_socialaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `socialaccount_socialaccount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider` varchar(30) NOT NULL,
  `uid` varchar(191) NOT NULL,
  `last_login` datetime(6) NOT NULL,
  `date_joined` datetime(6) NOT NULL,
  `extra_data` longtext NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `socialaccount_socialaccount_provider_uid_fc810c6e_uniq` (`provider`,`uid`),
  KEY `socialaccount_socialaccount_user_id_8146e70c_fk_api_user_id` (`user_id`),
  CONSTRAINT `socialaccount_socialaccount_user_id_8146e70c_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socialaccount_socialaccount`
--

LOCK TABLES `socialaccount_socialaccount` WRITE;
/*!40000 ALTER TABLE `socialaccount_socialaccount` DISABLE KEYS */;
/*!40000 ALTER TABLE `socialaccount_socialaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socialaccount_socialapp`
--

DROP TABLE IF EXISTS `socialaccount_socialapp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `socialaccount_socialapp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider` varchar(30) NOT NULL,
  `name` varchar(40) NOT NULL,
  `client_id` varchar(191) NOT NULL,
  `secret` varchar(191) NOT NULL,
  `key` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socialaccount_socialapp`
--

LOCK TABLES `socialaccount_socialapp` WRITE;
/*!40000 ALTER TABLE `socialaccount_socialapp` DISABLE KEYS */;
/*!40000 ALTER TABLE `socialaccount_socialapp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socialaccount_socialapp_sites`
--

DROP TABLE IF EXISTS `socialaccount_socialapp_sites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `socialaccount_socialapp_sites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `socialapp_id` int(11) NOT NULL,
  `site_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `socialaccount_socialapp_sites_socialapp_id_site_id_71a9a768_uniq` (`socialapp_id`,`site_id`),
  KEY `socialaccount_socialapp_sites_site_id_2579dee5_fk_django_site_id` (`site_id`),
  CONSTRAINT `socialaccount_social_socialapp_id_97fb6e7d_fk_socialacc` FOREIGN KEY (`socialapp_id`) REFERENCES `socialaccount_socialapp` (`id`),
  CONSTRAINT `socialaccount_socialapp_sites_site_id_2579dee5_fk_django_site_id` FOREIGN KEY (`site_id`) REFERENCES `django_site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socialaccount_socialapp_sites`
--

LOCK TABLES `socialaccount_socialapp_sites` WRITE;
/*!40000 ALTER TABLE `socialaccount_socialapp_sites` DISABLE KEYS */;
/*!40000 ALTER TABLE `socialaccount_socialapp_sites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socialaccount_socialtoken`
--

DROP TABLE IF EXISTS `socialaccount_socialtoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `socialaccount_socialtoken` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token` longtext NOT NULL,
  `token_secret` longtext NOT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `account_id` int(11) NOT NULL,
  `app_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `socialaccount_socialtoken_app_id_account_id_fca4e0ac_uniq` (`app_id`,`account_id`),
  KEY `socialaccount_social_account_id_951f210e_fk_socialacc` (`account_id`),
  CONSTRAINT `socialaccount_social_account_id_951f210e_fk_socialacc` FOREIGN KEY (`account_id`) REFERENCES `socialaccount_socialaccount` (`id`),
  CONSTRAINT `socialaccount_social_app_id_636a42d7_fk_socialacc` FOREIGN KEY (`app_id`) REFERENCES `socialaccount_socialapp` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socialaccount_socialtoken`
--

LOCK TABLES `socialaccount_socialtoken` WRITE;
/*!40000 ALTER TABLE `socialaccount_socialtoken` DISABLE KEYS */;
/*!40000 ALTER TABLE `socialaccount_socialtoken` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-12 12:13:29
