package com.pedigo.Kontraktor.dao;

import com.pedigo.Kontraktor.exception.UserNotFoundException;
import com.pedigo.Kontraktor.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("fakeDao")
public class FakeUserDataAccessService implements UserDAO {
    private static List<User> DB = new ArrayList<User>();

    @Override
    public int insertUser(UUID id, User user) throws IllegalArgumentException {
        if (user == null || user.getFirstName() == null || user.getLastName() == null) {
            throw new IllegalArgumentException("User object cannot be null or contain null values!");
        }
        DB.add(new User(id, user.getFirstName(), user.getLastName()));
        return 0;
    }

    @Override
    public List<User> getAllUsers() {
        return DB;
    }

    @Override
    public User getUserById(UUID id) throws UserNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("UUID object cannot be null!");
        }
        return DB.stream().filter(x -> id.equals(x.getId()))
                .findFirst().orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    @Override
    public int deleteUser(UUID id) throws UserNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("UUID object cannot be null!");
        }
        if (DB.remove(getUserById(id))) {
            return 0;
        }
        throw new UserNotFoundException(id.toString());
    }

    @Override
    public int updateUserById(UUID id, User user) throws UserNotFoundException {
        User existingUser = getUserById(id);
        DB.set(DB.indexOf(existingUser), new User(id, user.getFirstName(), user.getLastName()));
        return 0;
    }
}
