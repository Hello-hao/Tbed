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

 Date: 26/07/2019 17:38:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `sourcekey` int(4) NULL DEFAULT NULL,
  `emails` int(4) NULL DEFAULT NULL,
  `webname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站名',
  `explain` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '首页左下角说明',
  `logos` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '左下角的图片地址',
  `footed` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页脚版权',
  `links` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '友链',
  `notice` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告',
  `baidu` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '百度统计',
  `domain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站点域名',
  `background1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '首页背景图',
  `background2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传页面背景图',
  `sett` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 5, 1, 'Hellohao', '网站由JAVA语言编写应用SpringBoot框架开发，前端全部组件由BootStrap/Layui框架编写。由作者个人更新维护，后期会加入更全面的功能供大家使用，如有BUG请与我反馈。', 'https://hellohao.nos-eastchina1.126.net/%E7%BD%91%E7%AB%99%E7%B4%A0%E6%9D%90/logo2.png', 'Hellohao  切勿上传违反中华人民共和国互联网法律条约资源', '<li><a href=\"http://www.hellohao.cn/\" rel=\"nofollow\" target=\"_blank\">作者博客</a></li><li><a href=\"http://bz.hellohao.cn/\" rel=\"nofollow\" target=\"_blank\">高清壁纸</a></li><li><a href=\"http://json.hellohao.cn/\" rel=\"nofollow\" target=\"_blank\">json格式化</a></li>', '也许...|这将是最好用的图床', 'console.log(\'百度统计JS代码\');', 'http://tc.hellohao.cn', 'https://hellohao.oss-cn-beijing.aliyuncs.com/Hellohao/eb83a0714030248.jpg', 'https://hellohao.oss-cn-beijing.aliyuncs.com/Hellohao/086650714030248.jpg', NULL);

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
INSERT INTO `emailconfig` VALUES (1, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for imgdata
-- ----------------------------
DROP TABLE IF EXISTS `imgdata`;
CREATE TABLE `imgdata`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `imgname` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名',
  `imgurl` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片链接',
  `userid` int(10) NULL DEFAULT NULL COMMENT '用户名',
  `updatetime` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传时间',
  `sizes` int(255) NULL DEFAULT NULL COMMENT '文件大小',
  `abnormal` int(2) NULL DEFAULT NULL COMMENT '异常',
  `source` int(2) NULL DEFAULT NULL COMMENT '存储源',
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
  `count` int(10) NULL DEFAULT NULL COMMENT '拦截数量',
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
INSERT INTO `keys` VALUES (2, '11111111', '222222222222', '444444444444', '555555555', 'http://1.1.1', 2);
INSERT INTO `keys` VALUES (3, '123', '123', '0', '123', '123', 3);
INSERT INTO `keys` VALUES (4, '123', '123', '4', '412', 'https://0.0', 4);
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
  `urltype` int(2) NULL DEFAULT NULL,
  `isupdate` int(2) NULL DEFAULT NULL COMMENT '禁止游客上传',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of uploadconfig
-- ----------------------------
INSERT INTO `uploadconfig` VALUES (1, 3, 5, 1, 5, 'gif,jpg,jpeg,bmp,png', 1, 1);

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', 'admin', '2019-06-12', 2, 'admin', 1);

SET FOREIGN_KEY_CHECKS = 1;
