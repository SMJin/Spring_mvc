# 로그인 1 - 쿠키, 세션

##### 쿠키
- 영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지
- 세션 쿠키: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지 (<= 주로 얘를 쓰며, 세션과는 상관없다.)

- 다음과 같이 HttpServletResponse 에 쿠키정보를 넘겨주게 되면,
- Request에 쿠키정보가 계속 넘어간다.
```java
Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
response.addCookie(idCookie);
```

- 처음엔 Header 정보에 Set-Cookie: memberId=1 로 넘어가고,
- 이후에 요청을 보낼 때는 Cookie:memberId=1 로 계속 넘어간다.

- 다음과 같이 cookie 정보를 없앤다.
```java
Cookie cookie = new Cookie("memberId", null);
cookie.setMaxAge(0);
response.addCookie(cookie);
```

##### 하지만 쿠키에게는 심각한 보안 문제가 있다 !!!
- 쿠키는 위변조에 너무 취약하다. F12 열고 쿠키값 바꿔주면 손쉽게 바뀐다..
- 클라이언트 측에 보관되는 정보이기 때문에 탈취가 용이하다.

##### 해결 대안
- 사용자 별로 예측 불가능한 임의의 토큰(랜덤 값)을 노출하고, 서버에서 토큰과 사용자 id를 매핑해서 인식한다. 그리고 서버에서 토큰을 관리한다.
- 해커가 토큰을 털어가도 시간이 지나면 사용할 수 없도록 서버에서 해당 토큰의 만료시간을 짧게(예: 30분) 유지한다.
- 또는 해킹이 의심되는 경우 서버에서 해당 토큰을 강제로 제거하면 된다.

##### 해결책 :: 세션
- 쿠키 값을 변조 가능, 예상 불가능한 복잡한 세션Id를 사용한다. (해쉬함수화 하는 등의 UUID 처리를 한다.) (세선 저장소에 저장한다) (sessionId - value)
- 쿠키 탈취 후 사용 해커가 토큰을 털어가도 시간이 지나면 사용할 수 없도록 서버에서 세션의 만료시간을 짧게 (예: 30분) 유지한다.
- 또는 해킹이 의심되는 경우 서버에서 해당 세션을 강제로 제거하면 된다.

##### 세션 관리하기
1. 세션 생성
- > sessionId 생성 (임의의 추정 불가능한 랜덤 값)
- > 세션 저장소에 sessionId와 보관할 값 저장
- > sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
2. 세션 조회
- > 클라이언트가 요청한 sessionId 쿠키의 값으로, 세션 저장소에 보관한 값 조회
3. 세션 만료
- > 클라이언트가 요청한 sessionId 쿠키의 값으로, 세션 저장소에 보관한 sessionId와 값 제거


##### @SessionAttribute
- 이미 로그인 된 사용자를 찾을 때는 다음과 같이 사용하면 된다. 참고로 이 기능은 세션을 생성하지 않는다.
```java
@SessionAttribute(name = "loginMember", required = false) Member loginMember
```

- URL 전달 방식을 끄고 항상 쿠키를 통해서만 세션을 유지하고 싶으면 다음 옵션을 넣어주면 된다.
- 이렇게 하면 URL에 jsessionid 가 노출되지 않는다.
```application.properties
server.servlet.session.tracking-modes=cookie
```

##### 세선 타임아웃
- 세션은 예를들어, 사용자가 마지막으로 요청을 보낸 시점을 기점으로 특정 시간동안 세션을 유지하는 방식으로 동작한다.
- 그리고 항상 세션은 메모리를 잡아먹는다는 것을 기억하고, 최소한의 정보만 저장해야 한다.

##### 세션 타임아웃 설정하는 법
- application.properties에 다음과 같이 설정하기
```properties
server.servlet.session.timeout=60 : 60초, 기본은 1800(30분)
```
- 혹은, 자바 코드 내에서 지엽적으로 수정해주기
```java
// (글로벌 설정은 분 단위로 설정해야 한다. 60(1분), 120(2분), ...)
// 특정 세션 단위로 시간 설정
session.setMaxInactiveInterval(1800); //1800초
```
