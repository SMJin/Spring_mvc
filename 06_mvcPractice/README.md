# 데모 프로젝트 생성하기
#### 개발환경
- Java 17
- Gradle (build tool)
- Spring Boot 3.2.3
- Jar (Tymeleaf)

## PRG 패턴
#### Post / Redirect / Get
- 상품 등록을 완료하고 웹 브라우저에 새로고침 버튼을 누르면 상품이 계속해서 중복 등록되는 현상이 있다. 왜일까?
#### 새로고침을 했을 때, 마지막 POST 요청을 그대로 보내기 때문이다.
- 그래서 보통 POST 를 완료하였을 시, Redirect를 통해서 GET 요청이 마지막이 되도록 하는 것이다.
- 그러면 새로고침을 해도, 마지막 요청이 GET 요청이기 때문에 문제가 해결된다.

## RedirectAttributes
- RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVariable , 쿼리 파라미터까지 처리해준다.
```
redirectAttributes.addAttribute("itemId", savedItem.getId());
redirectAttributes.addAttribute("status", true);
```
- redirect:/basic/items/{itemId}
- pathVariable 바인딩: {itemId}
- 나머지는 쿼리 파라미터로 처리: ?status=true
