
drop database test;
create database `test`;

use database test;

create table `user`(
  `id` bigint(20) unsigned AUTO_INCREMENT NOT NULL DEFAULT '0' COMMENT 'id',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '数据集表';

create table `group`(
  `id` bigint(20) unsigned AUTO_INCREMENT NOT NULL DEFAULT '0' COMMENT 'id',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '名称',
  `users` json NOT NULL DEFAULT '' COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '数据集表';


insert into group(id, name, users) values (1, '小组1', '[{"id":1,"name":"王五"}]');