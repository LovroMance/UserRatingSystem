package cn.fzw.model;

import java.util.Date;

public class Review {
    private int reviewId;
    private int userId;
    private int productId;
    private String content;
    private int rating; // 评价星级, 1-5星
    private Date reviewTime; // 创建时间

    public Review(int reviewId, int userId, int productId, String content, int rating, Date reviewTime) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
        this.reviewTime = reviewTime;
    }

    public Review() {
    }

    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) { // 检查评价星级是否合法
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Invalid rating value. Must be between 1 and 5.");
        }
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", reviewTime=" + reviewTime +
                '}';
    }
}
