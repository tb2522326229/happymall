package com.mall.dao;

import org.apache.ibatis.annotations.Param;

import com.mall.pojo.User;

public interface UserMapper {
	/**
	 * 根据id删除用户
	 * @param id
	 * @return 操作成功后返回的行数
	 */
    int deleteByPrimaryKey(Integer id);
    
    /**
     * 新增用户
     * @param record
     * @return 操作成功后返回的行数
     */
    int insert(User record);
    
    /**
     * 根据属性进行新增
     * @param record
     * @return 操作成功后返回的行数
     */
    int insertSelective(User record);

    /**
     * 根据id查询用户
     * @param id
     * @return 查询成功后返回对应的user
     */
    User selectByPrimaryKey(Integer id);

    /**
     * 修改用户的个别属性
     * @param record
     * @return 操作成功后返回的行数
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 修改用户
     * @param record
     * @return 操作成功后返回的行数
     */
    int updateByPrimaryKey(User record);
    
    /**
     * 检查该用户名是否存在
     * @param username
     * @return 操作成功后返回的行数
     */
    int checkUsername(String username);
    
    /**
     * 用户登录
     * @param username
     * @param password
     * @return 操作成功后返回的行数
     */
    User login(@Param("username")String username,@Param("password")String password);

    /**
     * 检查email是否存在
     * @param email
     * @return 操作成功后返回的行数
     */
	int checkEmail(String email);

	/**
	 * 根据用户名查找密码问题
	 * @param username
	 * @return 该用户设置的问题
	 */
	String selectQuestionByUsername(String username);

	/**
	 * 根据用户名，问题，答案查询是否存在该用户
	 * @param username
	 * @param question
	 * @param answer 
	 * @return 符合条件的用户个数
	 */
	int checkAnswer(@Param("username")String username, @Param("question")String question, @Param("answer")String answer);

	/**
	 * 忘记密码后修改密码
	 * @param username
	 * @param mD5Password
	 * @return
	 */
	int updatePasswordByUsername(@Param("username")String username, @Param("newPassword")String newPassword);

	/**
	 * 查询该id下用户对应的密码
	 * @param oldPassword
	 * @param id
	 * @return
	 */
	int checkPassword(@Param("oldPassword")String oldPassword, @Param("userId")Integer userId);

	/**
	 * 修改用户前判断email是否存在
	 * @param email
	 * @param id
	 * @return
	 */
	int checkEmailByUserId(@Param("email")String email, @Param("userId")Integer id);

}