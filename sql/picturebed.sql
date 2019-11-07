/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : picturebed

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 07/11/2019 21:25:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for code
-- ----------------------------
DROP TABLE IF EXISTS `code`;
CREATE TABLE `code`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `value` int(20) NOT NULL,
  `code` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `sourcekey` int(4) NULL DEFAULT NULL,
  `emails` int(4) NULL DEFAULT NULL COMMENT '邮箱配置',
  `webname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站名',
  `explain` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '首页左下角说明',
  `video` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频地址',
  `backtype` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '背景类型',
  `links` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备案号',
  `notice` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告',
  `baidu` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '百度统计',
  `domain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站点域名',
  `background1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '首页背景图',
  `background2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传页面背景图',
  `sett` int(2) NOT NULL COMMENT '首页样式',
  `webms` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `webkeywords` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `webfavicons` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 7, 1, 'Hellohao', '网站由JAVA语言编写应用SpringBoot框架开发，前端全部组件由BootStrap/Layui框架编写。由作者个人更新维护，后期会加入更全面的功能供大家使用，如有BUG请与我反馈。', 'https://hellohao-cloud.oss-cn-beijing.aliyuncs.com/Pexels.mp4', '1', '<a href=\"http://hellohao.cn/\" rel=\"nofollow\" target=\"_blank\">Hellohao开发制作</a>', '也许...|这将是最好用的图床', 'console.log(\'百度统计JS代码\');', 'http://127.0.0.1:8088', 'https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1565769264&di=74d809d6cfae81bbab83bf9d573d8f9a&src=http://pic17.nipic.com/20110917/7420038_160826355111_2.jpg', 'https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1565769264&di=74d809d6cfae81bbab83bf9d573d8f9a&src=http://pic17.nipic.com/20110917/7420038_160826355111_2.jpg', 1, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for emailconfig
-- ----------------------------
DROP TABLE IF EXISTS `emailconfig`;
CREATE TABLE `emailconfig`  (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `emails` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `emailkey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权码',
  `emailurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务器',
  `port` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '端口',
  `emailname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `using` int(4) NULL DEFAULT NULL COMMENT '1为可用，其他为不使用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of emailconfig
-- ----------------------------
INSERT INTO `emailconfig` VALUES (1, '', '', '', '', '', 0);

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `groupname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组名称',
  `keyid` int(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of group
-- ----------------------------
INSERT INTO `group` VALUES (1, '默认群组', 5);

-- ----------------------------
-- Table structure for imgdata
-- ----------------------------
DROP TABLE IF EXISTS `imgdata`;
CREATE TABLE `imgdata`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `imgname` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名',
  `imgurl` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片链接',
  `userid` int(10) NULL DEFAULT NULL COMMENT '用户名',
  `updatetime` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传时间',
  `sizes` int(255) NULL DEFAULT NULL COMMENT '文件大小',
  `abnormal` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source` int(2) NULL DEFAULT NULL COMMENT '存储源',
  `imgtype` int(2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for imgreview
-- ----------------------------
DROP TABLE IF EXISTS `imgreview`;
CREATE TABLE `imgreview`  (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `api_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `secret_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Using` int(4) NULL DEFAULT NULL,
  `count` int(255) NULL DEFAULT NULL COMMENT '拦截数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of imgreview
-- ----------------------------
INSERT INTO `imgreview` VALUES (1, NULL, NULL, NULL, 0, 0);

-- ----------------------------
-- Table structure for keys
-- ----------------------------
DROP TABLE IF EXISTS `keys`;
CREATE TABLE `keys`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `AccessKey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `AccessSecret` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Endpoint` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Bucketname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RequestAddress` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `storageType` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of keys
-- ----------------------------
INSERT INTO `keys` VALUES (1, '', '', '', '', '', 1);
INSERT INTO `keys` VALUES (2, '', '', '', '', '', 2);
INSERT INTO `keys` VALUES (3, '', '', '0', '', '', 3);
INSERT INTO `keys` VALUES (4, '', '', '', '', '', 4);
INSERT INTO `keys` VALUES (5, '0', '0', '0', '0', '0', 5);
INSERT INTO `keys` VALUES (6, '', '', '', '', '', 6);
INSERT INTO `keys` VALUES (7, '', '', '', '', '', 7);
INSERT INTO `keys` VALUES (8, '', '', '', '', '', 8);
INSERT INTO `keys` VALUES (9, '', '', '', '', '', 9);
INSERT INTO `keys` VALUES (10, '0', '', '0', '0', '0', 10);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int(4) NOT NULL,
  `text` varchar(10000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sysconfig
-- ----------------------------
DROP TABLE IF EXISTS `sysconfig`;
CREATE TABLE `sysconfig`  (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `register` int(2) NOT NULL COMMENT '是否可以注册',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sysconfig
-- ----------------------------
INSERT INTO `sysconfig` VALUES (1, 1);

-- ----------------------------
-- Table structure for uploadconfig
-- ----------------------------
DROP TABLE IF EXISTS `uploadconfig`;
CREATE TABLE `uploadconfig`  (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `filesizetourists` int(10) NULL DEFAULT NULL COMMENT '游客上传文件最大',
  `filesizeuser` int(10) NULL DEFAULT NULL COMMENT '用户上传文件最大',
  `imgcounttourists` int(10) NULL DEFAULT NULL COMMENT '游客文件总数量, 超出则不允许加入队列',
  `imgcountuser` int(10) NULL DEFAULT NULL COMMENT '用户文件总数量, 超出则不允许加入队列',
  `suffix` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支持后缀',
  `urltype` int(2) NULL DEFAULT NULL COMMENT 'url类型',
  `isupdate` int(2) NULL DEFAULT NULL COMMENT '禁止游客上传',
  `api` int(2) NOT NULL COMMENT '开启api',
  `visitormemory` int(10) NULL DEFAULT NULL COMMENT '游客限制大小',
  `usermemory` int(10) NULL DEFAULT 0 COMMENT '用户默认大小',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of uploadconfig
-- ----------------------------
INSERT INTO `uploadconfig` VALUES (1, 3, 5, 1, 5, 'gif,jpg,jpeg,bmp,png', 1, 1, 1, 500, 1024);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `birthder` date NULL DEFAULT NULL COMMENT '注册时间',
  `level` int(10) NULL DEFAULT NULL COMMENT '等级',
  `uid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户唯一标识',
  `isok` int(2) NOT NULL,
  `memory` int(10) NULL DEFAULT NULL COMMENT '用户内存大小',
  `groupid` int(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'YWRtaW4=', 'admin', '2019-06-12', 2, 'admin', 1, 1024, 1);

-- ----------------------------
-- Table structure for usergroup
-- ----------------------------
DROP TABLE IF EXISTS `usergroup`;
CREATE TABLE `usergroup`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `userid` int(255) NULL DEFAULT NULL,
  `groupid` int(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of usergroup
-- ----------------------------
INSERT INTO `usergroup` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
