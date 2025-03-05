package cn.fzw.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String nickname;
    private int gender; // 0:未知, 1:男, 2:女
    private int userStatus; // 0:正常, 1:封禁, 2:注销
    private String signature;
    private int likeCount;
    private int reportCount;
    private int reviewCount;
    private boolean isAdmin; //标识是否为管理员

    public User(int userId, String username, String password, String nickname,boolean isAdmin) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.isAdmin = isAdmin;
    }

    public User() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    // Getters and Setters

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        if (gender >= 0 && gender <= 2) { // 检查性别值是否合法
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Invalid gender value. Must be 0, 1, or 2.");
        }
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        if (userStatus >= 0 && userStatus <= 2) { // 检查用户状态值是否合法
            this.userStatus = userStatus;
        } else {
            throw new IllegalArgumentException("Invalid user status value. Must be 0, 1, or 2.");
        }
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }


    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender=" + gender +
                ", userStatus=" + userStatus +
                ", signature='" + signature + '\'' +
                ", likeCount=" + likeCount +
                ", reportCount=" + reportCount +
                '}';
    }
}
