-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: 2019-08-25 16:13:11
-- 服务器版本： 5.5.62-log
-- PHP Version: 7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `picturebedpro`
--

-- --------------------------------------------------------

--
-- 表的结构 `code`
--

CREATE TABLE `code` (
  `id` int(255) NOT NULL,
  `value` int(20) NOT NULL,
  `code` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `config`
--

CREATE TABLE `config` (
  `id` int(4) NOT NULL,
  `sourcekey` int(4) DEFAULT NULL,
  `emails` int(4) DEFAULT NULL,
  `webname` varchar(255) DEFAULT NULL COMMENT '网站名',
  `explain` varchar(500) DEFAULT NULL COMMENT '首页左下角说明',
  `video` varchar(500) DEFAULT NULL COMMENT '视频地址',
  `backtype` varchar(500) DEFAULT NULL COMMENT '背景类型',
  `links` varchar(255) DEFAULT NULL COMMENT '友链',
  `notice` varchar(500) DEFAULT NULL COMMENT '公告',
  `baidu` varchar(500) DEFAULT NULL COMMENT '百度统计',
  `domain` varchar(255) DEFAULT NULL COMMENT '站点域名',
  `background1` varchar(500) DEFAULT NULL COMMENT '首页背景图',
  `background2` varchar(500) DEFAULT NULL COMMENT '上传页面背景图',
  `sett` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `config`
--

INSERT INTO `config` (`id`, `sourcekey`, `emails`, `webname`, `explain`, `video`, `backtype`, `links`, `notice`, `baidu`, `domain`, `background1`, `background2`, `sett`) VALUES
(1, 5, 1, 'Hellohao', '网站由JAVA语言编写应用SpringBoot框架开发，前端全部组件由BootStrap/Layui框架编写。由作者个人更新维护，后期会加入更全面的功能供大家使用，如有BUG请与我反馈。', 'https://hellohao-cloud.oss-cn-beijing.aliyuncs.com/Pexels.mp4', '2', '<li><a href=\"http://www.hellohao.cn/\" rel=\"nofollow\" target=\"_blank\">作者博客</a></li><li><a href=\"http://bz.hellohao.cn/\" rel=\"nofollow\" target=\"_blank\">高清壁纸</a></li><li><a href=\"http://json.hellohao.cn/\" rel=\"nofollow\" target=\"_blank\">json格式化</a></li>', '也许...|这将是最好用的图床', 'console.log(\'百度统计JS代码\');', 'http://tc.hellohao.cn', 'https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1565769264&di=74d809d6cfae81bbab83bf9d573d8f9a&src=http://pic17.nipic.com/20110917/7420038_160826355111_2.jpg', 'https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1565769264&di=74d809d6cfae81bbab83bf9d573d8f9a&src=http://pic17.nipic.com/20110917/7420038_160826355111_2.jpg', 1);

-- --------------------------------------------------------

--
-- 表的结构 `emailconfig`
--

CREATE TABLE `emailconfig` (
  `id` int(2) NOT NULL,
  `emails` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `emailkey` varchar(255) DEFAULT NULL COMMENT '授权码',
  `emailurl` varchar(255) DEFAULT NULL COMMENT '服务器',
  `port` varchar(255) DEFAULT NULL COMMENT '端口',
  `emailname` varchar(255) DEFAULT NULL COMMENT '用户名',
  `using` int(4) DEFAULT NULL COMMENT '1为可用，其他为不使用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `emailconfig`
--

INSERT INTO `emailconfig` (`id`, `emails`, `emailkey`, `emailurl`, `port`, `emailname`, `using`) VALUES
(1, '', '', '', '', '', 0);

-- --------------------------------------------------------

--
-- 表的结构 `group`
--

CREATE TABLE `group` (
  `id` int(255) NOT NULL,
  `groupname` varchar(255) NOT NULL COMMENT '组名称',
  `keyid` int(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `group`
--

INSERT INTO `group` (`id`, `groupname`, `keyid`) VALUES
(1, '默认群组', 5);

-- --------------------------------------------------------

--
-- 表的结构 `imgdata`
--

CREATE TABLE `imgdata` (
  `id` int(10) NOT NULL COMMENT '主键',
  `imgname` varchar(500) DEFAULT NULL COMMENT '图片名',
  `imgurl` varchar(500) DEFAULT NULL COMMENT '图片链接',
  `userid` int(10) DEFAULT NULL COMMENT '用户名',
  `updatetime` varchar(50) DEFAULT NULL COMMENT '上传时间',
  `sizes` int(255) DEFAULT NULL COMMENT '文件大小',
  `abnormal` int(2) DEFAULT NULL COMMENT '异常',
  `source` int(2) DEFAULT NULL COMMENT '存储源'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `imgdata`
--

INSERT INTO `imgdata` (`id`, `imgname`, `imgurl`, `userid`, `updatetime`, `sizes`, `abnormal`, `source`) VALUES
(28, 'admin/4cc700823104929.png', 'https://hellohao.nos-eastchina1.126.net/admin/4cc700823104929.png', 1, '2019-08-23', 263, NULL, 1),
(29, 'tourist/9c6560823105311.png', 'https://hellohao.nos-eastchina1.126.net/tourist/9c6560823105311.png', 0, '2019-08-23', 273, NULL, 1);

-- --------------------------------------------------------

--
-- 表的结构 `imgreview`
--

CREATE TABLE `imgreview` (
  `id` int(4) NOT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `api_key` varchar(255) DEFAULT NULL,
  `secret_key` varchar(255) DEFAULT NULL,
  `Using` int(4) DEFAULT NULL,
  `count` int(10) DEFAULT NULL COMMENT '拦截数量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `imgreview`
--

INSERT INTO `imgreview` (`id`, `app_id`, `api_key`, `secret_key`, `Using`, `count`) VALUES
(1, NULL, NULL, NULL, 0, 0);

-- --------------------------------------------------------

--
-- 表的结构 `keys`
--

CREATE TABLE `keys` (
  `id` int(11) NOT NULL,
  `AccessKey` varchar(255) DEFAULT NULL,
  `AccessSecret` varchar(255) DEFAULT NULL,
  `Endpoint` varchar(255) DEFAULT NULL,
  `Bucketname` varchar(255) DEFAULT NULL,
  `RequestAddress` varchar(255) DEFAULT NULL,
  `storageType` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `keys`
--

INSERT INTO `keys` (`id`, `AccessKey`, `AccessSecret`, `Endpoint`, `Bucketname`, `RequestAddress`, `storageType`) VALUES
(1, '', '', '', '', 'https://hellohao.nos-eastchina1.126.net', 1),
(2, '', '', '', '', '', 2),
(3, '', '', '', '', '', 3),
(4, '', '', '', '', '', 4),
(5, '0', '0', '0', '0', '0', 5),
(6, '', '', '', '', '', 6),
(7, '', '', '', '', '', 7),
(8, '', '', '', '', '', 8),
(9, '', '', '', '', '', 9),
(10, '0', '', '0', '0', '0', 10);

-- --------------------------------------------------------

--
-- 表的结构 `notice`
--

CREATE TABLE `notice` (
  `id` int(4) NOT NULL,
  `text` varchar(10000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- 表的结构 `sysconfig`
--

CREATE TABLE `sysconfig` (
  `id` int(2) NOT NULL,
  `register` int(2) NOT NULL COMMENT '是否可以注册'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `sysconfig`
--

INSERT INTO `sysconfig` (`id`, `register`) VALUES
(1, 1);

-- --------------------------------------------------------

--
-- 表的结构 `uploadconfig`
--

CREATE TABLE `uploadconfig` (
  `id` int(2) NOT NULL,
  `filesizetourists` int(10) DEFAULT NULL COMMENT '游客上传文件最大',
  `filesizeuser` int(10) DEFAULT NULL COMMENT '用户上传文件最大',
  `imgcounttourists` int(10) DEFAULT NULL COMMENT '游客文件总数量, 超出则不允许加入队列',
  `imgcountuser` int(10) DEFAULT NULL COMMENT '用户文件总数量, 超出则不允许加入队列',
  `suffix` varchar(255) DEFAULT NULL COMMENT '支持后缀',
  `urltype` int(2) DEFAULT NULL,
  `isupdate` int(2) DEFAULT NULL COMMENT '禁止游客上传',
  `api` int(2) NOT NULL COMMENT '开启api',
  `visitormemory` int(10) DEFAULT NULL COMMENT '游客限制大小',
  `usermemory` int(10) DEFAULT '0' COMMENT '用户默认大小'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `uploadconfig`
--

INSERT INTO `uploadconfig` (`id`, `filesizetourists`, `filesizeuser`, `imgcounttourists`, `imgcountuser`, `suffix`, `urltype`, `isupdate`, `api`, `visitormemory`, `usermemory`) VALUES
(1, 3, 5, 1, 5, 'gif,jpg,jpeg,bmp,png', 1, 1, 0, 500, 1024);

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `id` int(10) NOT NULL COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `email` varchar(20) NOT NULL COMMENT '邮箱',
  `birthder` date DEFAULT NULL COMMENT '注册时间',
  `level` int(10) DEFAULT NULL COMMENT '等级',
  `uid` varchar(50) DEFAULT NULL COMMENT '用户唯一标识',
  `isok` int(2) NOT NULL,
  `memory` int(10) DEFAULT NULL COMMENT '用户内存大小',
  `groupid` int(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `email`, `birthder`, `level`, `uid`, `isok`, `memory`, `groupid`) VALUES
(1, 'admin', 'admin', 'admin', '2019-06-12', 2, 'admin', 1, 1024, 1);

-- --------------------------------------------------------

--
-- 表的结构 `usergroup`
--

CREATE TABLE `usergroup` (
  `id` int(255) NOT NULL,
  `userid` int(255) DEFAULT NULL,
  `groupid` int(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `usergroup`
--

INSERT INTO `usergroup` (`id`, `userid`, `groupid`) VALUES
(1, 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `code`
--
ALTER TABLE `code`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `config`
--
ALTER TABLE `config`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `emailconfig`
--
ALTER TABLE `emailconfig`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `imgdata`
--
ALTER TABLE `imgdata`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `imgreview`
--
ALTER TABLE `imgreview`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `keys`
--
ALTER TABLE `keys`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `sysconfig`
--
ALTER TABLE `sysconfig`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `uploadconfig`
--
ALTER TABLE `uploadconfig`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `usergroup`
--
ALTER TABLE `usergroup`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `code`
--
ALTER TABLE `code`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100;

--
-- 使用表AUTO_INCREMENT `config`
--
ALTER TABLE `config`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `emailconfig`
--
ALTER TABLE `emailconfig`
  MODIFY `id` int(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `group`
--
ALTER TABLE `group`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- 使用表AUTO_INCREMENT `imgdata`
--
ALTER TABLE `imgdata`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键', AUTO_INCREMENT=30;

--
-- 使用表AUTO_INCREMENT `imgreview`
--
ALTER TABLE `imgreview`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `keys`
--
ALTER TABLE `keys`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用表AUTO_INCREMENT `sysconfig`
--
ALTER TABLE `sysconfig`
  MODIFY `id` int(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `uploadconfig`
--
ALTER TABLE `uploadconfig`
  MODIFY `id` int(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键', AUTO_INCREMENT=14;

--
-- 使用表AUTO_INCREMENT `usergroup`
--
ALTER TABLE `usergroup`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
