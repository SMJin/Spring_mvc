package com.hello.mvc.domain.user;

import com.hello.mvc.domain.UserRepository;
import com.hello.mvc.domain.UserVo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserRepositoryTest {
    UserRepository userRepository = UserRepository.getInstance();

    @AfterEach
    void afterEach() {
        userRepository.clearStore();
    }

    @Test
    void save() {
        // given
        UserVo user = new UserVo("hello", 20);

        // when
        UserVo savedUser = userRepository.save(user);

        // then
        UserVo findUser = userRepository.findById(savedUser.getId());
        Assertions.assertThat(findUser).isEqualTo(savedUser);
    }

    @Test
    void findAll() {
        // given
        UserVo user1 = new UserVo("member1", 20);
        UserVo user2 = new UserVo("member2", 22);

        userRepository.save(user1);
        userRepository.save(user2);

        // when
        List<UserVo> result = userRepository.findAll();

        // then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}
