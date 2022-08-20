DROP DATABASE IF EXISTS `test`;
CREATE DATABASE `test`;
USE test;

DROP TABLE IF EXISTS `id_biz`;

CREATE TABLE `id_biz`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `biz_type` varchar(200) NOT NULL DEFAULT '' COMMENT '业务标识比如 表的标识',
    `rule` int(10) NOT NULL DEFAULT '0' COMMENT '规则: 0:无 1:单号段 2:双号段缓存',
    PRIMARY KEY(`id`),
    UNIQUE KEY `uniq_biz_type`(`biz_type`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = 'id业务表';


INSERT INTO test.id_biz (id, biz_type, rule) VALUES (1, 'testGenIdSingleSection', 1);
INSERT INTO test.id_biz (id, biz_type, rule) VALUES (2, 'testGenIdTwoSection', 2);

DROP TABLE IF EXISTS `id_gen`;

CREATE TABLE `id_gen`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `max_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前最大id',
    `step` int(10) NOT NULL DEFAULT '0' COMMENT '号段长度',
    `version` bigint(10) NOT NULL DEFAULT '0' COMMENT '版本',
    `biz_id` bigint(10) NOT NULL DEFAULT '0' COMMENT '业务id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_biz_id`(`biz_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '业务id表';

INSERT INTO test.id_gen (id, max_id, step, version, biz_id) VALUES (1, 1, 10, 1, 1);
INSERT INTO test.id_gen (id, max_id, step, version, biz_id) VALUES (2, 1, 10, 1, 2);
