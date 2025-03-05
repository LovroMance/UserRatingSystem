package cn.fzw.DAO;

import cn.fzw.model.Product;

import java.util.List;

public interface ProductDao {
    // 添加商品
    int addProduct(Product product) throws Exception;
    // 根据ID查询商品
    Product getProductById(int productId) throws Exception;

    // 查询所有商品
    List<Product> getAllProducts() throws Exception;

    // 更新商品信息
    int updateProduct(Product product) throws Exception;

    // 根据ID删除商品
    int deleteProduct(int productId) throws Exception;
}
