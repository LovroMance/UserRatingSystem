package cn.fzw.DAO;

import cn.fzw.model.User;

import java.util.List;

public interface UserDao {
    // 添加用户
    int addUser(User user) throws Exception;

    // 根据ID查询用户
    User getUserById(int userId) throws Exception;

    // 根据用户名查询用户
    User getUserByUsername(String username) throws Exception;

    // 查询所有用户
    List<User> getAllUsers() throws Exception;

    // 更新用户信息
    int updateUser(User user) throws Exception;

    // 根据ID删除用户
    int deleteUser(int userId) throws Exception;

    // 获取用户是否为管理员
    boolean isAdmin(int userId) throws Exception;

}
