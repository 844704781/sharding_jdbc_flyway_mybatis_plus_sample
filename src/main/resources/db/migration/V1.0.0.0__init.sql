CREATE TABLE `commodity_0`
(
    `id`         bigint(20)  NOT NULL AUTO_INCREMENT,
    `commodityId` varchar(64) NOT NULL,
    `nonce`      varchar(32) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `commodityId` (`commodityId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;

CREATE TABLE `commodity_1`
(
    `id`         bigint(20)  NOT NULL AUTO_INCREMENT,
    `commodityId` varchar(64) NOT NULL,
    `nonce`      varchar(32) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `commodityId` (`commodityId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;

CREATE TABLE `commodity_2`
(
    `id`         bigint(20)  NOT NULL AUTO_INCREMENT,
    `commodityId` varchar(64) NOT NULL,
    `nonce`      varchar(32) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `commodityId` (`commodityId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;

CREATE TABLE `commodity_3`
(
    `id`         bigint(20)  NOT NULL AUTO_INCREMENT,
    `commodityId` varchar(64) NOT NULL,
    `nonce`      varchar(32) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `commodityId` (`commodityId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;
