package cn.fzw.model;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private String productName;
    private BigDecimal price;
    private String description;

    // 全参数构造函数
    public Product(int productId, String productName, BigDecimal price, String description) {
        this.productId = productId;
        setProductName(productName);
        setPrice(price);
        setDescription(description);
    }

    // 无参构造函数
    public Product() {}

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) >= 0) { // 检查价格是否非负
            this.price = price;
        } else {
            throw new IllegalArgumentException("Invalid price value. Must be non-negative.");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
