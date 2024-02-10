package com.hello.mvc.domain;

import java.util.HashMap;

public class UserRepository {
    private Long id = 1L;
    private HashMap<Long, UserVo> map = new HashMap<>();

    public HashMap<Long, UserVo> getUserList() {
        return map;
    }

    public void saveUser(String username, int age) {
        UserVo newUser = new UserVo();
        newUser.setId(id);
        newUser.setUsername(username);
        newUser.setAge(age);
        map.put(id++, newUser);
    }
}
