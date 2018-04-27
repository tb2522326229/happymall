package com.mall.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mall.commom.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class UserTest {
	@Autowired
	private IUserService userService;
	
	@Test
	public void testLogin(){
		ServerResponse<User> user = userService.login("username", "123456");
		if(user.isSuccess()){
			System.out.println(user.getStatus());
		}
	}
	
	
	@Test
	public void testList(){
		ServerResponse<User> user = userService.getInformation(1);
		if(user.isSuccess()){
			System.out.println(user.getStatus());
		}
	}
	
	@Test
	public void testAdd(){
		User user = new User();
		user.setUsername("user");
		user.setPassword("123456");
		user.setEmail("123@qq.com");
		ServerResponse<String> register = userService.register(user);
		System.out.println(register.getMes());
	}
}
