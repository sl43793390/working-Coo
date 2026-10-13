/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : project

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 04/11/2023 20:51:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for institutions
-- ----------------------------
DROP TABLE IF EXISTS `institutions`;
CREATE TABLE `institutions`  (
  `ID_INSTITUTION` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '机构标识',
  `ADDR_URL` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `NAME_INSTITUTION` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机构名称',
  `SITE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机构官网',
  `HOLDER` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `TIMESTAMP` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `CD_EMAIL` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '官方邮件地址',
  `CD_PHONE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  PRIMARY KEY (`ID_INSTITUTION`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of institutions
-- ----------------------------
INSERT INTO `institutions` VALUES ('123', NULL, '测试机构', NULL, 'sl', '2022-12-03 11:41:03', NULL, NULL);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `ID_MENU` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单ID',
  `NAME_MENU` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `NAME_CLASS` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对应类名',
  `ID_PARENT` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父节点',
  `NBR_ORDER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序号',
  `CD_MENU_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `FLAG_LOWEST` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否最下层',
  `NAME_PERMISSION` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名',
  PRIMARY KEY (`ID_MENU`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('backEndMgmt', '后台管理', NULL, NULL, '900', 'systemConfig', NULL, NULL);
INSERT INTO `menu` VALUES ('mytask', '我的任务', 'task.MyTaskComponent', 'taskRoot', '205', 'task', 'Y', NULL);
INSERT INTO `menu` VALUES ('myTodo', '我的待办', NULL, NULL, '300', 'mytodo', NULL, NULL);
INSERT INTO `menu` VALUES ('projectAdd', '新增项目', 'project.ProjectCreateComponent', 'projectMgmt', '1011', 'project', 'Y', NULL);
INSERT INTO `menu` VALUES ('projectDoc', '项目文档', 'project.ProjectDocumentComponent', 'projectMgmt', '1011', 'project', 'Y', NULL);
INSERT INTO `menu` VALUES ('projectMgmt', '项目管理', NULL, NULL, '100', 'project', NULL, NULL);
INSERT INTO `menu` VALUES ('projectMgmtDetail', '我的项目', 'project.MyProject', 'projectMgmt', '101', 'project', 'Y', NULL);
INSERT INTO `menu` VALUES ('ProjectRecycleBin', '项目回收站', 'project.ProjectRecycleBin', 'backEndMgmt', '9002', 'systemConfig', 'Y', NULL);
INSERT INTO `menu` VALUES ('projectStatistics', '项目统计信息', 'project.ProjectStatiticsComponent', 'projectMgmt', '102', 'project', 'Y', NULL);
INSERT INTO `menu` VALUES ('roleMgmt', '角色管理', 'user.UserRoleManagementComponent', 'userRoot', '650', 'user', 'Y', NULL);
INSERT INTO `menu` VALUES ('schedule', '日程', 'schedule.ScheduleComponent', 'myTodo', '306', 'task', 'Y', NULL);
INSERT INTO `menu` VALUES ('scheduleList', '日程列表', 'schedule.ScheduleListComponent', 'myTodo', '307', 'task', 'Y', NULL);
INSERT INTO `menu` VALUES ('systemConfig', '系统配置', NULL, 'backEndMgmt', '9001', 'systemConfig', NULL, NULL);
INSERT INTO `menu` VALUES ('systemOtherSetting', '其它', NULL, 'backEndMgmt', '9005', 'systemConfig', NULL, NULL);
INSERT INTO `menu` VALUES ('taskAdd', '新增任务', 'task.TaskUpdateComponent', 'taskRoot', '2051', 'task', 'Y', NULL);
INSERT INTO `menu` VALUES ('taskAssign', '我指派给他人的任务', 'task.MyTaskComponent', 'taskRoot', '2052', 'task', 'Y', NULL);
INSERT INTO `menu` VALUES ('taskRoot', '任务管理', NULL, NULL, '200', 'task', NULL, NULL);
INSERT INTO `menu` VALUES ('taskStatistics', '任务统计分析', 'task.TaskStatisticsComponent', 'taskRoot', '2053', 'task', 'Y', NULL);
INSERT INTO `menu` VALUES ('userMgmt', '用户列表', 'user.UserManageMentComponent', 'userRoot', '601', 'user', 'Y', NULL);
INSERT INTO `menu` VALUES ('userRoot', '用户管理', NULL, NULL, '600', 'user', NULL, NULL);

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `PERMISSION_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限ID',
  `RESOURCE_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名',
  `ACTION` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名',
  `VERSION` decimal(10, 0) NULL DEFAULT NULL COMMENT '版本',
  `ID_PARENT` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父id',
  `NBR_LEVEL` tinyint(1) NULL DEFAULT NULL COMMENT '层级',
  `NBR_ORDER` int NOT NULL COMMENT '序号',
  `TIMESTAMP` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`PERMISSION_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO `permissions` VALUES ('100', 'projectMgmt', '项目', NULL, NULL, NULL, 100, '2022-12-09 15:08:19');
INSERT INTO `permissions` VALUES ('106', 'projectMgmtDetail', '我的项目', NULL, '100', NULL, 101, '2022-12-15 22:46:04');
INSERT INTO `permissions` VALUES ('1061', 'projectUpdate', '修改项目', NULL, '106', NULL, 103, '2022-12-12 21:33:08');
INSERT INTO `permissions` VALUES ('1062', 'projectAdd', '新增项目', NULL, '106', NULL, 104, '2022-12-12 21:33:42');
INSERT INTO `permissions` VALUES ('1063', 'projectDelete', '删除项目', NULL, '106', NULL, 105, '2022-12-12 21:34:05');
INSERT INTO `permissions` VALUES ('1064', 'projectDoc', '项目文档', NULL, '106', NULL, 105, '2023-06-08 16:23:18');
INSERT INTO `permissions` VALUES ('107', 'projectStatistics', '项目统计信息', NULL, '100', NULL, 102, '2022-12-15 22:46:04');
INSERT INTO `permissions` VALUES ('200', 'taskRoot', '任务', NULL, NULL, NULL, 200, '2022-12-09 15:08:43');
INSERT INTO `permissions` VALUES ('202', 'myTask', '我的任务', NULL, '200', NULL, 202, '2022-12-09 15:09:53');
INSERT INTO `permissions` VALUES ('203', 'taskAssign', '我指派给他人的任务', NULL, '200', NULL, 203, '2023-06-05 15:26:44');
INSERT INTO `permissions` VALUES ('205', 'taskAdd', '新增任务', NULL, '202', NULL, 205, '2022-12-12 21:36:05');
INSERT INTO `permissions` VALUES ('206', 'taskUpDdate', '修改任务', NULL, '202', NULL, 206, '2022-12-12 21:36:30');
INSERT INTO `permissions` VALUES ('207', 'taskDelete', '删除任务', NULL, '202', NULL, 207, '2022-12-12 21:36:52');
INSERT INTO `permissions` VALUES ('208', 'taskStatistics', '任务统计分析', NULL, '200', NULL, 208, '2023-06-05 15:27:26');
INSERT INTO `permissions` VALUES ('300', 'userRoot', '用户管理', NULL, NULL, NULL, 300, '2022-12-03 21:38:00');
INSERT INTO `permissions` VALUES ('301', 'userAdd', '新增用户', NULL, '305', NULL, 308, '2022-12-03 21:31:50');
INSERT INTO `permissions` VALUES ('302', 'deleteUser', '删除用户', NULL, '305', NULL, 307, '2022-12-03 21:32:18');
INSERT INTO `permissions` VALUES ('303', 'updateUser', '修改用户', NULL, '305', NULL, 306, '2022-12-03 21:37:36');
INSERT INTO `permissions` VALUES ('305', 'userMgmt', '用户列表', NULL, '300', NULL, 305, '2022-12-12 21:41:33');
INSERT INTO `permissions` VALUES ('350', 'roleMgmt', '角色管理', NULL, NULL, NULL, 350, '2022-12-12 21:44:03');
INSERT INTO `permissions` VALUES ('351', 'roleAdd', '新增角色', NULL, '350', NULL, 351, '2022-12-12 21:45:16');
INSERT INTO `permissions` VALUES ('352', 'roleUpdate', '修改角色', NULL, '350', NULL, 352, '2022-12-12 21:45:41');
INSERT INTO `permissions` VALUES ('353', 'roleDelete', '删除角色', NULL, '350', NULL, 353, '2022-12-12 21:46:03');
INSERT INTO `permissions` VALUES ('400', 'myTodo', '我的代办', NULL, NULL, NULL, 400, '2022-12-12 21:47:29');
INSERT INTO `permissions` VALUES ('401', 'schedule', '日程', NULL, '400', NULL, 401, '2022-12-16 09:09:54');
INSERT INTO `permissions` VALUES ('402', 'scheduleList', '日程列表', NULL, '400', NULL, 402, '2022-12-16 09:10:41');
INSERT INTO `permissions` VALUES ('800', 'checkedAllUser', '管理员', NULL, NULL, NULL, 800, '2022-12-03 21:30:43');
INSERT INTO `permissions` VALUES ('8001', 'checkedInstitutionUser', '机构管理员', NULL, NULL, NULL, 8001, '2022-12-03 21:31:09');
INSERT INTO `permissions` VALUES ('900', 'backEndMgmt', '后台管理', NULL, NULL, NULL, 900, '2023-06-05 15:29:00');
INSERT INTO `permissions` VALUES ('901', 'systemConfig', '系统配置', NULL, '900', NULL, 901, '2022-12-16 13:06:32');
INSERT INTO `permissions` VALUES ('902', 'systemOtherSetting', '其它', NULL, '900', NULL, 902, '2023-06-05 15:29:33');
INSERT INTO `permissions` VALUES ('903', 'ProjectRecycleBin', '项目回收站', NULL, '900', NULL, 903, '2023-06-15 13:15:56');

-- ----------------------------
-- Table structure for project_group
-- ----------------------------
DROP TABLE IF EXISTS `project_group`;
CREATE TABLE `project_group`  (
  `ID_PROJECT` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CD_GROUP` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`ID_PROJECT`, `CD_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_group
-- ----------------------------
INSERT INTO `project_group` VALUES ('1231qq', '分组3');
INSERT INTO `project_group` VALUES ('1231qq', '分组5');
INSERT INTO `project_group` VALUES ('1599776699207684096', '分组1');
INSERT INTO `project_group` VALUES ('1601498474278866944', 'test');
INSERT INTO `project_group` VALUES ('1662466543942070272', 'test1');

-- ----------------------------
-- Table structure for project_tag
-- ----------------------------
DROP TABLE IF EXISTS `project_tag`;
CREATE TABLE `project_tag`  (
  `ID_PROJECT` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CD_TAG` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`ID_PROJECT`, `CD_TAG`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_tag
-- ----------------------------
INSERT INTO `project_tag` VALUES ('1231qq', '标签3');
INSERT INTO `project_tag` VALUES ('1599776699207684096', '标签1');
INSERT INTO `project_tag` VALUES ('1601498474278866944', 'label2');
INSERT INTO `project_tag` VALUES ('1662462206171901952', 'demo标签1');
INSERT INTO `project_tag` VALUES ('1662466543942070272', 'demo标签2');
INSERT INTO `project_tag` VALUES ('1662466543942070272', 'sefe');

-- ----------------------------
-- Table structure for project_task_file
-- ----------------------------
DROP TABLE IF EXISTS `project_task_file`;
CREATE TABLE `project_task_file`  (
  `ID_FILE` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件ID',
  `ID_PROJECT` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '项目ID',
  `ID_TASK` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '任务ID',
  `NAME_FILE` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件名',
  `CD_FILE_PATH` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '路径',
  `DT_UPLOAD` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上传时间',
  `ID_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '上传用户',
  `CD_COMMENT` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件描述',
  PRIMARY KEY (`ID_FILE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_task_file
-- ----------------------------
INSERT INTO `project_task_file` VALUES ('123321221', '1231qq', NULL, 'connections.ncx', 'd:\\', '2023-06-17 17:53:01', 'admin', 'test');
INSERT INTO `project_task_file` VALUES ('1600485790112616448', '1231qq', NULL, 'institutions.sql', 'd:\\\\admin\\1231qq', '2023-06-17 17:53:04', 'admin', '2342');
INSERT INTO `project_task_file` VALUES ('1601742802863546368', '1231qq', '1601739693068189696', '177.pdf', 'd:\\\\admin\\1231qq\\1601739693068189696', '2023-06-17 17:53:07', 'admin', '23gdsfdfd');
INSERT INTO `project_task_file` VALUES ('1670008286914965504', '1231qq', NULL, 'GRADING_TABLE.xlsx', 'd:\\\\admin\\1231qq', '2023-06-27 08:53:55', 'admin', 'qwer');

-- ----------------------------
-- Table structure for project_type
-- ----------------------------
DROP TABLE IF EXISTS `project_type`;
CREATE TABLE `project_type`  (
  `ID_PROJECT` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CD_TYPE` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`ID_PROJECT`, `CD_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_type
-- ----------------------------
INSERT INTO `project_type` VALUES ('1231qq', '开发');
INSERT INTO `project_type` VALUES ('1231qq', '测试');
INSERT INTO `project_type` VALUES ('1231qq', '需求分析');
INSERT INTO `project_type` VALUES ('1599776699207684096', '开发');
INSERT INTO `project_type` VALUES ('1599776699207684096', '测试');
INSERT INTO `project_type` VALUES ('1599776699207684096', '类型1');
INSERT INTO `project_type` VALUES ('1599776699207684096', '需求分析');
INSERT INTO `project_type` VALUES ('1601498474278866944', 'type1');
INSERT INTO `project_type` VALUES ('1662466543942070272', 'test2');

-- ----------------------------
-- Table structure for project_user
-- ----------------------------
DROP TABLE IF EXISTS `project_user`;
CREATE TABLE `project_user`  (
  `ID_PROJECT` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `ID_USER` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DT_UPDATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID_PROJECT`, `ID_USER`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '保存用户最近使用的项目，' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_user
-- ----------------------------
INSERT INTO `project_user` VALUES ('1231qq', 'admin', '2023-07-25 10:03:28');
INSERT INTO `project_user` VALUES ('1231qq', 'asdasd', '2023-06-12 20:51:09');
INSERT INTO `project_user` VALUES ('1231qq', 'luo', '2023-06-12 20:51:09');
INSERT INTO `project_user` VALUES ('1231qq', 'qweqwe', '2023-06-12 20:51:09');
INSERT INTO `project_user` VALUES ('1601498474278866944', 'admin', '2023-06-26 21:15:47');
INSERT INTO `project_user` VALUES ('1601498474278866944', 'asdasd', '2023-06-26 21:15:47');
INSERT INTO `project_user` VALUES ('1601498474278866944', 'luo', '2023-06-26 21:15:47');
INSERT INTO `project_user` VALUES ('1601498474278866944', 'qweqwe', '2023-06-26 21:15:47');
INSERT INTO `project_user` VALUES ('1601498474278866944', 'sl', '2023-06-26 21:15:47');
INSERT INTO `project_user` VALUES ('1601498474278866944', 'wushuang', '2023-06-26 21:15:47');
INSERT INTO `project_user` VALUES ('1601498474278866944', 'yang', '2023-06-26 21:15:47');
INSERT INTO `project_user` VALUES ('1601498474278866944', 'zhangwuji', '2023-06-26 21:15:47');
INSERT INTO `project_user` VALUES ('1601502911093350400', 'admin', '2023-06-14 16:26:06');
INSERT INTO `project_user` VALUES ('1601502911093350400', 'asdasd', '2023-06-14 16:26:06');
INSERT INTO `project_user` VALUES ('1601502911093350400', 'qweqwe', '2023-06-14 16:26:06');
INSERT INTO `project_user` VALUES ('1601525875239051264', 'asdasd', '2022-12-10 18:34:58');
INSERT INTO `project_user` VALUES ('1601551033907609600', 'admin', '2022-12-10 20:15:04');
INSERT INTO `project_user` VALUES ('1601551144104558592', 'admin', '2022-12-10 20:15:23');
INSERT INTO `project_user` VALUES ('1601563507826417664', 'admin', '2022-12-10 21:04:31');
INSERT INTO `project_user` VALUES ('1601563783123755008', 'admin', '2022-12-10 21:05:36');
INSERT INTO `project_user` VALUES ('1601565730115452928', 'admin', '2022-12-10 21:13:20');
INSERT INTO `project_user` VALUES ('1601565730115452928', 'asdasd', '2022-12-10 21:13:20');
INSERT INTO `project_user` VALUES ('1601566105241419776', 'admin', '2022-12-10 21:14:50');
INSERT INTO `project_user` VALUES ('1601566105241419776', 'asdasd', '2022-12-10 21:14:50');
INSERT INTO `project_user` VALUES ('1601566105241419776', 'qweqwe', '2022-12-10 21:14:50');
INSERT INTO `project_user` VALUES ('1601566465154646016', 'admin', '2022-12-10 21:16:16');
INSERT INTO `project_user` VALUES ('1601566465154646016', 'asdasd', '2022-12-10 21:16:16');
INSERT INTO `project_user` VALUES ('1601566465154646016', 'qweqwe', '2022-12-10 21:16:16');
INSERT INTO `project_user` VALUES ('1601566539704205312', 'admin', '2022-12-10 21:18:07');
INSERT INTO `project_user` VALUES ('1601566539704205312', 'asdasd', '2022-12-10 21:18:08');
INSERT INTO `project_user` VALUES ('1601566539704205312', 'qweqwe', '2022-12-10 21:18:09');
INSERT INTO `project_user` VALUES ('1662462206171901952', 'sl', '2023-05-27 22:14:12');
INSERT INTO `project_user` VALUES ('1662462206171901952', 'yang', '2023-05-27 22:14:12');
INSERT INTO `project_user` VALUES ('1662462206171901952', 'zhangwuji', '2023-05-27 22:14:12');
INSERT INTO `project_user` VALUES ('1662465863701458944', 'asdasd', '2023-05-27 22:28:44');
INSERT INTO `project_user` VALUES ('1662465863701458944', 'luo', '2023-05-27 22:28:44');
INSERT INTO `project_user` VALUES ('1662465863701458944', 'qweqwe', '2023-05-27 22:28:44');
INSERT INTO `project_user` VALUES ('1662465863701458944', 'sl', '2023-05-27 22:28:44');
INSERT INTO `project_user` VALUES ('1662466543942070272', 'qweqwe', '2023-05-27 22:31:26');
INSERT INTO `project_user` VALUES ('1662466543942070272', 'sl', '2023-05-27 22:31:26');
INSERT INTO `project_user` VALUES ('1662466543942070272', 'yang', '2023-05-27 22:31:26');
INSERT INTO `project_user` VALUES ('1668902268147589120', 'admin', '2023-06-14 16:44:42');
INSERT INTO `project_user` VALUES ('1668902268147589120', 'asdasd', '2023-06-14 16:44:42');
INSERT INTO `project_user` VALUES ('1668902268147589120', 'qweqwe', '2023-06-14 16:44:42');
INSERT INTO `project_user` VALUES ('1673268887133089792', 'asdasd', '2023-06-26 17:56:05');
INSERT INTO `project_user` VALUES ('1673268887133089792', 'sl', '2023-06-26 17:56:05');
INSERT INTO `project_user` VALUES ('luio89', 'asdasd', '2023-06-15 20:32:12');
INSERT INTO `project_user` VALUES ('luio89', 'luo', '2023-06-15 20:32:12');
INSERT INTO `project_user` VALUES ('luio89', 'qweqwe', '2023-06-15 20:32:12');

-- ----------------------------
-- Table structure for projects
-- ----------------------------
DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects`  (
  `ID_PROJECT` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '项目',
  `ID_INSTITUTION` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '所属机构',
  `NAME_PROJECT` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '项目名称',
  `CD_STATUS` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态：未开始、进行中、暂停、已完成',
  `CD_PRIOR` varchar(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '优先级',
  `PCT_COMPLETE` decimal(5, 2) NULL DEFAULT NULL COMMENT '完成率0-1',
  `PCT_OVERDUE` decimal(5, 2) NULL DEFAULT NULL COMMENT '逾期率',
  `DT_START` date NULL DEFAULT NULL COMMENT '开始时间',
  `DT_END` date NULL DEFAULT NULL COMMENT '项目结束时间',
  `DT_CREATE` date NULL DEFAULT NULL COMMENT '创建时间',
  `CD_CREATE_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '创建人',
  `CD_COMMENT` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `FLAG_PRIVATE` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '可见范围',
  `FLAG_DELETE` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '删除标记，Y表示删除',
  PRIMARY KEY (`ID_PROJECT`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of projects
-- ----------------------------
INSERT INTO `projects` VALUES ('1231qq', '123', '项目16', '进行中', '低', 0.20, NULL, '2022-12-05', NULL, '2022-12-05', 'qwe', '<h2><strong>test</strong></h2>', NULL, 'N');
INSERT INTO `projects` VALUES ('1599773895253159938', '123', 'test', NULL, '低', NULL, NULL, NULL, NULL, '2022-12-05', 'admin', '', NULL, 'N');
INSERT INTO `projects` VALUES ('1599776210571268096', '123', '4r', NULL, '高', 0.00, NULL, NULL, NULL, '2022-12-05', 'admin', '', NULL, 'N');
INSERT INTO `projects` VALUES ('1601498474278866944', '123', '项目3', '', '低', 0.00, NULL, NULL, NULL, '2022-12-10', 'admin', '<p>asdf</p>', 'N', 'N');
INSERT INTO `projects` VALUES ('1601502911093350400', '123', '5t', NULL, '低', 1.00, NULL, NULL, NULL, '2022-12-10', 'admin', '<b>weasdfass</b><div><b style=\"background-color: yellow;\"><br></b></div><div><b style=\"background-color: yellow;\"><font size=\"4\">fgdsfdddsds</font></b></div>', NULL, 'N');
INSERT INTO `projects` VALUES ('1662462206171901952', '123', 'wer', NULL, '低', 0.00, NULL, NULL, NULL, '2023-05-27', 'sl', '[{\"insert\":\"asdfasssaa\\n\"}]', NULL, 'N');
INSERT INTO `projects` VALUES ('1662465863701458944', '123', 'erterterre', NULL, '低', NULL, NULL, '2023-06-01', NULL, '2023-05-27', 'sl', '<p>egehewterereee</p>', NULL, 'N');
INSERT INTO `projects` VALUES ('1662466543942070272', '123', 'sdsvx', NULL, '低', NULL, NULL, '2023-05-27', NULL, '2023-05-27', 'sl', '<p>sdsds</p>', NULL, 'N');
INSERT INTO `projects` VALUES ('1668902268147589120', '123', '5t-1686732282108', NULL, '低', 0.00, NULL, NULL, NULL, '2023-06-14', 'admin', NULL, NULL, 'Y');
INSERT INTO `projects` VALUES ('1673268887133089792', '123', 'test55555', NULL, '低', NULL, NULL, '2023-06-26', NULL, '2023-06-26', 'sl', '<p>test</p>', 'N', 'Y');
INSERT INTO `projects` VALUES ('345rgege', '123', '项目161', NULL, '低', 1.00, NULL, '2022-12-05', NULL, '2022-12-05', 'qwe', NULL, NULL, 'N');
INSERT INTO `projects` VALUES ('asdassa', '123', '项目12', NULL, '低', 1.00, NULL, '2022-12-05', NULL, '2022-12-05', 'qwe', NULL, NULL, 'N');
INSERT INTO `projects` VALUES ('ht767hjg', '123', '项目17', NULL, '低', 1.00, NULL, '2022-12-05', NULL, '2022-12-05', 'qwe', NULL, NULL, 'N');
INSERT INTO `projects` VALUES ('luio89', '123', '项目18', '', '低', 1.00, NULL, '2022-12-05', NULL, '2022-12-05', 'qwe', '', 'N', 'N');
INSERT INTO `projects` VALUES ('qweqwe', '123', '项目1', NULL, '低', 1.00, NULL, '2022-12-05', NULL, '2022-12-05', 'qwe', NULL, NULL, 'Y');
INSERT INTO `projects` VALUES ('zxcxxzz45', '123', '项目14', NULL, '低', 1.00, NULL, '2022-12-05', NULL, '2022-12-05', 'qwe', NULL, NULL, 'N');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `ROLE_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  `ID_INSTITUTION` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '机构ID',
  `ROLE_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `VERSION` tinyint NULL DEFAULT NULL COMMENT '版本',
  `ROLE_DESC` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `TIMESTAMP` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`ID_INSTITUTION`, `ROLE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('admin', '123', 'admin', NULL, 'admin', '2022-12-03 21:29:49');
INSERT INTO `roles` VALUES ('asd', '123', 'asd', NULL, '', NULL);
INSERT INTO `roles` VALUES ('dev', '123', '开发', NULL, '', NULL);
INSERT INTO `roles` VALUES ('ert', '123', 'ert', NULL, 'ert', NULL);
INSERT INTO `roles` VALUES ('sdf', '123', 'sdf', NULL, '', NULL);
INSERT INTO `roles` VALUES ('test', '123', 'test', NULL, 'test', NULL);
INSERT INTO `roles` VALUES ('test23', '123', 'test34', NULL, 'sdas', NULL);
INSERT INTO `roles` VALUES ('wert', '123', 'ert', NULL, 'ert', NULL);

-- ----------------------------
-- Table structure for roles_permissions
-- ----------------------------
DROP TABLE IF EXISTS `roles_permissions`;
CREATE TABLE `roles_permissions`  (
  `ROLE_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  `PERMISSION_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限ID',
  `VERSION` tinyint NULL DEFAULT NULL COMMENT '版本',
  `TIMESTAMP` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`ROLE_ID`, `PERMISSION_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles_permissions
-- ----------------------------
INSERT INTO `roles_permissions` VALUES ('admin', '100', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '106', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '1061', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '1062', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '1063', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '1064', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '107', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '200', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '202', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '203', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '205', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '206', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '207', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '208', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '300', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '301', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '302', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '303', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '305', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '350', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '351', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '352', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '353', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '400', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '401', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '402', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '800', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '8001', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '900', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '901', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '902', NULL, '2023-06-12 20:47:23');
INSERT INTO `roles_permissions` VALUES ('admin', '903', NULL, '2023-06-15 13:16:11');
INSERT INTO `roles_permissions` VALUES ('asd', '200', NULL, '2022-12-16 13:07:47');
INSERT INTO `roles_permissions` VALUES ('asd', '201', NULL, '2022-12-16 13:07:47');
INSERT INTO `roles_permissions` VALUES ('asd', '202', NULL, '2022-12-16 13:07:47');
INSERT INTO `roles_permissions` VALUES ('asd', '400', NULL, '2022-12-16 13:07:47');
INSERT INTO `roles_permissions` VALUES ('asd', '401', NULL, '2022-12-16 13:07:47');
INSERT INTO `roles_permissions` VALUES ('asd', '402', NULL, '2022-12-16 13:07:47');
INSERT INTO `roles_permissions` VALUES ('dev', '100', NULL, '2022-12-11 21:06:57');
INSERT INTO `roles_permissions` VALUES ('dev', '200', NULL, '2022-12-11 21:06:57');
INSERT INTO `roles_permissions` VALUES ('dev', '201', NULL, '2022-12-11 21:06:57');
INSERT INTO `roles_permissions` VALUES ('dev', '202', NULL, '2022-12-11 21:06:57');
INSERT INTO `roles_permissions` VALUES ('dev', '300', NULL, '2022-12-11 21:06:57');
INSERT INTO `roles_permissions` VALUES ('dev', '301', NULL, '2022-12-11 21:06:57');
INSERT INTO `roles_permissions` VALUES ('dev', '302', NULL, '2022-12-11 21:06:58');
INSERT INTO `roles_permissions` VALUES ('dev', '303', NULL, '2022-12-11 21:06:58');
INSERT INTO `roles_permissions` VALUES ('ert', '300', NULL, '2023-06-06 09:56:52');
INSERT INTO `roles_permissions` VALUES ('sdf', '300', NULL, '2022-12-04 20:35:34');
INSERT INTO `roles_permissions` VALUES ('sdf', '301', NULL, '2022-12-04 20:35:34');
INSERT INTO `roles_permissions` VALUES ('sdf', '302', NULL, '2022-12-04 20:35:34');
INSERT INTO `roles_permissions` VALUES ('sdf', '303', NULL, '2022-12-04 20:35:34');
INSERT INTO `roles_permissions` VALUES ('test', '300', NULL, '2022-12-04 20:32:40');
INSERT INTO `roles_permissions` VALUES ('test', '303', NULL, '2022-12-04 20:32:40');
INSERT INTO `roles_permissions` VALUES ('test', 'qeq123121', NULL, '2022-12-04 20:32:40');
INSERT INTO `roles_permissions` VALUES ('test23', '106', NULL, '2023-06-06 09:54:15');
INSERT INTO `roles_permissions` VALUES ('test23', '107', NULL, '2023-06-06 09:54:15');
INSERT INTO `roles_permissions` VALUES ('test23', '200', NULL, '2023-06-06 09:54:15');
INSERT INTO `roles_permissions` VALUES ('test23', '202', NULL, '2023-06-06 09:54:15');
INSERT INTO `roles_permissions` VALUES ('test23', '203', NULL, '2023-06-06 09:54:15');
INSERT INTO `roles_permissions` VALUES ('test23', '208', NULL, '2023-06-06 09:54:15');
INSERT INTO `roles_permissions` VALUES ('test23', '305', NULL, '2023-06-06 09:54:15');
INSERT INTO `roles_permissions` VALUES ('wert', '203', NULL, '2023-06-06 09:54:36');

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule`  (
  `ID_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `ID_SCHEDULE` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DT_SCHEDULE` datetime NOT NULL COMMENT '日程时间',
  `DT_END` datetime NULL DEFAULT NULL COMMENT '日程结束时间',
  `CD_NATURE` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '日程类型',
  `NAME_SCHEDULE` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '日程名称、内容',
  `COMMENT` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `PARTICIPATING_USER` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '参与用户，多个逗号分隔，all代表全部用户',
  `TIMESTAMP` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID_USER`, `ID_SCHEDULE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '日程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of schedule
-- ----------------------------
INSERT INTO `schedule` VALUES ('admin', '123', '2022-12-21 00:00:00', '2022-12-22 00:00:00', '测试', '测试日程', 'demo', NULL, '2022-12-15 20:41:04');
INSERT INTO `schedule` VALUES ('admin', '1603341597078454272', '2022-12-04 00:00:00', '2022-12-04 00:00:00', '全天', 'test', 'sd', NULL, '2022-12-15 20:41:08');
INSERT INTO `schedule` VALUES ('admin', '1661365104821878784', '2023-05-25 00:00:00', '2023-05-25 00:00:00', '全天', 'test', 'test', NULL, '2023-05-24 21:34:41');
INSERT INTO `schedule` VALUES ('qwe', '1231', '2022-12-28 15:27:26', '2022-12-28 15:27:32', 'test', 'testa', 'tesssa', NULL, '2022-12-14 15:30:24');
INSERT INTO `schedule` VALUES ('sl', '1672798243463340032', '2023-06-26 03:00:00', '2023-06-26 07:00:00', NULL, 'tes', 'test', NULL, '2023-06-25 10:45:54');
INSERT INTO `schedule` VALUES ('sl', '1672806758647046144', '2023-07-12 00:00:00', NULL, '全天', 'qwerwerwe', 'qwerqwewqwwqq', NULL, '2023-06-25 11:19:45');
INSERT INTO `schedule` VALUES ('sl', '1673190911368368128', '2023-06-08 00:00:00', NULL, '全天', '8888', '88888', NULL, '2023-06-26 12:46:14');
INSERT INTO `schedule` VALUES ('sl', '1673190980712796160', '2023-07-27 00:00:00', NULL, '全天', '272727272', '272727272', NULL, '2023-06-26 12:46:30');

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `ID_PROJECT` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '项目ID',
  `ID_TASK` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '作业ID',
  `NAME_TASK` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CD_TASK_GROUP` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '任务分组',
  `CD_TASK_TYPE` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '任务类型、开发、测试、bug修复、运维等',
  `CD_TAG` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '标签',
  `CD_STATUS` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态',
  `INT_STAR` tinyint(1) NULL DEFAULT NULL COMMENT '星标',
  `DT_CREATE` date NOT NULL COMMENT '创建时间',
  `DT_END` datetime NOT NULL COMMENT '结束时间',
  `ID_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '创建人',
  `PCT_COMPLETE` decimal(5, 2) NULL DEFAULT NULL COMMENT '完成率',
  `PCT_OVERDUE` decimal(5, 2) NULL DEFAULT NULL COMMENT '逾期率',
  `CD_PRIOR` varchar(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '优先级',
  `ID_EXE_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '执行人',
  `CD_DESCRIPTION` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `FLAG_PRIVATE` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否私有',
  `TIMESTAMP` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID_TASK`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1599776699207684096', '1601447316227121152', 'asdAS', '默认分组', '开发', '默认标签', '未开始', NULL, '2022-12-10', '2022-12-12 00:00:00', 'admin', 0.00, NULL, '低', NULL, 'ASDa', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1599776699207684096', '1601462874326745088', 'devwer', '默认分组', '测试', '默认标签', '未开始', NULL, '2022-12-10', '2022-12-19 00:00:00', 'admin', 0.00, NULL, '低', NULL, '<u><font size=\"5\" style=\"background-color: white;\"><b>sdfgsdfdds</b></font></u>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1599776699207684096', '1601463342167801856', 'sdfasdsdas', '分组1', '测试', '默认标签', '未开始', NULL, '2022-12-10', '2022-12-21 00:00:00', 'admin', 0.00, NULL, '低', NULL, 'sdf', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1599776699207684096', '1601487723149426688', '任务3', '分组1', '需求分析', '标签1', '未开始', NULL, '2023-06-17', '2022-12-28 00:00:00', 'admin', 0.00, NULL, '低', NULL, '<p>大多数</p>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1231qq', '1601828615605518336', 'demo任务2', '分组3', '测试', '默认标签', '进行中', NULL, '2023-06-17', '2022-12-28 00:00:00', 'admin', 0.00, NULL, '低', 'asdasd', '<p>大多数</p>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1231qq', '1601829088748175360', 'demo任务8', '分组3', '需求分析', '默认标签', '进行中', NULL, '2023-06-17', '2023-01-07 00:00:00', 'admin', 0.10, NULL, '低', 'sl', '<p>大多数</p>', 'Y', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1231qq', '1601829676944785408', '测试任务1', '分组3', '测试', '默认标签', '未开始', NULL, '2023-06-17', '2023-01-05 00:00:00', 'admin', 0.00, NULL, '低', 'asdasd', '<p>大多数</p>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1231qq', '1601895589647872000', 'test5', '分组3', '测试', '默认标签', '进行中', NULL, '2023-06-17', '2023-01-04 00:00:00', 'admin', 0.00, NULL, '低', NULL, '<p>大多数</p>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1231qq', '1601922444639637504', 'asdf', '分组5', '测试', '标签3', '未开始', NULL, '2023-06-17', '2022-12-28 00:00:00', 'admin', 0.00, NULL, '低', NULL, '<p>大多数</p>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1231qq', '1602269116901109760', 'ceshirenwu', '分组3', '开发', '默认标签', '未开始', NULL, '2023-06-17', '2023-01-04 00:00:00', 'admin', 0.00, NULL, '低', NULL, '<p>大多数</p>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1662466543942070272', '1668167451772592128', 'testr任务', 'test1', 'test2', 'sefe', '未开始', NULL, '2023-06-12', '2023-06-30 00:00:00', 'sl', 0.00, NULL, '低', NULL, '<p>大多数</p>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1231qq', '1668233044544393216', 'test1', '分组3', '开发', '标签3', '进行中', NULL, '2023-06-12', '2023-06-30 00:00:00', 'admin', 0.00, NULL, '低', NULL, '<h1>adasdda</h1>\n<p>sdfadssassa</p>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1231qq', '1670016203567288320', '手动阀2', '分组3', '开发', '标签3', '未开始', NULL, '2023-06-17', '2023-06-23 00:00:00', 'admin', 0.00, NULL, '中', NULL, '<p>任务描述：</p>\n<ul>\n <li>建立用户表</li>\n <li>建立权限表</li>\n <li>建立关联表</li>\n <li>生成mapper</li>\n <li>编写service类</li>\n <li>编写页面</li>\n <li>。。。</li>\n</ul>', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1231qq', '1670037064873553920', 'qweqweqwe', '分组3', '开发', '标签3', '未开始', NULL, '2023-06-17', '2023-06-30 00:00:00', 'admin', 0.00, NULL, '中', NULL, '[{\"insert\":\"asdfasd\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"sdfs\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"ds\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"dsd\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"s\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"dsda\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"sdsad\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"sd\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"dsds\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"dsd\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"sdsads\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"sdsda\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"sdsd\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"},{\"insert\":\"asa\"},{\"attributes\":{\"list\":\"ordered\"},\"insert\":\"\\n\"}]', 'N', '2023-07-27 16:32:18');
INSERT INTO `task` VALUES ('1601498474278866944', '1673318566365458432', '任务三tttss', 'test', 'type1', 'label2', '进行中', NULL, '2023-06-26', '2023-06-30 00:00:00', 'sl', 0.10, NULL, '中', NULL, '[{\"attributes\":{\"bold\":true},\"insert\":\"test\"},{\"attributes\":{\"header\":2},\"insert\":\"\\n\"}]', 'N', '2023-07-27 16:32:18');

-- ----------------------------
-- Table structure for task_bugfix
-- ----------------------------
DROP TABLE IF EXISTS `task_bugfix`;
CREATE TABLE `task_bugfix`  (
  `ID_PROJECT` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `ID_TASK` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `NAME_TASK` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CD_TASK_TYPE` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `CD_TASK_TAG` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `CD_STATUS` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态：未开始、进行中、已修复、已结束、挂起',
  `DT_CREATE` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `DT_END` datetime NULL DEFAULT NULL COMMENT '关闭时间',
  `ID_CREATE_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CD_DESCRIPTION` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `NAME_TESTER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '测试人',
  `DT_FIXED` datetime NULL DEFAULT NULL COMMENT '修复时间',
  `CD_BUG_REASON` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缺陷原因',
  `CD_SEVERITY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '严重程度',
  `CD_MODUAL` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '模块',
  `NAME_SYSTEM` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缺陷系统',
  `CD_SUSPEND` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '挂起原因',
  PRIMARY KEY (`ID_TASK`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_bugfix
-- ----------------------------

-- ----------------------------
-- Table structure for task_comment
-- ----------------------------
DROP TABLE IF EXISTS `task_comment`;
CREATE TABLE `task_comment`  (
  `ID_TASK` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `COMMENT_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DT_COMMENT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CD_COMMENT` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`ID_TASK`, `DT_COMMENT`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '作业评论' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_comment
-- ----------------------------
INSERT INTO `task_comment` VALUES ('1601895589647872000', 'admin', '2023-06-17 18:07:15', 'teste');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 10:52:37', 'sadafs');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 10:56:31', 'sdsfssasssaa');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 10:57:24', '留言测试');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 13:22:45', '索拉卡大家啊');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 13:22:54', '测试测试');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 13:23:03', '测试测试是是是');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 13:23:05', '测试测试是是是');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 13:23:07', '测试测试是是是');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 13:23:08', '测试测试是是是');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-14 13:23:20', '测试测试是是是阿萨');
INSERT INTO `task_comment` VALUES ('1668233044544393216', 'admin', '2023-06-17 18:10:38', '测试测试是是是sdfad测试测试是是是sdfad测试测试是是是sdfad测试测试是是是sdfad测试测试是是是sdfad测试测试是是是sdfad测试测试是是是sdfad测试测试是是是sdfad测试测试是是是sdfad测试测试是是是sdfad测试测试是是是sdfad');
INSERT INTO `task_comment` VALUES ('1670037064873553920', 'admin', '2023-06-17 19:54:43', 'sdfsdfsdsdasdsdsdsdssssddsdsss\n\n\n\n\nsdfsd\n\n\n\n\n\nsdfs');

-- ----------------------------
-- Table structure for task_subtask
-- ----------------------------
DROP TABLE IF EXISTS `task_subtask`;
CREATE TABLE `task_subtask`  (
  `ID_PROJECT` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '项目ID',
  `ID_TASK_PARENT` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '父任务作业ID',
  `ID_SUBTASK` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '作业ID',
  `NAME_TASK` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '作业名',
  `CD_TASK_GROUP` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '任务分组',
  `CD_TASK_TYPE` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '任务类型、开发、测试、bug修复、运维等',
  `CD_TAG` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '标签',
  `CD_STATUS` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态',
  `INT_STAR` tinyint(1) NULL DEFAULT NULL COMMENT '星标',
  `DT_CREATE` datetime NOT NULL COMMENT '创建时间',
  `DT_END` datetime NOT NULL COMMENT '结束时间',
  `ID_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '创建人',
  `PCT_COMPLETE` decimal(5, 2) NULL DEFAULT NULL COMMENT '完成率',
  `PCT_OVERDUE` decimal(5, 2) NULL DEFAULT NULL COMMENT '逾期率',
  `NBR_PRIOR` tinyint NULL DEFAULT NULL COMMENT '优先级',
  `ID_EXE_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '执行人',
  `CD_DESCRIPTION` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `FLAG_PRIVATE` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否私有',
  PRIMARY KEY (`ID_SUBTASK`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '子任务拆分表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_subtask
-- ----------------------------

-- ----------------------------
-- Table structure for task_user
-- ----------------------------
DROP TABLE IF EXISTS `task_user`;
CREATE TABLE `task_user`  (
  `ID_TASK` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `ID_PROJECT` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `ID_EXE_USER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CD_MARK` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '记录主动加入任务的人：主动加入',
  PRIMARY KEY (`ID_TASK`, `ID_EXE_USER`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '未实现一个作业有多个参与者的情况，该表存放多个参与作业的人员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_user
-- ----------------------------
INSERT INTO `task_user` VALUES ('1601829676944785408', '1231qq', 'admin', NULL);
INSERT INTO `task_user` VALUES ('1601829676944785408', '1231qq', 'asdasd', NULL);
INSERT INTO `task_user` VALUES ('1601829676944785408', '1231qq', 'luo', NULL);
INSERT INTO `task_user` VALUES ('1601829676944785408', '1231qq', 'qweqwe', NULL);
INSERT INTO `task_user` VALUES ('1601829676944785408', '1231qq', 'sl', NULL);
INSERT INTO `task_user` VALUES ('1601829676944785408', '1231qq', 'yang', NULL);
INSERT INTO `task_user` VALUES ('1601829676944785408', '1231qq', 'zhangwuji', NULL);
INSERT INTO `task_user` VALUES ('1601895589647872000', '1231qq', 'asdasd', NULL);
INSERT INTO `task_user` VALUES ('1601895589647872000', '1231qq', 'sl', NULL);
INSERT INTO `task_user` VALUES ('1601922444639637504', '1231qq', 'asdasd', NULL);
INSERT INTO `task_user` VALUES ('1602269116901109760', '1231qq', 'admin', NULL);
INSERT INTO `task_user` VALUES ('1602269116901109760', '1231qq', 'asdasd', NULL);
INSERT INTO `task_user` VALUES ('1602269116901109760', '1231qq', 'qweqwe', NULL);
INSERT INTO `task_user` VALUES ('1668167451772592128', '1662466543942070272', 'qweqwe', NULL);
INSERT INTO `task_user` VALUES ('1668233044544393216', '1231qq', 'asdasd', NULL);
INSERT INTO `task_user` VALUES ('1668233044544393216', '1231qq', 'luo', NULL);
INSERT INTO `task_user` VALUES ('1670016203567288320', '1231qq', 'asdasd', NULL);
INSERT INTO `task_user` VALUES ('1670037064873553920', '1231qq', 'asdasd', NULL);
INSERT INTO `task_user` VALUES ('1673318566365458432', '1601498474278866944', 'asdasd', NULL);
INSERT INTO `task_user` VALUES ('1673318566365458432', '1601498474278866944', 'luo', NULL);
INSERT INTO `task_user` VALUES ('1673318566365458432', '1601498474278866944', 'sl', NULL);
INSERT INTO `task_user` VALUES ('1673318566365458432', '1601498474278866944', 'wushuang', NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `USER_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `USER_NAME` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `EMAIL` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `PASSWORD` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CREATE_TIME` datetime NULL DEFAULT NULL,
  `EXPIRE_TIME` datetime NULL DEFAULT NULL,
  `DEPARTMENT` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ROLE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ORGANIZATION` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ID_INSTITUTION` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `VERSION` tinyint NULL DEFAULT NULL COMMENT '版本',
  `CD_PHONE` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `USER_FLAG` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户类型',
  `CD_FROZEN_STATE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冻结状态',
  `DT_LOGIN` date NULL DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('admin', 'admin', NULL, 'DC1FD00E3EEEB940FF46F457BF97D66BA7FCC36E0B20802383DE142860E76AE6', '2022-12-03 20:49:35', NULL, NULL, NULL, NULL, '123', NULL, NULL, NULL, NULL, '2022-12-03');
INSERT INTO `users` VALUES ('asdasd', 'asd', '', '9A6AE9FF2F0D0AD072BF309DB0E19B3AB35ECF034DAA5DB25964045324C19522', NULL, NULL, '', NULL, 'asd', '123', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES ('luo', '老罗', '', 'C68AC63173FCFC537BF22F19A425977029D7DD35DDC5D76B36E58AF222DFDA39', NULL, NULL, '', NULL, '123', '123', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES ('qweqwe', 'qwewerw', 'admin', 'E77232262309FC562074872AB392A4712620AC9CA05A9B67EE51CF945B61ECAC', NULL, NULL, '', NULL, 'qwe', '123', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES ('sl', 'sl', NULL, '9372BDF423216B65D80FBFD3D47C8FC4F5CF0606336292084CFABD82154EAE81', '2022-12-03 11:39:43', NULL, NULL, NULL, NULL, '123', NULL, NULL, NULL, NULL, '2022-12-03');
INSERT INTO `users` VALUES ('wushuang', '无双', 'wushang@163.com', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', NULL, NULL, '123', NULL, '123', '123', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES ('yang', '老杨', '', 'C68AC63173FCFC537BF22F19A425977029D7DD35DDC5D76B36E58AF222DFDA39', NULL, NULL, '', NULL, '123', '123', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES ('zhangwuji', '张无忌', '', 'C68AC63173FCFC537BF22F19A425977029D7DD35DDC5D76B36E58AF222DFDA39', NULL, NULL, '', NULL, '123', '123', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for users_roles
-- ----------------------------
DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE `users_roles`  (
  `USER_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `ROLE_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  `VERSION` tinyint NULL DEFAULT NULL COMMENT '版本',
  `TIMESTAMP` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`USER_ID`, `ROLE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users_roles
-- ----------------------------
INSERT INTO `users_roles` VALUES ('admin', 'admin', NULL, '2022-12-03 22:04:39');
INSERT INTO `users_roles` VALUES ('asdasd', 'asd', NULL, '2022-12-04 20:28:35');
INSERT INTO `users_roles` VALUES ('dfg', 'admin', NULL, '2022-12-04 13:13:36');
INSERT INTO `users_roles` VALUES ('dfgdfg', 'admin', NULL, '2022-12-04 13:20:53');
INSERT INTO `users_roles` VALUES ('luo', 'dev', NULL, '2022-12-11 21:07:57');
INSERT INTO `users_roles` VALUES ('qweqwe', 'qwe', NULL, '2022-12-04 20:31:59');
INSERT INTO `users_roles` VALUES ('sl', 'admin', NULL, '2023-06-05 15:50:50');
INSERT INTO `users_roles` VALUES ('test', 'admin', NULL, '2022-12-03 22:10:58');
INSERT INTO `users_roles` VALUES ('wushuang', 'test', NULL, '2023-06-05 17:52:23');
INSERT INTO `users_roles` VALUES ('yang', 'test', NULL, '2022-12-11 21:08:25');
INSERT INTO `users_roles` VALUES ('zhangwuji', 'asd', NULL, '2022-12-11 21:06:15');

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 全局搜索全文索引（ngram 中文分词，需 MySQL 5.7.6+）。
-- 全新库随本脚本初始化即可；已有库请单独执行下面三条 ALTER，
-- 重复执行会报 Duplicate key name，忽略即可。
-- ============================================================
ALTER TABLE `task` ADD FULLTEXT INDEX `ft_task_name_desc` (`NAME_TASK`, `CD_DESCRIPTION`) WITH PARSER ngram;
ALTER TABLE `task_comment` ADD FULLTEXT INDEX `ft_task_comment` (`CD_COMMENT`) WITH PARSER ngram;
ALTER TABLE `project_task_file` ADD FULLTEXT INDEX `ft_file_name_comment` (`NAME_FILE`, `CD_COMMENT`) WITH PARSER ngram;
