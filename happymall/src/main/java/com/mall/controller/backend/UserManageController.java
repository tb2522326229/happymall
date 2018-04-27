package com.mall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.commom.Const;
import com.mall.commom.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;

/**
 * 后台管理员登录的controller
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping(value="login.do",method = RequestMethod.POST)
    @ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession httpSession){
		ServerResponse<User> response = userService.login(username, password);
		if(response.isSuccess()){
			User user = response.getData();
			if(user.getRole() == Const.Role.ROLE_ADMIN){
				httpSession.setAttribute(Const.CURRENT_USER,response.getData());
				return response;
			}else{
				return ServerResponse.createByErrorMessage("不是管理员,无法登录");
			}
		}
		return response;
	}
}
