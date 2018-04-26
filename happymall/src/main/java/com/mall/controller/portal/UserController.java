package com.mall.controller.portal;

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

@Controller
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private IUserService userService;
	/**
	 * 登录操作
	 * @param username 
	 * @param password
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession httpSession){
		ServerResponse<User> serverResponse = userService.login(username, password);
		if(serverResponse.isSuccess()){
			httpSession.setAttribute(Const.CURRENT_USER,serverResponse.getData());
		}
		return serverResponse;
	}
	
	/**
	 * 注销操作
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value="logout.do",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> logout(HttpSession httpSession){
		httpSession.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}
	
	/**
	 * 注册操作
	 * @param user
	 * @return
	 */
	@RequestMapping(value="register.do",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> register(User user){
		return userService.register(user);
	}
	
	/**
	 * 校验用户名/email是否存在
	 * @param str 传入的值
	 * @param type 传入的类型是用户名还是密码
	 * @return
	 */
	@RequestMapping(value="check_valid.do",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type){
		return userService.checkValid(str,type);
	}
	
	/**
	 * 获取当前用户的信息
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value="get_user_info.do",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> getUserinfo(HttpSession httpSession){
		User user = (User)httpSession.getAttribute(Const.CURRENT_USER);
		if(user != null){
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获取信息");
	}
	
	/**
	 * 忘记密码，需要通过问题找回密码或重设密码的操作
	 * @param username
	 * @return
	 */
	@RequestMapping(value="forget_get_question.do",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> getQuestionByUsername(String username){
		return userService.getQuestionByUsername(username);
	}
	
	/**
	 * 校验该用户的答案是否正确
	 * @param username
	 * @param question
	 * @param answer
	 * @return
	 */
	@RequestMapping(value="forget_check_answer.do",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> checkAnswer(String username,String question,String answer){
		return userService.checkAnswer(username,question,answer);
	}
	
	
}
