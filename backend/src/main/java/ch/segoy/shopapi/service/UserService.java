package ch.segoy.shopapi.service;


import ch.segoy.shopapi.entity.User;

import java.util.Collection;

/**
 * Created By Zhu Lin on 3/13/2018.
 */
public interface UserService {
    User findOne(String email);

    Collection<User> findByRole(String role);

    User save(User user);

    User update(User user);
}
