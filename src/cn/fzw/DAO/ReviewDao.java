package cn.fzw.DAO;

import cn.fzw.model.Review;

import java.util.List;

public interface ReviewDao {
    // 添加评价
    int addReview(Review review) throws Exception;

    // 根据ID查询评价
    Review getReviewById(int reviewId) throws Exception;

    // 查询某个商品的所有评价
    List<Review> getReviewsByProductId(int productId) throws Exception;

    // 查询某个用户的所有评价
    List<Review> getReviewsByUserId(int userId) throws Exception;

    // 更新评价内容
    int updateReview(Review review) throws Exception;

    // 根据ID删除评价
    int deleteReview(int reviewId) throws Exception;

    // 新增方法：删除评论及关联的举报记录
    int deleteReviewWithReports(int reviewId) throws Exception;
}
