//创建用户表
String createUserTableSql = "CREATE TABLE user (" +
"user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户id', " +
"username VARCHAR(20) NOT NULL UNIQUE COMMENT '用户名', " +
"password VARCHAR(50) NOT NULL COMMENT '密码', " +
"nickname VARCHAR(20) COMMENT '用户昵称', " +
"gender TINYINT DEFAULT 0 CHECK(gender IN (0,1,2)) COMMENT '性别(0:未知,1:男,2:女)', " +
"user_status TINYINT DEFAULT 0 CHECK(user_status IN (0,1,2)) NOT NULL COMMENT '用户状态(0:正常,1:封禁,2:注销)', " +
"signature VARCHAR(100) COMMENT '个性签名', " +
"like_count INT DEFAULT 0 NOT NULL COMMENT '收到点赞数量', " +
"review_count INT DEFAULT 0 NOT NULL COMMENT '发布评价数量', " +
"report_count INT DEFAULT 0 NOT NULL COMMENT '被举报次数'" +
") COMMENT='用户表';";
//创建商品表
String createProductTableSql = "CREATE TABLE product (" +
"product_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '商品id', " +
"product_name VARCHAR(20) NOT NULL UNIQUE COMMENT '商品名称', " +
"price DECIMAL(10,2) CHECK(price >= 0) NOT NULL COMMENT '商品价格', " +
"description TEXT COMMENT '商品描述'" +
") COMMENT='商品表';";
//创建评价表
String createReviewTableSql = "CREATE TABLE review (" +
"review_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '评价id', " +
"user_id INT NOT NULL COMMENT '用户id', " +
"product_id INT NOT NULL COMMENT '商品id', " +
"content TEXT NOT NULL COMMENT '评价内容', " +
"rating INT NOT NULL DEFAULT 3 CHECK(rating BETWEEN 1 AND 5) COMMENT '评价星级(1-5星)', " +
"review_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间', " +
"FOREIGN KEY (user_id) REFERENCES user(user_id), " +
"FOREIGN KEY (product_id) REFERENCES product(product_id)" +
") COMMENT='评价表';";
//创建举报表
String createReportTableSql = "CREATE TABLE report (" +
"report_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '举报id', " +
"user_id INT NOT NULL COMMENT '举报人id', " +
"review_id INT COMMENT '被举报的评论id', " +
"report_reason TEXT NOT NULL COMMENT '举报原因', " +
"report_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '举报时间', " +
"FOREIGN KEY (user_id) REFERENCES user(user_id), " +
"FOREIGN KEY (review_id) REFERENCES review(review_id)" +
") COMMENT='举报表';";
try {
DatabaseUtils.executeUpdate(createUserTableSql);
DatabaseUtils.executeUpdate(createProductTableSql);
DatabaseUtils.executeUpdate(createReviewTableSql);
DatabaseUtils.executeUpdate(createReportTableSql);
} catch (SQLException e) {
throw new RuntimeException(e);
}

1. (*)仅第一遍演示需要允许