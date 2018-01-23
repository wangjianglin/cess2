package io.cess.auth.service.impl;

import io.cess.auth.entity.User;
import io.cess.auth.repository.UserRepository;
import io.cess.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Component
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static Pattern emailPattern = Pattern.compile(".{1,}@.{1,}\\..{1,}");

    private static Pattern digitPattern = Pattern.compile("\\d*");

    @Override
    public User loginUser(String username) {

        User user = null;
        if(emailPattern.matcher(username).matches()) {
            user = userRepository.findByEmail(username);
            if(user != null){
                return user;
            }
        }

        if(digitPattern.matcher(username).matches()){
            user = userRepository.findByMobile(username);
            if(user != null){
                return user;
            }
        }

        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).get();
    }

}
