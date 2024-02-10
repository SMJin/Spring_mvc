package com.hello.mvc.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 동시성 문제가 고려되지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
*
* ConcurrentHashMap 클래스란?
*  => Multi-Thread 환경에서 사용할 수 있도록 나온 클래스
*  => 읽기 작업에는 여러 쓰레드가 동시에 읽을 수 있지만, 쓰기 작업에는 특정 세그먼트 or 버킷에 대한 Lock을 사용
*
* AtomicLong 이란?
*  => Long 자료형을 갖고 있는 Wrapping 클래스
*  => Thread-safe로 구현되어 멀티쓰레드에서 synchronized 없이 사용
* */
public class UserRepository {
    private static long sequence = 0L;
    private static Map<Long, UserVo> store = new HashMap<>();

    private static final UserRepository instance = new UserRepository();

    public static UserRepository getInstance() {
        return instance;
    }

    public UserRepository() {

    }

    public UserVo save(UserVo userVo) {
        userVo.setId(++sequence);
        store.put(userVo.getId(), userVo);
        return userVo;
    }

    public UserVo findById(Long id) {
        return store.get(id);
    }

    public List<UserVo> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
