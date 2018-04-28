package com.mall.controller.backend;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.commom.Const;
import com.mall.commom.ResponseCode;
import com.mall.commom.ServerResponse;
import com.mall.pojo.Category;
import com.mall.pojo.User;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;

@Controller
@RequestMapping("/manage/category")
public class CategoryManagerController {
	@Autowired
	private IUserService userService;
	@Autowired
	private ICategoryService categoryService;
	
	/**
	 * 添加品类
	 * @param categoryName
	 * @param parentId 品类id，如果不写，默认是父品类
	 * @param session
	 * @return
	 */
	@RequestMapping(value="add_category.do",method = RequestMethod.POST)
    @ResponseBody
	public ServerResponse<String> addCategory(String categoryName,@RequestParam(value = "parentId",defaultValue = "0")Integer parentId,HttpSession session){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"请重新登陆");
		}
		if(userService.checkAdminRole(user).isSuccess()){
			// 添加操作
			return categoryService.addCategory(categoryName, parentId);
		}else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
	}
	
	/**
	 * 修改品类名
	 * @param categoryName
	 * @param categoryId
	 * @param session
	 * @return
	 */
	@RequestMapping(value="update_category_name.do",method = RequestMethod.POST)
    @ResponseBody
	public ServerResponse<String> updateCategory(String categoryName,Integer categoryId,HttpSession session){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"请重新登陆");
		}
		if(userService.checkAdminRole(user).isSuccess()){
			// 添加操作
			return categoryService.updateCategoryName(categoryName, categoryId);
		}else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
	}
	
	/**
	 * 查询该节点下的子节点的category信息，不递归，只查询下面平级的节点
	 * @param categoryId
	 * @param session
	 * @return
	 */
	@RequestMapping(value="get_category.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<List<Category>> getChildrenParallelCategory(@RequestParam(value = "parentId",defaultValue = "0")Integer categoryId,HttpSession session){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"请重新登陆");
		}
		if(userService.checkAdminRole(user).isSuccess()){
			// 添加操作
			return categoryService.getChildrenParallelCategory(categoryId);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
		}
	}
	
	/**
	 * 通过递归方法查询该节点以及下面所有的子节点
	 * @param session
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("get_deep_category.do")
    @ResponseBody
	public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"请重新登陆");
		}
		if(userService.checkAdminRole(user).isSuccess()){
			// 添加操作
			return categoryService.selectCategoryChildrenByParentId(categoryId);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
		}
	}
}
