# front Controller 도입
- Servlet 을 이용하여 구현했을때, 공통적인 부분들을 front 로 묶어서 구현한다.

## 개념정리
#### webapp vs WEB-INF
- webapp은 jsp와 같은 정적파일을 정적주소자체로 접근할 수 있는 반면,
- WEB-INF는 컨트롤러를 통해서만 접근이 가능하다. 보안성이 더 뛰어난 것이다.

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
