package com.mall.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.mall.commom.ServerResponse;
import com.mall.vo.OrderVo;

public interface IOrderService {

	/**
	 * 付款操作
	 * @param userId
	 * @param orderNo
	 * @param path
	 * @return
	 */
	ServerResponse pay(Integer userId, Long orderNo,String path);

	/**
	 * 支付宝回调函数
	 * @param params
	 * @return
	 */
	ServerResponse aliCallback(Map<String, String> params);

	/**
	 * 查询支付状态
	 * @param userId
	 * @param orderNo 订单号
	 * @return
	 */
	ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

	/**
	 * 生成订单
	 * @param userId
	 * @param shippingId 地址id
	 * @return
	 */
	ServerResponse createOrder(Integer userId, Integer shippingId);

	/**
	 * 取消订单
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	ServerResponse cancel(Integer userId, Long orderNo);

	/**
	 * 展示购物车中的信息
	 * @param id
	 * @return
	 */
	ServerResponse getOrderCartProduct(Integer userId);

	/**
	 * 订单详情页
	 * @param id
	 * @param orderNo
	 * @return
	 */
	ServerResponse getOrderDetail(Integer userId, Long orderNo);

	/**
	 * 个人中心查看我的订单
	 * @param id
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse getOrderList(Integer id, int pageNum, int pageSize);

	/**
	 * 后台管理员查看订单信息
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo> manageList(int pageNum, int pageSize);
	
	/**
	 * 后台管理员查看订单列表
	 * @param orderNo
	 * @return
	 */
	ServerResponse<OrderVo> manageDetail(Long orderNo);
	
	/**
	 * 后台管理员搜索订单
	 * @param orderNo
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);
	
	/**
	 * 发货操作
	 * @param orderNo
	 * @return
	 */
	ServerResponse<String> manageSendGoods(Long orderNo);

}
