package com.pedigo.Kontraktor.dao;

import com.pedigo.Kontraktor.exception.FailedToInsertException;
import com.pedigo.Kontraktor.exception.UserNotFoundException;
import com.pedigo.Kontraktor.model.User;

import java.util.List;
import java.util.UUID;

public interface UserDAO {
    int insertUser(UUID id, User user) throws FailedToInsertException;

    default int insertUser(User user) throws FailedToInsertException {
        UUID id = UUID.randomUUID();
        return insertUser(id, user);
    }

    List<User> getAllUsers();

    User getUserById(UUID id) throws UserNotFoundException;

    int deleteUser(UUID id) throws UserNotFoundException;

    int updateUserById(UUID id, User user) throws UserNotFoundException;
}
