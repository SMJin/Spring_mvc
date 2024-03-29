# 스프링 MVC - 백엔드 웹 개발 핵심 기술
- 인프런 김영한님 강의 참고

## WAS (Web Application Server) 란?
- 웹 서버는 단순히 정적 리소스를 반환해주는 역할을 한다.
- 이때 WAS 는 웹서버의 기능을 포함한다.
- 한 동적 리소스(동적 html, api(json) 등)나 애플리케이션 로직(서블릿, jsp, 스프링mvc 등)을 수행한다.
- 자바는 서블릿 컨테이너 기능을 제공하면 WAS 라고 한다.
- WAS는 애플리케이션 코드를 실행하는데 특화되어 있다.

#### 사실, WAS와 DB만으로도 간단하게 시스템 구성이 가능하다. 
#### 하지만 ...
- WAS가 너무 많은 역할 -> 서버 과부화
- 정적 리소스까지 전부 처리 -> 비싼 애플리케이션 로직에 장애
- WAS 장애시 -> 오류 메시지조차 출력 불가능

#### 그래서 웹 시스템은 3가지로 구성된다.
## WEB Server, WAS, DB
- 웹 서버 -> 정적 리소스나 단순 반환 로직
- WAS -> 애플리케이션 로직같은 동적 처리 로직

#### 이렇게 하면 정적 리소스와 애플리케이션 리소스가 분리된다.

## Servlet 이란?
- HTTP 요청/응답 메시지에는 너무나도 많은 정보가 담겨있다.
- 그 정보들은 반복적으로 비슷한 형식으로 사용된다.
- 따라서 HTTP 요청/응답 정보를 편리하게 관리할 수 있게 해주는 것이 Servlet이다.
- 이를 통해서 개발자는 HTTP 스펙을 편리하게 사용할 수 있다.
- HttpServletRequest 객체 / HttpServletResponse 객체

## Servlet Container
- 서블릿 컨테이너에서 서블릿 생명주기를 이용하여 서블릿을 생성, 호출, 관리한다.

#### tomcat 처럼 서블릿을 지원하는 WAS 를 서블릿 컨테이너라고 한다.
- 서블릿 객체는 *싱글톤*으로 관리한다.
- 모든 고객 요청은 동일한 서블릿 객체 인스턴스에 접근한다.
- *공유변수* 사용 주의
- *jsp*도 서블릿으로 변환되어서 사용된다.
- 동시요청을 위한 *멀티 쓰레드* 처리를 지원해준다.

#### 이때 서블릿 객체를 호출하는 것은 *쓰레드*이다.
- 예를들어 Java의 메인 메서드를 처음 실행하면 main 이라는 쓰레드가 실행된다.
- 쓰레드는 하나의 코드 라인만 수행한다.
- 동시 처리가 필요하면 더 많은 쓰레드가 필요하다.

## Multi-Thread (멀티 쓰레드)
- 쓰레드를 하나만 사용하면, 다중 요청이 들어왔을 때 응답이 지연/대기가 길어질 수 있다.
- 따라서 동시 요청을 처리하기 위해 멀티 쓰레드가 필요하다.
- 그러나 *Context Switching 비용*이 발생하며,
- 고객 요청이 너무 많을시, 메모리 임계점이 넘어서 서버가 죽을 수도 있다. -> **Thread Pull (쓰레드 풀)** 생성
- WAS가 멀티 쓰레드를 관리해주기 때문에, 개발자가 관련 코드를 신경쓰지 않아도 된다. (싱글톤 객체는 주의해서 사용)

#### **★ 실무에서 성능 튜닝 포인트는 최대 쓰레드 개수를 얼마나 설정할 것이냐 이다.**
- 애플리케이션 로직 복잡도, CPU, 메모리, IO 리소스 상황에 따라서 적절히 적용해야 한다.
- 성능 테스트를 최대한 시리제 서비스와 비슷하게 시도해본다.
- tool : 아파치 ab, 제이미터, nGrinder

## HTML API
- 주로 json 형태
- ex. React, Vue

## SSR : Server-Side Rendering (서버 사이드 렌더링)
- 주로 정적인 화면에서 사용
- ex. JSP, Thymeleaf -> 웹 백엔드 개발자

## CSR : Client-Side Rendering (클라이언트 사이드 렌더링)
- 주로 동적인 화면에서 사용
- ex. React, Vue -> 웹 프론트엔드 개발자
