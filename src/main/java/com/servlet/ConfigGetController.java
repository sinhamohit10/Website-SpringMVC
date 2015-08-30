package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.common.lang3.StringUtils;

@SuppressWarnings("serial")
@WebServlet(
		name = "ConfigGetController", 
		urlPatterns = {"/configget"}
		)
public class ConfigGetController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String config = DataInsertionService.findConfig();

		if(StringUtils.isNoneBlank(config)){
			ServletOutputStream out = response.getOutputStream();
			out.write(config.getBytes());
			out.flush();
		}
	}
}
