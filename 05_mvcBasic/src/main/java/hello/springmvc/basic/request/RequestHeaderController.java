package hello.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {

//    MultiValueMap 이란, 하나의 키에 여러 값을 받을 수 있는 map이다.
//    공식 문서의 Method Arguments 페이지에서 알수있는 여러가지 헤더 정보를 알 수 있다.
    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie) {
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);  // 언어 ex.ko_KR
        log.info("headerMap={}", headerMap);    // 전체 헤더 출력
        log.info("header host={}", host);   // 호스트 주소
        log.info("myCookie={}", cookie);    // 쿠키 정보
        return "ok";
    }
}
