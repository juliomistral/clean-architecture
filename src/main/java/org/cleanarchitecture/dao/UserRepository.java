package org.cleanarchitecture.dao;

import org.cleanarchitecture.common.dao.Repository;
import org.cleanarchitecture.domain.User;

public interface UserRepository extends Repository<User> {
    User findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
    boolean existsByContactEmail(String email);
}
