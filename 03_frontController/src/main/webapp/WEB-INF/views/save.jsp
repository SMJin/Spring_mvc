<%@ page import="com.hello.mvc.domain.MemberRepository" %>
<%@ page import="com.hello.mvc.domain.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
     MemberRepository memberRepository = MemberRepository.getInstance();
     System.out.println("save.jsp");
     String username = request.getParameter("username");
     int age = Integer.parseInt(request.getParameter("age"));
     Member member = new Member(username, age);
     System.out.println("user = " + member);
     MemberRepository.save(member);
%>
<html>
<head>
 <meta charset="UTF-8">
</head>
<body>
성공<ul>
 <li>id=<%=member.getId()%></li>
 <li>username=<%=member.getUsername()%></li>
 <li>age=<%=member.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>