package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NoUserFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.exception.UserAlreadyExistsException;
import ru.otus.spring.model.User;
import ru.otus.spring.repository.UserRepository;
import ru.otus.spring.security.usertypes.CommonUser;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public User addUser(String firstName, String lastName, String phone, String password){
        try {
            return userRepository.save(new User(firstName, lastName, phone, bCryptPasswordEncoder.encode(password)));
        } catch (DataAccessException e) {
            if (e.contains(DuplicateKeyException.class)){
                throw new UserAlreadyExistsException("User already exists");
            }
            throw new OtherAccessException(e);
        }
    }

    public User getUserById(String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NoUserFoundException("User not found"));
    }

    public User getUserByPhone(String phone){
        return userRepository.findUserByPhone(phone)
                .orElseThrow(() -> new NoUserFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String phone) {
        return buildUserForAuthentication(getUserByPhone(phone));
    }

    private UserDetails buildUserForAuthentication(User user) {
        return CommonUser.build(user);
    }
}
