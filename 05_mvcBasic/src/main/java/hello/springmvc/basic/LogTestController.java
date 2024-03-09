package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogTestController {
//    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name = " + name);

        // 각 로그별로 레벨이 다르다
        // (낮은 레벨) trace -> debug -> info -> warn -> error (높은 레벨)
        log.trace("trace log={}", name);    //
        log.debug("debug log={}", name);    // debug - 개발서버같은 곳
        log.info("  info log={}", name);    // 중요한 정보
        log.warn("  warn log={}", name);    // 경고
        log.error("error log={}", name);    // error

        return "ok";
    }
}
