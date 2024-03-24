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
##### URL 링크
- @{/url}
- @{/url/{p1}/{p2}(p1=${p1}, p2=${p2}, p3=${p3}, p4=${p4})} : 이렇게하면 p1, p2 는 path Variable로 들어가고 p3, p4 는 query parameter로 들어간다.
##### 리터럴 : 소스 코드상에 고정된 값
 - th:text="'hello world!'" 혹은 "'hello ' + ${data}"
 - 문자 리터럴은 항상 ' (작은 따옴표)로 감싸야만 함
 - ' 없어도, 공백 없이 쭉 이어진다면 하나의 의미있는 토큰으로 인지
##### 리터럴 대체(Literal substitutions)
- th:text="|hello ${data}|"
- 마지막의 리터럴 대체 문법을 사용하면 마치 템플릿을 사용하는 것 처럼 편리하다.
##### 연산
- 비교연산: HTML 엔티티를 사용해야 하는 부분을 주의하자,
- > \> (gt), < (lt), >= (ge), <= (le), ! (not), == (eq), != (neq, ne)
- 조건식: 자바의 조건식과 유사하다.
- Elvis 연산자: 조건식의 편의 버전
- No-Operation: _ 인 경우 마치 타임리프가 실행되지 않는 것 처럼 동작한다.
##### 속성
- th:name 이렇게 하면 기존 name 속성은 버려짐
- 속성 추가 (large를 추가하는 예시)
- > th:attrappend : 속성 값의 뒤에 값을 추가한다. (textlarge)
- > th:attrprepend : 속성 값의 앞에 값을 추가한다. (largetext)
- > th:classappend : class 속성에 자연스럽게 추가한다. (text large)
##### 반복
- th:each
- th:each="user, userStat : ${users}" 와 같이 두번째 파라미터를 사용해서 상태 확인이 가능
> 두번째 파라미터는 생략이 가능한데, 생략하면 지정한 변수명(user) + Stat 으로 자동 지정된다.
##### 반복의 상태 확인 Stat 의 지원 기능
- index : 0부터 시작하는 값
- count : 1부터 시작하는 값
- size : 전체 사이즈
- even , odd : 홀수, 짝수 여부( boolean )
- first , last :처음, 마지막 여부( boolean )
- current : 현재 객체
##### 조건부 평가
- if, unless
- switch, case
##### 주석
- 표준 html 주석 : <!-- --> f12 열면 보
- thymeleaf 파서 주석 : <!--/* */--> f12 열어도 주석 안보임
- thymeleaf 프로토타입 주석 : <!--/*/ /*/--> 타임리프를 적용했을때만 보이게 함, 그냥 열면 주석처리됨
##### 블록 : 타임리프에서만 제공되는 tag (잘안씀, 표현이 난감한 경우에만 사용)
- th:block
##### 자바스크립트 인라인
- thymeleaf 를 사용할 때 <script></script> 내부에서는 [[${변수}]] 로 변수를 가져와 사용하면 된다.
- 그런데, 자바스크립트도 네추럴 템플릿이 있다.
- <script th:inline="javascript">...</script> 이렇게 쓰면 된다. 문자열이나 객체(json)등을 알아서 변환해준다. escape 해준다.
- ***자바스크립트 네추럴 템플릿*** : /*[[${user.username}]]*/ "test username" 앞에꺼는 thymeleaf, 뒤에꺼는 html 일때 보여준다.
##### 템플릿 조각
- th:fragment=프레그먼트명(파라미터, 파라미터) 이런식으로 프레그먼트 배정
- th:insert="~{html파일경로 :: fragment 명}" 혹은 replace (교체)
- common_header(~{::title},~{::link}) :: 태그를 넘길수도있다.
- html 자체를 넘길 수도 있다.
