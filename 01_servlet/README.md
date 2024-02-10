# 서블릿 Servlet

## Jar 배포 vs War 배포
#### JAR : Java ARchive
- 독립적인 Java 애플리케이션을 패키징한다.
- 실행가능한 JAR 파일 생성
- JVM에서 직접 실행하기 때문에 별도의 웹 컨테이너나 서버가 필요하지 않다.
- ***즉, JAR는 JRE(Java Runtime Environment)만 존재하면 구동이 가능하다.**
#### WAR : Web Application aRchive
- 서블릿/jsp컨테이너에 배치할 수 있는 웹 어플리케이션(Web application) 압축 파일 포맷이다.
- 웹 응용 프로그램을 위한 포맷이기 때문에 웹 관련 자원만 포함하고 있다.
- WAR 파일을 실행하려면 Tomcat 과 같은 웹 서버 혹은 웹 컨테이너가 필요하다.
- ***즉, WAR는 웹서버 혹은 WAS(웹 컨테이너)가 있으야 구동이 가능하다.**

### 서블릿 프로젝트에서는 jsp를 사용할 것이기 때문에 WAR 파일로 구동을 할 것이다.
### Servlet Project 환경
- Gradle(Groovy)
- Java 17
- War Packaging
- Spring Boot 3.2.2
- IDE : Intellij 사용

## HttpServlet 클래스
- 서블릿 클래스가 되기 위해서는 다음과 같은 순서의 *생명주기*를 가져야 한다.
- ★ **init()** -> **service()** -> **destroy()**
- HttpSevletRequest 클래스와 HTtpServletResponse 클래스의 객체를 파라미터로 받는다.
- HttpServlet 메소드는 HTTP 요청에 따라 service 메소드의 실행이 8가지로 분리되는데, 그중 가장 기본적으로 사용되는 4가지가 다음과 같다.
- **POST, GET, PUT, DELETE**
- **doPost(), doGet(), doPut(), doDelete()**

## HttpServletRequest
- http 요청 메시지를 직접 파싱하지 않고 개발자 대신 요청 메시지를 파싱해주는 클래스이다.
- START LINE 구성 (HTTP메소드, URL, 쿼리스트링, 스키마/프로토콜)
- 헤더 구성 (헤더)
- 바디 구성 (form 파라미터 형식 조회, message body 데이터 직접 조회)
#### 임시 저장소 기능
- 해당 HTTP 요청이 시작했을 때부터 끝날 때까지 유지되는 임시 저장소 기능
- 저장 : *request.setAttribute(name, value)*
- 조회 : *request.getAttribute(name)*
#### 세선 관리 기능
- *request.getSession(create: true)*
