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
	public void testList(){
		ServerResponse<User> user = userService.login("username", "123456");
		if(user.isSuccess()){
			System.out.println(user.getStatus());
		}
	}
}
