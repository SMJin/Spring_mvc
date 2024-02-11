package com.mvc.frontController.servletController.memberInstance;

import com.mvc.frontController.servletController.MemberServletInterface;

import java.util.Map;


public class MemberFormServlet implements MemberServletInterface {
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        return "new-form";
    }
}
