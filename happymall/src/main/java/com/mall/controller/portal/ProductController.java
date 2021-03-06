package com.mall.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.mall.commom.ServerResponse;
import com.mall.service.IProductService;
import com.mall.vo.ProductDetailVo;

@Controller
@RequestMapping("/product/")
public class ProductController {
	
	private IProductService productService;

	 @RequestMapping("detail.do")
	 @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId){
        return productService.getProductDetail(productId);
    }
	 
	 @RequestMapping("list.do")
	    @ResponseBody
	    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
	                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
	                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
	                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
	                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
	        return productService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
	    }
}
