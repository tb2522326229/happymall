package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.commom.ServerResponse;
import com.mall.pojo.Product;
import com.mall.vo.ProductDetailVo;

public interface IProductService {
	/**
	 * 根据产品id的存在与否来确定是新增或修改商品
	 * @param product
	 * @return
	 */
	public ServerResponse<String> saveOrUpdateProduct(Product product);

	/**
	 * 修改产品销售状态（是否成功出售）
	 * @param productId
	 * @param status
	 * @return
	 */
	public ServerResponse<String> setSaleStatus(Integer productId, Integer status);

	/**
	 * 获取商品详细信息
	 * @param productId
	 * @return
	 */
	public ServerResponse<ProductDetailVo> getDetail(Integer productId);

	/**
	 * 前台搜索所有商品
	 * @param pageNum 当前页码
	 * @param pageSize 每页显示的数量
	 * @return
	 */
	public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

	/**
	 * 后台搜索所有商品
	 * @param productName 商品名
	 * @param productId 商品id
	 * @param pageNum 当前页码
	 * @param pageSize 每页显示的数量
	 * @return
	 */
	public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

	/**
	 * 前台搜索所有商品
	 * @param productId 商品id
	 * @return
	 */
	public ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

	/**
	 * 前台用户搜索时的请求
	 * @param keyword
	 * @param categoryId
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */
	public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum,
			int pageSize, String orderBy);
}
