# front Controller 도입
- Servlet 을 이용하여 구현했을때, 공통적인 부분들을 front 로 묶어서 구현한다.

## 개념정리
#### webapp vs WEB-INF
- webapp은 jsp와 같은 정적파일을 정적주소자체로 접근할 수 있는 반면,
- WEB-INF는 컨트롤러를 통해서만 접근이 가능하다. 보안성이 더 뛰어난 것이다.
## Servlet 종속성을 제거하기
#### frontController 생성
- frontController를 따로 빼서, 모든 컨트롤러를 하나의 컨트롤러로 모이게 관리한다. (공통 interface 활용)
- 이때 컨트롤러를 관리하는 Map을 만들어서 <String(ViewPath), Controller Interface> 타입으로 관리한다.
#### View 객체 생성
- ViewPath 정보를 관리하도록 private String 으로 선언한다.
- render(request, response) 메소드를 활용하여 RequestDispatche로 viewPath를 담아 forward한다.
- (이후에 추가되지만) View 객체에는 Model 정보도 들어있는 render(model, request, response) 메소드도 만든다.
- model 정보를 넘기기 위해서는 request.setAttribute(key, value) 를 활용해서 request 객체에 담는다.
- 즉, View 객체에서만 *HttpServletReqeust*와 *HttpServletResponse*를 건드린다.
- 다른 컨트롤러들은 paramMap으로 값을 관리한다.
#### ModelView 객체 생성
- ViewName 정보와 Model 정보를 관리하는 객체를 생성한다. (각각 String, Map<String,Obejct>)
- frontController에서 ViewResolver(viewName) 메소드를 이용하여 return viewPath 로 변환해주는 메소드를 생성한다.
- 즉, ViewResolver 에서 viewName을 => viewPath로 변환해주는 역할을 하는 것이다.
- ModelView 안에 넣어진 Model 객체는 frontController에서 직접 관리하는 것이다.
- Model 객체는 Map<String, Obejct> 의 형식으로 frontController에서 파라미터로 관리된다.
#### frontController에 Adapter Pattern 적용

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
