CREATE TABLE `commodity`
(
    `id`         bigint(20)  NOT NULL AUTO_INCREMENT,
    `commodityId` varchar(64) NOT NULL,
    `nonce`      varchar(32) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `commodityId` (`commodityId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;
