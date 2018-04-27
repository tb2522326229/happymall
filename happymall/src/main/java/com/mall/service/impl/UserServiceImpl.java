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
			TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMessage("答案错误");
	}

	@Override
	public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
		if(StringUtils.isBlank(forgetToken)){
			return ServerResponse.createByErrorMessage("参数错误，token为空，需要重新传递");
		}
		ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
		if(!checkValid.isSuccess()){
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
		if(StringUtils.equals(forgetToken, token)){
			String MD5Password = MD5Util.MD5EncodeUtf8(newPassword);
			int returnCount = userMapper.updatePasswordByUsername(username,MD5Password);
			if(returnCount > 0){
				return ServerResponse.createBySuccess("修改密码成功");
			}
		}else{
			return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
		}
		return ServerResponse.createByErrorMessage("密码修改失败");
	}

	@Override
	public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user) {
		//防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
		int count = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
		if(count == 0){
			return ServerResponse.createByErrorMessage("旧密码输入错误");
		}
		user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
		int resultCount = userMapper.updateByPrimaryKeySelective(user);
		if(resultCount > 0){
			return ServerResponse.createBySuccess("修改密码成功");
		}
		return ServerResponse.createByErrorMessage("密码修改失败");
	}

	@Override
	public ServerResponse<User> updateUser(User user) {
		//username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
		int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
		if(resultCount > 0){
            return ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
        }
		User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
	}

	@Override
	public ServerResponse<User> getInformation(Integer id) {
		User user = userMapper.selectByPrimaryKey(id);
		if(user == null){
			return ServerResponse.createByErrorMessage("找不到当前用户");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess(user);
	}

}
