package com.mall.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mall.commom.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Category;
import com.mall.service.ICategoryService;
@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private CategoryMapper categoryMapper;
	@Override
	public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
		if(parentId == null || StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMessage("添加品类参数错误");
		}
		Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的
        int resultCount = categoryMapper.insert(category);
        if(resultCount > 0){
        	return ServerResponse.createBySuccess("添加品类成功");
        }
		return ServerResponse.createByErrorMessage("添加品类失败");
	}
	@Override
	public ServerResponse<String> updateCategoryName(String categoryName, Integer categoryId) {
		if(categoryId == null || StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMessage("添加品类参数错误");
		}
		Category category = new Category();
        category.setName(categoryName);
        category.setParentId(categoryId);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount > 0){
        	return ServerResponse.createBySuccess("修改品类名成功");
        }
		return ServerResponse.createByErrorMessage("修改品类名失败");
	}
	@Override
	public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		if(!CollectionUtils.isEmpty(categoryList)){
			return ServerResponse.createBySuccess(categoryList);
		}
		return ServerResponse.createByErrorMessage("未找到当前子分类");
	}
	@Override
	public ServerResponse<List<Integer>> selectCategoryChildrenByParentId(Integer categoryId) {
		Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        // 获取所有子节点+孙子节点的categoryId
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
	}

	 //递归算法,根据父节点的categoryId查询出子节点，再通过查出的子节点的categoryId查出子节点下的孙子节点
    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId){
    	// 根据父节点的categoryId查询出子节点
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        //查找子节点,递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categoryItem : categoryList){
        	// 通过查出的子节点的categoryId查出子节点下的孙子节点
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
