# Spring MVC :: 기본기능

## WAR vs JAR
#### WAR :: Web Application Achive
- Servlet 이나 JSP 으로 동작하는 웹 애플리케이션 전체를 packing하기 위한 압축파일 포맷
- 사전 정의된 구조인 WEB-INF 를 사용
- 별도의 웹 서버(ex. apache tomcat)나 웹 컨테이너(AWS)를 필요로 한다.
#### JAR :: Java ARchive
- Java Application 이 동작할 수 있도록 오로지 Java Project를 압축한 파일
- JVM (Java Virtual Machine)에서 직접 실행하기 때문에 별도의 웹 컨테이너나 서버가 불필요
- JRE (Java Runtime Environment)만 있어도 실행 가능

## @Controller VS @RestController
- 가장 주요한 차이점은 HTTP Response Body가 생성되는 방식이다
#### @Controller
- 전통적인 Spring MVC Controller
- 주로 View를 반환 (ViewResolver가 사용됨)
- @ResponseBody 어노테이션을 활용하여 Json 형태로 데이터 반환 가능
- Controller로 객체를 반환할 때는 일반적으로 ResponseEntity로 감싸서 반환하며, ViewResolver 대신에 HttpMessageConverter가 동작
#### @RestController
- @Controller에 @ResponseBody가 추가된 것
- 즉, JSON 형태로 된 객체 데이터를 반환하는 것이 목적
- 데이터를 응답으로 제공하는 REST API 개발시에 주로 사용

## Log 사용하기
- import 하는 package는 Slf4j 라이브러리로 해야한다.
- 사하는 방법은 다음과 같다.
```
private Logger log = LoggerFactory.getLogger(getClass());
private static final Logger log = LoggerFactory.getLogger(Xxx.class)
```
- @Slf4j : Lombok으로 선언하면 위의 코드를 선언하지 않아도 사용이 가능 ★
- LEVEL: TRACE > DEBUG > INFO > WARN > ERROR
- (개발 서버는 debug 출력, 운영 서버는 info 출력)
- application.properties 내부는 다음과 같다.
```
# 전체 로그 레벨 설정(기본 info)
logging.level.root=info
# hello.springmvc 패키지와 그 하위 로그 레벨 설정
logging.level.hello.springmvc=debug
```
#### Log를 사용해야 하는 이유
1. Thread 정보, 클래스 이름 같은 부가 정보를 함께 볼 수 있고, 출력 모양을 조정할 수 있다.
2. 로그 레벨에 따라 개발 서버에서는 모든 로그를 출력하고, 운영서버에서는 출력하지 않는 등 로그를 상황에 맞게 조절할 수 있다.
3. System.out.println에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있다. 특히 파일로 남길 때는 일별, 특정 용량에 따라 로그를 분할하는 것도 가능하다.
4. 성능도 일반 System.out보다 좋다. (내부 버퍼링, 멀티 쓰레드 등등) 그래서 실무에서는 꼭 로그를 사용해야 한다.
