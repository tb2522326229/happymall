package com.mall.service;

import java.util.List;

import com.mall.commom.ServerResponse;
import com.mall.pojo.Category;

public interface ICategoryService {

	/**
	 * 添加产品分类
	 * @param categoryName 分类名
	 * @param parentId 父节点分类id
	 * @return
	 */
	public ServerResponse<String> addCategory(String categoryName,Integer parentId);
	/**
	 * 修改类别名称
	 * @param categoryName
	 * @param categoryId
	 * @return
	 */
	public ServerResponse<String> updateCategoryName(String categoryName, Integer categoryId);
	/**
	 * 查询该节点下的子节点的category信息，不递归，只查询下面平级的节点
	 * @param categoryId
	 * @return
	 */
	public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
	/**
	 * 通过递归方法查询该节点以及下面所有的子节点id
	 * @param categoryId
	 * @return
	 */
	public ServerResponse<List<Integer>> selectCategoryChildrenByParentId(Integer categoryId);
}
