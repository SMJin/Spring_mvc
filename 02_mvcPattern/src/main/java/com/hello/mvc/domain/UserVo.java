package com.hello.mvc.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVo {
    private Long id;
    private String username;
    private int age;

    @Override
    public String toString() {
        return "UserVo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
