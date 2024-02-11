package com.mvc.frontController.servletController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MemberServletInterface {
    MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
