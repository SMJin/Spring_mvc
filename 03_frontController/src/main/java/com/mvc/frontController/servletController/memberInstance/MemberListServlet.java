package com.mvc.frontController.servletController.memberInstance;

import com.mvc.frontController.domain.Member;
import com.mvc.frontController.domain.MemberRepository;
import com.mvc.frontController.servletController.MemberServletInterface;
import com.mvc.frontController.servletController.MyView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class MemberListServlet implements MemberServletInterface {

    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Member> members = memberRepository.findAll();

        request.setAttribute("members", members);

        return new MyView("/WEB-INF/views/members.jsp");
    }
}
