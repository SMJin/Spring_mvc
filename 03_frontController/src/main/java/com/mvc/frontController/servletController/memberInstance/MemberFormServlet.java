package com.mvc.frontController.servletController.memberInstance;

import com.mvc.frontController.servletController.MemberServletInterface;
import com.mvc.frontController.servletController.ModelView;
import com.mvc.frontController.servletController.MyView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class MemberFormServlet implements MemberServletInterface {
    @Override
    public ModelView process(Map<String, String> paramMap) throws ServletException, IOException {
        return new ModelView("new-form");
    }
}
