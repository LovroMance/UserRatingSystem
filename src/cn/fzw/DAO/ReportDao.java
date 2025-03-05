package cn.fzw.DAO;

import cn.fzw.model.Report;

import java.util.List;

public interface ReportDao {
    // 添加举报
    int addReport(Report report) throws Exception;

    // 根据ID查询举报
    Report getReportById(int reportId) throws Exception;

    // 查询某个用户的所有举报
    List<Report> getReportsByUserId(int userId) throws Exception;

    // 查询某个评价的所有举报
    List<Report> getReportsByReviewId(int reviewId) throws Exception;

    // 查询所有举报
    List<Report> getAllReports() throws Exception;

    // 根据ID删除举报
    int deleteReport(int reportId) throws Exception;
}
