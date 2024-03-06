# MVC Pattern 의 실제 구조 이해
- 앞서 구현해보았던 03_frontController를 실제 Spring MVC에서는 어떻게 구현되어 있을까?
- frontController => DispatcherServlet (*핵심* 정중앙의 컨트롤러) `org.springframework.web.servlet.DispatcherServlet`
- handlerMappingMap => HandlerMapping (핸들러 매핑 및 핸들러 어댑터 목록) `org.springframework.web.servlet.HandlerMapping`
- MyHandlerAdapter => HandlerAdapter (핸들러 어댑터) `org.springframework.web.servlet.HandlerAdapter`
- ModelView => ModelAndView
- viewResolver => ViewResolver `org.springframework.web.servlet.ViewResolver`
- MyView => View `org.springframework.web.servlet.View`

# SpringMVC 구조/동작방식
1. *핸들러(컨트롤러) 조회* : 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러) 조회
2. *핸들러 어댑터 조회* : 핸들러(컨트롤러)를 실행할 수 있는 핸들러 어댑터 조회
3. *핸들러 어댑터 실행*
4. *ModelAndView 반환*
5. *ViewResolver 호출* : View의 논리이름(viewName)을 물리이름(viewPath)으로 바꿔준다.
6. *View 반환* : 렌더링 역할을 담당하는 뷰 객체 반환
7. *View Rendering*

## DispatcherServlet
- 부모 클래스에서 HttpServlet을 상속받아서 Servlet(서블릿)으로 동작한다.
- Spring Boot는 DispatcherServlet을 자동으로 등록하면서 모든 경로(urlPattern="/")에 대해서 매핑한다.
#### DispatcherServlet의 요청 흐름
1. Servlet 호출
2. HttpServlet이 제공하는 service() 메서드 호출
- 스프링MVC는 DispacherServlet의 부모 클래스인 FrameworkServlet에서 service()를 오버라이드 해두었다.
3. 결국 최종적으로 *DispatcherServlet.doDispatch()* 메서드가 호출

## HandlerMapping
- 핸들러 매핑에서 컨트롤러를 찾고 싶다면, Spring Bean의 이름으로 handler(Controller)를 찾을 수 있는 HandlerMapping이 필요하다
- 0 = ★***RequestMappingHandlerMapping***★ : 애노테이션 기반의 컨트롤러만 @RequestMapping에서 사용
- 1 = ***BeanNameUrlHandlerMapping*** : 스프링 빈의 이름으로 핸들러를 찾음 (ex. @ComponentScan(name = ...))
## Handler Adapter
- 0 = ★***RequestMappingHandlerAdapter***★ : 애노테이션 기반의 컨트롤러만 @RequestMapping에서 사용
- 1 = ***HttpReqeustHandlerAdapter*** : HttpReqeustHandler 처리
- 2 = ***SimpleControllerHandlerAdapter*** : Controller Interface (애노테이션x, 과거에 사용) 처리
#### OldController의 경우, HandlerMapping/Adapter의 요청 흐름
1. HandlerMapping을 순서대로 조회해서 핸들러를 찾는다.
2. Bean 이름으로 핸들러를 찾기에, `BeanNameUrlHandlerMapping`이 실행에 성공하고 핸들러인 `OldController`를 반환
3. HandlerAdapter의 supperts() 순서대로 호출
4. SimpleControllerHandlerAdapter가 Controller 인터페이스를 지원하므로 대상이 된다.
5. 결론 :: 즉, OldController를 실행하면서 사용한 객체는 다음과 같다.
   - HandlerMapping = BeanNameUrlHandlerMapping
   - HandlerAdapter = SimpleControllerHandlerAdapter
#### HttpReqeustHandler의 경우, HandlerMapping/Adapter의 요청 흐름
   - HandlerMapping = BeanNameUrlHandlerMapping
   - HandlerAdapter = HttpReqeustHandlerAdapter

## ViewResolver
- application.properties에 다음과 같은 설정정보를 등록하고 사용한다
```
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
```
- 1 = ***BeanNameViewResolver*** : Bean 이름으로 View를 찾아서 반환
- 2 = ***InternalResourceViewResolver*** : JSP를 처리할 수 있는 뷰를 반환 (forward()를 호출하여 처리할 수 있는 경우에 해당) (RequestDispatcher.forward(request, response))
