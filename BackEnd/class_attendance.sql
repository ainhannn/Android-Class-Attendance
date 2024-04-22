SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS `class_attendance` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `class_attendance`;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` FUNCTION `GenerateRandomCode` () RETURNS VARCHAR(6) CHARSET utf8mb4 COLLATE utf8mb4_general_ci  BEGIN
    DECLARE characters VARCHAR(62) DEFAULT 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    DECLARE code VARCHAR(6) DEFAULT '';
    DECLARE i INT DEFAULT 1;
    
    WHILE i <= 6 DO
        SET code = CONCAT(code, SUBSTRING(characters, FLOOR(1 + RAND() * 62), 1));
        SET i = i + 1;
    END WHILE;
    
    RETURN code;
END$$

DELIMITER ;

CREATE TABLE `attendancecodes` (
  `AttendanceId` int(11) NOT NULL,
  `Code` char(6) NOT NULL,
  `Location` text NOT NULL,
  `LateTime` datetime NOT NULL,
  `ExpiryTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
DELIMITER $$
CREATE TRIGGER `create_code` BEFORE INSERT ON `attendancecodes` FOR EACH ROW BEGIN
  	DECLARE new_code VARCHAR(6);
    SET new_code = GenerateRandomCode();
    
    WHILE (SELECT COUNT(*) FROM attendancecodes WHERE code = new_code) > 0 DO
        SET new_code = GenerateRandomCode();
    END WHILE;

    SET NEW.code = new_code;
END
$$
DELIMITER ;

CREATE TABLE `attendancerecords` (
  `AttendanceId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL,
  `Time` datetime DEFAULT current_timestamp(),
  `Status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `attendances` (
  `Id` int(11) NOT NULL,
  `Time` datetime DEFAULT current_timestamp(),
  `ClassId` int(11) NOT NULL,
  `Times` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `classes` (
  `Id` int(11) NOT NULL,
  `Code` char(6) NOT NULL,
  `Time` datetime NOT NULL DEFAULT current_timestamp(),
  `TeacherId` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Section` varchar(255) DEFAULT NULL,
  `Subject` varchar(255) DEFAULT NULL,
  `Room` varchar(255) DEFAULT NULL,
  `IsArchived` bit(1) DEFAULT b'0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
DELIMITER $$
CREATE TRIGGER `before_insert_classes` BEFORE INSERT ON `classes` FOR EACH ROW BEGIN
	DECLARE new_code VARCHAR(6);
    SET new_code = GenerateRandomCode();
    
    WHILE (SELECT COUNT(*) FROM classes WHERE code = new_code) > 0 DO
        SET new_code = GenerateRandomCode();
    END WHILE;

    SET NEW.code = new_code;  
END
$$
DELIMITER ;

CREATE TABLE `classmembers` (
  `ClassId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `notifications` (
  `Id` int(11) NOT NULL,
  `Time` datetime DEFAULT current_timestamp(),
  `ClassId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL,
  `Content` text DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `users` (
  `Id` int(11) NOT NULL COMMENT 'Primary Key',
  `Time` datetime DEFAULT current_timestamp() COMMENT 'Create time',
  `UID` char(28) NOT NULL COMMENT 'FB Authentication UID',
  `Name` varchar(30) NOT NULL COMMENT 'Display name'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `users` (`Id`, `Time`, `UID`, `Name`) VALUES
(1, '2024-03-28 22:04:20', 'hMnuzMB8SdMPHxaYMD8ooQHcrXB2', 'Ái Nhân'),
(2, '2024-03-28 22:04:44', 'KUzltrVATCZXPFCUZN14EqurWD72', 'Lilly after changed');


ALTER TABLE `attendancecodes`
  ADD PRIMARY KEY (`AttendanceId`),
  ADD UNIQUE KEY `code` (`Code`);

ALTER TABLE `attendancerecords`
  ADD PRIMARY KEY (`AttendanceId`,`UserId`),
  ADD KEY `fk_att_user` (`UserId`);

ALTER TABLE `attendances`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `fk_att_class` (`ClassId`);

ALTER TABLE `classes`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `fk_class_teacher` (`TeacherId`);

ALTER TABLE `classmembers`
  ADD PRIMARY KEY (`ClassId`,`UserId`),
  ADD KEY `fk_class_member` (`UserId`);

ALTER TABLE `notifications`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `fk_noti_class` (`ClassId`),
  ADD KEY `fk_noti_user` (`UserId`);

ALTER TABLE `users`
  ADD PRIMARY KEY (`Id`);


ALTER TABLE `attendances`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `classes`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `notifications`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `users`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key', AUTO_INCREMENT=9;


ALTER TABLE `attendancecodes`
  ADD CONSTRAINT `fk_att_code` FOREIGN KEY (`AttendanceId`) REFERENCES `attendances` (`Id`);

ALTER TABLE `attendancerecords`
  ADD CONSTRAINT `fk_att` FOREIGN KEY (`AttendanceId`) REFERENCES `attendances` (`Id`),
  ADD CONSTRAINT `fk_att_user` FOREIGN KEY (`UserId`) REFERENCES `users` (`Id`);

ALTER TABLE `attendances`
  ADD CONSTRAINT `fk_att_class` FOREIGN KEY (`ClassId`) REFERENCES `classes` (`Id`);

ALTER TABLE `classes`
  ADD CONSTRAINT `fk_class_teacher` FOREIGN KEY (`TeacherId`) REFERENCES `users` (`Id`);

ALTER TABLE `classmembers`
  ADD CONSTRAINT `fk_class` FOREIGN KEY (`ClassId`) REFERENCES `classes` (`Id`),
  ADD CONSTRAINT `fk_class_id` FOREIGN KEY (`ClassId`) REFERENCES `classes` (`Id`),
  ADD CONSTRAINT `fk_class_member` FOREIGN KEY (`UserId`) REFERENCES `users` (`Id`);

ALTER TABLE `notifications`
  ADD CONSTRAINT `fk_noti_class` FOREIGN KEY (`ClassId`) REFERENCES `classes` (`Id`),
  ADD CONSTRAINT `fk_noti_user` FOREIGN KEY (`UserId`) REFERENCES `users` (`Id`);
COMMIT;
