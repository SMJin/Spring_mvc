package com.mvc.frontController.servletController.memberInstance;

import com.mvc.frontController.domain.Member;
import com.mvc.frontController.domain.MemberRepository;
import com.mvc.frontController.servletController.MemberServletInterface;

import java.util.List;
import java.util.Map;

public class MemberListServlet implements MemberServletInterface {

    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        List<Member> members = memberRepository.findAll();

        model.put("members", members);

        return "members";
//        ModelView mv = new ModelView("members");
//        mv.getModel().put("members", members);
//        return mv;
    }
}
