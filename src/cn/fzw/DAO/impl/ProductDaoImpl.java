package cn.fzw.DAO.impl;

import cn.fzw.DAO.ProductDao;
import cn.fzw.model.Product;
import cn.fzw.utils.DButils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    // 添加商品
    @Override
    public int addProduct(Product product) throws Exception {
        String sql = "INSERT INTO product (product_name, price, description) VALUES (?, ?, ?)";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getProductName());
            pstmt.setBigDecimal(2, product.getPrice());
            pstmt.setString(3, product.getDescription());
            int affectedRows = pstmt.executeUpdate();

            // 获取自增的主键值
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // 返回生成的product_id
                    }
                }
            }
            return -1; // 插入失败
        }
    }

    // 根据ID查询商品
    @Override
    public Product getProductById(int productId) throws Exception {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
                return null; // 未找到商品
            }
        }
    }

    // 查询所有商品
    @Override
    public List<Product> getAllProducts() throws Exception {
        String sql = "SELECT * FROM product";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    // 更新商品信息
    @Override
    public int updateProduct(Product product) throws Exception {
        String sql = "UPDATE product SET product_name = ?, price = ?, description = ? WHERE product_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getProductName());
            pstmt.setBigDecimal(2, product.getPrice());
            pstmt.setString(3, product.getDescription());
            pstmt.setInt(4, product.getProductId());
            return pstmt.executeUpdate(); // 返回受影响的行数
        }
    }

    // 根据ID删除商品
    @Override
    public int deleteProduct(int productId) throws Exception {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (Connection conn = DButils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            return pstmt.executeUpdate(); // 返回受影响的行数
        }
    }

    // 将ResultSet映射为Product对象
    private Product mapResultSetToProduct(ResultSet rs) throws Exception {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setDescription(rs.getString("description"));
        return product;
    }
}

