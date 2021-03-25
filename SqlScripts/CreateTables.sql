-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema youtube
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema youtube
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `youtube` DEFAULT CHARACTER SET utf8 ;
USE `youtube` ;

-- -----------------------------------------------------
-- Table `youtube`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `age` INT(11) NULL DEFAULT NULL,
  `password` VARCHAR(50) NOT NULL,
  `city` VARCHAR(45) NULL DEFAULT NULL,
  `register_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `youtube`.`videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`videos` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `upload_date` DATETIME NOT NULL,
  `description` VARCHAR(300) NULL DEFAULT 'No description.',
  `path` VARCHAR(100) NOT NULL,
  `link` VARCHAR(200) NOT NULL,
  `owner_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `video_has_owner_fk_idx` (`owner_id` ASC),
  CONSTRAINT `video_has_owner_fk`
    FOREIGN KEY (`owner_id`)
    REFERENCES `youtube`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `youtube`.`comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`comments` (
  `id` INT(11) NOT NULL,
  `text` VARCHAR(250) NOT NULL,
  `commented_on` DATETIME NOT NULL,
  `user_id` INT(11) NOT NULL,
  `video_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_commented_fk_idx` (`user_id` ASC),
  INDEX `commented_video_fk_idx` (`video_id` ASC),
  CONSTRAINT `commented_video_fk`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtube`.`videos` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `user_commented_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtube`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `youtube`.`playlists`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`playlists` (
  `id` INT(11) NOT NULL,
  `title` VARCHAR(50) NOT NULL,
  `created_date` DATETIME NOT NULL,
  `owner_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `playlist_has_owner_fk_idx` (`owner_id` ASC),
  CONSTRAINT `playlist_has_owner_fk`
    FOREIGN KEY (`owner_id`)
    REFERENCES `youtube`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `youtube`.`playlists_have_videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`playlists_have_videos` (
  `playlist_id` INT(11) NOT NULL,
  `video_id` INT(11) NOT NULL,
  PRIMARY KEY (`playlist_id`, `video_id`),
  INDEX `included_video_fk_idx` (`video_id` ASC),
  CONSTRAINT `included_video_fk`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtube`.`videos` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `playlist_has_video_fk`
    FOREIGN KEY (`playlist_id`)
    REFERENCES `youtube`.`playlists` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `youtube`.`subscriptions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`subscriptions` (
  `subscribed_to_id` INT(11) NOT NULL,
  `subscriber_id` INT(11) NOT NULL,
  PRIMARY KEY (`subscribed_to_id`, `subscriber_id`),
  INDEX `subscribed_user_fk_idx` (`subscriber_id` ASC),
  CONSTRAINT `subscribed_user_fk`
    FOREIGN KEY (`subscriber_id`)
    REFERENCES `youtube`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `user_has_subscription_fk`
    FOREIGN KEY (`subscribed_to_id`)
    REFERENCES `youtube`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `youtube`.`users_dislike_comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`users_dislike_comments` (
  `comment_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  PRIMARY KEY (`comment_id`, `user_id`),
  INDEX `user_dislike_comment_idx` (`user_id` ASC),
  CONSTRAINT `disliked_comment_fk`
    FOREIGN KEY (`comment_id`)
    REFERENCES `youtube`.`comments` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `user_dislike_comment_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtube`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `youtube`.`users_dislike_videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`users_dislike_videos` (
  `user_id` INT(11) NOT NULL,
  `video_id` INT(11) NOT NULL,
  PRIMARY KEY (`user_id`, `video_id`),
  INDEX `disliked_video_idx` (`video_id` ASC),
  CONSTRAINT `disliked_video`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtube`.`videos` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `user_dislike_video`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtube`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `youtube`.`users_like_comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`users_like_comments` (
  `comment_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  PRIMARY KEY (`comment_id`, `user_id`),
  INDEX `user_like_comment_fk_idx` (`user_id` ASC),
  CONSTRAINT `liked_comment_fk`
    FOREIGN KEY (`comment_id`)
    REFERENCES `youtube`.`comments` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `user_like_comment_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtube`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `youtube`.`users_like_videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube`.`users_like_videos` (
  `user_id` INT(11) NOT NULL,
  `video_id` INT(11) NOT NULL,
  PRIMARY KEY (`video_id`, `user_id`),
  INDEX `liked_video_fk_idx` (`video_id` ASC),
  INDEX `user_like_video_fk` (`user_id` ASC),
  CONSTRAINT `liked_video_fk`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtube`.`videos` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `user_like_video_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtube`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
