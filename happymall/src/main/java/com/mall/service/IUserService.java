package com.mall.service;

import com.mall.commom.ServerResponse;
import com.mall.pojo.User;

public interface IUserService {
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	public ServerResponse<User> login(String username,String password);

	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public ServerResponse<String> register(User user);

	/**
	 * 判断用户名和密码是否已经存在
	 * @param str 输入的值
	 * @param type 判断的类型是username还是email
	 * @return
	 */
	public ServerResponse<String> checkValid(String str, String type);

	/**
	 * 根据用户名获取自己定义的修改密码问题
	 * @param username
	 * @return
	 */
	public ServerResponse<String> getQuestionByUsername(String username);

	/**
	 * 根据用户名，问题，答案验证答案是否正确
	 * @param username
	 * @param question
	 * @param answer
	 * @return
	 */
	public ServerResponse<String> checkAnswer(String username, String question, String answer);
}
