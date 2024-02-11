package com.mvc.frontController.servletController.memberInstance;

import com.mvc.frontController.domain.Member;
import com.mvc.frontController.domain.MemberRepository;
import com.mvc.frontController.servletController.MemberServletInterface;

import java.util.Map;

public class MemberSaveServlet implements MemberServletInterface {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.put("member", member);

        return "save-result";
//        ModelView mv = new ModelView("save-result");
//        mv.getModel().put("member", member);
//        return mv;
    }
}
