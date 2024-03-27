# 검증 1 - Validation

##### error Map 직접 개발
- @ModelAttribute를 사용하면 해당 객체를 그대로 다시 model에 넣어준다.
- Safe Navigation Operator 를 이용한 SpringEL 문법 null값 처리. (ex. errors?.containsKey('...'))
- thymeliaf 에는 th:classappend= 라는걸 사용할 수 있다.
- 먼저 고객들한테 입력폼 보여주고 오류가 있으면 error list 반환. 주로 th:if 사용해서 입력안된 것들 판별

- 근데 왜이렇게 중복되는 코드가 많지?
- 타입 오류는 수정이 안되는구나..

- 결국.. 고객이 입력한 값도 어딘가에서 관리가 되어야 할 거 같아..!!
