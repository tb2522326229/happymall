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

	/**
	 * 忘记密码后重置密码
	 * @param username
	 * @param newPassword
	 * @param forgetToken
	 * @return
	 */
	public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);

	/**
	 * 登录状态下修改密码
	 * @param oldPassword
	 * @param newPassword
	 * @param user
	 */
	public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user);

	/**
	 * 修改user
	 * @param user
	 * @return
	 */
	public ServerResponse<User> updateUser(User user);

	/**
	 * 根据id获取用户的详细信息
	 * @param id
	 * @return
	 */
	public ServerResponse<User> getInformation(Integer id);

	/**
	 * 判断是否为管理员
	 * @param user
	 * @return
	 */
	public ServerResponse checkAdminRole(User user);
}
