# 서블릿 Servlet

## Jar 배포 vs War 배포
#### JAR : Java ARchive
- 독립적인 Java 애플리케이션을 패키징한다.
- 실행가능한 JAR 파일 생성
- JVM에서 직접 실행하기 때문에 별도의 웹 컨테이너나 서버가 필요하지 않다.
- ***즉, JAR는 JRE(Java Runtime Environment)만 존재하면 구동이 가능하다.**
#### WAR : Web Application aRchive
- 서블릿/jsp컨테이너에 배치할 수 있는 웹 어플리케이션(Web application) 압축 파일 포맷이다.
- 웹 응용 프로그램을 위한 포맷이기 때문에 웹 관련 자원만 포함하고 있다.
- WAR 파일을 실행하려면 Tomcat 과 같은 웹 서버 혹은 웹 컨테이너가 필요하다.
- ***즉, WAR는 웹서버 혹은 WAS(웹 컨테이너)가 있으야 구동이 가능하다.**

## 서블릿 프로젝트에서는 jsp를 사용할 것이기 때문에 WAR 파일로 구동을 할 것이다.
