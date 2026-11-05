-- MySQL dump 10.13  Distrib 8.0.46, for Linux (x86_64)
--
-- Host: localhost    Database: Projet
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `Equipement`
--

DROP TABLE IF EXISTS `Equipement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Equipement` (
  `id_equipement` int NOT NULL AUTO_INCREMENT,
  `reference` varchar(100) NOT NULL,
  `description` varchar(250) NOT NULL,
  `type` enum('Escalator','Ascenseur','Portique','Distributeur ticket') NOT NULL,
  `id_station` int NOT NULL,
  `etat_fontionnement` enum('En marche','En panne','En cours de réparation') NOT NULL,
  PRIMARY KEY (`id_equipement`),
  UNIQUE KEY `reference` (`reference`),
  KEY `id_station` (`id_station`),
  CONSTRAINT `Equipement_ibfk_1` FOREIGN KEY (`id_station`) REFERENCES `Station` (`id_station`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Equipement`
--

LOCK TABLES `Equipement` WRITE;
/*!40000 ALTER TABLE `Equipement` DISABLE KEYS */;
INSERT INTO `Equipement` VALUES (1,'6767','ascenceur principal de la gare. assez vétuste.','Ascenseur',1,'En marche'),(2,'6768','portique usagé, en piteux état.','Portique',1,'En panne'),(3,'6769','énième escalator de la gare, très long.','Escalator',2,'En cours de réparation');
/*!40000 ALTER TABLE `Equipement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Evaluation`
--

DROP TABLE IF EXISTS `Evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Evaluation` (
  `id_evaluation` int NOT NULL AUTO_INCREMENT,
  `note` int NOT NULL,
  `commentaire` varchar(100) DEFAULT NULL,
  `date_evaluation` date NOT NULL,
  PRIMARY KEY (`id_evaluation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Evaluation`
--

LOCK TABLES `Evaluation` WRITE;
/*!40000 ALTER TABLE `Evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `Evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Incident`
--

DROP TABLE IF EXISTS `Incident`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Incident` (
  `id_incident` int NOT NULL AUTO_INCREMENT,
  `id_equipement` int NOT NULL,
  `description` varchar(250) NOT NULL,
  `date_ouverture` datetime NOT NULL,
  `statut` enum('Non affecté','Affecté à un prestataire','Résolu') NOT NULL,
  `id_prestataire` int DEFAULT NULL,
  `date_resolution` datetime DEFAULT NULL,
  `id_evaluation` int DEFAULT NULL,
  PRIMARY KEY (`id_incident`),
  UNIQUE KEY `uq_incident_evaluation` (`id_evaluation`),
  KEY `id_equipement` (`id_equipement`),
  KEY `id_prestataire` (`id_prestataire`),
  CONSTRAINT `fk_incident_evaluation` FOREIGN KEY (`id_evaluation`) REFERENCES `Evaluation` (`id_evaluation`),
  CONSTRAINT `Incident_ibfk_1` FOREIGN KEY (`id_equipement`) REFERENCES `Equipement` (`id_equipement`),
  CONSTRAINT `Incident_ibfk_2` FOREIGN KEY (`id_prestataire`) REFERENCES `Prestataire` (`id_prestataire`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Incident`
--

LOCK TABLES `Incident` WRITE;
/*!40000 ALTER TABLE `Incident` DISABLE KEYS */;
INSERT INTO `Incident` VALUES (1,2,'un fraudeur a cassé le tourniquet du portique.','2026-11-01 17:46:25','Résolu',1,'2026-11-03 10:21:10',NULL),(2,2,'quelqu\'un a encore cassé la porte... ça suffit !','2026-11-05 15:42:27','Non affecté',NULL,NULL,NULL);
/*!40000 ALTER TABLE `Incident` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Prestataire`
--

DROP TABLE IF EXISTS `Prestataire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Prestataire` (
  `id_prestataire` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `mail` varchar(100) NOT NULL,
  PRIMARY KEY (`id_prestataire`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Prestataire`
--

LOCK TABLES `Prestataire` WRITE;
/*!40000 ALTER TABLE `Prestataire` DISABLE KEYS */;
INSERT INTO `Prestataire` VALUES (1,'Jane Doe','janedoe@gmail.com'),(2,'John Doe','john@hotmail.com');
/*!40000 ALTER TABLE `Prestataire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Station`
--

DROP TABLE IF EXISTS `Station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Station` (
  `id_station` int NOT NULL AUTO_INCREMENT,
  `nom_station` varchar(100) NOT NULL,
  PRIMARY KEY (`id_station`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Station`
--

LOCK TABLES `Station` WRITE;
/*!40000 ALTER TABLE `Station` DISABLE KEYS */;
INSERT INTO `Station` VALUES (1,'Villejuif - Louis Aragon'),(2,'Villejuif - Gustave Roussy');
/*!40000 ALTER TABLE `Station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Utilisateur`
--

DROP TABLE IF EXISTS `Utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Utilisateur` (
  `id_connexion` int NOT NULL,
  `mot_de_passe` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `profil` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_connexion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Utilisateur`
--

LOCK TABLES `Utilisateur` WRITE;
/*!40000 ALTER TABLE `Utilisateur` DISABLE KEYS */;
INSERT INTO `Utilisateur` VALUES (1,'$2a$10$CFnieixg9/.a2lMxcBraF.YEFOUAkdOAZwTcvFaANBXjZdla7cthu','Kim','admin');
/*!40000 ALTER TABLE `Utilisateur` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-05 22:33:30
