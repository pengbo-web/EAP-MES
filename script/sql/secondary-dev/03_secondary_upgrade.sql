-- 二开增量脚本(从原版 Liao-MES 基线升级用): 5 张新表 + 存量表新增列
-- 全新环境请直接导入 01_schema_full.sql + 02_base_data.sql, 无需本文件
-- 注意: MySQL 的 ALTER ADD COLUMN 不支持 IF NOT EXISTS, 重复执行报 Duplicate column 可忽略

﻿
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
DROP TABLE IF EXISTS `mes_eap_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `mes_eap_device` (
  `id` bigint NOT NULL COMMENT 'primary key',
  `device_code` varchar(64) NOT NULL COMMENT 'device code',
  `device_name` varchar(128) DEFAULT NULL COMMENT 'device name',
  `status` char(1) DEFAULT '0' COMMENT '0 running 1 alarm',
  `last_event_time` datetime DEFAULT NULL COMMENT 'last event time',
  `today_count` int DEFAULT '0' COMMENT 'today completed count',
  `today_alarm` int DEFAULT '0' COMMENT 'today alarm count',
  `today_defective` int DEFAULT '0' COMMENT 'today defective count',
  `del_flag` char(1) DEFAULT '0',
  `tenant_id` varchar(20) DEFAULT '000000',
  `create_dept` bigint DEFAULT NULL,
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device` (`device_code`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='EAP device state (secondary development)';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `mes_eap_device_state_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `mes_eap_device_state_log` (
  `id` bigint NOT NULL,
  `device_code` varchar(64) NOT NULL,
  `from_status` char(1) DEFAULT NULL,
  `to_status` char(1) NOT NULL,
  `event_time` datetime NOT NULL,
  `tenant_id` varchar(20) DEFAULT '000000',
  `del_flag` char(1) DEFAULT '0',
  `create_dept` bigint DEFAULT NULL,
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_dev_time` (`device_code`,`event_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='device state change log';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `mes_eap_device_spec`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `mes_eap_device_spec` (
  `id` bigint NOT NULL,
  `device_code` varchar(64) NOT NULL,
  `ideal_cycle_sec` decimal(12,4) NOT NULL DEFAULT '0.0000',
  `planned_minutes` int NOT NULL DEFAULT '480',
  `tenant_id` varchar(20) DEFAULT '000000',
  `del_flag` char(1) DEFAULT '0',
  `create_dept` bigint DEFAULT NULL,
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dev` (`device_code`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='device OEE spec';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `mes_workplace_suspend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `mes_workplace_suspend` (
  `id` bigint NOT NULL,
  `workplace_id` bigint NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `suspend_until` datetime NOT NULL,
  `tenant_id` varchar(20) DEFAULT '000000',
  `del_flag` char(1) DEFAULT '0',
  `create_dept` bigint DEFAULT NULL,
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_wp` (`workplace_id`,`suspend_until`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='workplace suspend (alarm linkage)';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `mes_fifo_alloc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `mes_fifo_alloc` (
  `id` bigint NOT NULL COMMENT 'primary key',
  `ledger_sn` int NOT NULL COMMENT 'inbound ledger lot sn',
  `stockid` bigint NOT NULL COMMENT 'stock archive id',
  `qty` int NOT NULL COMMENT 'allocated quantity from this lot',
  `out_no` varchar(64) DEFAULT NULL COMMENT 'outbound document number/order number',
  `del_flag` char(1) DEFAULT '0',
  `tenant_id` varchar(20) DEFAULT '000000',
  `create_dept` bigint DEFAULT NULL,
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ledger` (`ledger_sn`),
  KEY `idx_stock` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='FIFO batch-level outbound allocation details (secondary development)';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


-- 存量表新增列
ALTER TABLE mes_work_rec ADD COLUMN device_code varchar(64) DEFAULT NULL COMMENT 'EAP设备编码(报工快照)';
ALTER TABLE mes_work_rec ADD COLUMN params varchar(500) DEFAULT NULL COMMENT 'EAP工艺参数快照JSON';
ALTER TABLE mes_task ADD COLUMN workplace_dispatch varchar(64) DEFAULT NULL COMMENT '派工方式 RULE/MANUAL';
