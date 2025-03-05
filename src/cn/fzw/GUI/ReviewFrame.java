package cn.fzw.GUI;

import cn.fzw.DAO.ReviewDao;
import cn.fzw.DAO.UserDao;
import cn.fzw.DAO.ReportDao;
import cn.fzw.DAO.impl.ReviewDaoImpl;
import cn.fzw.DAO.impl.UserDaoImpl;
import cn.fzw.DAO.impl.ReportDaoImpl;
import cn.fzw.model.Review;
import cn.fzw.model.User;
import cn.fzw.model.Report;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReviewFrame extends JFrame {
    private JPanel reviewPanel;
    private int productId;
    private int currentUserId; // 当前登录用户ID
    private boolean isAdmin; // 当前用户是否为管理员

    public ReviewFrame(int productId, int currentUserId) {
        this.productId = productId;
        this.currentUserId = currentUserId;
        this.isAdmin = checkIfAdmin(currentUserId); // 检查是否为管理员
        initUI();
        loadReviews();
    }

    // 检查当前用户是否为管理员
    private boolean checkIfAdmin(int userId) {
        try {
            UserDao userDao = new UserDaoImpl();
            return userDao.isAdmin(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initUI() {
        setTitle("商品评论 - 商品ID：" + productId);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 主面板使用滚动面板包裹
        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 评论展示面板
        reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        reviewPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 设置评论面板的最大高度（每页最多显示3条评论）
        int maxHeight = 3 * 150; // 每条评论高度约为150px
        reviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxHeight));

        scrollPane.setViewportView(reviewPanel);
    }

    private void loadReviews() {
        reviewPanel.removeAll(); // 清空原有内容

        try {
            ReviewDao reviewDao = new ReviewDaoImpl();
            UserDao userDao = new UserDaoImpl();
            List<Review> reviews = reviewDao.getReviewsByProductId(productId);

            if (reviews.isEmpty()) {
                reviewPanel.add(new JLabel("暂无评论", JLabel.CENTER));
            } else {
                for (Review review : reviews) {
                    // 获取用户信息
                    User user = userDao.getUserById(review.getUserId());
                    String nickname = (user != null) ? user.getNickname() : "未知用户";

                    // 添加评论项
                    reviewPanel.add(createReviewItem(review, nickname));
                    reviewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "评论加载失败：" + e.getMessage());
        }

        reviewPanel.revalidate();
        reviewPanel.repaint();
    }

    private JPanel createReviewItem(Review review, String nickname) {
        // 主面板
        JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        itemPanel.setPreferredSize(new Dimension(700, 160));

        // 顶部信息面板
        JPanel topPanel = new JPanel(new BorderLayout());

        // 用户信息面板（调整为垂直布局）
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

        // 用户昵称和发布时间
        JLabel nicknameLabel = new JLabel(nickname);
        nicknameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));

        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        JLabel timeLabel = new JLabel(sdf.format(review.getReviewTime()));
        timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        timeLabel.setForeground(Color.GRAY);

        userPanel.add(nicknameLabel);
        userPanel.add(timeLabel);

        // 评分面板
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        ratingPanel.add(createStarRating(review.getRating()));

        // 组合用户信息和评分
        JPanel userInfoPanel = new JPanel(new BorderLayout());
        userInfoPanel.add(userPanel, BorderLayout.WEST);
        userInfoPanel.add(ratingPanel, BorderLayout.EAST);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton likeButton = new JButton("点赞");
        JButton reportButton = new JButton("举报");
        JButton deleteButton = new JButton("删除");
        // 仅非自己发布的评论显示举报按钮
        if (review.getUserId() != currentUserId) {
            buttonPanel.add(reportButton);
        }

        // 点赞按钮事件
        likeButton.addActionListener(e -> handleLikeReview(review.getUserId()));

        // 举报按钮事件
        reportButton.addActionListener(e -> handleReportReview(review));

        // 删除按钮事件
        deleteButton.addActionListener(e -> handleDeleteReview(review.getReviewId(), review.getUserId()));

        // 控制删除按钮的显示
        if (isAdmin || review.getUserId() == currentUserId) {
            buttonPanel.add(deleteButton); // 管理员或评论发布者可以删除
        }

        // 仅非自己发布的评论显示举报按钮
        if (review.getUserId() != currentUserId) {
            buttonPanel.add(reportButton);
        }

        buttonPanel.add(likeButton);
        topPanel.add(userPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // 评论内容
        JTextArea contentArea = new JTextArea(review.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        itemPanel.add(topPanel, BorderLayout.NORTH);
        itemPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        return itemPanel;
    }

    // 点赞评论
    private void handleLikeReview(int targetUserId) {
        try {
            UserDao userDao = new UserDaoImpl();
            User targetUser = userDao.getUserById(targetUserId);

            if (targetUser == null) {
                JOptionPane.showMessageDialog(this, "用户不存在！");
                return;
            }

            // 更新点赞数
            targetUser.setLikeCount(targetUser.getLikeCount() + 1);
            int result = userDao.updateUser(targetUser);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "点赞成功！");
                loadReviews(); // 刷新评论列表
            } else {
                JOptionPane.showMessageDialog(this, "点赞失败！");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "操作失败：" + e.getMessage());
        }
    }

    // 举报评论
    private void handleReportReview(Review review) {
        try {
            // 获取举报理由
            String reason = JOptionPane.showInputDialog(
                    this,
                    "请输入举报理由：",
                    "举报评论",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (reason != null && !reason.trim().isEmpty()) {
                Report report = new Report();
                report.setUserId(currentUserId); // 举报人ID
                report.setReviewId(review.getReviewId()); // 被举报评论ID
                report.setReportedUserId(review.getUserId()); // 被举报用户ID
                report.setReportReason(reason);
                report.setReportTime(new Date());

                ReportDao reportDao = new ReportDaoImpl();
                int result = reportDao.addReport(report);

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "举报已提交！");
                } else {
                    JOptionPane.showMessageDialog(this, "举报提交失败！");
                }
            } else if (reason != null) { // 用户点击确定但未输入内容
                JOptionPane.showMessageDialog(this, "举报理由不能为空！");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "操作失败：" + e.getMessage());
        }
    }

    // 删除评论
    private void handleDeleteReview(int reviewId, int reviewUserId) {
        if (!isAdmin && reviewUserId != currentUserId) {
            JOptionPane.showMessageDialog(this, "您无权删除此评论！");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除这条评论吗？（关联举报记录将标记为已删除）",
                "删除确认",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ReviewDao reviewDao = new ReviewDaoImpl();
                int result = reviewDao.deleteReviewWithReports(reviewId); // 使用新方法
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "删除成功！");
                    loadReviews(); // 重新加载评论列表
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败：" + e.getMessage());
            }
        }
    }

    // 创建星级评分显示
    private JPanel createStarRating(int rating) {
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        starPanel.setPreferredSize(new Dimension(120, 20));

        // 添加实心星
        for (int i = 0; i < rating; i++) {
            starPanel.add(new JLabel("★", JLabel.CENTER)); // 实心星
        }
        // 添加空心星
        for (int i = rating; i < 5; i++) {
            starPanel.add(new JLabel("☆", JLabel.CENTER)); // 空心星
        }
        return starPanel;
    }
}