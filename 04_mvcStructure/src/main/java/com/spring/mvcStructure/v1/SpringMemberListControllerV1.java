package com.spring.mvcStructure.v1;

import com.spring.mvcStructure.domain.Member;
import com.spring.mvcStructure.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Component
@RequestMapping
public class SpringMemberListControllerV1 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process() {

        List<Member> members = memberRepository.findAll();

        ModelAndView mv = new ModelAndView("members");
        mv.getModel().put("members", members);

        return mv;
    }
}
