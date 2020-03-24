package com.pedigo.Kontraktor.service;

import com.pedigo.Kontraktor.dao.UserDAO;
import com.pedigo.Kontraktor.exception.FailedToInsertException;
import com.pedigo.Kontraktor.exception.UserNotFoundException;
import com.pedigo.Kontraktor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserService(@Qualifier("postgres") UserDAO userDAO) {
        if (userDAO == null) {
            throw new IllegalArgumentException("UserDAO object cannot be null!");
        }
        this.userDAO = userDAO;
    }

    public int addUser(User user) throws FailedToInsertException {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null!");
        }
        return userDAO.insertUser(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUserById(UUID id) throws UserNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("UUID object cannot be null!");
        }
        return userDAO.getUserById(id);
    }

    public int deleteUser(UUID id) throws UserNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("UUID object cannot be null!");
        }
        return userDAO.deleteUser(id);
    }

    public int updateUserById(UUID id, User user) throws UserNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("UUID object cannot be null!");
        } else if (user == null) {
            throw new IllegalArgumentException("User object cannot be null!");
        }
        return userDAO.updateUserById(id, user);
    }
}
