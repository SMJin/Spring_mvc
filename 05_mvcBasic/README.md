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

## RequestMapping
#### @PathVariable
- 말그대로 *경로 변수* 이다.
- 경로 변수는 중괄호 {userId}로 둘러싸인 형태로 @RequestMapping URL에서 사용된다.
- 메소드 내에서 지정한 경로 변수와 parameter 값이 같다면 이름 지정을 생략할 수 있다.
#### params 특정 파라미터 조건 매핑
```
@GetMapping(value = "/mapping-param", params = "mode=debug")
```
- 이런식으로 사용하면, 다음과 같이 url에 반드시 mode=debug가 포함되어야 한다.
- http://localhost:8080/mapping-param?mode=debug
#### headers 특정 헤더 조건 매핑
```
@GetMapping(value = "/mapping-header", headers = "mode=debug")
```
- 이런식의로 사용하면, 요청을 보낼때 request header에 key값이 mode인 조건의 value값이 debug여야 한다.
#### Content-Type 매핑 consume, produces
```
@PostMapping(value = "/mapping-consume", consumes = "application/json")
```
- 이런식으로 사용하면, request header의 Content-Type 이 json 이어야 한다.
```
@PostMapping(value = "/mapping-produce", produces = "text/html")
```
- 이런식으로 사용하면, request header의 Accept key기반으로 매핑이 되는데,
- 즉 Client가 어떤 형식으로 받을 건지 지정하는 것이다.

## HTTP request
#### header 정보
- 공식문서 >  Method Arguments 페이지에서 알 수 있는 여러가지 헤더 정보를 알 수 있다.
```
// 예시
HttpServletRequest request,
HttpServletResponse response,
HttpMethod httpMethod,
Locale locale,
@RequestHeader MultiValueMap<String, String> headerMap,
@RequestHeader("host") String host,
@CookieValue(value = "myCookie", required = false) String cookie
```
#### HTTP 요청 파라미터
1. **GET - query parameter** (ex. String username = request.getParameter("username"))
2. **POST - HTML form** (ex. @RequestParam("username") String memberName)
  - required = false 면 파라미터에 없어도 된다. true면 무조건 있어야 한다.
  - defaultValue = "guest" 와 같이 기본 값을 지정해주면 값이 없을때 기본값을 배정해준다.
  - Map<String, Object> paramMap 과 같은 형태로 map으로 받아올 수도 있다. 꺼내쓸땐 paramMap.get("username") 과 같이 호출하면 된다.
  - dataVo 에 롬복 @Data를 선언하면 @Getter , @Setter , @ToString , @EqualsAndHashCode , @RequiredArgsConstructor 를 자동으로 적용해준다.
  - @ModelAttribute 를 지정하면 VO Class 객체로 된 값을 파라미터로 받아온다.
3. **HTTP message body** 에 데이터 담아 직접 요청
  - Http message body에 데이터(ex. JSON, XML, TEXT)를 직접 담아서 요청
  - Http method는 POST, PUT, PATCH 이다.
  - 이런 경우에는 @RequestParam, @ModelAttribute를 사용할 수 없다.
  - InputStream으로 messageBody 값을 가져올 수 있다. request.getInputStream()
  - ☆★ ***HttpEntity***<String> httpEntity를 이용하면 InputStream도 필요없다.
  - 즉, HttpEntity는 HTTP header, body 정보를 편리하게 조회할 수 있게 한다.
  - 이때 HttpEntity를 상속받은 두 개의 객체가 존재한다.
  - 1) RequestEntity :: HttpMethod, url 정보가 추가, 요청에서 사용
  - 2) ResponseEntity :: HTTP 상태 코드 설정 가능, 응답에서 사용
```
return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)
```
  - @RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회
  -  참고로 헤더 정보가 필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
#### 정리
- 요청 파라미터 vs HTTP 메시지 바디
- 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
- HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody
