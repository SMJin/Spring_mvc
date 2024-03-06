package com.spring.mvcStructure.v3;

import com.spring.mvcStructure.domain.Member;
import com.spring.mvcStructure.domain.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

/*    @RequestMapping(value = "/new-form", method = RequestMethod.GET)*/
    @GetMapping("/new-form")
    public String newForm() {
        return "new-form";
    }

/*    @RequestMapping(method = RequestMethod.GET)*/
    @GetMapping
    public String members(Model model) {

        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);

        return "members";
    }

/*    @RequestMapping(name = "/save", method = RequestMethod.POST)*/
    @PostMapping("/save")
    public String save(
            @RequestParam("username") String username,
            @RequestParam("age") int age,
            Model model
    ) {

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save";
    }
}
