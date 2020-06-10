-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: localhost    Database: cshare
-- ------------------------------------------------------
-- Server version	5.7.30-0ubuntu0.18.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_emailaddress`
--

LOCK TABLES `account_emailaddress` WRITE;
/*!40000 ALTER TABLE `account_emailaddress` DISABLE KEYS */;
INSERT INTO `account_emailaddress` VALUES (1,'virginie.galtier@cs.fr',0,1,1),(2,'michel.ianotto@cs.fr',0,1,2),(3,'babacar.toure@cs.fr',0,1,3);
/*!40000 ALTER TABLE `account_emailaddress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_emailconfirmation`
--

DROP TABLE IF EXISTS `account_emailconfirmation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `api_order_customer_id_8cb4e7b7_fk_api_user_id` (`customer_id`),
  KEY `api_order_product_id_e3e75af3_fk_api_product_id` (`product_id`),
  CONSTRAINT `api_order_customer_id_8cb4e7b7_fk_api_user_id` FOREIGN KEY (`customer_id`) REFERENCES `api_user` (`id`),
  CONSTRAINT `api_order_product_id_e3e75af3_fk_api_product_id` FOREIGN KEY (`product_id`) REFERENCES `api_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_order`
--

LOCK TABLES `api_order` WRITE;
/*!40000 ALTER TABLE `api_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_product`
--

DROP TABLE IF EXISTS `api_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `category` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  `product_picture` varchar(100) NOT NULL,
  `quantity` varchar(50) NOT NULL,
  `expiration_date` date NOT NULL,
  `campus` varchar(10) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `api_product_supplier_id_6b83e5ee_fk_api_user_id` (`supplier_id`),
  CONSTRAINT `api_product_supplier_id_6b83e5ee_fk_api_user_id` FOREIGN KEY (`supplier_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_product`
--

LOCK TABLES `api_product` WRITE;
/*!40000 ALTER TABLE `api_product` DISABLE KEYS */;
INSERT INTO `api_product` VALUES (1,'Avocado','Fruits/Légumes','Available','media/product/avocado.jpeg','2','2020-06-13','Metz','2020-06-10 06:14:36.951251','2020-06-10 06:14:36.951273',1),(2,'Eggs','Viandes/Oeufs','Available','media/product/eggs.jpeg','6','2020-06-20','Metz','2020-06-10 06:15:02.517767','2020-06-10 06:15:02.517788',1),(3,'Soft bread','Desserts/Pain','Available','media/product/soft_bread.jpeg','25','2020-07-03','Metz','2020-06-10 06:15:59.875249','2020-06-10 06:15:59.875330',1),(4,'Bananas','Fruits/Légumes','Available','media/product/banana.jpeg','3','2020-06-18','Metz','2020-06-10 06:16:59.041844','2020-06-10 06:16:59.041875',2),(5,'Lentils','Conserves/Plats cuisinés','Available','media/product/lentils.jpeg','250g','2021-12-21','Metz','2020-06-10 06:17:34.309343','2020-06-10 06:17:34.309375',2),(6,'Penne','Féculents','Available','media/product/penne.jpeg','1kg','2022-11-08','Metz','2020-06-10 06:18:03.483205','2020-06-10 06:18:03.483226',2),(7,'Lime','Fruits/Légumes','Available','media/product/lime.jpeg','2','2020-06-14','Metz','2020-06-10 06:18:37.332496','2020-06-10 06:18:37.332538',2),(8,'Zucchinis','Fruits/Légumes','Available','media/product/zucchinis.jpeg','2','2020-06-14','Metz','2020-06-10 06:18:54.244337','2020-06-10 06:18:54.244357',2),(10,'Rice','Féculents','Available','media/product/rice_aEUJrng.jpeg','2kg','2022-02-01','Metz','2020-06-10 06:20:35.004435','2020-06-10 06:20:35.004456',1),(11,'Chicken legs','Viandes/Oeufs','Available','media/product/chicken_legs.jpeg','2 legs','2020-06-19','Metz','2020-06-10 06:21:10.035381','2020-06-10 06:21:10.035402',1),(12,'Macaroni','Féculents','Available','media/product/macaroni.jpeg','500g','2025-09-26','Metz','2020-06-10 06:21:59.142046','2020-06-10 06:21:59.142067',1),(13,'Biscuits','Autres','Available','media/product/biscuits.jpeg','15','2024-03-09','Metz','2020-06-10 06:23:16.020624','2020-06-10 06:23:16.020657',2),(14,'Lasagnas','Conserves/Plats cuisinés','Available','media/product/lasagnas.jpeg','250g','2024-03-09','Metz','2020-06-10 06:27:09.363835','2020-06-10 06:27:09.363860',2),(15,'Milka','Desserts/Pain','Available','media/product/milka.jpeg','2x250g','2020-07-15','Metz','2020-06-10 06:27:48.165863','2020-06-10 06:27:48.165891',2),(16,'Tea','Autres','Available','media/product/tea.jpeg','50','2026-10-24','Metz','2020-06-10 06:29:11.996997','2020-06-10 06:29:11.997016',1),(17,'Melon','Fruits/Légumes','Available','media/product/melon.jpeg','2','2020-06-12','Metz','2020-06-10 06:30:01.599513','2020-06-10 06:30:01.599538',1),(18,'Tomato sauce','Fruits/Légumes','Available','media/product/tomato_sauce.jpeg','500ml','2020-07-12','Gif','2020-06-10 06:30:53.824948','2020-06-10 06:30:53.824969',3),(19,'Milk','Produits laitiers','Available','media/product/milk.jpeg','2L','2020-09-30','Gif','2020-06-10 06:31:28.407426','2020-06-10 06:31:28.407448',3),(20,'Sanitizer','Produits d\'entretien','Available','media/product/sanitizer.jpeg','1L','2030-01-01','Gif','2020-06-10 06:32:01.582159','2020-06-10 06:32:01.582208',3);
/*!40000 ALTER TABLE `api_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_user`
--

DROP TABLE IF EXISTS `api_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `room_number` varchar(50) DEFAULT NULL,
  `campus` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user`
--

LOCK TABLES `api_user` WRITE;
/*!40000 ALTER TABLE `api_user` DISABLE KEYS */;
INSERT INTO `api_user` VALUES (1,'pbkdf2_sha256$180000$ep13sGoPgfcH$QpYKu3hRSUCQVn+mwOsNC/GEivRjJO+GARQdAfvpQuY=','2020-06-10 06:28:02.618186','virginie.galtier@cs.fr','2020-06-10 06:11:52.309198',1,0,0,'Virginie','Galtier','media/user/android.png','C2','Metz'),(2,'pbkdf2_sha256$180000$LNLnKaTS1WgO$4ZpvhHqlWFhpj4/wmGu/Tks80AHyFVHPLqTcBCgZc38=','2020-06-10 06:22:23.320537','michel.ianotto@cs.fr','2020-06-10 06:12:21.683482',1,0,0,'Michel','Ianotto','media/user/android.png','G6','Metz'),(3,'pbkdf2_sha256$180000$9xkMNSAlp4Vn$8pS2dEX/amtbGPqoytLdf5OIKlw+0rUiocDfLACJmW8=','2020-06-10 06:30:16.353129','babacar.toure@cs.fr','2020-06-10 06:13:06.768398',1,0,0,'Babacar','Toure','media/user/babscanor.jpeg','4H303','Gif');
/*!40000 ALTER TABLE `api_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_user_groups`
--

DROP TABLE IF EXISTS `api_user_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_user_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `api_user_groups_user_id_group_id_9c7ddfb5_uniq` (`user_id`,`group_id`),
  KEY `api_user_groups_group_id_3af85785_fk_auth_group_id` (`group_id`),
  CONSTRAINT `api_user_groups_group_id_3af85785_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`),
  CONSTRAINT `api_user_groups_user_id_a5ff39fa_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_user_user_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `api_user_user_permissions_user_id_permission_id_a06dd704_uniq` (`user_id`,`permission_id`),
  KEY `api_user_user_permis_permission_id_305b7fea_fk_auth_perm` (`permission_id`),
  CONSTRAINT `api_user_user_permis_permission_id_305b7fea_fk_auth_perm` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`),
  CONSTRAINT `api_user_user_permissions_user_id_f3945d65_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_group_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `auth_group_permissions_group_id_permission_id_0cd325b0_uniq` (`group_id`,`permission_id`),
  KEY `auth_group_permissio_permission_id_84c5c92e_fk_auth_perm` (`permission_id`),
  CONSTRAINT `auth_group_permissio_permission_id_84c5c92e_fk_auth_perm` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`),
  CONSTRAINT `auth_group_permissions_group_id_b120cbf9_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `content_type_id` int(11) NOT NULL,
  `codename` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `auth_permission_content_type_id_codename_01ab375a_uniq` (`content_type_id`,`codename`),
  CONSTRAINT `auth_permission_content_type_id_2f476e4b_fk_django_co` FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_permission`
--

LOCK TABLES `auth_permission` WRITE;
/*!40000 ALTER TABLE `auth_permission` DISABLE KEYS */;
INSERT INTO `auth_permission` VALUES (1,'Can add user',1,'add_user'),(2,'Can change user',1,'change_user'),(3,'Can delete user',1,'delete_user'),(4,'Can view user',1,'view_user'),(5,'Can add product',2,'add_product'),(6,'Can change product',2,'change_product'),(7,'Can delete product',2,'delete_product'),(8,'Can view product',2,'view_product'),(9,'Can add order',3,'add_order'),(10,'Can change order',3,'change_order'),(11,'Can delete order',3,'delete_order'),(12,'Can view order',3,'view_order'),(13,'Can add permission',4,'add_permission'),(14,'Can change permission',4,'change_permission'),(15,'Can delete permission',4,'delete_permission'),(16,'Can view permission',4,'view_permission'),(17,'Can add group',5,'add_group'),(18,'Can change group',5,'change_group'),(19,'Can delete group',5,'delete_group'),(20,'Can view group',5,'view_group'),(21,'Can add content type',6,'add_contenttype'),(22,'Can change content type',6,'change_contenttype'),(23,'Can delete content type',6,'delete_contenttype'),(24,'Can view content type',6,'view_contenttype'),(25,'Can add log entry',7,'add_logentry'),(26,'Can change log entry',7,'change_logentry'),(27,'Can delete log entry',7,'delete_logentry'),(28,'Can view log entry',7,'view_logentry'),(29,'Can add site',8,'add_site'),(30,'Can change site',8,'change_site'),(31,'Can delete site',8,'delete_site'),(32,'Can view site',8,'view_site'),(33,'Can add session',9,'add_session'),(34,'Can change session',9,'change_session'),(35,'Can delete session',9,'delete_session'),(36,'Can view session',9,'view_session'),(37,'Can add Token',10,'add_token'),(38,'Can change Token',10,'change_token'),(39,'Can delete Token',10,'delete_token'),(40,'Can view Token',10,'view_token'),(41,'Can add email address',11,'add_emailaddress'),(42,'Can change email address',11,'change_emailaddress'),(43,'Can delete email address',11,'delete_emailaddress'),(44,'Can view email address',11,'view_emailaddress'),(45,'Can add email confirmation',12,'add_emailconfirmation'),(46,'Can change email confirmation',12,'change_emailconfirmation'),(47,'Can delete email confirmation',12,'delete_emailconfirmation'),(48,'Can view email confirmation',12,'view_emailconfirmation'),(49,'Can add social account',13,'add_socialaccount'),(50,'Can change social account',13,'change_socialaccount'),(51,'Can delete social account',13,'delete_socialaccount'),(52,'Can view social account',13,'view_socialaccount'),(53,'Can add social application',14,'add_socialapp'),(54,'Can change social application',14,'change_socialapp'),(55,'Can delete social application',14,'delete_socialapp'),(56,'Can view social application',14,'view_socialapp'),(57,'Can add social application token',15,'add_socialtoken'),(58,'Can change social application token',15,'change_socialtoken'),(59,'Can delete social application token',15,'delete_socialtoken'),(60,'Can view social application token',15,'view_socialtoken');
/*!40000 ALTER TABLE `auth_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authtoken_token`
--

DROP TABLE IF EXISTS `authtoken_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authtoken_token` (
  `key` varchar(40) NOT NULL,
  `created` datetime(6) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`key`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `authtoken_token_user_id_35299eff_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authtoken_token`
--

LOCK TABLES `authtoken_token` WRITE;
/*!40000 ALTER TABLE `authtoken_token` DISABLE KEYS */;
INSERT INTO `authtoken_token` VALUES ('2b8d20e6d3385a29ad9fe0f13a4464c4c1d4241b','2020-06-10 06:13:06.861366',3),('c34e7a42e1c1086bec4b4fe58b88cd3aa47a2c6b','2020-06-10 06:11:52.407769',1),('dfb24c8cf9c63ba98e766dad5aa6fc8f3ff8c4d6','2020-06-10 06:12:21.908850',2);
/*!40000 ALTER TABLE `authtoken_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_admin_log`
--

DROP TABLE IF EXISTS `django_admin_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  CONSTRAINT `django_admin_log_user_id_c564eba6_fk_api_user_id` FOREIGN KEY (`user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `django_admin_log`
--

LOCK TABLES `django_admin_log` WRITE;
/*!40000 ALTER TABLE `django_admin_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `django_admin_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_content_type`
--

DROP TABLE IF EXISTS `django_content_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `django_content_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_label` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `django_content_type_app_label_model_76bd3d3b_uniq` (`app_label`,`model`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `django_content_type`
--

LOCK TABLES `django_content_type` WRITE;
/*!40000 ALTER TABLE `django_content_type` DISABLE KEYS */;
INSERT INTO `django_content_type` VALUES (11,'account','emailaddress'),(12,'account','emailconfirmation'),(7,'admin','logentry'),(3,'api','order'),(2,'api','product'),(1,'api','user'),(5,'auth','group'),(4,'auth','permission'),(10,'authtoken','token'),(6,'contenttypes','contenttype'),(9,'sessions','session'),(8,'sites','site'),(13,'socialaccount','socialaccount'),(14,'socialaccount','socialapp'),(15,'socialaccount','socialtoken');
/*!40000 ALTER TABLE `django_content_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_migrations`
--

DROP TABLE IF EXISTS `django_migrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `django_migrations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `applied` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `django_migrations`
--

LOCK TABLES `django_migrations` WRITE;
/*!40000 ALTER TABLE `django_migrations` DISABLE KEYS */;
INSERT INTO `django_migrations` VALUES (1,'contenttypes','0001_initial','2020-06-10 05:50:12.795041'),(2,'contenttypes','0002_remove_content_type_name','2020-06-10 05:50:14.015101'),(3,'auth','0001_initial','2020-06-10 05:50:16.133838'),(4,'auth','0002_alter_permission_name_max_length','2020-06-10 05:50:25.719279'),(5,'auth','0003_alter_user_email_max_length','2020-06-10 05:50:25.782313'),(6,'auth','0004_alter_user_username_opts','2020-06-10 05:50:25.915022'),(7,'auth','0005_alter_user_last_login_null','2020-06-10 05:50:26.041168'),(8,'auth','0006_require_contenttypes_0002','2020-06-10 05:50:26.108977'),(9,'auth','0007_alter_validators_add_error_messages','2020-06-10 05:50:26.185980'),(10,'auth','0008_alter_user_username_max_length','2020-06-10 05:50:26.260130'),(11,'auth','0009_alter_user_last_name_max_length','2020-06-10 05:50:26.341595'),(12,'auth','0010_alter_group_name_max_length','2020-06-10 05:50:26.556889'),(13,'auth','0011_update_proxy_permissions','2020-06-10 05:50:26.640276'),(14,'api','0001_initial','2020-06-10 05:50:39.415048'),(15,'account','0001_initial','2020-06-10 06:01:20.292750'),(16,'account','0002_email_max_length','2020-06-10 06:01:24.125605'),(17,'admin','0001_initial','2020-06-10 06:01:24.680891'),(18,'admin','0002_logentry_remove_auto_add','2020-06-10 06:01:43.957536'),(19,'admin','0003_logentry_add_action_flag_choices','2020-06-10 06:01:44.033477'),(20,'authtoken','0001_initial','2020-06-10 06:01:45.409352'),(21,'authtoken','0002_auto_20160226_1747','2020-06-10 06:01:48.525939'),(22,'sessions','0001_initial','2020-06-10 06:01:49.839606'),(23,'sites','0001_initial','2020-06-10 06:01:51.905306'),(24,'sites','0002_alter_domain_unique','2020-06-10 06:01:52.422245'),(25,'socialaccount','0001_initial','2020-06-10 06:02:03.085338'),(26,'socialaccount','0002_token_max_lengths','2020-06-10 06:02:20.095424'),(27,'socialaccount','0003_extra_data_default_dict','2020-06-10 06:02:20.184369');
/*!40000 ALTER TABLE `django_migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_session`
--

DROP TABLE IF EXISTS `django_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `django_session` (
  `session_key` varchar(40) NOT NULL,
  `session_data` longtext NOT NULL,
  `expire_date` datetime(6) NOT NULL,
  PRIMARY KEY (`session_key`),
  KEY `django_session_expire_date_a5c62663` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `django_session`
--

LOCK TABLES `django_session` WRITE;
/*!40000 ALTER TABLE `django_session` DISABLE KEYS */;
INSERT INTO `django_session` VALUES ('altladmha23n8i3y0bzijhc9p7lgcfq4','NmU4Y2Q4NzcwZmY4OTg3ZmQzYWY0ZWE1ZDg3OGIwMWMzYjgwYmQwMTp7Il9hdXRoX3VzZXJfaWQiOiIzIiwiX2F1dGhfdXNlcl9iYWNrZW5kIjoiZGphbmdvLmNvbnRyaWIuYXV0aC5iYWNrZW5kcy5Nb2RlbEJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiJhM2ZiZGI1MWIxNWY5YmIxNjY3YmVjNmJhZjEyNjM5NzQzOGYwOWNmIn0=','2020-06-24 06:30:16.394781');
/*!40000 ALTER TABLE `django_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `django_site`
--

DROP TABLE IF EXISTS `django_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `django_site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `domain` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `django_site_domain_a2e37b91_uniq` (`domain`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
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
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `socialaccount_socialapp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider` varchar(30) NOT NULL,
  `name` varchar(40) NOT NULL,
  `client_id` varchar(191) NOT NULL,
  `secret` varchar(191) NOT NULL,
  `key` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `socialaccount_socialapp_sites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `socialapp_id` int(11) NOT NULL,
  `site_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `socialaccount_socialapp_sites_socialapp_id_site_id_71a9a768_uniq` (`socialapp_id`,`site_id`),
  KEY `socialaccount_socialapp_sites_site_id_2579dee5_fk_django_site_id` (`site_id`),
  CONSTRAINT `socialaccount_social_socialapp_id_97fb6e7d_fk_socialacc` FOREIGN KEY (`socialapp_id`) REFERENCES `socialaccount_socialapp` (`id`),
  CONSTRAINT `socialaccount_socialapp_sites_site_id_2579dee5_fk_django_site_id` FOREIGN KEY (`site_id`) REFERENCES `django_site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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

-- Dump completed on 2020-06-10  9:01:35
