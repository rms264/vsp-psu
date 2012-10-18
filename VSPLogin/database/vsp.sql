CREATE DATABASE  IF NOT EXISTS `vsp` /*!40100 DEFAULT CHARACTER SET latin1 */;
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `vsp` ;
USE `vsp` ;

-- -----------------------------------------------------
-- Table `vsp`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vsp`.`User` ;

CREATE  TABLE IF NOT EXISTS `vsp`.`User` (
  `user_name` VARCHAR(45) NOT NULL UNIQUE,
  `user_pass` VARCHAR(65) NOT NULL ,
  `email` VARCHAR(65) NOT NULL UNIQUE,
  `signup` DATE NOT NULL ,
  `security_question_id` INT NOT NULL ,
  `security_answer` VARCHAR(65) NOT NULL ,
  `current_balance` DOUBLE NOT NULL ,
  PRIMARY KEY (`user_name`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vsp`.`Role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vsp`.`Role` ;

CREATE  TABLE IF NOT EXISTS `vsp`.`Role` (
  `user_name` VARCHAR(45) NOT NULL ,
  `role_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`user_name`, `role_name`) ,
  CONSTRAINT FOREIGN KEY (`user_name`)
    REFERENCES `vsp`.`User` (`user_name`)
    ON DELETE CASCADE 
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `vsp`.`Stock`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vsp`.`Stock` ;

CREATE  TABLE IF NOT EXISTS `vsp`.`Stock` (
  `stock_symbol` VARCHAR(45) NOT NULL ,
  `stock_description` VARCHAR(250) NOT NULL ,
  PRIMARY KEY (`stock_symbol`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vsp`.`Order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vsp`.`Order` ;

CREATE  TABLE IF NOT EXISTS `vsp`.`Order` (
  `order_id` VARCHAR(40) NOT NULL UNIQUE,
  `user_name` VARCHAR(45) NOT NULL ,
  `stock_symbol` VARCHAR(45) NOT NULL ,
  `date_submitted` DATE NOT NULL ,
  `state` INT NOT NULL ,
  `quantity` FLOAT NOT NULL ,
  `action` INT NOT NULL ,
  `type` INT NOT NULL ,
  `limit_price` DOUBLE NULL DEFAULT NULL ,
  `stop_price` DOUBLE NULL DEFAULT NULL ,
  `time_in_force` INT NOT NULL ,
  `last_evaluated` DATE NOT NULL ,
  PRIMARY KEY (`order_id`) ,
  CONSTRAINT FOREIGN KEY (`stock_symbol`)
    REFERENCES `vsp`.`Stock` (`stock_symbol`)
    ON DELETE CASCADE 
    ON UPDATE NO ACTION,
  CONSTRAINT FOREIGN KEY (`user_name` )
    REFERENCES `vsp`.`User` (`user_name` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `vsp`.`Transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vsp`.`Transaction` ;

CREATE  TABLE IF NOT EXISTS `vsp`.`Transaction` (
  `transaction_id` VARCHAR(40) NOT NULL UNIQUE,
  `type` INT NOT NULL ,
  `order_id` VARCHAR(40) NULL DEFAULT NULL ,
  `stock_symbol` VARCHAR(45) NOT NULL ,
  `date` DATE NOT NULL ,
  `quantity` FLOAT NULL DEFAULT NULL ,
  `price_per_share` DOUBLE NULL DEFAULT NULL ,
  `total_value` DOUBLE NULL DEFAULT NULL ,
  `user_name` VARCHAR(45) NOT NULL,
  `note` VARCHAR(128) NULL DEFAULT NULL,
  PRIMARY KEY (`transaction_id`) , 
  CONSTRAINT FOREIGN KEY (`stock_symbol` )
    REFERENCES `vsp`.`Stock` (`stock_symbol` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT FOREIGN KEY (`user_name` )
    REFERENCES `vsp`.`User` (`user_name` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT FOREIGN KEY (`order_id` )
    REFERENCES `vsp`.`Order` (`order_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vsp`.`PortfolioEntry`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vsp`.`PortfolioEntry` ;

CREATE  TABLE IF NOT EXISTS `vsp`.`PortfolioEntry` (
  `user_name` VARCHAR(45) NOT NULL,
  `stock_symbol` VARCHAR(45) NOT NULL ,
  `quantity` FLOAT NOT NULL ,
  `cost_basis_per_share` DOUBLE NOT NULL ,
  PRIMARY KEY (`user_name`, `stock_symbol`) ,
  CONSTRAINT FOREIGN KEY (`stock_symbol` )
    REFERENCES `vsp`.`Stock` (`stock_symbol` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT FOREIGN KEY (`user_name` )
    REFERENCES `vsp`.`User` (`user_name` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

LOCK TABLES `User` WRITE, `Role` WRITE;
INSERT INTO `User` VALUES ('admin','3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2','admin@admin.com','2012-09-20',0,'c7941b6920e2ed43e6bb1a2b450a801a9e3e4e7ab3c018b128b104c9ce24df6a', 0.0),('test','9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08','test@test.com','2012-09-20',0,'16477688c0e00699c6cfa4497a3612d7e83c532062b64b250fed8908128ed548', 20000.0),('test1','1b4f0e9851971998e732078544c96b36c3d01cedf7caa332359d6f1d83567014','test1@test1.com','2012-09-20',0,'16477688c0e00699c6cfa4497a3612d7e83c532062b64b250fed8908128ed548', 20000.0);
INSERT INTO `Role` VALUES ('admin','admin'),('test','trader'),('test1','trader');
UNLOCK TABLES;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;