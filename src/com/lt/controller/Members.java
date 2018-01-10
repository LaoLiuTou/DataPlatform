package com.lt.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lt.utils.JdbcUtils;
@Controller
public class Members {
 
	Logger logger = Logger.getLogger("DipLogger");

	@RequestMapping("/Members") 
	public void operate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8"); 
		String username = request.getParameter("username"); 
		String password = request.getParameter("userpwd"); 
		PrintWriter out = response.getWriter();
		
		String result=JdbcUtils.getToken(username,password);
		out.print(result);
		out.flush();
		out.close();
	}

	 
}
