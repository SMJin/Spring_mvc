package com.hello.mvc.controller;

import com.hello.mvc.domain.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class UserController {
    private final UserRepository repository = new UserRepository();

    @GetMapping(path = "/userList")
    public void getUserList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("UserController.getUserList");
        PrintWriter writer = response.getWriter();
        writer.println(repository.getUserList().toString());
//        return repository.getUserList().toString();
    }

    @GetMapping(path = "/user")
    public void saveUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("UserController.saveUser");
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        repository.saveUser(username, age);

        PrintWriter writer = response.getWriter();
        writer.println("ok");
    }
}
