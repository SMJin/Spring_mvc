package com.mvc.frontController.servletController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public interface MemberServletInterface {
    ModelView process(Map<String, String> paramMap) throws ServletException, IOException;
}
