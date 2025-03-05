package cn.fzw.DAO.impl;

import cn.fzw.DAO.UserDao;
import cn.fzw.model.User;
import cn.fzw.utils.DButils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    // 添加用户
    @Override
    public int addUser(User user) throws Exception {
    String sql = "INSERT INTO user (username, password, nickname, gender, user_status, signature, like_count, review_count, report_count, is_admin) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DButils.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setInt(4, user.getGender());
            pstmt.setInt(5, user.getUserStatus());
            pstmt.setString(6, user.getSignature());
            pstmt.setInt(7, user.getLikeCount());
            pstmt.setInt(8, user.getReviewCount());
            pstmt.setInt(9, user.getReportCount());
            pstmt.setBoolean(10, user.isAdmin());
            int affectedRows = pstmt.executeUpdate();

            // 获取自增的主键值
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // 返回生成的user_id
                    }
                }
            }
            return -1; // 插入失败
        }
    }

    // 根据ID查询用户
    @Override
    public User getUserById(int userId) throws Exception {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    // 映射所有字段
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setNickname(rs.getString("nickname"));
                    user.setGender(rs.getInt("gender"));
                    user.setUserStatus(rs.getInt("user_status"));
                    user.setSignature(rs.getString("signature"));
                    user.setLikeCount(rs.getInt("like_count"));
                    user.setReviewCount(rs.getInt("review_count"));
                    user.setReportCount(rs.getInt("report_count"));
                    user.setAdmin(rs.getBoolean("is_admin")); // 关键字段
                    return user;
                } else {
                    System.out.println("用户不存在");
                    return null; // 用户不存在
                }
            }
        } catch (SQLException e) {
            throw new Exception("数据库查询失败: " + e.getMessage());
        }
    }

    // 根据用户名查询用户
    @Override
    public User getUserByUsername(String username) throws Exception {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null; // 未找到用户
            }
        }
    }

    // 查询所有用户
    @Override
    public List<User> getAllUsers() throws Exception {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    // 更新用户信息
    @Override
    public int updateUser(User user) throws Exception {
        String sql = "UPDATE user SET " +
                "username=?, password=?, nickname=?, gender=?, " +
                "user_status=?, signature=?, like_count=?, " +
                "review_count=?, report_count=? " + // 确保包含 report_count
                "WHERE user_id=?";

        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setInt(4, user.getGender());
            pstmt.setInt(5, user.getUserStatus());
            pstmt.setString(6, user.getSignature());
            pstmt.setInt(7, user.getLikeCount());
            pstmt.setInt(8, user.getReviewCount());
            pstmt.setInt(9, user.getReportCount()); // 设置 report_count
            pstmt.setInt(10, user.getUserId());

            return pstmt.executeUpdate();
        }
    }

    // 根据ID删除用户
    @Override
    public int deleteUser(int userId) throws Exception {
        String sql = "DELETE FROM user WHERE user_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate(); // 返回受影响的行数
        }
    }

    // 将ResultSet映射为User对象
    private User mapResultSetToUser(ResultSet rs) throws Exception {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setNickname(rs.getString("nickname"));
        user.setGender(rs.getInt("gender"));
        user.setUserStatus(rs.getInt("user_status"));
        user.setSignature(rs.getString("signature"));
        user.setLikeCount(rs.getInt("like_count"));
        user.setReviewCount(rs.getInt("review_count"));
        user.setReportCount(rs.getInt("report_count"));
        return user;
    }

    @Override
    public boolean isAdmin(int userId) throws Exception {
        String sql = "SELECT is_admin FROM user WHERE user_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_admin");
                }
            }
        }
        return false;
    }
}
