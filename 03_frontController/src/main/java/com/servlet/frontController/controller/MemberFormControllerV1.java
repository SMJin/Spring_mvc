package com.servlet.frontController.controller;

import com.servlet.frontController.ControllerV1;
import com.servlet.frontController.ModelView;
import com.servlet.frontController.MyView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class MemberFormControllerV1 implements ControllerV1 {
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        return "new-form";
    }
}
