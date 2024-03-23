# Thymeleaf 기본 사용법

### 1. basic-text
##### 변수 표현
 - tag 안에서는 ${변수}
 - tag 밖에서는 [[${변수}]]
##### escape (HTML에서 사용하는 특수 문자를 HTML 엔티티(tag를 문자로 표현)로 변경)
 - th:text 혹은 [[${변수}]] :: 자동 escape 적용되어 tag를 문자로 인식한다.
 - th:utext 혹은 [(${변수})] :: escape 적용되지 않아서 tag를 인식한다.
 - ***★ 실무에서 escape 적용을 추천하는 이유 ★*** : 사용자는 별의 별 문자를 다 입력하기 때문..
##### [Spring EL](./src/main/resources/templates/basic/variable.html)
 - 스프링 EL이라는 스프링이 제공하는 표현식
##### 지역변수
 - th:with
##### 편의 객체 (자주 쓰는건 그냥 지원함)
 - ${param.파라미터변수명} : 파라미터 그냥 가져올 수 있음
 - ${session.세션데이터명} : 세션 그냥 가져올 수 있음
 - ${@스프링빈명.메소드(...)} : 스프링 빈 그냥 가져올 수 있음
##### 유틸리티 객체 : 문자, 숫자, 날짜, URI등을 편리하게 다룰 수 있도록 "타임리프"에서 제공
- #message : 메시지, 국제화 처리
- #uris : URI 이스케이프 지원
- #dates : java.util.Date 서식 지원
- #calendars : java.util.Calendar 서식 지원
- #temporals : 자바8 날짜 서식 지원
- #numbers : 숫자 서식 지원
- #strings : 문자 관련 편의 기능
- #objects : 객체 관련 기능 제공
- #bools : boolean 관련 기능 제공
- #arrays : 배열 관련 기능 제공
- #lists , #sets , #maps : 컬렉션 관련 기능 제공
- #ids : 아이디 처리 관련 기능 제공, 뒤에서 설명
###### 타임리프 유틸리티 객체
###### https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#expression-utility-objects
###### 유틸리티 객체 예시
###### https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expressionutility-objects
