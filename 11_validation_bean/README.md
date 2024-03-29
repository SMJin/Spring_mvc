# Bean Validation
- Bean Validation 2.0(JSR-380)이라는 기술 표준
- 일반적으로 구현체는 하이버네이트 Validator이다. (이름이 하이버네이트가 붙어서 그렇지 ORM과는 관련이 없다.)
```java
// 이 기술을 사용하려면 다음 라이브러리를 build.gradle 에 추가해주어야 한다.
implementation 'org.springframework.boot:spring-boot-starter-validation'
```
##### @Data 객체에 어노테이션으로 검증하는 것.
- 예시로 @NotBlank, @NotNull, @Range(min = 100, max = 1000000), @Max(9999) 등이 있다.
- 반드시 @Validated 가 있어야 해당 모델 @ModelAttribute Item item 을 검증한다.
- 검증한 모델의 error 메시지나 결과들은 바로 다음 파라미터로 전달된 BindingResult bindingResult 에 담긴다.

##### 검증 순서
1. @ModelAttribute 각각의 필드에 타입 변환 시도
- > 1. 성공하면 다음으로
- > 2. 실패하면 typeMismatch 로 FieldError 추가
2. Validator 적용

##### 에러 코드 관리
- @검증애노테이션 기준으로 오류코드가 등록된다.
- 예를들어, @NotBlack의 경우, 다음과 같은 순서대로 MessageCodesResolver 를 통해 오류코드가 호출된다.
1. NotBlank.item.itemName
2. NotBlank.itemName
3. NotBlank.java.lang.String
4. NotBlank
- BeanValidation 메시지 찾는 순서
1. 생성된 메시지 코드 순서대로 messageSource 에서 메시지 찾기
2. 애노테이션의 message 속성 사용 @NotBlank(message = "공백! {0}")
3. 라이브러리가 제공하는 기본 값 사용 공백일 수 없습니다.
```java
// message 속성 사용
@NotBlank(message = "공백은 입력할 수 없습니다.")
private String itemName;
```
##### 오브젝트 오류 관리 (글로벌 오류를 말함)
- @ScriptAssert 사용하기
- 하지만 .. 너무 복잡해서 그냥 코드에서 처리하는 방식이 더 좋을 수도 있다.

##### 하지만 ... 이렇게 너무 좋은 @검증애노테이션의 side effect 가 있다면 ..?
- 바로바로바로 동일한 Model에 대해서 검증 조건이 다른 경우가 있을 수 있다 !
- 예를들어 동일한 item 모델에 대해서 등록시에는 id가 not null이 아니지만.. 수정일 때는 not null이어야 한다면?!
- 이런 상황에서는 어떻게 상황을 타개해야 할까?? 크게 두 가지의 방법이 있다.
1. BeanValidation의 ***groups 기능***을 사용한다.
2. Item을 직접 사용하지 않고, ItemSaveForm, ItemUpdateForm 같은 폼 전송을 위한 ***별도의 모델 객체***를 만들어서 사용한다.

##### BeanValidation의 Groups 기능에 대하여 ...
- 
