CREATE TABLE `tenant` (
  `tenant_id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_uuid` varchar(45) DEFAULT NULL,
  `tenant_name` varchar(100) DEFAULT NULL,
  `tenant_description` varchar(100) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `encrypted_datasource` longtext,
  PRIMARY KEY (`tenant_id`)
);