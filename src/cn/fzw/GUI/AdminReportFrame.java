package cn.fzw.GUI;

import cn.fzw.DAO.ReportDao;
import cn.fzw.DAO.UserDao;
import cn.fzw.DAO.impl.ReportDaoImpl;
import cn.fzw.DAO.impl.UserDaoImpl;
import cn.fzw.model.Report;
import cn.fzw.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminReportFrame extends JFrame {
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public AdminReportFrame() {
        initUI();
        loadReports();
    }

    private void initUI() {
        setTitle("举报记录管理 - 管理员");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 表格模型
        String[] columnNames = {"举报ID", "举报人", "被举报人", "关联评论", "举报理由", "举报时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 禁止编辑表格内容
            }
        };
        reportTable = new JTable(tableModel);

        // 列宽调整
        reportTable.getColumnModel().getColumn(4).setPreferredWidth(300); // 举报理由列加宽

        // 滚动面板
        JScrollPane scrollPane = new JScrollPane(reportTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 刷新按钮
        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadReports());

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadReports() {
        tableModel.setRowCount(0); // 清空表格

        try {
            ReportDao reportDao = new ReportDaoImpl();
            UserDao userDao = new UserDaoImpl();
            List<Report> reports = reportDao.getAllReports();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            for (Report report : reports) {
                // 获取举报人和被举报人名称
                User reporter = userDao.getUserById(report.getUserId());
                User reportedUser = userDao.getUserById(report.getReportedUserId());

                Object[] rowData = {
                        report.getReportId(),
                        reporter != null ? reporter.getNickname() : "未知用户",
                        reportedUser != null ? reportedUser.getNickname() : "未知用户",
                        report.getReviewId() != null ? "评论ID：" + report.getReviewId() : "无关联评论",
                        report.getReportReason(),
                        sdf.format(report.getReportTime()),
                        report.isDeleted() ? "✔" : "✖"
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载失败：" + e.getMessage());
        }
    }
}