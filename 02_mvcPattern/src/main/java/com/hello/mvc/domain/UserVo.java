package com.hello.mvc.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

@Getter
@Setter
public class UserVo {
    private Long id;
    private String username;
    private int age;

    public UserVo(String username, int age) {
        this.username = username;
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
