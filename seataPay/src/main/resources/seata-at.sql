SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`
(
    `branch_id`     bigint(20) NOT NULL COMMENT '分支事务ID',
    `xid`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全局事务ID',
    `context`       varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上下文',
    `rollback_info` longblob                                                NOT NULL COMMENT '回滚信息',
    `log_status`    int(11) NOT NULL COMMENT '状态，0正常，1全局已完成',
    `log_created`   datetime(6) NOT NULL COMMENT '创建时间',
    `log_modified`  datetime(6) NOT NULL COMMENT '修改时间',
    UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Compact;

SET
FOREIGN_KEY_CHECKS = 1;
