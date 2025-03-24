package cn.fzw.GUI;

import cn.fzw.DAO.ProductDao;
import cn.fzw.DAO.UserDao;
import cn.fzw.DAO.impl.ProductDaoImpl;
import cn.fzw.DAO.impl.UserDaoImpl;
import cn.fzw.model.Product;
import cn.fzw.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProductListFrame extends JFrame {
    private int currentUserId;

    public ProductListFrame(int currentUserId) {
        this.currentUserId = currentUserId;
        initUI();
    }

    private void initUI() {
        setTitle("商品列表");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置全局字体
        setUIFont(new javax.swing.plaf.FontUIResource("微软雅黑", Font.PLAIN, 14));

        // 添加菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu userMenu = new JMenu("用户");
        userMenu.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 个人信息菜单项
        JMenuItem profileItem = new JMenuItem("个人信息");
        profileItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        profileItem.addActionListener(e -> {
            try {
                UserDao userDao = new UserDaoImpl();
                User currentUser = userDao.getUserById(currentUserId);
                new UserProfileFrame(currentUser).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "加载用户信息失败！");
            }
        });
        userMenu.add(profileItem);

        // 管理员专属菜单项
        try {
            UserDao userDao = new UserDaoImpl();
            User currentUser = userDao.getUserById(currentUserId);
            if (currentUser.isAdmin()) {
                // 举报管理菜单项
                JMenuItem reportManageItem = new JMenuItem("举报管理");
                reportManageItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                reportManageItem.addActionListener(e -> new AdminReportFrame().setVisible(true));
                userMenu.add(reportManageItem);

                // 查看用户评论菜单项
                JMenuItem viewUserReviewsItem = new JMenuItem("查看用户评论");
                viewUserReviewsItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                viewUserReviewsItem.addActionListener(e -> {
                    // 弹出输入框，输入用户ID
                    String userIdStr = JOptionPane.showInputDialog(this, "请输入用户ID：", "查看用户评论", JOptionPane.PLAIN_MESSAGE);
                    if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                        try {
                            int userId = Integer.parseInt(userIdStr);
                            new UserReviewFrame(userId).setVisible(true); // 打开用户评论页面
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "用户ID必须是数字！");
                        }
                    }
                });
                userMenu.add(viewUserReviewsItem);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载用户信息失败！");
        }

        menuBar.add(userMenu);
        setJMenuBar(menuBar);

        // 主面板使用滚动面板包裹
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 增加内边距
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 商品展示面板
        JPanel productPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // 增加内边距
        productPanel.setBackground(new Color(240, 240, 240)); // 设置背景色

        try {
            ProductDao productDao = new ProductDaoImpl();
            List<Product> products = productDao.getAllProducts();

            for (Product product : products) {
                // 单个商品面板
                JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
                itemPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // 边框颜色
                        BorderFactory.createEmptyBorder(10, 10, 10, 10) // 内边距
                ));
                itemPanel.setBackground(Color.WHITE); // 设置背景色

                // 顶部名称价格
                JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
                topPanel.setBackground(Color.WHITE);

                // 商品名称
                JLabel nameLabel = new JLabel("名称：" + product.getProductName());
                nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 设置字体
                nameLabel.setForeground(new Color(50, 50, 50)); // 设置文字颜色
                topPanel.add(nameLabel);

                // 商品价格
                JLabel priceLabel = new JLabel("价格：¥" + product.getPrice());
                priceLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
                priceLabel.setForeground(new Color(255, 87, 34)); // 橙色
                topPanel.add(priceLabel);

                // 商品描述
                JTextArea descArea = new JTextArea(product.getDescription());
                descArea.setEditable(false);  //不可编辑
                descArea.setLineWrap(true);
                descArea.setWrapStyleWord(true);
                descArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                descArea.setForeground(new Color(80, 80, 80)); // 深灰色文字
                descArea.setBackground(new Color(248, 248, 248)); // 浅灰色背景
                descArea.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1), // 边框颜色
                        BorderFactory.createEmptyBorder(10, 10, 10, 10) // 内边距
                ));

                // 滚动面板
                JScrollPane descScrollPane = new JScrollPane(descArea);
                descScrollPane.setBorder(BorderFactory.createEmptyBorder()); // 去掉滚动面板的默认边框
                descScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                descScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                // 自定义滚动条样式
                JScrollBar verticalScrollBar = descScrollPane.getVerticalScrollBar();
                verticalScrollBar.setBackground(new Color(240, 240, 240)); // 滚动条背景色
                verticalScrollBar.setForeground(new Color(180, 180, 180)); // 滚动条滑块颜色
                verticalScrollBar.setUnitIncrement(16); // 设置滚动速度
                verticalScrollBar.setBorder(BorderFactory.createEmptyBorder()); // 去掉滚动条边框

                // 添加评论按钮
                JButton addReviewButton = new JButton("添加评论");
                addReviewButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
                addReviewButton.setBackground(new Color(33, 150, 243)); // 蓝色背景
                addReviewButton.setForeground(Color.WHITE); // 白色文字
                addReviewButton.setFocusPainted(false); // 去掉焦点边框
                addReviewButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // 内边距
                addReviewButton.addActionListener(e -> {
                    new AddReviewFrame(product.getProductId(), currentUserId).setVisible(true);
                });

                // 布局
                itemPanel.add(topPanel, BorderLayout.NORTH);
                itemPanel.add(descScrollPane, BorderLayout.CENTER);
                itemPanel.add(addReviewButton, BorderLayout.SOUTH);

                // 点击事件传递用户ID
                itemPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        new ReviewFrame(product.getProductId(), currentUserId).setVisible(true);
                    }
                });

                productPanel.add(itemPanel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "商品加载失败：" + e.getMessage());
        }

        scrollPane.setViewportView(productPanel);
    }

    // 设置全局字体
    private static void setUIFont(javax.swing.plaf.FontUIResource font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}
