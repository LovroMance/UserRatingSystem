package cn.fzw;

import cn.fzw.DAO.ProductDao;
import cn.fzw.DAO.UserDao;
import cn.fzw.DAO.impl.ProductDaoImpl;
import cn.fzw.DAO.impl.UserDaoImpl;
import cn.fzw.GUI.LoginFrame;
import cn.fzw.model.Product;
import cn.fzw.model.User;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // 创建主界面
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame().setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        List<User> users = new ArrayList<>();
//        List<Product> products = new ArrayList<>();
//
        UserDao userDao = new UserDaoImpl();
//        ProductDao productDao = new ProductDaoImpl();
//
//        //添加用户数据
//        users.add(new User(1,"zhangsan","123456","张三",false));
//        users.add(new User(2,"lisi","123456","李四",false));
//        users.add(new User(3,"wangwu","123456","王五",false));
//        users.add(new User(4,"admin","123456","管理员",true));
//
//        // 添加商品数据
//        products.add(new Product(1, "智能手表", new BigDecimal("599.99"),
//                "支持心率监测、运动追踪、消息提醒等功能，适合运动爱好者和商务人士。"));
//        products.add(new Product(2, "无线蓝牙耳机", new BigDecimal("299.99"),
//                "高保真音质，降噪功能强大，续航时间长达 20 小时，适合通勤和运动使用。"));
//        products.add(new Product(3, "便携式咖啡机", new BigDecimal("199.99"),
//                "小巧轻便，支持多种咖啡冲泡方式，适合办公室和家庭使用。"));
//        products.add(new Product(4, "智能扫地机器人", new BigDecimal("1299.99"),
//                "自动规划清扫路径，支持手机远程控制，适合家庭清洁。"));
//        products.add(new Product(5, "4K 超高清电视", new BigDecimal("4999.99"),
//                "55 英寸大屏幕，支持 HDR 显示，适合家庭影院体验。"));
//        products.add(new Product(6, "游戏笔记本电脑", new BigDecimal("8999.99"),
//                "高性能显卡，16GB 内存，1TB SSD，适合游戏玩家和专业设计师。"));
//        products.add(new Product(7, "智能空气净化器", new BigDecimal("899.99"),
//                "高效过滤 PM2.5 和甲醛，支持手机 APP 控制，适合家庭和办公室。"));
//        products.add(new Product(8, "电动牙刷", new BigDecimal("199.99"),
//                "高频震动，多种刷牙模式，适合日常口腔护理。"));
//        products.add(new Product(9, "智能门锁", new BigDecimal("699.99"),
//                "支持指纹、密码、手机 APP 开锁，适合家庭和公寓使用。"));
//        products.add(new Product(10, "无线充电器", new BigDecimal("99.99"),
//                "支持 Qi 无线充电协议，兼容多种手机型号，适合桌面使用。"));
//        products.add(new Product(11, "智能音箱", new BigDecimal("399.99"),
//                "支持语音助手，可播放音乐、控制智能家居设备，适合家庭娱乐。"));
//        products.add(new Product(12, "便携式投影仪", new BigDecimal("799.99"),
//                "支持 1080P 高清投影，内置电池，适合户外和家庭影院。"));
//        products.add(new Product(13, "智能体重秤", new BigDecimal("149.99"),
//                "支持体脂率、肌肉量等多项健康数据测量，适合健身爱好者。"));
//        products.add(new Product(14, "电动滑板车", new BigDecimal("1999.99"),
//                "续航里程 30 公里，最高时速 25km/h，适合城市短途通勤。"));
//        products.add(new Product(15, "智能窗帘电机", new BigDecimal("299.99"),
//                "支持手机 APP 和语音控制，定时开关窗帘，适合智能家居场景。"));
//
//        //将products中的数据传入数据库 (*)
//        for (int i = 0; i < products.toArray().length; i++) {
//            productDao.addProduct(products.get(i));
//        }
//        //将users中的数据传入数据库 (*)
//        for (int i = 0; i < users.toArray().length; i++) {
//            userDao.addUser(users.get(i));
//        }

    }
}
