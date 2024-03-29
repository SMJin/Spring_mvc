# 검증 1 - Validation

##### error Map 직접 개발
- @ModelAttribute를 사용하면 해당 객체를 그대로 다시 model에 넣어준다.
- Safe Navigation Operator 를 이용한 SpringEL 문법 null값 처리. (ex. errors?.containsKey('...'))
- thymeleaf 에는 th:classappend= 라는걸 사용할 수 있다.
- 먼저 고객들한테 입력폼 보여주고 오류가 있으면 error list 반환. 주로 th:if 사용해서 입력안된 것들 판별

- 근데 왜이렇게 중복되는 코드가 많지?
- 타입 오류는 수정이 안되는구나..

- 결국.. 고객이 입력한 값도 어딘가에서 관리가 되어야 할 거 같아..!!

##### BindingResult (Interface)
- > 타임리프는 스프링의 BindingResult 를 활용해서 편리하게 검증 오류를 표현하는 기능을 제공한다.
- > Errors interface를 상속한다.
- > 실제로 넘어오는 구현체는 BeanPropertyBindingResult 이다.
- ***★ BindingResult bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야 한다. ★***
- > #fields : #fields 로 BindingResult 가 제공하는 검증 오류에 접근할 수 있다.
- > th:errors : 해당 필드에 오류가 있는 경우에 태그를 출력한다. th:if 의 편의 버전이다.
- > th:errorclass : th:field 에서 지정한 필드에 오류가 있으면 class 정보를 추가한다.
- void addError(ObjectError oe) : error 상황에 대한 object를 추가할 수 있다.
- boolean hasErrors() : error 상황이 추가되었는지 확인할 수 있다.

###### BindingResult에 검증 오류를 적용하는 3가지 방법
- @ModelAttribute 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 FieldError 생성해서 BindingResult 에 넣어준다.
- 개발자가 직접 넣어준다. (if 문과 addError(ObjectError ...) 등 활용)
- Validator 사용 이것은 뒤에서 설명

###### ObjectError (Class)
- Global 에러 객체를 담을 수 있다.
- FieldError Class가 상속하고 있다.
