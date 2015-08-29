package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(
        name = "PostController", 
        urlPatterns = {"/data"}
    )
public class PostController  extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data = request.getParameter("payload");
        
        DataBean dataBean = new DataBean();
        dataBean.setPayload(data);
        
        DataInsertionService.inserData(dataBean);
        
        ServletOutputStream out = response.getOutputStream();
        out.write("Done".getBytes());
        out.flush();
    }
}
