
-- -----------------------------------------------------
-- Schema gift-certificates
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS test;
-- -----------------------------------------------------
-- Table test.`gift-certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS  test.certificates(
    `id` INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    `name` VARCHAR(45) NOT NULL,
    `description` VARCHAR(45) NOT NULL,
    `price` DECIMAL(18,2) NOT NULL,
    `createDate` DATETIME NOT NULL,
    `createDateTimezone` VARCHAR(45) NOT NULL,
    `lastUpdateDate` DATETIME NOT NULL,
    `lastUpdateDateTimezone` VARCHAR(45) NOT NULL,
    `duration` BIGINT(20) NOT NULL
                                                                  );

-- -----------------------------------------------------
-- Table test.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS  test.tag(
    `id` INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    `name` VARCHAR(45) NOT NULL
                                                     );
-- -----------------------------------------------------

-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS  test.tag_m2m_gift_certificate (
    `tagId` INT(11) NOT NULL,
    `giftCertificateId` INT(11) NOT NULL,

     FOREIGN KEY (`giftCertificateId`) REFERENCES  test.certificates (`id`) ON DELETE CASCADE,
     FOREIGN KEY (`tagId`) REFERENCES  test.tag(`id`) ON DELETE CASCADE
                                                                          );





