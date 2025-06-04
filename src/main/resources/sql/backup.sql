-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: forkosdb
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Refrescos, aguas, zumos','Bebidas'),(2,'Platos principales, entrantes','Comida');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comandas`
--

DROP TABLE IF EXISTS `comandas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comandas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_hora_apertura` datetime(6) NOT NULL,
  `fecha_hora_cierre` datetime(6) DEFAULT NULL,
  `total` decimal(38,2) DEFAULT NULL,
  `mesa_id` bigint DEFAULT NULL,
  `mozo_id` bigint DEFAULT NULL,
  `cantidad_comensales` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlsihfserb4hx4jdgx2sslp66b` (`mesa_id`),
  KEY `FKr4yrd0rtnl5v0ytoc1rskrjf1` (`mozo_id`),
  CONSTRAINT `FKlsihfserb4hx4jdgx2sslp66b` FOREIGN KEY (`mesa_id`) REFERENCES `mesas` (`id`),
  CONSTRAINT `FKr4yrd0rtnl5v0ytoc1rskrjf1` FOREIGN KEY (`mozo_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comandas`
--

LOCK TABLES `comandas` WRITE;
/*!40000 ALTER TABLE `comandas` DISABLE KEYS */;
INSERT INTO `comandas` VALUES (2,'CERRADA','2025-05-16 17:44:43.902775','2025-05-16 17:48:00.528046',0.00,1,1,NULL),(3,'ABIERTA','2025-05-30 01:28:12.504772',NULL,0.00,1,1,NULL),(4,'ABIERTA','2025-05-30 01:55:09.212413',NULL,0.00,1,1,NULL),(5,'ABIERTA','2025-05-30 08:54:19.443661',NULL,0.00,1,1,NULL),(6,'ABIERTA','2025-05-30 08:59:10.468087',NULL,0.00,1,1,NULL),(7,'ABIERTA','2025-05-30 09:05:46.434470',NULL,0.00,1,1,NULL),(8,'ABIERTA','2025-05-30 09:10:01.833554',NULL,0.00,1,1,NULL),(9,'ABIERTA','2025-05-30 09:11:39.557375',NULL,0.00,2,1,NULL),(10,'ABIERTA','2025-05-30 09:11:43.375490',NULL,0.00,3,1,NULL),(11,'ABIERTA','2025-05-30 09:11:51.187955',NULL,0.00,4,1,NULL),(12,'ABIERTA','2025-05-30 09:12:14.832145',NULL,0.00,1,1,NULL),(13,'ABIERTA','2025-05-30 09:12:37.398208',NULL,0.00,2,1,NULL),(14,'ABIERTA','2025-05-30 09:13:59.445852',NULL,0.00,1,1,NULL),(15,'ABIERTA','2025-05-30 09:14:07.900151',NULL,0.00,2,1,NULL),(16,'ABIERTA','2025-05-30 09:14:11.192898',NULL,0.00,4,1,NULL),(17,'ABIERTA','2025-05-30 16:55:05.869013',NULL,12.45,1,1,1),(18,'ABIERTA','2025-05-30 16:56:43.137977',NULL,16.95,2,1,5),(19,'ABIERTA','2025-05-30 17:14:47.879607',NULL,12.45,3,1,1),(20,'ABIERTA','2025-05-30 17:15:57.796838',NULL,0.00,4,1,1);
/*!40000 ALTER TABLE `comandas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredientes`
--

DROP TABLE IF EXISTS `ingredientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredientes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `stock_actual` int DEFAULT NULL,
  `stock_minimo` int DEFAULT NULL,
  `unidad` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKmswp95l2180nvkxkl3hoge6fy` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredientes`
--

LOCK TABLES `ingredientes` WRITE;
/*!40000 ALTER TABLE `ingredientes` DISABLE KEYS */;
/*!40000 ALTER TABLE `ingredientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_comanda`
--

DROP TABLE IF EXISTS `item_comanda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_comanda` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad` int NOT NULL,
  `estado` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `notas` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `precio_unitario` decimal(38,2) NOT NULL,
  `comanda_id` bigint NOT NULL,
  `producto_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjngt3x6tjbcm766jjw5mefs2e` (`comanda_id`),
  KEY `FKs6wsgh41qptws7inghl0u0dl9` (`producto_id`),
  CONSTRAINT `FKjngt3x6tjbcm766jjw5mefs2e` FOREIGN KEY (`comanda_id`) REFERENCES `comandas` (`id`),
  CONSTRAINT `FKs6wsgh41qptws7inghl0u0dl9` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_comanda`
--

LOCK TABLES `item_comanda` WRITE;
/*!40000 ALTER TABLE `item_comanda` DISABLE KEYS */;
INSERT INTO `item_comanda` VALUES (7,1,'PEDIDO','',8.95,17,2),(8,1,'PEDIDO','',3.50,17,3),(9,1,'PEDIDO','',8.95,18,2),(10,1,'PEDIDO','',3.50,18,3),(11,1,'PEDIDO','',2.50,18,4),(12,1,'PEDIDO','',2.00,18,5),(13,1,'PEDIDO','',8.95,19,2),(14,1,'PEDIDO','',3.50,19,3);
/*!40000 ALTER TABLE `item_comanda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mesas`
--

DROP TABLE IF EXISTS `mesas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mesas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `capacidad` int DEFAULT NULL,
  `estado` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `numero` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKj0e0hutvqcecq56ogqll8feqw` (`numero`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesas`
--

LOCK TABLES `mesas` WRITE;
/*!40000 ALTER TABLE `mesas` DISABLE KEYS */;
INSERT INTO `mesas` VALUES (1,4,'\"OCUPADA\"','Mesa 1'),(2,6,'\"OCUPADA\"','Mesa 2'),(3,2,'\"OCUPADA\"','Barra 1'),(4,6,'OCUPADA','Mesa 3');
/*!40000 ALTER TABLE `mesas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto_ingrediente`
--

DROP TABLE IF EXISTS `producto_ingrediente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto_ingrediente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad_necesaria` decimal(38,2) NOT NULL,
  `ingrediente_id` bigint NOT NULL,
  `producto_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr44ajcf484mqo0qh0u7emblk0` (`ingrediente_id`),
  KEY `FK9x6sp9j9n1p5ha0uh3ftdqafd` (`producto_id`),
  CONSTRAINT `FK9x6sp9j9n1p5ha0uh3ftdqafd` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FKr44ajcf484mqo0qh0u7emblk0` FOREIGN KEY (`ingrediente_id`) REFERENCES `ingredientes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_ingrediente`
--

LOCK TABLES `producto_ingrediente` WRITE;
/*!40000 ALTER TABLE `producto_ingrediente` DISABLE KEYS */;
/*!40000 ALTER TABLE `producto_ingrediente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `precio` double NOT NULL,
  `stock` int NOT NULL,
  `categoria_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2fwq10nwymfv7fumctxt9vpgb` (`categoria_id`),
  CONSTRAINT `FK2fwq10nwymfv7fumctxt9vpgb` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (2,'Carne 150g, lechuga, tomate, queso','Hamburguesa Clásica',8.95,20,2),(3,'Ración de patatas fritas','Patatas Fritas',3.5,50,2),(4,'Lata de refresco cola','Refresco Cola',2.5,30,1),(5,'Botella de agua 500ml','Agua sin gas',2,40,1);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN'),(2,'MOZO'),(3,'COCINA');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pin` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rol_id` bigint DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKm2dvbwfge291euvmk6vkkocao` (`username`),
  KEY `FKqf5elo4jcq7qrt83oi0qmenjo` (`rol_id`),
  CONSTRAINT `FKqf5elo4jcq7qrt83oi0qmenjo` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,_binary '','mozo1@example.com','Mozo Principal','temporal_password_hash','1234',2,''),(2,_binary '','mozo.prueba@forkos.com','TestUser','$2a$10$1zlsk/yNDWkgMfSi2pUiTetWoww2VaoBgJHCLWapYowvcyjM9mPou','123456',1,'TestUser'),(3,_binary '','newtestuser@forkos.com','Nuevo Usuario Prueba','$2a$10$xv6ibjH6DTyoIfLpvVD//ueQWPIa5ezus.8mK8h8YPJ.hL7ybjFsS','123456',1,'abatido');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-04 15:56:45
