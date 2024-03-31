# 로그인 2 - 필터, 인터셉터

##### 필터? 인터셉터?
- 필터는 Servlet이 제공하는 기능이며,
- 인터셉터는 Spring이 제공하는 기능이다.
- 둘은 비슷하게 공통 관심사를 처리하는 역할을 한다.
- 물론 AOP를 사용해도 되지만, 웹과 관련된 공통 관심사를 처리할 때는 HTTP 헤더나 URL 정보들이 필요한데, 서블릿 필터나 스프링 인터셉터는 ***HttpServletRequest***을 제공한다.

##### 필터란 ? 서블릿이 지원하는 수문장.
- 필터 흐름 :: HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러
> 필터를 먼저 호출한 다음에 서블릿이 호출되는 것이다. 필터를 거치면서 요청을 제한할 것인지 판단할 수 있고, 필터 체인도 가능하다. (여러 필터를 거칠수 있음)
```java
public interface Filter {
 public default void init(FilterConfig filterConfig) throws ServletException { ... }
 public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException;
 public default void destroy() {...}
}
```
- 필터 인터페이스를 implements로 상속해 구현하고 등록하면 서블릿 컨테이너가 필터를 싱글톤 객체로 생성하고 관리한다.
- > init(): 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출된다.
- > doFilter(): 고객의 요청이 올 때 마다 해당 메서드가 호출된다. 필터의 로직을 구현하면 된다.
- > destroy(): 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출된다.

##### 필터의 등록 방법!
1. 필터 클래스에 @WebFilter 어노테이션 사용
- 따로 Config나 web.xml에 등록 없이 사용할 수 있다.
- 다만, Filter의 순서를 적용하기 힘들다.
2. Filter interface 상속받기
- @Configuration 클래스에 등록해주어야 한다.
- 등록하는 방법은 FilterRegistrationBean 사용해서 반환해주어야 한다. 예시는 다음과 같다.
```java
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
```
