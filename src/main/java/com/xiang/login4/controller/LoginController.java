package com.xiang.login4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xiang.login4.Service.LoginService;

@Controller
public class LoginController {
	
	@Autowired
	LoginService loginService;
	
	@RequestMapping("/login")
	public String hello() {
		System.out.println("Hello");
		return "redirect:index.html";
	}
	/**
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "login/transfer", method = RequestMethod.POST)
	@ResponseBody
	public String getPublicKey(@RequestParam("user") String user) {
		System.out.println("用户名："+user);
		String result = loginService.getPublicKey();
		System.out.println("公钥："+result);
		return result;
	}
	/**
	 * 
	 * @param user
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value = "login/authpwd", method = RequestMethod.POST)
	@ResponseBody
	public String authpsw(@RequestParam("loginName") String user, @RequestParam("passWord") String pwd) {
		
		try {
			System.out.println("加密后的密码："+pwd);
			String decodePwd = loginService.decodePassWd(pwd);
			System.out.println("解密后的密码："+decodePwd);
			return decodePwd;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}