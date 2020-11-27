CREATE SCHEMA IF NOT EXISTS testdb;

CREATE TABLE IF NOT EXISTS  testdb.CERTIFICATES(
    `id` INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY ,
     name VARCHAR(45) NOT NULL,
     description VARCHAR(45) NOT NULL,
    `price` DECIMAL(18,2) NOT NULL,
    `createDate` DATETIME NOT NULL,
    `createDateTimezone` VARCHAR(45) NOT NULL,
    `lastUpdateDate` DATETIME NOT NULL,
    `lastUpdateDateTimezone` VARCHAR(45) NOT NULL,
    `duration` BIGINT(20) NOT NULL
                                                                  );

CREATE TABLE IF NOT EXISTS  testdb.tag(
    `id` INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    `name` VARCHAR(45) NOT NULL
                                                     );

CREATE TABLE IF NOT EXISTS  testdb.tag_m2m_gift_certificate (
    `tagId` INT(11) NOT NULL,
    `giftCertificateId` INT(11) NOT NULL,

     FOREIGN KEY (`giftCertificateId`) REFERENCES  testdb.certificates (`id`) ON DELETE CASCADE,
     FOREIGN KEY (`tagId`) REFERENCES  testdb.tag(`id`) ON DELETE CASCADE
                                                                          );

CREATE TABLE IF NOT EXISTS testdb.users (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
     PRIMARY KEY (`id`)
                                        );

CREATE TABLE IF NOT EXISTS testdb.orders (
    id INT NOT NULL AUTO_INCREMENT,
    `orderDate` DATETIME NOT NULL,
    `userId` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`userId`) REFERENCES testdb.users (`id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION
                                           );

CREATE TABLE IF NOT EXISTS testdb.orders_m2m_gift_certificate (
    `ordersId` INT NOT NULL,
    `giftCertificateId` INT(11) NOT NULL,
    PRIMARY KEY (`ordersId`, `giftCertificateId`),

    FOREIGN KEY (`ordersId`) REFERENCES testdb.orders (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (`giftCertificateId`) REFERENCES testdb.certificates (`id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION
                                                              );

INSERT INTO testdb.tag(`name`) VALUES ('velo' );
INSERT INTO testdb.tag(`name`) VALUES ('spa' );

INSERT INTO testdb.certificates( name, description,  `price`, `createDate`,
                                 `createDateTimezone`, `lastUpdateDate`, `lastUpdateDateTimezone`, `duration`)
VALUES ('Bike Certificate', 'Very good bikes', 12.99, '2020-09-12 12:12:11', '+03:00','2020-09-12 12:12:11', '+03:00', 12);
INSERT INTO testdb.certificates(name, description,  `price`, `createDate`,
                                `createDateTimezone`, `lastUpdateDate`, `lastUpdateDateTimezone`, `duration`)
VALUES ('Spa Certificate', 'Great spa', 19.99, '2020-09-12 12:12:11', '+03:00','2020-09-12 12:12:11', '+03:00', 22);

INSERT INTO testdb.tag_m2m_gift_certificate VALUES ( 1, 1);
INSERT INTO testdb.tag_m2m_gift_certificate VALUES ( 2, 2);
