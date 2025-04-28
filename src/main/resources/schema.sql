CREATE SCHEMA IF NOT EXISTS calendar;

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
);

CREATE TABLE `date_note` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `event_date` DATETIME NOT NULL
);


CREATE TABLE `note` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `date_creation` DATETIME NOT NULL,
    `date_modification` DATETIME,
    `description` TEXT,
    `is_important` BOOLEAN DEFAULT FALSE,
    `title` VARCHAR(255) NOT NULL,
    `date_note_id` INT,
    `user_id` INT,
    `archived` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

CREATE TABLE `attachment` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `base64` TEXT,
    `date_creation` DATETIME NOT NULL,
    `nome` VARCHAR(255),
    `path` VARCHAR(255),
    `note_id` INT,
    FOREIGN KEY (`note_id`) REFERENCES `note`(`id`)
);

CREATE TABLE `personalized_note` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `color` VARCHAR(20),
    `custom_message` TEXT,
    `note_id` INT,
    FOREIGN KEY (`note_id`) REFERENCES `note`(`id`)
);

CREATE TABLE `change_history` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `change_type` VARCHAR(255),
    `modification_date` DATETIME,
    `modified_entity` VARCHAR(255),
    `new_data` TEXT,
    `previous_data` TEXT,
    `modified_by` INT,
    `note_id` INT,
    FOREIGN KEY (`note_id`) REFERENCES `note`(`id`),
    FOREIGN KEY (`modified_by`) REFERENCES `user`(`id`)
);

CREATE TABLE `feedbacks` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `comment` TEXT,
    `response` TEXT,
    `admin_id` INT,
    FOREIGN KEY (`admin_id`) REFERENCES `user`(`id`)
);

CREATE TABLE `error_log` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `endpoint` VARCHAR(255),
    `error_message` TEXT,
    `stack_trace` TEXT,
    `timestamp` DATETIME NOT NULL,
    `account` VARCHAR(255),
    `user` INT,
    FOREIGN KEY (`user`) REFERENCES `user`(`id`)
);
