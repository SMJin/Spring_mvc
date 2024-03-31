# 로그인 2 - 필터, 인터셉터

##### 필터? 인터셉터?
- 필터는 Servlet이 제공하는 기능이며,
- 인터셉터는 Spring이 제공하는 기능이다.
- 둘은 비슷하게 공통 관심사를 처리하는 역할을 한다.
- 물론 AOP를 사용해도 되지만, 웹과 관련된 공통 관심사를 처리할 때는 HTTP 헤더나 URL 정보들이 필요한데, 서블릿 필터나 스프링 인터셉터는 ***HttpServletRequest***을 제공한다.

## 필터란 ? 서블릿이 지원하는 수문장.
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
1. 첫번째 방법: 필터 클래스에 @WebFilter 어노테이션 사용
- 따로 Config나 web.xml에 등록 없이 사용할 수 있다.
- 다만, Filter의 순서를 적용하기 힘들다.
2. 두번째 방법: Filter interface 상속받기
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

## 스프링 MVC가 제공하는 인터셉터란?
- 스프링 인터셉터 흐름 :: HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러
> 인터셉터의 경우 시작은 디스패쳐 서블릿 이후이다. 필터와 마찬가지로 제한과 체인이 가능하다.
```java
public interface HandlerInterceptor {
    default boolean preHandle( HttpServletRequest request,
                               HttpServletResponse response, 
                               Object handler
                              ) throws Exception {}

    default void postHandle( HttpServletRequest request, 
                             HttpServletResponse response, 
                             Object handler, 
                             @Nullable ModelAndView modelAndView
                           ) throws Exception {}

    default void afterCompletion( HttpServletRequest request,
                                  HttpServletResponse response, 
                                  Object handler, 
                                  @Nullable Exception ex
                                ) throws Exception {}
}
```
- 인터셉터는 호출이 *단계적으로 잘 세분화*되어 분리돼 있으며, 어떤 ***컨트롤러(handler)가 호출***되는지 호출 정보와 어떤 ***modelAndView가 반환***되는지 응답 정보까지 받을 수 있다.
- > preHandle: 컨트롤러 호출 전
- > postHandle: 컨트롤러 호출 후 (컨트롤러에서 예외가 발생하면 호출되지 않는다.)
- > afterCompletion: 요청 완료 이후 (뷰가 렌더링 된 이후, 컨트롤러에서 예외가 발생해도 호출된다.)

##### 필터에서 handler 정보 뽑아보는 법
- HandlerMethod 에 담아서 확인이 가능하다.
```java
// @RequestMapping: HandlerMethod
// 정적 리소스 : ResourceHttpHandlerMethod
if (handler instanceof HandlerMethod) {
    HandlerMethod hm = (HandlerMethod) handler; // 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
}
```

##### 인터셉터의 등록 방법!
1. HandlerInterceptor interface 상속받기
2. preHandle, postHandle, afterCompleate 작성해준다.
3. WebMvcConfigurer interface를 상속받은 @Configuration 클래스에 등록해주어야 한다.
4. 등록하는 방법은 void addInterceptors(InterceptorRegistry registry) 메서드를 사용하면 된다.
5. addInterceptors 메서드 안에서 registry.addIntercepter를 이용해서 계속 인터셉터를 추가할 수 있다. 예시는 다음과 같다.
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error"
                );
    }
```

##### 인터셉터의 PathPatter (대표적인 것만 예시)
- ? 한 문자 일치
> ex. /pages/t?st.html
- * 경로(/) 안에서 0개 이상의 문자 일치
> ex. /resources/*.png
- ** 경로 끝까지 0개 이상의 경로(/) 일치
> ex. /resources/**
- 경로(/) 안에서 모든 path를 가져다가 변수명 "path"에 저장
> /resources/{*path}
##### 인터셉터가 좋은 이유?
- 서블릿 필터에 비해 코드가 상당히 간결하다.
- chain.doFilter를 매번 계속 호출해주어야 체인 연결이 되는 Filter와 다르게 그럴 필요가 없다.
- 인터셉터를 적용하지 않을 부분은 excludePathPatterns를 이용해서 쉽게 할 수 있다. (whitelist 따로 만들 필요가 없다.)
## 부록. ArgumentResolver 활용.
##### ArgumentResolver 란?
- 자자.. 복습을 해봅시다. Controller 에서 파라미터들을 처리해주는 게 바로 ArgumentResolver 였습니다.
> 이때 파라미터(argument)의 종류는 다양하겠죠. HttpServletRequest 부터 시작해서 @ModelAttribute 등등등 ...
- RequestMappingHandlerAdaptor, 즉 @ReqeustMapping 핸들러 어댑터는 ArgumentResolver를 호출해서 컨트롤러(핸들러)가 필요한 argument들을 준비해서 넘긴다.
> ArgumentResolver, 정확하게는 HandlerMethodArgumentResolver interface에는 다양한 resolveArgument 객체를 가지고 있다.
```java
public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(MethodParameter var1);
    @Nullable
    Object resolveArgument(MethodParameter var1, @Nullable ModelAndViewContainer var2, NativeWebRequest var3, @Nullable WebDataBinderFactory var4) throws Exception;
}
```
##### ArgumentResolver 동작방식
1. ArgumentResolver 의 supportsParameter() 를 호출
2. 해당 파라미터를 지원하는지 체크
3. 지원하면 resolveArgument() 를 호출해서 실제 객체를 생성
4. 생성된 객체 컨트롤러 호출시 넘기기
##### 참고. ReturnValueHandler
- HandlerMethodReturnValueHandler 를 줄여서 ReturnValueHandler 라 부른다.
- ArgumentResolver 와 비슷한데, 이것은 ***응답 값***을 변환하고 처리한다.
### 나만의 Custom ArgumentResolver 만들기.
##### 1. Custom Annotation 만들기
```java
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {}
```
- 먼저 Annotation을 만들어준다.
- 해당 어노테이션을 사용할 곳에  사용하는 방법은 다음과 같다.
```java
public void method(@Login Member loginMember) { ... }
```
- 그냥 어노테이션과 함께 원하는 argument를 호출해주면 된다.
2. HandlerMethodArgumentResolver, 즉 스프링이 읽어올 수 있게 **ArgumentResolver로 등록**해주어야 한다.
```java
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        log.info("supportsParameter 실행");
        boolean hasLoginAnnotation = methodParameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = Member.class.isAssignableFrom(methodParameter.getParameterType());
        return hasLoginAnnotation && hasMemberType;
    }
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        log.info("resolveArgument");
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
```
- 먼저 HandlerMethodArgumentResolver 를 implements 해주어야 한다.
- supprotsParameter(MethodParameter) 메소드 내에서는 내가 원하는 파라미터가 들어왔는지 확인해주는 작업을 한다.
- > 여기서 아까 내가 만든 @Login 어노테이션이 맞는지 확인해주어야 한다.
- > 그 뿐만이 아니라, 내가 원하는 타입의 파라미터가 들어왔는지 확인해주어야 한다.
- resolveArgument(MethodParameter, ...) 메소드 내에서는 들어온 파라미터가 어떤 형식(Object 지정)으로 반환되었으면 좋겠는지 작성하고 return 해주면 된다.
3. WebConfig에 등록해주어야 한다.
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
```
- addArgumentResolvers 메소드를 상속받아서 List<HandlerMethodArgumentResolver> resolvers 리스트에 내가 만든 리졸버를 추가해 주어야 한다.



###### 보충학습 [스프링이 제공하는 다양한 유틸성 클래스들(StringUtils 등)](https://mangkyu.tistory.com/238)
