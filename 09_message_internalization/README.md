# 메시지, 국제화

### 메시지란?
- HTML 파일에 문자열을 하드코딩하지 않고 다양한 메시지(문자열)를 한 곳에서 관리하도록 하는 기능
### 국제화란?
- 나라별로 서비스를 다르게 구현할 수 있다.
- 인식하는 방법은 http 에서는 accept-language 헤더 값이었다.

### MessageSource 클래스
- ms.getMessage("hello", null, null) 이용해서 메시지를 가져올 수 있다.
- code: hello
- args: null
- locale: null

### messages.properties 에 변수 지정하는 방법
- label.name = 사원 이런식으로 지정해주는데
- hello.name=안녕 {0} 이와 같이 파라미터 지정도 가능하다.
- 원래 application.properties 에 spring.messages.basename=messages 와 같이 지정해줘야 하는데, 사실 이는 스프링 기본값이다.

### Spring thymeleaf에서 메시지 사용하기
- messages.properties 와 같이 프로퍼티 파일에 변수들을 지정해준다.
- #{메시지 변수명} 을 이용하여 불러올 수 있다.

### Spring 국제화 사용하기
 - chrome같은 웹 브라우저에서 언어를 설정해주면 messages_en.properties로 매칭해준다. (Accept-Language 값을 바꾸는 것)

### Locale Resolver
- Locale 선택방식을 바꿀 수 있다.
