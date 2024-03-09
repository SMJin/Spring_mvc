# Spring MVC :: 기본기능

## WAR vs JAR
#### WAR :: Web Application Achive
- Servlet 이나 JSP 으로 동작하는 웹 애플리케이션 전체를 packing하기 위한 압축파일 포맷
- 사전 정의된 구조인 WEB-INF 를 사용
- 별도의 웹 서버(ex. apache tomcat)나 웹 컨테이너(AWS)를 필요로 한다.
#### JAR :: Java ARchive
- Java Application 이 동작할 수 있도록 오로지 Java Project를 압축한 파일
- JVM (Java Virtual Machine)에서 직접 실행하기 때문에 별도의 웹 컨테이너나 서버가 불필요
- JRE (Java Runtime Environment)만 있어도 실행 가능
