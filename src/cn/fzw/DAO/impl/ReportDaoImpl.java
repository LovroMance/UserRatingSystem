package cn.fzw.DAO.impl;

import cn.fzw.DAO.ReportDao;
import cn.fzw.DAO.UserDao;
import cn.fzw.model.Report;
import cn.fzw.model.User;
import cn.fzw.utils.DButils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDaoImpl implements ReportDao {
    // 添加举报
    @Override
    public int addReport(Report report) throws Exception {
        Connection conn = null;
        try {
            conn = DButils.getConnection();
            conn.setAutoCommit(false); // 开启事务

            // 步骤1：插入举报记录
            String sql = "INSERT INTO report (user_id, review_id, reported_user_id, report_reason, report_time) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, report.getUserId());
                pstmt.setObject(2, report.getReviewId());
                pstmt.setInt(3, report.getReportedUserId());
                pstmt.setString(4, report.getReportReason());
                pstmt.setTimestamp(5, new Timestamp(report.getReportTime().getTime()));
                pstmt.executeUpdate();
            }

            // 步骤2：更新被举报用户的 report_count
            UserDao userDao = new UserDaoImpl();
            try {
                User reportedUser = userDao.getUserById(report.getReportedUserId());
                reportedUser.setReportCount(reportedUser.getReportCount() + 1); // 计数器+1
                userDao.updateUser(reportedUser); // 更新用户信息
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("更新用户举报次数失败: " + e.getMessage());
            }

            conn.commit(); // 提交事务
            return 1;
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // 根据ID查询举报
    @Override
    public Report getReportById(int reportId) throws Exception {
        String sql = "SELECT * FROM report WHERE report_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReport(rs);
                }
                return null; // 未找到举报
            }
        }
    }

    // 查询某个用户的所有举报
    @Override
    public List<Report> getReportsByUserId(int userId) throws Exception {
        String sql = "SELECT * FROM report WHERE user_id = ?";
        List<Report> reports = new ArrayList<>();
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToReport(rs));
                }
            }
        }
        return reports;
    }

    // 查询某个评价的所有举报
    @Override
    public List<Report> getReportsByReviewId(int reviewId) throws Exception {
        String sql = "SELECT * FROM report WHERE review_id = ?";
        List<Report> reports = new ArrayList<>();
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToReport(rs));
                }
            }
        }
        return reports;
    }

    // 查询所有举报
    @Override
    public List<Report> getAllReports() throws Exception {
        String sql = "SELECT * FROM report";
        List<Report> reports = new ArrayList<>();
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                reports.add(mapResultSetToReport(rs));
            }
        }
        return reports;
    }

    // 根据ID删除举报
    @Override
    public int deleteReport(int reportId) throws Exception {
        String sql = "DELETE FROM report WHERE report_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            return pstmt.executeUpdate(); // 返回受影响的行数
        }
    }

    // 将ResultSet映射为Report对象
    private Report mapResultSetToReport(ResultSet rs) throws Exception {
        Report report = new Report();
        report.setReportId(rs.getInt("report_id"));
        report.setUserId(rs.getInt("user_id"));
        report.setReviewId(rs.getObject("review_id", Integer.class));
        report.setReportedUserId(rs.getInt("reported_user_id")); // 新增映射
        report.setReportReason(rs.getString("report_reason"));
        report.setReportTime(new Date(rs.getTimestamp("report_time").getTime()));
        return report;
    }

}
