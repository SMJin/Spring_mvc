# API 예외 처리

###### 복습부터 합시다.
## HTTP message body에 직접 담아서 요청을 보내는 방식에 대하여 (API)

1. InputStream, OutputStream 을 통해서 Http message body의 데이터를 읽을 수 있다.
- 스프링 MVC는 다음 파라미터를 지원한다.
- > InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
- > OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
```java
@PostMapping("/request-body-string-v1")
public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
  ServletInputStream inputStream = request.getInputStream();
  String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
  log.info("messageBody={}", messageBody); // data가 담긴 곳이 바로 여기 messageBody
  response.getWriter().write("ok");
}

/**
 * InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
 * OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
 */
@PostMapping("/request-body-string-v2")
public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
  String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
  log.info("messageBody={}", messageBody);
  responseWriter.write("ok");
}

/*
  단순 텍스트가 아닌, JSON 객체 보내
*/
@PostMapping("/request-body-json-v1")
public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
  ServletInputStream inputStream = request.getInputStream();
  String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
  log.info("messageBody={}", messageBody);
  HelloData data = objectMapper.readValue(messageBody, HelloData.class);
  log.info("username={}, age={}", data.getUsername(), data.getAge());
  response.getWriter().write("ok");
 }
```

2. HttpEntity 을 통해서 Http message body의 데이터를 읽을 수 있다.
- 스프링 MVC는 다음 파라미터를 지원한다.
- > HttpEntity: HTTP header, body 정보를 편리하게 조회 (요청, 응답 모두 사용 가능)
```java
/**
 * HttpEntity: HTTP header, body 정보를 편리하게 조회
 * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
 * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 *
 * 응답에서도 HttpEntity 사용 가능
 * - 메시지 바디 정보 직접 반환(view 조회X)
 * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 */
@PostMapping("/request-body-string-v3")
public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
  String messageBody = httpEntity.getBody();
  log.info("messageBody={}", messageBody);
  return new HttpEntity<>("ok");
}

@ResponseBody
@PostMapping("/request-body-json-v4")
public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
  HelloData data = httpEntity.getBody();
  log.info("username={}, age={}", data.getUsername(), data.getAge());
  return "ok";
}
```
- HttpEntity 를 상속받은 다음 객체들도 같은 기능을 제공한다.
- > RequestEntity : HttpMethod, url 정보가 추가, 요청에서 사용
- > ResponseEntity : HTTP 상태 코드 설정 가능, 응답에서 사용
```java
// body message, headers, statusCode 가 각각 파라미터로 들어간다.
return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)
```

3. @ReqeustBody, @ResponseBody 를 통해서 Http message body의 데이터를 읽을 수 있다.
```java
/**
 * @RequestBody
 * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
 * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 *
 * @ResponseBody
 * - 메시지 바디 정보 직접 반환(view 조회X)
 * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 */
@ResponseBody
@PostMapping("/request-body-string-v4")
public String requestBodyStringV4(@RequestBody String messageBody) {
  log.info("messageBody={}", messageBody);
  return "ok";
}

/**
 * @RequestBody
 * HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 *
 * @ResponseBody
 * - 모든 메서드에 @ResponseBody 적용
 * - 메시지 바디 정보 직접 반환(view 조회X)
 * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 */
@ResponseBody
@PostMapping("/request-body-json-v2")
public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
  HelloData data = objectMapper.readValue(messageBody, HelloData.class);
  log.info("username={}, age={}", data.getUsername(), data.getAge());
  return "ok";
}

/**
 * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
 * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content-type:
application/json)
 *
 */
@ResponseBody
@PostMapping("/request-body-json-v3")
public String requestBodyJsonV3(@RequestBody HelloData data) {
  log.info("username={}, age={}", data.getUsername(), data.getAge());
  return "ok"; 
}

/**
 * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
 * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content-type:
application/json)
 *
 * @ResponseBody 적용
 * - 메시지 바디 정보 직접 반환(view 조회X)
 * - HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 적용(Accept:
application/json)
 */
@ResponseBody
@PostMapping("/request-body-json-v5")
public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
  log.info("username={}, age={}", data.getUsername(), data.getAge());
  return data;
}
```
> 참고로 헤더 정보가 필요하다면, HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.

4. HTTP 응답을 처리하는 다양한 방법
```java
@GetMapping("/response-body-string-v1")
public void responseBodyV1(HttpServletResponse response) throws IOException {
  response.getWriter().write("ok");
}

 /**
 * HttpEntity, ResponseEntity(Http Status 추가)
 * 엔티티에 제네릭 타입 추가하여 <String> 지정
 * @return
 */
 @GetMapping("/response-body-string-v2")
 public ResponseEntity<String> responseBodyV2() {
   return new ResponseEntity<>("ok", HttpStatus.OK);
 }

/*
 * String 뿐만 아니라 HelloData 객체도 보낼 수 있다.
 */
@GetMapping("/response-body-json-v1")
public ResponseEntity<HelloData> responseBodyJsonV1() {
  HelloData helloData = new HelloData();
  helloData.setUsername("userA");
  helloData.setAge(20);
  return new ResponseEntity<>(helloData, HttpStatus.OK);
}

/*
 * @ResponseBody 를 사용하면 그냥 String으로 보내도 됨.
 * 다만 이런 방식은 HttpStatus를 지정할 수 없다.
 */
@ResponseBody
@GetMapping("/response-body-string-v3")
public String responseBodyV3() {
  return "ok";
}

/*
 * @ResponseStatus(HttpStatus.OK) 어노테이션을 이런식으로 추가하면,
 * @ResponseBody 로 호출함에도 HttpStatus를 지정할 수 있다.
 */
@ResponseStatus(HttpStatus.OK)
@ResponseBody
@GetMapping("/response-body-json-v2")
public HelloData responseBodyJsonV2() {
  HelloData helloData = new HelloData();
  helloData.setUsername("userA");
  helloData.setAge(20);
  return helloData;
}
```
###### 서론(복습) 끝. 예외처리, 진짜 시작!
## Servlet 에서 제공하는 에러 페이지를 이용해서 API 에러 처리하기
- WebServerFactoryCustomizer<ConfigurableWebServerFactory> interface 를 상속받아서 예외가 발생했을 때 어디로 갈지 url을 지정해준다.
> WebServerFactoryCustomizer interface 살펴보기
```java
@FunctionalInterface
public interface WebServerFactoryCustomizer<T extends WebServerFactory> {
    void customize(T factory);
}
```
> 상속받아서 각 에러 HttpStatus 별로 보내고 싶은 곳으로 연결 시켜주기.
```java
@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
  @Override
  public void customize(ConfigurableWebServerFactory factory) {
    ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/errorpage/404");
    ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
    ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/errorpage/500");
    factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
  }
}
```
- 그런데 이렇게까지만 해주면, Servlet 에서는 자동으로 /error-page/ 폴더 하위에 있는 404.html 혹은 500.html 페이지 등으로 자동 연결해준다. 즉, API 통신을 선택한 우리에게 html 반환을 해준다는 뜻이다. 그러면 안되지 않은가! 그래서 다음과 같이 produces = JSON 타입을 연결해주면 JSON API 반환이 가능하다.
- 다만!! 요청을 받은 클라이언트 쪽에서 Accept : application/json 타입으로 받아야 한다.
```java
@RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response) {
  log.info("API errorPage 500");
  Map<String, Object> result = new HashMap<>();
  Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
  result.put("status", request.getAttribute(ERROR_STATUS_CODE));
  result.put("message", ex.getMessage());
  Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
  return new ResponseEntity(result, HttpStatus.valueOf(statusCode));
}
```
## Spring Boot 에서 제공하는 기본 예외 처리
- BasicErrorController가 기본적으로 모든 기능을 자동으로 해준다.
```java
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class BasicErrorController extends AbstractErrorController {
  /*
   * errorHtml() : produces = MediaType.TEXT_HTML_VALUE :
   * 클라이언트 요청의 Accept 해더 값이 text/html 인 경우에는 errorHtml() 을 호출해서 view를 제공한다.
   */
  @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {}

  /*
   *  error() : 그외 경우에 호출되고 ResponseEntity 로 HTTP Body에 JSON 데이터를 반환한다
   */
  @RequestMapping
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {} 
}
```

## API 예외처리 :: HandlerExceptionResolver
- Spring MVC 의 인터셉터는 preHandler -> controller -> postHandler -> afterCompletion 을 거쳐서 요청을 처리한다.
- 이때, 에러가 발생하면 postHandler는 발생하지 않고, HandlerExceptionResolver를 등록해주면 에러 처리를 할 수 있다.
```java
/*
 * handler : 핸들러(컨트롤러) 정보
 * Exception ex : 핸들러(컨트롤러)에서 발생한 발생한 예외
 */
public interface HandlerExceptionResolver {
  ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);
}
```
- 위의 인터페이스가 바로 HandlerExceptionResolver인데, 이 인터페이스를 상속받아서 예외 리졸버를 지정해주면 예외 처리를 할 수 있다.
```java
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
  try {
    if (ex instanceof IllegalArgumentException) {
      log.info("IllegalArgumentException resolver to 400");
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
      return new ModelAndView();
    }  
  } catch (IOException e) {
    log.error("resolver ex", e);
  }
    return null;
  }
}
```
- 반환 값에 따른 동작 방식
- > 빈 ModelAndView: 뷰를 렌더링 하진 않고, 정상 흐름으로 서블릿이 리턴된다. (에러 반환이 되지 않는다.)
- > ModelAndView 지정: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면 뷰를 렌더링 한다.
- > null: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다. (만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다.)

- 주의! HandlerExceptionResolver를 WebConfig에서 extendHandlerExceptionResolvers메소드에 등록해주어야 동작한다.
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
    }
}

```

## Spring 에서 제공하는 ExceptionResolver
- 스프링 부트가 기본으로 제공하는 ExceptionResolver 는 다음과 같다. (HandlerExceptionResolverComposite 에 다음 순서로 등록)
1. ① ExceptionHandlerExceptionResolver
- @ExceptionHandler 라는 애노테이션을 사용
```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(IllegalArgumentException.class)
public ErrorResult illegalExHandle(IllegalArgumentException e) {
  log.error("[exceptionHandle] ex", e);
  return new ErrorResult("BAD", e.getMessage());
}
```
1. ② @ControllerAdvice / @RestControllerAdvice 
- 해당 어노테이션을 붙인 클래스에 에러처리 핸들러들을 넣어주면 에러 처리를 글로벌(모든 핸들러에게 적용)하게 사용할 수 있다.
- 대상을 지정할 수도 있는데 그 방법은 다음과 같다.
```java
// Target all Controllers annotated with @RestController
@ControllerAdvice(annotations = RestController.class)
public class ExampleAdvice1 {}

// Target all Controllers within specific packages
// 특정 패키지에만 적용할 수도 있다. 보통 이런식으로 사용한다.
@ControllerAdvice("org.example.controllers")
public class ExampleAdvice2 {}

// Target all Controllers assignable to specific classes
@ControllerAdvice(assignableTypes = {ControllerInterface.class,
AbstractController.class})
public class ExampleAdvice3 {} 
```

2. ResponseStatusExceptionResolver
```java
/* 어노테이션으로 지정 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
```
```java
/* throw new 를 이용해서 직접 클래스를 호출한다. */
@GetMapping("/api/response-status-ex2")
public String responseStatusEx2() {
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
}
```
3. DefaultHandlerExceptionResolver 우선 순위가 가장 낮다. => 스프링 내부에서 기본적을 예외를 처리하는 부분이다.
 - 스프링은 기본적으로 해당 클래스가 앵간한 500 예외처리를 400 예외로 바꿔준다.
 - 예를들어, 파라미터 바인딩이 맞지 않아 발생하는 500 예외인 "TypeMismatchException" 는 사실 보통 사용자가 타입을 잘못 입력했을 때 주로 발생한다. 이런 경우의 예외들을 한데 모아서 400 에러로 반환해주는 역할을 하는 것이 바로 이 DefaultHandlerExceptionResolver 이다. 내부 구조를 보면 여러 예외 처리를 관리하고 있는 것을 알 수 있다.
 -  API뿐만이 아니라 View 나 빈 ModelAndView 등도 반환할 수 있지만, 대부분 RESTapi를 위해서 사용한다.
```java
public class DefaultHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
    protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");

    public DefaultHandlerExceptionResolver() {
        this.setOrder(Integer.MAX_VALUE);
        this.setWarnLogCategory(this.getClass().getName());
    }

    @Nullable
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
        try {
            if (ex instanceof ErrorResponse errorResponse) {
                ModelAndView mav = null;
                if (ex instanceof HttpRequestMethodNotSupportedException theEx) {
                    mav = this.handleHttpRequestMethodNotSupported(theEx, request, response, handler);
                } else if (ex instanceof HttpMediaTypeNotSupportedException theEx) {
                    mav = this.handleHttpMediaTypeNotSupported(theEx, request, response, handler);
                } else if (ex instanceof HttpMediaTypeNotAcceptableException theEx) {
                    mav = this.handleHttpMediaTypeNotAcceptable(theEx, request, response, handler);
                } else if (ex instanceof MissingPathVariableException theEx) {
                    mav = this.handleMissingPathVariable(theEx, request, response, handler);
                } else if (ex instanceof MissingServletRequestParameterException theEx) {
                    mav = this.handleMissingServletRequestParameter(theEx, request, response, handler);
                } else if (ex instanceof MissingServletRequestPartException theEx) {
                    mav = this.handleMissingServletRequestPartException(theEx, request, response, handler);
                } else if (ex instanceof ServletRequestBindingException theEx) {
                    mav = this.handleServletRequestBindingException(theEx, request, response, handler);
                } else if (ex instanceof MethodArgumentNotValidException theEx) {
                    mav = this.handleMethodArgumentNotValidException(theEx, request, response, handler);
                } else if (ex instanceof HandlerMethodValidationException theEx) {
                    mav = this.handleHandlerMethodValidationException(theEx, request, response, handler);
                } else if (ex instanceof NoHandlerFoundException theEx) {
                    mav = this.handleNoHandlerFoundException(theEx, request, response, handler);
                } else if (ex instanceof NoResourceFoundException theEx) {
                    mav = this.handleNoResourceFoundException(theEx, request, response, handler);
                } else if (ex instanceof AsyncRequestTimeoutException theEx) {
                    mav = this.handleAsyncRequestTimeoutException(theEx, request, response, handler);
                }\

.
.
.
```
