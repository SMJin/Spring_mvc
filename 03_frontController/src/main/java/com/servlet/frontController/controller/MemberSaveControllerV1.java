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
import org.springframework.boot.Banner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MemberSaveControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.put("member", member);
        return "save";
    }
}
