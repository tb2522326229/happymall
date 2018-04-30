package com.mall.service;

import com.mall.commom.ServerResponse;
import com.mall.vo.CartVo;

public interface ICartService {
	/**
	 * 新增购物车中的商品
	 * @param userId 用户id
	 * @param productId 商品id
	 * @param count 商品数量
	 * @return
	 */
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);
    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);

    ServerResponse<CartVo> list (Integer userId);
    ServerResponse<CartVo> selectOrUnSelect (Integer userId,Integer productId,Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
