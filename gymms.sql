/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : gymms

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 20/11/2024 11:25:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activities
-- ----------------------------
DROP TABLE IF EXISTS `activities`;
CREATE TABLE `activities`
(
    `activity_id`      int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '活动ID',
    `organizer_id`     int(11) UNSIGNED                                              NOT NULL COMMENT '组织者ID',
    `name`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
    `location`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '活动地点',
    `category_id`      int(11) UNSIGNED                                              NOT NULL COMMENT '运动类型ID',
    `start_time`       datetime                                                      NOT NULL COMMENT '开始时间',
    `end_time`         datetime                                                      NOT NULL COMMENT '结束时间',
    `status`           int(11)                                                       NOT NULL DEFAULT 0 COMMENT '0未开始；1进行中；2已结束',
    `signed_up_count`  int(11)                                                       NULL     DEFAULT 0 COMMENT '已报名人数',
    `sign_up_deadline` datetime                                                      NULL     DEFAULT NULL COMMENT '报名截至时间',
    `description`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '简介',
    PRIMARY KEY (`activity_id`) USING BTREE,
    INDEX `fk_activity_organizer` (`organizer_id` ASC) USING BTREE,
    INDEX `fk_activity_categories` (`category_id` ASC) USING BTREE,
    CONSTRAINT `fk_activity_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_activity_organizer` FOREIGN KEY (`organizer_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activities
-- ----------------------------
INSERT INTO `activities`
VALUES (4, 12, 'qww', '教学楼', 2, '2024-11-13 16:00:00', '2024-11-18 16:00:00', 0, 15, '2024-11-13 11:20:47', '简介');
INSERT INTO `activities`
VALUES (5, 12, 'wetwe', 'egwegeg', 2, '2024-11-12 02:50:39', '2024-11-13 02:50:40', 1, 21, '2024-11-15 13:20:53', '简述');
INSERT INTO `activities`
VALUES (6, 11, '12141', '155', 2, '2024-11-19 16:00:00', '2024-11-22 16:00:00', 0, 0, '2024-11-20 16:00:00', '==--');
INSERT INTO `activities`
VALUES (8, 11, '12323', '112wdww', 1, '2024-11-21 16:00:00', '2024-11-24 16:00:00', 0, 0, '2024-11-18 16:00:00',
        'qwwqww');

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`
(
    `id`          int(10) UNSIGNED                                                NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci    NOT NULL COMMENT '文章标题',
    `content`     varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
    `cover_img`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '文章封面',
    `state`       varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NULL DEFAULT '草稿' COMMENT '文章状态: 只能是[已发布] 或者 [草稿]',
    `category_id` int(10) UNSIGNED                                                NULL DEFAULT NULL COMMENT '文章分类ID',
    `create_user` int(10) UNSIGNED                                                NOT NULL COMMENT '创建人ID',
    `create_time` datetime                                                        NOT NULL COMMENT '创建时间',
    `update_time` datetime                                                        NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_article_category` (`category_id` ASC) USING BTREE,
    INDEX `fk_article_user` (`create_user` ASC) USING BTREE,
    CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_article_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 39
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article`
VALUES (38, '测试文章1', '测试1', 'http://localhost:5173/src/assets/uploadImg/df09b4ea-4988-412c-9449-03301cf90fd7.jpg',
        '已发布', 11, 11, '2024-10-22 09:31:18', '2024-10-22 09:31:18');
INSERT INTO `article`
VALUES (39, '测试2', '测试测试', 'http://localhost:5173/src/assets/uploadImg/253bbb1b-93b8-4688-8ee2-fee24fb1ed40.jpg', '已发布',
        11, 11, '2024-11-11 20:43:03', '2024-11-11 20:43:03');

-- ----------------------------
-- Table structure for bookings
-- ----------------------------
DROP TABLE IF EXISTS `bookings`;
CREATE TABLE `bookings`
(
    `booking_id`   int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '预约ID',
    `user_id`      int(11) UNSIGNED NOT NULL COMMENT '用户ID',
    `court_id`     int(11) UNSIGNED NOT NULL COMMENT '球场ID',
    `start_time`   time             NOT NULL COMMENT '预约时间',
    `end_time`     time             NULL DEFAULT NULL,
    `created_at`   timestamp        NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `day_of_year`  int(11)          NULL DEFAULT NULL COMMENT '预约年',
    `day_of_month` int(11)          NULL DEFAULT NULL COMMENT '预约月份',
    `day`          int(11)          NULL DEFAULT NULL COMMENT '预约日期',
    PRIMARY KEY (`booking_id`) USING BTREE,
    INDEX `fk_booking_user` (`user_id` ASC) USING BTREE,
    INDEX `fk_booking_court` (`court_id` ASC) USING BTREE,
    CONSTRAINT `fk_booking_court` FOREIGN KEY (`court_id`) REFERENCES `courts` (`court_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_booking_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '预约表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bookings
-- ----------------------------
INSERT INTO `bookings`
VALUES (1, 11, 17, '10:00:00', '11:00:00', '2024-11-09 11:13:00', 2024, 11, 9);
INSERT INTO `bookings`
VALUES (2, 11, 16, '09:30:00', '10:30:00', NULL, 2024, 11, 10);
INSERT INTO `bookings`
VALUES (3, 11, 16, '10:00:00', '11:00:00', NULL, 2024, 11, 11);
INSERT INTO `bookings`
VALUES (4, 11, 17, '11:30:00', '14:30:00', NULL, 2024, 11, 10);
INSERT INTO `bookings`
VALUES (5, 11, 15, '09:00:00', '09:30:00', NULL, 2024, 11, 9);
INSERT INTO `bookings`
VALUES (6, 11, 16, '15:00:00', '16:30:00', NULL, 2024, 11, 9);
INSERT INTO `bookings`
VALUES (7, 11, 15, '10:30:00', '11:00:00', '2024-11-09 16:58:44', 2024, 11, 9);
INSERT INTO `bookings`
VALUES (8, 11, 28, '10:30:00', '11:30:00', '2024-11-09 16:59:24', 2024, 11, 10);
INSERT INTO `bookings`
VALUES (9, 15, 17, '11:00:00', '14:00:00', '2024-11-18 22:49:46', 2024, 11, 19);
INSERT INTO `bookings`
VALUES (10, 15, 17, '15:30:00', '17:30:00', '2024-11-18 22:50:37', 2024, 11, 19);
INSERT INTO `bookings`
VALUES (11, 15, 17, '18:00:00', '19:30:00', '2024-11-18 22:53:16', 2024, 11, 19);

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`
(
    `category_id` int(11) UNSIGNED                                             NOT NULL AUTO_INCREMENT COMMENT '类别ID',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类别名称',
    PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '类别表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories`
VALUES (1, '篮球');
INSERT INTO `categories`
VALUES (2, '足球');
INSERT INTO `categories`
VALUES (3, '排球');
INSERT INTO `categories`
VALUES (4, '乒乓球');
INSERT INTO `categories`
VALUES (5, '羽毛球');
INSERT INTO `categories`
VALUES (6, '网球');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `id`             int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `category_name`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
    `category_alias` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类别名',
    `create_user`    int(10) UNSIGNED                                             NOT NULL COMMENT '创建人ID',
    `create_time`    datetime                                                     NOT NULL COMMENT '创建时间',
    `update_time`    datetime                                                     NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_category_user` (`create_user` ASC) USING BTREE,
    CONSTRAINT `fk_category_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category`
VALUES (11, '测试分类1', 'cs1', 11, '2024-10-22 09:30:55', '2024-10-22 09:30:55');

-- ----------------------------
-- Table structure for clubs
-- ----------------------------
DROP TABLE IF EXISTS `clubs`;
CREATE TABLE `clubs`
(
    `club_id`         int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '社团ID',
    `category_id`     int(11) UNSIGNED                                              NOT NULL COMMENT '类别ID',
    `name`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '俱乐部名称',
    `description`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NULL COMMENT '简介',
    `address`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
    `contact_user_id` int(11) UNSIGNED                                              NOT NULL COMMENT '联系人ID',
    `members`         int(11)                                                       NULL DEFAULT 0 COMMENT '社团人数',
    `clubs_pic`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`club_id`) USING BTREE,
    INDEX `fk_club_category` (`category_id` ASC) USING BTREE,
    INDEX `fk_club_contact_user` (`contact_user_id` ASC) USING BTREE,
    CONSTRAINT `fk_club_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_club_contact_user` FOREIGN KEY (`contact_user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '俱乐部表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of clubs
-- ----------------------------
INSERT INTO `clubs`
VALUES (1, 3, '校园排球社', '欢迎加入排球大家庭', '学生活动中心三楼 102室', 11, 2,
        'http://localhost:5173/src/assets/uploadImg/f7b2a070-e3eb-41f4-8342-a71c000cbe8f.jpg');
INSERT INTO `clubs`
VALUES (13, 2, '校园足球社团', '欢迎加入足球社团', '学生活动中心三楼101室', 12, 1,
        'http://localhost:5173/src/assets/uploadImg/23109328-a7df-433e-8163-f9e804f08665.jpg');
INSERT INTO `clubs`
VALUES (14, 4, '乒乓球社团', '欢迎加入乒乓球社团', '学生活动中心二楼103室', 11, 0,
        'http://localhost:5173/src/assets/uploadImg/5f595a7f-97ad-4052-9dc3-a677d0ec1e05.jpg');
INSERT INTO `clubs`
VALUES (15, 5, '羽毛球社团', '欢迎加入羽毛球社团', '学生活动中心二楼104室', 11, 0,
        'http://localhost:5173/src/assets/uploadImg/27a6bea1-7adb-48f7-b0c1-a53b036d1df9.jpg');
INSERT INTO `clubs`
VALUES (16, 1, '校园篮球社', '欢迎加入篮球社', '学生活动中心二楼105室', 11, 0,
        'http://localhost:5173/src/assets/uploadImg/736ee23b-a2ab-4e34-9dcb-302b562650a3.jpg');
INSERT INTO `clubs`
VALUES (17, 6, '网球社', '欢迎加入校园网球社', '学生活动中心二楼106室', 11, 0,
        'http://localhost:5173/src/assets/uploadImg/74a98b25-e9f4-41d8-99fd-e06e3fcb5658.jpg');

-- ----------------------------
-- Table structure for courts
-- ----------------------------
DROP TABLE IF EXISTS `courts`;
CREATE TABLE `courts`
(
    `court_id`     int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '球场ID',
    `category_id`  int(11) UNSIGNED                                              NOT NULL COMMENT '类别ID',
    `location`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '位置',
    `court_number` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '场地号',
    `cover_img`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`court_id`) USING BTREE,
    INDEX `fk_court_category` (`category_id` ASC) USING BTREE,
    CONSTRAINT `fk_court_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 43
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '球场表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courts
-- ----------------------------
INSERT INTO `courts`
VALUES (15, 2, '室外', '一号足球场', 'http://localhost:5173/src/assets/uploadImg/00ed963a-ea1d-482e-bba9-140a6546eedc.jpg');
INSERT INTO `courts`
VALUES (16, 2, '室外', '二号足球场', 'http://localhost:5173/src/assets/uploadImg/01810742-d186-415a-9852-36ffeff6f501.jpg');
INSERT INTO `courts`
VALUES (17, 3, '室内', '一号排球场', 'http://localhost:5173/src/assets/uploadImg/3c1b33a7-aa6a-4b31-b5ad-3b35cb533a3f.jpg');
INSERT INTO `courts`
VALUES (18, 3, '室内', '二号排球场', 'http://localhost:5173/src/assets/uploadImg/866ce10b-8212-4708-8b24-a53c0ebbfb31.jpg');
INSERT INTO `courts`
VALUES (19, 3, '室外', '三号排球场', 'http://localhost:5173/src/assets/uploadImg/01aa6aba-111a-4116-8d49-8c39236f0898.jpg');
INSERT INTO `courts`
VALUES (20, 4, '室内', '一号乒乓球桌', 'http://localhost:5173/src/assets/uploadImg/c18abe4d-f2ee-4264-b642-cb81fb818b8f.jpg');
INSERT INTO `courts`
VALUES (21, 4, '室内', '二号乒乓球桌', 'http://localhost:5173/src/assets/uploadImg/e2b048b6-ed29-4eca-9c0b-86292543a7cb.jpg');
INSERT INTO `courts`
VALUES (24, 4, '室内', '三号乒乓球桌', 'http://localhost:5173/src/assets/uploadImg/545207a7-c3b2-4e50-94de-f23bdcaa4678.jpg');
INSERT INTO `courts`
VALUES (25, 4, '室内', '四号乒乓球桌', 'http://localhost:5173/src/assets/uploadImg/9d9f016b-65a8-468c-b692-0757dd1be744.jpg');
INSERT INTO `courts`
VALUES (26, 4, '室内', '五号乒乓球桌', 'http://localhost:5173/src/assets/uploadImg/aa6ec176-dd97-4331-b37c-89cb850e96be.jpg');
INSERT INTO `courts`
VALUES (27, 5, '室内', '一号羽毛球场', 'http://localhost:5173/src/assets/uploadImg/b7863460-5c74-40b0-9ed7-b385d355c1cd.jpg');
INSERT INTO `courts`
VALUES (28, 5, '室内', '二号羽毛球场', 'http://localhost:5173/src/assets/uploadImg/f8d0f13a-915e-4529-8fd5-bbd555048b63.jpg');
INSERT INTO `courts`
VALUES (29, 5, '室内', '三号羽毛球场', 'http://localhost:5173/src/assets/uploadImg/168a2cf4-2312-4acc-a921-d6d8ce9e2308.jpg');
INSERT INTO `courts`
VALUES (30, 5, '室内', '四号羽毛球场', 'http://localhost:5173/src/assets/uploadImg/b616ea72-ad78-4c38-b72f-dc17724f8f62.jpg');
INSERT INTO `courts`
VALUES (31, 5, '室内', '五号羽毛球场', 'http://localhost:5173/src/assets/uploadImg/24e6afae-75af-4b23-b550-72bf0ff37479.jpg');
INSERT INTO `courts`
VALUES (32, 6, '室外', '一号网球场', 'http://localhost:5173/src/assets/uploadImg/ad3ad4e0-e11b-4f78-b62f-fb7b2115ae81.jpg');
INSERT INTO `courts`
VALUES (33, 6, '室外', '二号网球场', 'http://localhost:5173/src/assets/uploadImg/1965bd98-9508-40f9-8f8f-d5565e8bb6e1.jpg');
INSERT INTO `courts`
VALUES (34, 6, '室外', '三号网球场', 'http://localhost:5173/src/assets/uploadImg/29f0f845-2699-4433-8750-5dfa9755aa60.jpg');
INSERT INTO `courts`
VALUES (35, 1, '室外', '一号篮球场', 'http://localhost:5173/src/assets/uploadImg/7f7806a1-141b-4a0c-86c6-e3b303c82ea6.jpg');
INSERT INTO `courts`
VALUES (36, 1, '室外', '二号篮球场', 'http://localhost:5173/src/assets/uploadImg/aaf9a7b3-ca69-4ee2-9ea4-c489f68dafe4.jpg');
INSERT INTO `courts`
VALUES (37, 1, '室外', '三号篮球场', 'http://localhost:5173/src/assets/uploadImg/966201f1-beaf-4c00-b716-897e70b5d843.jpg');
INSERT INTO `courts`
VALUES (38, 1, '室外', '四号篮球场', 'http://localhost:5173/src/assets/uploadImg/558ff616-b7df-4121-90d9-19704d7f9230.jpg');
INSERT INTO `courts`
VALUES (39, 1, '室外', '五号篮球场', 'http://localhost:5173/src/assets/uploadImg/1507a98a-6c49-475b-87d5-8f16b01e4e9d.jpg');
INSERT INTO `courts`
VALUES (40, 1, '室外', '六号篮球场', 'http://localhost:5173/src/assets/uploadImg/34ee5662-99a6-432f-be39-a06bba62a53c.jpg');
INSERT INTO `courts`
VALUES (41, 2, '室外', '一号足球场', 'http://localhost:5173/src/assets/uploadImg/6ce49544-38e0-4151-8f24-ee207b915d3d.jpg');
INSERT INTO `courts`
VALUES (42, 2, '室外', '二号足球场', 'http://localhost:5173/src/assets/uploadImg/4427deea-bbf7-4784-8e32-0cf3ed982c22.jpg');

-- ----------------------------
-- Table structure for equipment
-- ----------------------------
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment`
(
    `equipment_id`    int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '器材ID',
    `name`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '器材名称',
    `equipment_count` int(11)                                                       NOT NULL COMMENT '器材数量',
    `location`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '存放位置',
    `cover_img`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '器材图片',
    PRIMARY KEY (`equipment_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '器材表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of equipment
-- ----------------------------
INSERT INTO `equipment`
VALUES (1, '哑铃', 10, '健身房A区', 'http://localhost:5173/src/assets/uploadImg/b5696bdc-d1ba-4635-945b-56f26e36b115.jpg');
INSERT INTO `equipment`
VALUES (3, '乒乓球', 10, '器材室', 'http://localhost:5173/src/assets/uploadImg/b4c837a9-5c50-4620-9831-f3d608672900.jpg');
INSERT INTO `equipment`
VALUES (6, '乒乓球拍', 10, '器材室', 'http://localhost:5173/src/assets/uploadImg/07268782-cc14-4e63-a549-8671718666f4.jpg');

-- ----------------------------
-- Table structure for equipmentborrowings
-- ----------------------------
DROP TABLE IF EXISTS `equipmentborrowings`;
CREATE TABLE `equipmentborrowings`
(
    `borrowing_id`    int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '借用ID',
    `equipment_id`    int(11) UNSIGNED NOT NULL COMMENT '器材ID',
    `user_id`         int(11) UNSIGNED NOT NULL COMMENT '用户ID',
    `borrow_time`     datetime         NOT NULL COMMENT '借用时间',
    `return_time`     datetime         NOT NULL COMMENT '归还时间',
    `borrow_status`   int(11)          NOT NULL DEFAULT 0 COMMENT '借用状态 \r\n0 申请中\r\n1 申请通过，借用中\r\n2 借用已归还\r\n3 逾期未归还',
    `borrow_quantity` int(11)          NOT NULL COMMENT '借用数量',
    PRIMARY KEY (`borrowing_id`) USING BTREE,
    INDEX `fk_borrowing_equipment` (`equipment_id` ASC) USING BTREE,
    INDEX `fk_borrowing_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_borrow_time` (`borrow_time` ASC) USING BTREE,
    INDEX `idx_return_time` (`return_time` ASC) USING BTREE,
    CONSTRAINT `fk_borrowing_equipment` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_borrowing_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '器材借用表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of equipmentborrowings
-- ----------------------------
INSERT INTO `equipmentborrowings`
VALUES (2, 3, 11, '2024-11-09 16:00:00', '2024-11-11 16:00:00', 2, 1);

-- ----------------------------
-- Table structure for registrations
-- ----------------------------
DROP TABLE IF EXISTS `registrations`;
CREATE TABLE `registrations`
(
    `registration_id`      int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '报名ID',
    `user_id`              int(11) UNSIGNED NOT NULL COMMENT '用户ID',
    `activity_id`          int(11) UNSIGNED NOT NULL COMMENT '活动ID',
    `registrations_status` int(11)          NULL DEFAULT 0 COMMENT '报名状态',
    PRIMARY KEY (`registration_id`) USING BTREE,
    INDEX `fk_registration_user` (`user_id` ASC) USING BTREE,
    INDEX `fk_registration_activity` (`activity_id` ASC) USING BTREE,
    CONSTRAINT `fk_registration_activity` FOREIGN KEY (`activity_id`) REFERENCES `activities` (`activity_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_registration_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '报名表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registrations
-- ----------------------------
INSERT INTO `registrations`
VALUES (8, 11, 4, 0);

-- ----------------------------
-- Table structure for time_slots
-- ----------------------------
DROP TABLE IF EXISTS `time_slots`;
CREATE TABLE `time_slots`
(
    `time_slot_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `time`         time             NOT NULL,
    `status`       int(11)          NOT NULL DEFAULT 0 COMMENT '0未预约 1已预约 2已过期  ',
    PRIMARY KEY (`time_slot_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 25
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of time_slots
-- ----------------------------
INSERT INTO `time_slots`
VALUES (1, '09:00:00', 0);
INSERT INTO `time_slots`
VALUES (2, '09:30:00', 0);
INSERT INTO `time_slots`
VALUES (3, '10:00:00', 0);
INSERT INTO `time_slots`
VALUES (4, '10:30:00', 0);
INSERT INTO `time_slots`
VALUES (5, '11:00:00', 0);
INSERT INTO `time_slots`
VALUES (6, '11:30:00', 0);
INSERT INTO `time_slots`
VALUES (7, '12:00:00', 0);
INSERT INTO `time_slots`
VALUES (8, '14:00:00', 0);
INSERT INTO `time_slots`
VALUES (9, '14:30:00', 0);
INSERT INTO `time_slots`
VALUES (10, '15:00:00', 0);
INSERT INTO `time_slots`
VALUES (11, '15:30:00', 0);
INSERT INTO `time_slots`
VALUES (12, '16:00:00', 0);
INSERT INTO `time_slots`
VALUES (13, '16:30:00', 0);
INSERT INTO `time_slots`
VALUES (14, '17:00:00', 0);
INSERT INTO `time_slots`
VALUES (15, '17:30:00', 0);
INSERT INTO `time_slots`
VALUES (16, '18:00:00', 0);
INSERT INTO `time_slots`
VALUES (17, '18:30:00', 0);
INSERT INTO `time_slots`
VALUES (18, '19:00:00', 0);
INSERT INTO `time_slots`
VALUES (19, '19:30:00', 0);
INSERT INTO `time_slots`
VALUES (20, '20:00:00', 0);
INSERT INTO `time_slots`
VALUES (21, '20:30:00', 0);
INSERT INTO `time_slots`
VALUES (22, '21:00:00', 0);
INSERT INTO `time_slots`
VALUES (23, '21:30:00', 0);
INSERT INTO `time_slots`
VALUES (24, '22:00:00', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '用户名',
    `phone`       varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '电话',
    `password`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '密码',
    `nickname`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT '' COMMENT '昵称',
    `email`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT '' COMMENT '邮箱',
    `user_pic`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT '' COMMENT '头像',
    `create_time` datetime                                                      NOT NULL COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL COMMENT '修改时间',
    `role`        tinyint(1)                                                    NOT NULL DEFAULT 0 COMMENT '角色：0-普通用户，1-管理员',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username` (`username` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (11, 'haohao', '15360789397', '113035048d5e87ee1c0a10142a545544', '测试121', '22354245@qq.com',
        'http://localhost:5173/src/assets/uploadImg/ef27e138-e049-44ce-a160-6cc5a00cb075.jpg', '2024-10-18 23:15:15',
        '2024-11-16 17:42:44', 0);
INSERT INTO `user`
VALUES (12, 'admin', '13432902167', '21232f297a57a5a743894a0e4a801fc3', '浩浩', '124358985@qq.com',
        'http://localhost:5173/src/assets/uploadImg/64c0ffca-f228-40cd-a743-efd8d65b0cea.jpg', '2024-10-21 12:35:46',
        '2024-11-16 15:47:08', 1);
INSERT INTO `user`
VALUES (14, 'qqqqqq', '13488869677', '343b1c4a3ea721b2d640fc8700db0f36', '', '', '', '2024-11-12 16:12:39',
        '2024-11-12 16:12:39', 0);
INSERT INTO `user`
VALUES (15, 'aaaaaa', '15360789391', '0b4e7a0e5fe84ad35fb5f95b9ceeac79', '', '', '', '2024-11-18 21:53:35',
        '2024-11-18 21:54:01', 0);

-- ----------------------------
-- Table structure for userclubs
-- ----------------------------
DROP TABLE IF EXISTS `userclubs`;
CREATE TABLE `userclubs`
(
    `user_club_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `user_id`      int(11) UNSIGNED NOT NULL COMMENT '用户ID',
    `club_id`      int(11) UNSIGNED NOT NULL COMMENT '俱乐部ID',
    PRIMARY KEY (`user_club_id`) USING BTREE,
    UNIQUE INDEX `user_id` (`user_id` ASC, `club_id` ASC) USING BTREE,
    INDEX `fk_userclub_club` (`club_id` ASC) USING BTREE,
    CONSTRAINT `fk_userclub_club` FOREIGN KEY (`club_id`) REFERENCES `clubs` (`club_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_userclub_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 23
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户俱乐部关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userclubs
-- ----------------------------
INSERT INTO `userclubs`
VALUES (22, 11, 1);
INSERT INTO `userclubs`
VALUES (4, 14, 1);
INSERT INTO `userclubs`
VALUES (6, 14, 13);

SET FOREIGN_KEY_CHECKS = 1;
