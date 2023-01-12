/*
 Navicat Premium Data Transfer

 Source Server         : 102
 Source Server Type    : MySQL
 Source Server Version : 50631
 Source Host           : localhost:3306
 Source Schema         : jeff

 Target Server Type    : MySQL
 Target Server Version : 50631
 File Encoding         : 65001

 Date: 16/05/2019 23:19:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `loginName` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码加密盐',
  `sex` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '性别',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'RESOURCE_ENABLE' COMMENT '用户状态',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `nickName` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `headimgUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `roleId` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `createName` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `modifyTime` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `modifyName` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `IDx_user_login_name`(`loginName`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 220 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '9283a03246ef2dacdc21a9b137817ec1', 'test', '0', 'RESOURCE_ENABLE', 'admin', NULL, '18707173376', 'admin@163.com', NULL, 7, '2019-03-06 13:14:05', 'admin', '2019-05-08 13:02:12', 'admin');
INSERT INTO `user` VALUES (66, 'jeff', 'b56ab47690305b89be147d0fa16111d1', '47de1c7487d9436db49e6ac8076192c1', '1', 'RESOURCE_ENABLE', '杰', NULL, '13000000001', 'jeff@163.com', NULL, 20, '2019-03-14 10:36:54', 'admin', '2019-05-08 13:10:05', 'jeff');
INSERT INTO `user` VALUES (67, 'zs', 'bbbe949ce3aeb0c113139f60ec77d057', '7fa91b678c8f434882c6a3fc3e29072d', '1', 'RESOURCE_ENABLE', '张三', NULL, '13000000005', 'zs@163.com', NULL, 2, '2019-03-26 17:54:47', 'admin', '2019-04-26 13:58:03', 'jeff');
INSERT INTO `user` VALUES (68, 'ls', 'ee5690bf377418ab9bd8fcbeafdeb712', 'b4c8aa6861a94cc8aa002d89929603c6', '2', 'RESOURCE_ENABLE', '李四', NULL, '13000000004', 'ls@163.com', NULL, 2, '2019-03-26 17:55:36', 'admin', '2019-04-26 13:58:27', 'jeff');
INSERT INTO `user` VALUES (69, 'ww', 'f9bca0416627d62c3946d541661e75f0', 'f19df35517e044bf9fa67048ca6a1048', '0', 'RESOURCE_ENABLE', '王五', NULL, '13000000005', 'ww@163.com', NULL, 2, '2019-03-26 17:59:08', 'admin', '2019-04-26 13:58:43', 'admin');
INSERT INTO `user` VALUES (162, 'zs1', '4ce6082a8469550ecd7d7f098fc65033', '88caccfcb662424c8cb4c2ffe99be7c2', '1', 'RESOURCE_ENABLE', '张三1', NULL, '13000000011', 'zs1@163.com', NULL, 2, '2019-04-26 13:52:26', 'admin', '2019-04-26 14:02:24', 'admin');
INSERT INTO `user` VALUES (163, 'zs2', 'f455aaae6b4090436f2a3b2af7a30bb3', '83c3c6aa0e524e9a9a7c54385fe65a67', '1', 'RESOURCE_ENABLE', '张三2', NULL, '13000000022', 'zs2@163.com', NULL, 2, '2019-04-26 13:53:22', 'admin', '2019-04-26 14:02:31', 'admin');
INSERT INTO `user` VALUES (164, 'zs3', '9ab9c430b0214b1fbf3100049d19e2e1', 'e6351b2b9bac434fba75faf794b024fa', '1', 'RESOURCE_ENABLE', '张三3', NULL, '13000000003', 'zs3@163.com', NULL, 2, '2019-04-26 13:54:32', 'admin', '2019-04-26 14:02:40', 'admin');
INSERT INTO `user` VALUES (165, 'zs4', 'ea1be174cf9092c90c999ab3423aaee0', 'f95fc3eac87b48ed83e2ea1e93f102f4', '1', 'RESOURCE_ENABLE', '张三4', NULL, '13000000004', 'zs4@163.com', NULL, 2, '2019-04-26 13:56:02', 'admin', '2019-04-26 14:02:45', 'admin');
INSERT INTO `user` VALUES (166, 'ls2', '1aa4db34be0579b0c457ef85a9080e0e', '66cd40c3f5684e50b2ce2861ae475dc7', '0', 'RESOURCE_ENABLE', '李四2', NULL, '13000000022', 'ls2@163.com', NULL, 2, '2019-04-26 13:59:46', 'admin', '2019-04-26 14:03:18', 'admin');
INSERT INTO `user` VALUES (204, 'cs601', 'bab4867766afe851c60ff82d85fef5b7', 'f0ffb142b53c4289a3ba8cc14c136609', '1', 'RESOURCE_ENABLE', '测试601', NULL, '13000000601', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (205, 'cs602', 'ccc22dbb546c0240b984532c444c2feb', '002b7ea1581f4387a43ee9a5981d39b9', '1', 'RESOURCE_ENABLE', '测试602', NULL, '13000000602', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (206, 'cs603', '81ec0d993c91a540f7c0ebee50caae72', 'cccb047f977d4c4eb1c2b1b900b88e6f', '1', 'RESOURCE_ENABLE', '测试603', NULL, '13000000603', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (207, 'cs604', 'aa64d4b638c9cee4fc6b200f50dc23d4', 'ed9ea1f2a2644f1b9474acd051dae438', '1', 'RESOURCE_ENABLE', '测试604', NULL, '13000000604', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (208, 'cs605', '38de4283bf20b8d605866212523a8f02', '623c5da250374cf19ded61ec92663b9b', '1', 'RESOURCE_ENABLE', '测试605', NULL, '13000000605', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (209, 'cs606', '84cc2f9cb7f6e8490ce68370d96d0522', '3b82351b07bf4f25a18eaaa05134c1c0', '1', 'RESOURCE_ENABLE', '测试606', NULL, '13000000606', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (210, 'cs607', '1849320090b569bda77e719048b9d551', '2aec32d9037c4603b3797bbe2cfe3cad', '1', 'RESOURCE_ENABLE', '测试607', NULL, '13000000607', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (211, 'cs608', 'a09d5f469f0469a9f4fd82d6254ec998', '958ac2e608df4782a4a53afb07eb14e3', '1', 'RESOURCE_ENABLE', '测试608', NULL, '13000000608', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (212, 'cs609', 'a144fa4c5c3e075e0eb4b85e70baa9c6', '05523624dd6245229c5f95995e241243', '1', 'RESOURCE_ENABLE', '测试609', NULL, '13000000609', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (213, 'cs610', 'c86ffb04fb0c2f2d946e18213e2181f8', '272d7413232f46908c86e5b7ddb3d27f', '1', 'RESOURCE_ENABLE', '测试610', NULL, '13000000610', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (214, 'cs611', '2f4700e1a141a5cc65a8508fbe5a71e3', '2677199f41764f78a62a3f1612eb55b8', '1', 'RESOURCE_ENABLE', '测试611', NULL, '13000000611', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (215, 'cs612', '01cd994595f06f6130c971b3b822a93b', '19bbdcbb90c5470a9eb8f2b33c4accc7', '1', 'RESOURCE_ENABLE', '测试612', NULL, '13000000612', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (216, 'cs613', '216b4fda908abe6481fc27921cfee6dc', 'ae292f084b7c40178ca4a3994ea9a67e', '1', 'RESOURCE_ENABLE', '测试613', NULL, '13000000613', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (217, 'cs614', 'c7b322abb27b7e9c0cf5e075861fa210', '1c91d160733842008ec64cbec0cd1a2e', '1', 'RESOURCE_ENABLE', '测试614', NULL, '13000000614', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);
INSERT INTO `user` VALUES (218, 'cs615', '6c227fdeea071cd1d3e1abca4ddd8feb', '273d8b66e04640e78099c9dcc01bb203', '1', 'RESOURCE_ENABLE', '测试615', NULL, '13000000615', NULL, NULL, 2, '2019-05-14 15:55:55', 'admin', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
