package com.mall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mall.pojo.Cart;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /**
     * 通过用户id和产品id查询购物车中的信息
     * @param userId
     * @param productId
     * @return
     */
    Cart selectCartByUserIdProductId(@Param("userId")Integer userId, @Param("productId")Integer productId);

	List<Cart> selectCartByUserId(Integer userId);

	int selectCartProductCheckedStatusByUserId(Integer userId);

	void deleteByUserIdProductIds(@Param("userId")Integer userId, @Param("productIdList") List<String> productIdList);

	void checkedOrUncheckedProduct(@Param("userId")Integer userId, @Param("productId")Integer productId, @Param("checked")Integer checked);
	
	int selectCartProductCount(@Param("userId") Integer userId);
}