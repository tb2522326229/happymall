package com.mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall.commom.Const;
import com.mall.commom.ResponseCode;
import com.mall.commom.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.ICategoryService;
import com.mall.service.IProductService;
import com.mall.util.DateTimeUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;
@Service("productService")
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private ICategoryService categoryService;
	@Override
	public ServerResponse<String> saveOrUpdateProduct(Product product) {
		if(product != null){
			if(StringUtils.isNotBlank(product.getSubImages())){
				String[] imageName = product.getSubImages().split(",");
				if(imageName.length > 0){
					product.setMainImage(imageName[0]);
				}
			}
			Product product2 = productMapper.selectByPrimaryKey(product.getId());// 根据前台传过来的product的id查询数据库中是否已经有了该商品
			if(product2.getId() != null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            }else{
                int rowCount = productMapper.insert(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
		}
		return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
	}
	
	@Override
	public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
		if(productId == null || status == null){
			return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
	}

	@Override
	public ServerResponse<ProductDetailVo> getDetail(Integer productId) {
		if(productId == null){
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
	}

	private ProductDetailVo assembleProductDetailVo(Product product) {
		ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        // 因为mybaits从数据库中查出来后是带有毫秒数，不利于阅读，所以把获取到的时间转换为自己定义格式的时间后输入到前台页面
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
	}

	@Override
	public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
		List<Product> productList = productMapper.selectProductList();
		List<ProductListVo> productListVO = Lists.newArrayList();
		for (Product product : productList) {
			ProductListVo productListVo = assembleProductListVo(product);
			productListVO.add(productListVo);
		}
		// 设置页数很每页显示数量
		PageHelper.startPage(pageNum, pageSize);
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVO);
		return ServerResponse.createBySuccess(pageResult);
	}
	
	private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

	@Override
	public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
		if(StringUtils.isNotBlank(productName)){
			productName = new StringBuilder().append("%").append(productName).append("%").toString();// 模糊查询
		}
		List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
		List<ProductListVo> productListVO = Lists.newArrayList();
		for (Product product : productList) {
			ProductListVo productListVo = assembleProductListVo(product);
			productListVO.add(productListVo);
		}
		PageHelper.startPage(pageNum, pageSize);
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVO);
		return ServerResponse.createBySuccess(pageResult);
	}

	@Override
	public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
		if(productId == null){
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
        	return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
	}

	@Override
	public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
		if(StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
		
		// 用于封装传入categoryId下所有的子菜单
		List<Integer> categoryIdList = new ArrayList<Integer>();
		if(categoryId != null){
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if(category == null && StringUtils.isBlank(keyword)){
				//没有该分类,并且还没有关键字,这个时候返回一个空的商品结果集,不报错
				PageHelper.startPage(pageNum, pageSize);
				List<ProductListVo> productListVO = Lists.newArrayList();
				PageInfo pageInfo = new PageInfo(productListVO);
				return ServerResponse.createBySuccess(pageInfo);
			}else{
				// 通过递归操作查询该分类下的所有子分类
				categoryIdList = categoryService.selectCategoryChildrenByParentId(categoryId).getData();
			}
		}
		
		// 拼接关键字查询
		if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
		// 处理排序问题
		if(StringUtils.isNotBlank(orderBy)){
			if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
				String[] order_by = orderBy.split("_");
				PageHelper.orderBy(order_by[0]+" "+order_by[1]);
			}
		}
		// 查询所有子商品
		PageHelper.startPage(pageNum,pageSize);
		List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product product : productList) {
			ProductListVo productListVo = assembleProductListVo(product);
			productListVoList.add(productListVo);
		}
		PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
	}
}
