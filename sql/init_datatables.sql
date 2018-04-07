CREATE DATABASE byochain;
USE byochain;

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

-- -----------------------------------------------------
-- Table `byochain`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(256) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  `creation_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login` DATETIME NULL,
  `enabled` INT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `byochain`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` INT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `byochain`.`user_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_roles_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_roles_id`),
  INDEX `fk_username_idx` (`user_id` ASC),
  INDEX `fk_role_idx` (`role_id` ASC),
  CONSTRAINT `fk_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `byochain`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `byochain`.`role` (`role_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `byochain`.`block_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `block_data` (
  `block_data_id` INT NOT NULL AUTO_INCREMENT,
  `data` VARCHAR(5000) NOT NULL,
  `logo` VARCHAR(5000) NOT NULL,
  `expiration_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `enabled` INT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`block_data_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `byochain`.`block`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `block` (
  `block_id` INT NOT NULL AUTO_INCREMENT,
  `miner_id` INT NOT NULL,
  `block_data_id` INT NOT NULL,
  `nonce` INT NOT NULL,
  `hash` VARCHAR(5000) NOT NULL,
  `previous_hash` VARCHAR(5000) NOT NULL,
  `timestamp` DATETIME(3) NOT NULL,
  PRIMARY KEY (`block_id`),
  CONSTRAINT `fk_miner`
    FOREIGN KEY (`miner_id`)
    REFERENCES `byochain`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_block_data`
    FOREIGN KEY (`block_data_id`)
    REFERENCES `byochain`.`block_data` (`block_data_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- -----------------------------------------------------
-- Table `byochain`.`block_validation_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `block_validation_user` (
  `block_validation_user_id` INT NOT NULL AUTO_INCREMENT,
  `block_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`block_validation_user_id`),
  INDEX `fk_block_idx` (`block_id` ASC),
  INDEX `fk_user_validator_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_validator`
    FOREIGN KEY (`user_id`)
    REFERENCES `byochain`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_block`
    FOREIGN KEY (`block_id`)
    REFERENCES `byochain`.`block` (`block_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `byochain`.`block_referer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `block_referer` (
  `block_referer_id` INT NOT NULL AUTO_INCREMENT,
  `referer` VARCHAR(500) NOT NULL,
  `owner_id` INT NOT NULL,
  PRIMARY KEY (`block_referer_id`),
  CONSTRAINT `fk_user_owner`
    FOREIGN KEY (`owner_id`)
    REFERENCES `byochain`.`block` (`block_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;