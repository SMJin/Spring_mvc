# 복습 일지 v.240517

# 1. 웹 애플리케이션 이해
#### `Server` vs `WAS`
#### Java Servlet
#### Multi Thread - 서블릿이 관리하는 쓰레드 풀
#### `SSR` vs `CSR`

# 2. 서블릿
##### @ServletComponentScan
#### @WebServlet
##### extends HttpServlet
#### `HttpServletReqeust` vs `HttpServletResponse`
#### GET/POST - query parameter *(request.getParameter())*
#### HTTP API - text, JSON *(request.getInputStream(), ObjectMapper.readValue(messageBody, Vo.class))*
##### response.setHeader("...", ",,,")
##### response.getWriter().println("ok")

# 3. 서블릿, JSP, MVC 패턴
#### 서블릿 + html 만으로 만드는 웹 어플리케이션의 한계
###### 정적인 문서만 만들기 편함. 코드가 복잡하고 비효율적임.
#### JSP 도입
###### JSP는 서버에서 처리된 후 클라이언트에게 HTML로 변환되어 보여짐.
###### HTML 에서 JAVA언어를 사용할 수 있음 (JSP태그 <% %> 이용)
###### Java 코드는 서블릿 클래스의 `service()` 메서드 내애서 실행됨
#### JSP + html 만으로 만드는 웹 어플리케이션의 한계
###### Java코드, 데이터 조회하는 Repository 코드 등 다양한 코드가 JSP에 모두 노출
###### 비즈니스 로직이 분리되지 않는다, JSP가 너무 많은 역할을 하게 됨.
#### MVC 패턴 도입 (Model View Controller)
```java
RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
dispatcher.forward(request, response); 
```
###### ReqeustDispatcher - dispatcher.forward(request, response) 를 통해 다른 서블릿이나 JSP로 이동 가능
###### 컨트롤러의 역할과 뷰를 렌더링 하는 역할 분리
###### 그럼에도 아직 수정할 부분이 많다 .... (다음 챕터에서 계속) ex. 프론트 컨트롤러

# 4. MVC 프레임워크 만들기
#### 프론트 컨트롤러
##### 컨트롤러 인터페이스
```java
public interface ControllerV1 {
  void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
```
##### 해당 컨트롤러 인터페이스를 implements로 상속받은 컨트롤러들 생성
```java
// 예시 컨트롤러
public class MemberListControllerV1 implements ControllerV1 {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Member> members = memberRepository.findAll();
    request.setAttribute("members", members);

    String viewPath = "/WEB-INF/views/members.jsp";
    RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
    dispatcher.forward(request, response);
  }
}
```
##### 프론트 컨트롤러 예시
```java
@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {
  private Map<String, ControllerV1> controllerMap = new HashMap<>();
  public FrontControllerServletV1() {
    controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
    controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
    controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
  }
  
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("FrontControllerServletV1.service");
    String requestURI = request.getRequestURI();

    ControllerV1 controller = controllerMap.get(requestURI);
    if (controller == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    controller.process(request, response);
  }
}
```

#### View 분리
```java
public class MyView {
  private String viewPath;
  public MyView(String viewPath) {
    this.viewPath = viewPath;
  }
  public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
    dispatcher.forward(request, response);
  }
}
```
##### 프론트 컨트롤러의 마지막 부분 수정
###### 물론, 각 컨트롤러의 return 타입도 MyView 로 변환해주어야 한다.
```java
MyView view = controller.process(request, response);
view.render(request, response);
```

#### ModelView 도입
###### View + Model

#### ViewResolver 도입
###### 논리 뷰 이름(ex. members) => 물리 뷰 경로(ex. /WEB-INF/views/members.jsp)로 변환

#### controllerMap 을 HandlerAdapter로 변환(도입)
###### supports(Object handler) 를 이용해서 해당 컨트롤러(=handler)가 맞는지 확인
###### 해당 컨트롤러(handler)가 맞다면, handle(request, response) 를 이용해서 로직을 처리하고 ModelView 반환

# 5. 스프링 MVC - 구조 이해
#### 직접 만든 프레임워크 --> 실제 스프링 MVC 클래스 비교
###### FrontController --> DispatcherServlet
###### handlerMappingMap --> HandlerMapping
###### MyHandlerAdapter --> HandlerAdapter
###### ModelView --> ModelAndView
###### viewResolver --> ViewResolver
###### MyView --> View

#### 스프링 MVC 동작 순서
###### 1. 핸들러 조회: 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
###### 2. 핸들러 어댑터 조회: 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
###### 3. 핸들러 어댑터 실행: 핸들러 어댑터를 실행한다.
###### 4. 핸들러 실행: 핸들러 어댑터가 실제 핸들러를 실행한다.
###### 5. ModelAndView 반환: 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해서 반환한다.
###### 6. viewResolver 호출: 뷰 리졸버를 찾고 실행한다.
######  - JSP의 경우: InternalResourceViewResolver 가 자동 등록되고, 사용된다.
###### 7. View 반환: 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를 반환한다.
######  - JSP의 경우 InternalResourceView(JstlView) 를 반환하는데, 내부에 forward() 로직이 있다.
###### 8. 뷰 렌더링: 뷰를 통해서 뷰를 렌더링 한다.

#### 주요 인터페이스 목록
###### 핸들러 매핑: org.springframework.web.servlet.HandlerMapping
###### 핸들러 어댑터: org.springframework.web.servlet.HandlerAdapter
###### 뷰 리졸버: org.springframework.web.servlet.ViewResolver
###### 뷰: org.springframework.web.servlet.View

#### ★ @RequestMapping
###### 요청 정보를 매핑한다. 해당 URL 이 호출되면 이 메서드가 호출된다. 애노테이션을 기반으로 동작하기 때문에, 메서드의 이름은 임의로 지으면 된다.
###### 스프링에서 가장 우선순위가 높은 핸들러 매핑/어댑터
##### RequestMappingHandlerMapping
###### RequestMappingHandlerMapping 은 스프링 빈 중에서 @RequestMapping, @Controller 가 클래스 레벨이 붙어 있는 경우에 매핑 정보로 인식한다.
##### RequestMappingHandlerAdapter

# 6. 스프링 MVC - 기본 기능
#### @RequestMapping : URL 매핑
###### @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping

#### @PathVariable : 경로 변수

#### 요청 파라미터 vs HTTP 메시지 바디
##### 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
###### @RequestParam : 파라미터 이름으로 바인딩 (request.getParameter("...","..."))
###### @ModelAttribute : vo 매칭, 생략도 가능
##### HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody
###### @ResponseBody : View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력

##### HttpEntity: HTTP header, body 정보를 편리하게 조회 --> @ResponseBody로 해결 가능
###### 메시지 바디 정보를 직접 조회
###### RequestEntity : HttpMethod, url 정보 추가, 요청에서 사용
###### ResponseEntity : HTTP 상태 코드 설정 가능, 응답에서 사용

#### `@RequestBody` vs `@ResponseBody`
###### 요청의 경우, `canRead()` 조건을 만족하면 `read()`를 호출하여 객체를 생성하고 반환
###### 응답의 경우, `canWrite()` 조건을 만족하면 `write()`를 호출해서 HTTP 응답 메시지 바디에 데이터를 생성

# 7. 스프링 MVC - 웹 페이지 만들기
##### tymeleaf
##### PRG : Post -> Redirect -> Get
##### RedirectAttributes
