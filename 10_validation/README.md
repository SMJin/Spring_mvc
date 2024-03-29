# 검증 1 - Validation

###### 깨알 꿀팁
- @ModelAttribute를 사용하면 해당 객체를 그대로 다시 model에 넣어준다.
- SpringEL 문법 중 null 값을 처리할 때 좋은 방법이 있다. errors?.containsKey('...') 이런식으로 하는걸 Safe Navigation Operator 라고 한다.

##### error Map 직접 개발
- thymeleaf 에는 th:classappend= 라는걸 사용해서 class를 추가할 수 있다.
- 먼저 고객들한테 입력폼 보여주고 오류가 있으면 error list 반환한다.
- 주로 th:if 사용해서 입력안된 값들에 대한 경고 띄운다.

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
```java
// error 상황에 대한 object를 추가할 수 있다.
void addError(ObjectError oe)

// error 상황이 추가되었는지 확인할 수 있다.
boolean haerrors()
```

###### BindingResult에 검증 오류를 적용하는 3가지 방법
- @ModelAttribute 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 FieldError 생성해서 BindingResult 에 넣어준다.
- 개발자가 직접 넣어준다. (if 문과 addError(ObjectError ...) 등 활용)
- Validator 사용 이것은 뒤에서 설명

###### ObjectError (Class)
- Global 에러 객체를 담을 수 있다.
- FieldError Class가 상속하고 있다.

###### FieldError 생성자
- FieldError 는 두 가지 생성자를 제공한다. 
```java
public FieldError(String objectName, String field, String defaultMessage);
public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure,
                  @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)

// 예시
new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다.")
```
- <파라미터 목록 설명>
- > objectName : 오류가 발생한 객체 이름
- > field : 오류 필드
- > rejectedValue : 사용자가 입력한 값(거절된 값)
- > bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
- > codes : 메시지 코드
- > arguments : 메시지에서 사용하는 인자
- > defaultMessage : 기본 오류 메시지
