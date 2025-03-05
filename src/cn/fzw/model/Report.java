package cn.fzw.model;

import java.util.Date;

public class Report {
    private int reportId;
    private int userId;
    private Integer reviewId; // 使用Integer类型以允许为空
    private int reportedUserId; // 新增字段：被举报用户ID
    private String reportReason;
    private Date reportTime;
    private boolean isDeleted; // 新增字段：是否关联评论已删除

    public Report(int reportId, int userId, Integer reviewId, String reportReason, Date reportTime) {
        this.reportId = reportId;
        this.userId = userId;
        this.reviewId = reviewId;
        this.reportReason = reportReason;
        this.reportTime = reportTime;
    }

    public Report() {

    }

    // Getters and Setters

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(int reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", userId=" + userId +
                ", reviewId=" + reviewId +
                ", reportReason='" + reportReason + '\'' +
                ", reportTime=" + reportTime +
                '}';
    }
}
