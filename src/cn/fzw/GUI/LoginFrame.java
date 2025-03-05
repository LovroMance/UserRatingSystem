package cn.fzw.GUI;

import cn.fzw.DAO.UserDao;
import cn.fzw.DAO.impl.UserDaoImpl;
import cn.fzw.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() throws Exception{
        setTitle("电子商城用户评价系统");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        // 创建面板
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("账号:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("登录");
        panel.add(loginButton);

        registerButton = new JButton("注册");
        panel.add(registerButton);

        // 添加面板到窗口
        add(panel);

        // 登录按钮事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                UserDao userDao = new UserDaoImpl();
                try {
                    User user = userDao.getUserByUsername(username);
                    if (user != null && user.getPassword().equals(password)) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "登录成功！");
                        // 传递用户ID到商品列表
                        new ProductListFrame(user.getUserId()).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, "账号或密码错误！");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "数据库连接失败！");
                }
            }
        });

        // 注册按钮事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true); // 打开注册界面
            }
        });

    }
}
