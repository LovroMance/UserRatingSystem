package cn.fzw.GUI;

import cn.fzw.DAO.UserDao;
import cn.fzw.DAO.impl.UserDaoImpl;
import cn.fzw.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProfileFrame extends JFrame {
    private User currentUser;
    private JComboBox<Integer> genderComboBox;
    private JTextField nicknameField;
    private JTextArea signatureArea;
    private boolean isAdmin;

    public UserProfileFrame(User user) {
        this.currentUser = user;
        this.isAdmin = user.isAdmin(); // 获取管理员状态
        initUI();
        loadUserData();
    }

    private void initUI() {
        setTitle("个人信息管理");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 主面板
        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 用户名（不可编辑）
        mainPanel.add(new JLabel("用户名："));
        JTextField usernameField = new JTextField(currentUser.getUsername());
        usernameField.setEditable(false);
        mainPanel.add(usernameField);

        // 昵称
        mainPanel.add(new JLabel("昵称："));
        nicknameField = new JTextField();
        mainPanel.add(nicknameField);

        // 性别
        mainPanel.add(new JLabel("性别："));
        genderComboBox = new JComboBox<>(new Integer[]{0, 1, 2});
        genderComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                switch ((Integer) value) {
                    case 0: setText("未知"); break;
                    case 1: setText("男"); break;
                    case 2: setText("女"); break;
                }
                return this;
            }
        });
        mainPanel.add(genderComboBox);

        // 个性签名
        mainPanel.add(new JLabel("个性签名："));
        signatureArea = new JTextArea();
        signatureArea.setLineWrap(true);
        mainPanel.add(new JScrollPane(signatureArea));

        // 统计信息（只读）
        mainPanel.add(new JLabel("获赞数："));
        JTextField likeCountField = new JTextField(String.valueOf(currentUser.getLikeCount()));
        likeCountField.setEditable(false);
        mainPanel.add(likeCountField);

        mainPanel.add(new JLabel("被举报次数："));
        JTextField reportCountField = new JTextField(String.valueOf(currentUser.getReportCount()));
        reportCountField.setEditable(false);
        mainPanel.add(reportCountField);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("保存修改");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUserData();
            }
        });
        buttonPanel.add(saveButton);

        // 管理员专属功能：举报记录查看
        if (isAdmin) {
            JButton reportManageButton = new JButton("查看举报记录");
            reportManageButton.addActionListener(e -> {
                new AdminReportFrame().setVisible(true); // 打开举报管理界面
            });
            buttonPanel.add(reportManageButton);
        }

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadUserData() {
        nicknameField.setText(currentUser.getNickname());
        genderComboBox.setSelectedItem(currentUser.getGender());
        signatureArea.setText(currentUser.getSignature());
    }

    private void saveUserData() {
        try {
            currentUser.setNickname(nicknameField.getText());
            currentUser.setGender((Integer) genderComboBox.getSelectedItem());
            currentUser.setSignature(signatureArea.getText());

            UserDao userDao = new UserDaoImpl();
            int result = userDao.updateUser(currentUser);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "信息更新成功！");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "信息更新失败！");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "操作失败：" + e.getMessage());
        }
    }
}