package com.mall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mall.pojo.Category;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

	List<Category> selectCategoryChildrenByParentId(Integer categoryId);
}