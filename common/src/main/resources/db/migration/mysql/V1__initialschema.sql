CREATE TABLE `product` (
           `product_id` bigint NOT NULL AUTO_INCREMENT,
           `name` varchar(100) DEFAULT NULL,
           `price` double DEFAULT NULL,
           PRIMARY KEY (`product_id`)
);