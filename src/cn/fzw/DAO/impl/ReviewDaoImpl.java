package cn.fzw.DAO.impl;

import cn.fzw.DAO.ReviewDao;
import cn.fzw.model.Review;
import cn.fzw.utils.DButils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDaoImpl implements ReviewDao {
    // 添加评价
    @Override
    public int addReview(Review review) throws Exception {
        String sql = "INSERT INTO review (user_id, product_id, content, rating, review_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getProductId());
            pstmt.setString(3, review.getContent());
            pstmt.setInt(4, review.getRating());
            pstmt.setTimestamp(5, new Timestamp(review.getReviewTime().getTime()));
            int affectedRows = pstmt.executeUpdate();

            // 获取自增的主键值
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // 返回生成的review_id
                    }
                }
            }
            return -1; // 插入失败
        }
    }

    // 根据ID查询评价
    @Override
    public Review getReviewById(int reviewId) throws Exception {
        String sql = "SELECT * FROM review WHERE review_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReview(rs);
                }
                return null; // 未找到评价
            }
        }
    }

    // 查询某个商品的所有评价
    @Override
    public List<Review> getReviewsByProductId(int productId) throws Exception {
        String sql = "SELECT * FROM review WHERE product_id = ?";
        List<Review> reviews = new ArrayList<>();
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        }
        return reviews;
    }

    // 查询某个用户的所有评价
    @Override
    public List<Review> getReviewsByUserId(int userId) throws Exception {
        String sql = "SELECT * FROM review WHERE user_id = ?";
        List<Review> reviews = new ArrayList<>();
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        }
        return reviews;
    }

    // 更新评价内容
    @Override
    public int updateReview(Review review) throws Exception {
        String sql = "UPDATE review SET content = ?, rating = ? WHERE review_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, review.getContent());
            pstmt.setInt(2, review.getRating());
            pstmt.setInt(3, review.getReviewId());
            return pstmt.executeUpdate(); // 返回受影响的行数
        }
    }

    // 根据ID删除评价
    @Override
    public int deleteReview(int reviewId) throws Exception {
        String sql = "DELETE FROM review WHERE review_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate(); // 返回受影响的行数
        }
    }

    // 将ResultSet映射为Review对象
    private Review mapResultSetToReview(ResultSet rs) throws Exception {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setProductId(rs.getInt("product_id"));
        review.setContent(rs.getString("content"));
        review.setRating(rs.getInt("rating"));
        review.setReviewTime(new Date(rs.getTimestamp("review_time").getTime()));
        return review;
    }

    @Override
    public int deleteReviewWithReports(int reviewId) throws Exception {
        Connection conn = null;
        try {
            conn = DButils.getConnection();
            conn.setAutoCommit(false); // 开启事务

            // 第一步：删除关联的举报记录
            String deleteReportsSQL = "DELETE FROM report WHERE review_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteReportsSQL)) {
                pstmt.setInt(1, reviewId);
                pstmt.executeUpdate();
            }

            // 第二步：删除评论
            String deleteReviewSQL = "DELETE FROM review WHERE review_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteReviewSQL)) {
                pstmt.setInt(1, reviewId);
                int result = pstmt.executeUpdate();
                conn.commit(); // 提交事务
                return result;
            }
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback(); // 回滚事务
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
