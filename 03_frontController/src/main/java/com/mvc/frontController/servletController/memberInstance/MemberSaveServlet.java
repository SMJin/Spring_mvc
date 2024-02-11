package com.mvc.frontController.servletController.memberInstance;

import com.mvc.frontController.domain.Member;
import com.mvc.frontController.domain.MemberRepository;
import com.mvc.frontController.servletController.MemberServletInterface;
import com.mvc.frontController.servletController.ModelView;
import com.mvc.frontController.servletController.MyView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MemberSaveServlet implements MemberServletInterface {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) throws ServletException, IOException {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        return mv;
    }
}
