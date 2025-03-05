package cn.fzw.GUI;

import cn.fzw.DAO.UserDao;
import cn.fzw.DAO.impl.UserDaoImpl;
import cn.fzw.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nicknameField;
    private JButton submitButton;

    public RegisterFrame() {
        setTitle("用户注册");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭时不退出程序
        setLocationRelativeTo(null); // 居中显示

        // 创建面板
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("账号:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("昵称:"));
        nicknameField = new JTextField();
        panel.add(nicknameField);

        submitButton = new JButton("提交");
        panel.add(submitButton);

        // 添加面板到窗口
        add(panel);

        // 提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String nickname = nicknameField.getText();

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setNickname(nickname);

                UserDao userDao = new UserDaoImpl();
                try {
                    int userId = userDao.addUser(user);
                    if (userId != -1) {
                        JOptionPane.showMessageDialog(RegisterFrame.this, "注册成功！");
                        dispose(); // 关闭注册界面
                    } else {
                        JOptionPane.showMessageDialog(RegisterFrame.this, "注册失败！");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(RegisterFrame.this, "数据库连接失败！");
                }
            }
        });
    }
}
