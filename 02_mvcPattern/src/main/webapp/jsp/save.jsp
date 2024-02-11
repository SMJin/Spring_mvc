<%@ page import="com.hello.mvc.domain.UserRepository" %>
<%@ page import="com.hello.mvc.domain.UserVo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
     UserRepository userRepository = UserRepository.getInstance();
     System.out.println("save.jsp");
     String username = request.getParameter("username");
     int age = Integer.parseInt(request.getParameter("age"));
     UserVo user = new UserVo(username, age);
     System.out.println("user = " + user);
     userRepository.save(user);
%>
<html>
<head>
 <meta charset="UTF-8">
</head>
<body>
성공<ul>
 <li>id=<%=user.getId()%></li>
 <li>username=<%=user.getUsername()%></li>
 <li>age=<%=user.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>