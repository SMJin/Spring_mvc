package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
//    public String home() {
//        return "home";
//    }


//    @GetMapping("/")
//    public String home(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
//
//        if (memberId == null) {
//            return "home";
//        }
//
//        Member loginMember = memberRepository.findById(memberId);
//        if (loginMember == null) {
//            return "home";
//        }
//
//        model.addAttribute("member", loginMember);
//
//        return "loginHome";
//    }

//    @GetMapping("/")
//    public String homeLoginV2(HttpServletRequest request, Model model) {
//
//        Member loginMember = (Member) sessionManager.getSession(request);
//
//        if (loginMember == null) {
//            return "home";
//        }
//
//        model.addAttribute("member", loginMember);
//
//        return "loginHome";
//    }

    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        // 로그인 사용자가 없을 때도 세션이 생성되면 안되기 때문에 create 파라미터는 false로 지정
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);

        return "loginHome";
    }


}