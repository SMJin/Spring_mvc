# 예외처리와 오류 페이지

##### 시작 전, 참고
###### 1. 웹 애플리케이션은 사용자 요청별로 별도의 쓰레드가 할당되고, 서블릿 컨테이너 안에서 실행된다.
###### 2. Whitelabel Error Page (기본 Spring 제공 오류 페이지) 꺼주는 방법
```properties
# application.properties
server.error.whitelabel.enabled=false
```

### 서블릿은 다음 2가지 방식으로 예외 처리를 지원한다.
1. Exception (예외)
```java
throw new RuntimeException("예외 발생!");
```
2. response.sendError(HTTP 상태 코드, 오류 메시지)
```java
response.sendError(404, "404 오류!");
response.sendError(500);
```

### Servlet이 제공하는 오류 화면 기능 : 과거
- 과거에는 web.xml에 오류 페이지를 다음과 같이 등록해주었다.
```xml
<web-app>
    <error-page>
        <error-code>404</error-code>
        <location>/error-page/404.html</location>
    </error-page>
    <error-page>
        <error-code>500</error-code>
        <location>/error-page/500.html</location>
    </error-page>
    <error-page>
        <exception-type>java.lang.RuntimeException</exception-type>
        <location>/error-page/500.html</location>
    </error-page>
</web-app>
```

### Servlet이 제공하는 오류 화면 기능 : 현재
**1. 흐름 파악하기**
- 예외 발생 흐름
```
WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
```
- sendError 흐름
```
WAS(sendError 호출 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(response.sendError())
```

**2. WebServerFactoryCustomizer interface를 상속하여 WAS가 오류 페이지 정보를 확인할 수 있게 한다.**
```java
@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
```

**3. WAS는 오류 페이지를 출력하기 위해서, 페이지를 다시 Controller까지 요청한다.**
> 중요한 점은 웹 브라우저(클라이언트)는 서버 내부에서 이런 일이 일어나는지 전혀 모른다는 점이다. 오직 서버 내부에서 오류 페이지를 찾기 위해 추가적인 호출을 한다.

### RequestDispatcher 의 오류 정보
- request.attribute에 서버가 담아준 정보
- RequestDispatcher 상수로 정의되어 있음
```
javax.servlet.error.exception : 예외
javax.servlet.error.exception_type : 예외 타입
javax.servlet.error.message : 오류 메시지
javax.servlet.error.request_uri : 클라이언트 요청 URI
javax.servlet.error.servlet_name : 오류가 발생한 서블릿 이름
javax.servlet.error.status_code : HTTP 상태 코드
```

### DispatchType
- 결국 클라이언트로 부터 발생한 정상 요청인지, 아니면 오류 페이지를 출력하기 위한 내부 요청인지 구분할 수 있어야 한다. 서블릿은 이런 문제를 해결하기 위해 DispatcherType 이라는 추가 정보를 제공
```
REQUEST : 클라이언트 요청
ERROR : 오류 요청
FORWARD : MVC에서 배웠던 서블릿에서 다른 서블릿이나 JSP를 호출할 때
RequestDispatcher.forward(request, response);
INCLUDE : 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때
RequestDispatcher.include(request, response);
ASYNC : 서블릿 비동기 호출
```
- 따라서, Filter의 경우는, 필터를 등록할 때 등록하고 싶은 때의 Type만 지정해주면 된다.
```java
filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
```
- Interceptor의 경우는, 경로 정보로 지정할 수 있다.
```java
.excludePathPatterns("/css/**", ".ico", "/error",
                        "/error-page/**"); // 오류 페이지 경로 자체를 넣어줌.
```

### SpringBoot 에서 제공하는 에러 페이지
- 스프링 부트는 ErrorPage 를 자동으로 등록한다. 이때 /error 라는 경로로 기본 오류 페이지를 설정한다.
> 서블릿 밖으로 예외가 발생하거나, response.sendError(...) 가 호출되면 모든 오류는 /error 를 호출하게 된다.
- BasicErrorController 라는 스프링 컨트롤러를 자동으로 등록한다.
> ErrorPage 에서 등록한 /error 를 매핑해서 처리하는 컨트롤러다. 오류가 발생했을 때 오류 페이지로 /error 를 기본 요청한다.
- ErrorMvcAutoConfiguration 이라는 클래스가 오류 페이지를 자동으로 등록하는 역할을 한다.

### 개발자는 /templates/error/ 폴더 하위에 오류 페이지만 등록하면 되는 것이다 ! !
> 해당 경로 위치에 HTTP 상태 코드 이름의 뷰 파일을 넣어두면 된다.
- 뷰 선택 우선순위
1. 뷰 템플릿
```
resources/templates/error/500.html
resources/templates/error/5xx.html
```
2. 정적 리소스( static , public )
```
resources/static/error/400.html
resources/static/error/404.html
resources/static/error/4xx.html
```
3. 적용 대상이 없을 때 뷰 이름( error )
```
resources/templates/error.html
```

### BasicErrorController가 제공하는 기본 정보들
application.properties
```properties
# : exception 포함 여부( true , false )
server.error.include-exception=false

# : message 포함 여부
server.error.include-message=on-param

# : trace 포함 여부
server.error.include-stacktrace=never

# : errors 포함 여부
server.error.include-binding-errors=never
```
- 기본 값이 never 인 부분은 다음 3가지 옵션을 사용할 수 있다. => never, always, on_param
1. never : 사용하지 않음
2. always :항상 사용
3. on_param : 파라미터가 있을 때 사용

##### 스프링 부트 오류 관련 옵션
```
server.error.whitelabel.enabled=true
```
> 오류 처리 화면을 못 찾을 시, 스프링 whitelabel 오류 페이지 적용

```
server.error.path=/error
```
> 오류 페이지 경로, 스프링이 자동 등록하는 서블릿 글로벌 오류 페이지 경로와 BasicErrorController 오류 컨트롤러 경로에 함께 사용된다.

- 확장 포인트
> 에러 공통 처리 컨트롤러의 기능을 변경하고 싶으면 ErrorController 인터페이스를 상속 받아서 구현하거나 BasicErrorController 상속 받아서 기능을 추가하면 된다.
