package io.cess.auth.service;

import io.cess.auth.entity.User;

public interface UserService {
    User loginUser(String username);

    User findById(long l);
}
