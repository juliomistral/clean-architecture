package com.hci.dao;

import com.hci.common.dao.Repository;
import com.hci.domain.User;

public interface UserRepository extends Repository<User>{
    User findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
    boolean existsByContactEmail(String email);
}
