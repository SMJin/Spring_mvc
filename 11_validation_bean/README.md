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
- Interface 를 분리해서 지정해서 다른 Model로 분리한다.
- 그리고 Model 들마다 적용할 검증코드를 등록해준다. 다음과 같이 말이다.
```java
@NotNull(groups = UpdateCheck.class) //수정시에만 적용
 private Long id;
 @NotBlank(groups = {SaveCheck.class, UpdateCheck.class}) // 등록 및 수정 둘다 적
 private String itemName;
```
- 참고: @Valid 에는 groups를 적용할 수 있는 기능이 없다. 따라서 groups를 사용하려면 @Validated 를 사용해야 한다.
- 그.러.나..!!
- 데이터 전달을 위해 보통은 별도의 객체를 사용하고, 등록, 수정용 폼 객체를 나누면 등록, 수정이 완전히 분리되기 때문에 groups 를 적용할 일은 드물다.
- 데이터를 넘기는 등록/수정시의 두 가지의 방법이 있는데 ...
1. 폼 데이터 전달에 Item 도메인 객체 사용
*HTML Form -> Item -> Controller -> Item -> Repository*
- > 장점: Item 도메인 객체를 컨트롤러, 리포지토리 까지 직접 전달해서 중간에 Item을 만드는 과정이 없어서 간단하다.
- > 단점: 간단한 경우에만 적용할 수 있다. 수정시 검증이 중복될 수 있고, groups를 사용해야 한다.
2. 폼 데이터 전달을 위한 별도의 객체 사용
*HTML Form -> ItemSaveForm -> Controller -> Item 생성 -> Repository*
- > 장점: 전송하는 폼 데이터가 복잡해도 거기에 맞춘 별도의 폼 객체를 사용해서 데이터를 전달 받을 수 있다.
  > 보통 등록과, 수정용으로 별도의 폼 객체를 만들기 때문에 검증이 중복되지 않는다.
- > 단점: 폼 데이터를 기반으로 컨트롤러에서 Item 객체를 생성하는 변환 과정이 추가된다.
  

##### 복습
1. @ModelAttribute 는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용한다.
2. @RequestBody 는 HTTP Body의 데이터를 객체로 변환할 때 사용한다. 주로 API JSON 요청을 다룰 때 사용한다.

##### @RequestBody 이용한 JSON 데이터를 @Validated 적용하기.
- API와 예외처리에서 배움.
```java
// 파라미터 예시
@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult
```
- API의 경우 3가지 경우를 나누어 생각해야 한다.
1. 성공 요청: 성공
2. 실패 요청: JSON을 객체로 생성하는 것 자체가 실패함 (== 타입 자체가 안맞음, 이렇게 되면 컨트롤러 자체가 호출이 안됨.)
3. 검증 오류 요청: JSON을 객체로 생성하는 것은 성공했고, 검증에서 실패함 (== 내가 설정한 검증 오류 발생)

##### @ModelAttribute vs @RequestBody
- @ModelAttribute 는 필드 단위로 정교하게 바인딩이 적용된다.
- 그러니까, 타입이 맞지 않아도 어쨌든 컨트롤러는 반환된다.
- @RequestBody 는 HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면 이후 단계 자체가 진행되지 않고 예외가 발생한다.
- 타입이 안맞으면 컨트롤러 자체도 호출되지 않는다. => 따라서 이럴 때 원하는 모양으로 에러를 반환해주는 것이 중요하다.
