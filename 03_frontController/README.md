# front Controller 도입
- Servlet 을 이용하여 구현했을때, 공통적인 부분들을 front 로 묶어서 구현한다.

## 개념정리
#### webapp vs WEB-INF
- webapp은 jsp와 같은 정적파일을 정적주소자체로 접근할 수 있는 반면,
- WEB-INF는 컨트롤러를 통해서만 접근이 가능하다. 보안성이 더 뛰어난 것이다.
### Servlet 종속성을 제거하기
1. frontController를 따로 빼서, 모든 컨트롤러를 하나의 컨트롤로 모이게 관리한다. (공통 interface 활용)
2. View 객체 생성 => ViewPath 정보를 따로 담아서 RequestDispatcher로 forward한다.
3. View 객체에는 Model 정보도 들어있다 => request.setAttribute(key, value) 를 이용하여 model 값도 넘겨준다.
4. 즉, View 객체에서만 HttpServletReqeust 와 HttpServletResponse를 건드린다. 다른 컨트롤러는 paramMap으로 값을 관리한다.
5. ModelView 객체 생성 => ViewName 과 Model 정보만을 관리하는 객체를 생성한다.
6. ViewResolver를 이용하여 ModelView 의 viewName을 View의 viewPath로 변환한다.
7. ModelView를 반환하다가, model 객체를 frontController에서 직접 관리하면서 model을 파라미터로 관리하도록 한다.

## 맞딱트린 오류
#### jsp 파일 접근만 하면 자꾸 jsp 파일이 다운로드 됨
- Gradle에서 jsp impletation을 잘 해왔는지 확인해야 한다.
- 스프링 3.0이상일 때와 아닐때의 impletation이 달라진다.
#### Path with "WEB-INF" or "META-INF" 에러 - Whitelabel Error Page
```
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
implementation group: 'org.glassfish.web', name: 'jakarta.servlet.jsp.jstl', version: '2.0.0'
```
- 위 두 라이브러리를 추가해준 뒤, Gradle reload를 해주어야한다.
- JSTL 이란 1. JSP 표준라이브러리(JSP Standard Tag Library) 이다.
- 스프링 부트에서는 기본적으로 JSP를 지원하지 않기 때문에 추가해주어야 한다.
- 즉, 스프링 부트에 내장된 tomcat에는 컴파일하는 jsp 관련 엔진이 포함되지 않기에 jasper를 통해 추가한다.
