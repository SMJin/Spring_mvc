## Thymeleaf 스프링 통합

#### 입력 폼 처리
- th:object
- th:field=*{object의 속성} : (선택변수 식)
#### checkbox 개선
- checkbox에 체크하지 않으면 필드 값이 아예 안넘어간다. null로 넘어가버린다.
- checkbox를 지정할때, hidden input 값으로 name="_필드명" 으로 지정하면 체크가 안되었을 때 false로 넘어간다.
- 하지만 th:field=*{선택변수} 를 지정하면 알아서 hidden input을 지정해준다.
- ${#ids.prev('변수')} 이렇게 쓰면 id 값 지정해준다
#### @ModelAttribute("모델명") 를 이용해서 메서드를 넣어놓으면 컨트롤러별로 메소드를 다 가져와서 사용할 수 있다.
