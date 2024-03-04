package com.servlet.frontController.controller;

import com.servlet.frontController.ControllerV1;
import com.servlet.frontController.ModelView;
import com.servlet.frontController.MyView;
import com.servlet.frontController.domain.Member;
import com.servlet.frontController.domain.MemberRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberListControllerV1 implements ControllerV1 {

    MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        model.put("members", memberRepository.findAll());
        return "members";
    }
}
