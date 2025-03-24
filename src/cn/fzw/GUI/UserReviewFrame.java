package cn.fzw.GUI;

import cn.fzw.DAO.ReviewDao;
import cn.fzw.DAO.UserDao;
import cn.fzw.DAO.impl.ReviewDaoImpl;
import cn.fzw.DAO.impl.UserDaoImpl;
import cn.fzw.model.Review;
import cn.fzw.model.User;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserReviewFrame extends JFrame {
    private int userId;
    private JPanel reviewPanel; // 用于存放评论的 JPanel

    public UserReviewFrame(int userId) {
        this.userId = userId;
        initUI();
        loadReviews();
    }

    private void initUI() {
        setTitle("用户评论 - 用户ID：" + userId);
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

        // 将 reviewPanel 添加到 scrollPane 中
        scrollPane.setViewportView(reviewPanel);
    }

    private void loadReviews() {
        reviewPanel.removeAll(); // 清空原有内容

        try {
            ReviewDao reviewDao = new ReviewDaoImpl();
            UserDao userDao = new UserDaoImpl();
            List<Review> reviews = reviewDao.getReviewsByUserId(userId);

            if (reviews.isEmpty()) {
                JOptionPane.showMessageDialog(this, "该用户暂无评论！");
            } else {
                for (Review review : reviews) {
                    // 获取用户信息
                    User user = userDao.getUserById(review.getUserId());
                    String nickname = (user != null) ? user.getNickname() : "未知用户";

                    // 添加评论项
                    JPanel reviewItem = createReviewItem(review, nickname);
                    reviewPanel.add(reviewItem);
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

        topPanel.add(userPanel, BorderLayout.WEST);

        // 评论内容
        JTextArea contentArea = new JTextArea(review.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        itemPanel.add(topPanel, BorderLayout.NORTH);
        itemPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        return itemPanel;
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
