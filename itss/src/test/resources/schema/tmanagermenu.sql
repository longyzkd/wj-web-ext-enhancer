
CREATE TABLE tmanagermenu (
  MenuID varchar(100) NOT NULL,
  MenuName varchar(400) DEFAULT NULL,
  IconURL varchar(1000) DEFAULT NULL,
  FunURL varchar(1000) DEFAULT NULL,
  FMenuID varchar(1000) DEFAULT NULL,
  FunID varchar(100) DEFAULT NULL,
  OrderID int(11) DEFAULT NULL,
  Note varchar(2000) DEFAULT NULL,
  PRIMARY KEY (MenuID)
) ;

-- ----------------------------
-- Records of tmanagermenu
-- ----------------------------
INSERT INTO tmanagermenu VALUES ('01', '服务台', 'menu_setting', null, '', '01', '1', null);
INSERT INTO tmanagermenu VALUES ('0101', '服务台', 'jslib/ExtJs/resources/themes/icons/grid.png', 'serviceDesk/main', '01', '0101', '101', null);
INSERT INTO tmanagermenu VALUES ('0102', '待办事项', 'jslib/ExtJs/resources/themes/icons/book_open.png', 'serviceDesk/todoList', '01', '0102', '102', null);

