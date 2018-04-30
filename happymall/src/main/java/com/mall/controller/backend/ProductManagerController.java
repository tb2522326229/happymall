package com.mall.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.commom.Const;
import com.mall.commom.ResponseCode;
import com.mall.commom.ServerResponse;
import com.mall.pojo.Product;
import com.mall.pojo.User;
import com.mall.service.IFileService;
import com.mall.service.IProductService;
import com.mall.service.IUserService;
import com.mall.util.PropertiesUtil;
import com.mall.vo.ProductDetailVo;

@Controller
@RequestMapping("/manage/product/")
public class ProductManagerController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IFileService fileService;
	/**
	 * 新增或修改商品
	 * @param session
	 * @param product
	 * @return
	 */
	@RequestMapping(value="save.do",method = RequestMethod.POST)
    @ResponseBody
	public ServerResponse<String> productSave(HttpSession session, Product product){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }if(userService.checkAdminRole(user).isSuccess()){
			// 添加或修改操作
			return productService.saveOrUpdateProduct(product);
		}else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
	}
	
	/**
	 * 修改产品销售状态
	 * @param session
	 * @param productId
	 * @param status
	 * @return
	 */
	@RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse<String> setSaleStatus(HttpSession session, Integer productId,Integer status){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }if(userService.checkAdminRole(user).isSuccess()){
			return productService.setSaleStatus(productId,status);
		}else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
	}
	
	/**
	 * 获取商品详情
	 * @param session
	 * @param productId
	 * @return
	 */
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse<ProductDetailVo> getDetail(HttpSession session, Integer productId){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
		}if(userService.checkAdminRole(user).isSuccess()){
			return productService.getDetail(productId);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
		}
	}
	
	 @RequestMapping("list.do")
	    @ResponseBody
	    public ServerResponse<PageInfo> getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
	        User user = (User)session.getAttribute(Const.CURRENT_USER);
	        if(user == null){
	            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	        }
	        if(userService.checkAdminRole(user).isSuccess()){
	            //填充业务
	            return productService.getProductList(pageNum,pageSize);
	        }else{
	            return ServerResponse.createByErrorMessage("无权限操作");
	        }
	    }
	 
	 @RequestMapping("search.do")
	 @ResponseBody
	 public ServerResponse<PageInfo> productSearch(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
		 if(user == null){
			 return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
		 }
		 if(userService.checkAdminRole(user).isSuccess()){
			 //填充业务
			 return productService.searchProduct(productName,productId,pageNum,pageSize);
		 }else{
			 return ServerResponse.createByErrorMessage("无权限操作");
		 }
	 }
	 
	 @RequestMapping("upload.do")
	 @ResponseBody
	 public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file,HttpServletRequest request){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	        /*if(user == null){
	            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	        }if(userService.checkAdminRole(user).isSuccess()){*/
	        	String path = request.getSession().getServletContext().getRealPath("upload");
	            String targetFileName = fileService.upload(file,path);
	            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
	            Map fileMap = Maps.newHashMap();
	            fileMap.put("uri",targetFileName);
	            fileMap.put("url",url);
	            return ServerResponse.createBySuccess(fileMap);
	      /*  }else{
	            return ServerResponse.createByErrorMessage("无权限操作");
	        }*/
	 }
	 
	 @RequestMapping("richtext_img_upload.do")
	    @ResponseBody
	    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
	        Map resultMap = Maps.newHashMap();
	        User user = (User)session.getAttribute(Const.CURRENT_USER);
	        if(user == null){
	            resultMap.put("success",false);
	            resultMap.put("msg","请登录管理员");
	            return resultMap;
	        }
	        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//	        {
//	            "success": true/false,
//	                "msg": "error message", # optional
//	            "file_path": "[real file path]"
//	        }
	        if(userService.checkAdminRole(user).isSuccess()){
	            String path = request.getSession().getServletContext().getRealPath("upload");
	            String targetFileName = fileService.upload(file,path);
	            if(StringUtils.isBlank(targetFileName)){
	                resultMap.put("success",false);
	                resultMap.put("msg","上传失败");
	                return resultMap;
	            }
	            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
	            resultMap.put("success",true);
	            resultMap.put("msg","上传成功");
	            resultMap.put("file_path",url);
	            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
	            return resultMap;
	        }else{
	            resultMap.put("success",false);
	            resultMap.put("msg","无权限操作");
	            return resultMap;
	        }
	    }
}
