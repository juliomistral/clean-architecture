package com.hci.dao;

import com.hci.common.Repository;
import com.hci.domain.User;

public interface UserRepository extends Repository<User>{
    User findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
