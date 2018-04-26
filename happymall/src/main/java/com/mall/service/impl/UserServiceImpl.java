package com.mall.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.commom.Const;
import com.mall.commom.ServerResponse;
import com.mall.commom.TokenCache;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.MD5Util;
@Service("userService")
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper userMapper;
	public ServerResponse<User> login(String username, String password) {
		// 检查用户名是否存在
		int userCount = userMapper.checkUsername(username);
		if(userCount == 0){
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		// MD5加密密码
		String md5password = MD5Util.MD5EncodeUtf8(password);
		// 检查密码是否输入错误
		User user = userMapper.login(username, md5password);
		if(user == null){
			return ServerResponse.createByErrorMessage("密码输入错误");
		}
		user.setPassword(StringUtils.EMPTY);
		// 返回
		return ServerResponse.createBySuccess("登陆成功",user);
	}
	
	public ServerResponse<String> register(User user) {
		// 判断用户名是否存在
		ServerResponse<String> checkValid = this.checkValid(user.getUsername(), Const.USERNAME);
		if(!checkValid.isSuccess()){
			return checkValid;
		}
		// 判断email是否存在
		checkValid = this.checkValid(user.getUsername(), Const.EMAIL);
		if(!checkValid.isSuccess()){
			return checkValid;
		}
		user.setRole(Const.Role.ROLE_CUSTOMER);
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int userCount = userMapper.insert(user);
		if(userCount == 0){
			return ServerResponse.createByErrorMessage("注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");
	}

	public ServerResponse<String> checkValid(String str, String type) {
		// 用户已存在
		if(StringUtils.isNotBlank(type)){
			if(type.equals(Const.EMAIL)){
				int userCount = userMapper.checkEmail(str);
				if(userCount != 0){
					return ServerResponse.createByErrorMessage("email已存在");
				}
			}
			
			if(type.equals(Const.USERNAME)){
				int userCount = userMapper.checkUsername(str);
				if(userCount != 0){
					return ServerResponse.createByErrorMessage("用户已存在");
				}
			}
		}else{
			return ServerResponse.createByErrorMessage("输入不正确");
		}
		// 用户不存在
		return ServerResponse.createBySuccessMessage("校验成功");
	}

	public ServerResponse<String> getQuestionByUsername(String username) {
		ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
		if(checkValid.isSuccess()){
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		String question = userMapper.selectQuestionByUsername(username);
		if(question != null){
			return ServerResponse.createBySuccess(question);
		}
		return ServerResponse.createByErrorMessage("您注册时并未设置问题");
	}

	public ServerResponse<String> checkAnswer(String username, String question, String answer) {
		int userCount = userMapper.checkAnswer(username,question,answer);
		if(userCount > 0){// 说明该用户存在并且问题答案都正确
			String forgetToken = UUID.randomUUID().toString();
			TokenCache.setKey("token_" + username, forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMessage("答案错误");
	}

}
