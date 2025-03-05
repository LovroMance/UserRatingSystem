package cn.fzw.GUI;

import cn.fzw.DAO.ReviewDao;
import cn.fzw.DAO.impl.ReviewDaoImpl;
import cn.fzw.model.Review;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class AddReviewFrame extends JFrame {
    private int productId;
    private int userId;

    public AddReviewFrame(int productId, int userId) {
        this.productId = productId;
        this.userId = userId;
        initUI();
    }

    private void initUI() {
        setTitle("添加评论");
        setSize(500, 400); // 增大窗口尺寸
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 使用 BorderLayout 布局
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 评论内容面板
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        JLabel contentLabel = new JLabel("评论内容：");
        contentLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        contentPanel.add(contentLabel, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setPreferredSize(new Dimension(450, 200)); // 增大评价框尺寸
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // 评分面板
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel ratingLabel = new JLabel("评分（1-5星）：");
        ratingLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        ratingPanel.add(ratingLabel);

        JComboBox<Integer> ratingComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        ratingComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        ratingPanel.add(ratingComboBox);

        // 提交按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton submitButton = new JButton("提交");
        submitButton.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        submitButton.setPreferredSize(new Dimension(100, 30)); // 设置按钮尺寸
        submitButton.addActionListener(e -> {
            String content = contentArea.getText();
            int rating = (int) ratingComboBox.getSelectedItem();

            if (content.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "评论内容不能为空！");
                return;
            }

            try {
                Review review = new Review();
                review.setUserId(userId);
                review.setProductId(productId);
                review.setContent(content);
                review.setRating(rating);
                review.setReviewTime(new Date());

                ReviewDao reviewDao = new ReviewDaoImpl();
                int result = reviewDao.addReview(review);

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "评论提交成功！");
                    dispose(); // 关闭窗口
                } else {
                    JOptionPane.showMessageDialog(this, "评论提交失败！");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "操作失败：" + ex.getMessage());
            }
        });
        buttonPanel.add(submitButton);

        // 将各个面板添加到主面板
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(ratingPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }
}