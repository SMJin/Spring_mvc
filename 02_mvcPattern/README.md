# 서블릿, JSP, MVC 패턴

## 요구사항
- 회원정보(이름, 나이)로 구성
- 회원 저장과 목록 조회 구현

## 개념정리
#### RequestDispatcher
- 현재 reqeust의 정보를 갖고 있다가 다른 jsp 페이지로 해당 정보를 redirect 해주는 클래스
- dispatcher.forward(request, response) : 다른 서블릿이나 jsp로 이동할 수 있는 기능이다.
- url 정보는 Dispatcher를 호출할 때 넣을 수 있다.
- jsp 파일 경로를 노출시키는 것보다 이런식으로 컨트롤러를 거쳐서 jsp로 이동하도록 하는 것이 좋다.

#### redirect vs forward
- 리다이렉트는 실제 클라이언트(웹 브라우저)에 응답이 나갔다가, 클라이언트가 redirect 경로로 다시 요청한다.
- 따라서 클라이언트가 인지할 수 있고, URL 경로도 실제로 변경된다.
- 반면에 포워드는 서버 내부에서 일어나는 호출이기 때문에 클라이언트가 전혀 인지하지 못한다.

## 오류정리
#### 맞딱트린 오류 정리
- HttpServlet 클래스를 사용하고 출력하기 위해서는 메인 Application에 **@ServletComponentScan** 을 추가해야한다.
