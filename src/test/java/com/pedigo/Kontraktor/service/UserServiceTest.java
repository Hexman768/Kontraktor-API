package com.pedigo.Kontraktor.service;

import com.pedigo.Kontraktor.dao.FakeUserDataAccessService;
import com.pedigo.Kontraktor.dao.UserDAO;
import com.pedigo.Kontraktor.exception.FailedToInsertException;
import com.pedigo.Kontraktor.exception.UserNotFoundException;
import com.pedigo.Kontraktor.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    UserService mockUserService;

    @Mock
    UserDAO userDao;

    private static FakeUserDataAccessService fakeService;
    private static UserService userService;

    private static final User USER = new User(UUID.randomUUID(), "John", "Doe");

    @Before
    public void setup() {
        fakeService = new FakeUserDataAccessService();
        userService = new UserService(fakeService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        new UserService(null);
    }

    @Test
    public void testAddUser_ReturnsRowsAffcted() throws FailedToInsertException {
        when(userDao.insertUser(any(User.class))).thenReturn(1);

        assertThat(mockUserService.addUser(USER)).isEqualTo(1);
    }

    @Test
    public void testGetAllUsers_ReturnsAllUsers() {
        List<User> users = Arrays.asList(USER);

        when(userDao.getAllUsers()).thenReturn(users);

        assertThat(mockUserService.getAllUsers()).isEqualTo(users);
    }

    @Test
    public void testGetUserById_ReturnsCorrectUser() throws UserNotFoundException {
        when(userDao.getUserById(USER.getId())).thenReturn(USER);

        assertThat(mockUserService.getUserById(USER.getId())).isEqualTo(USER);
    }

    @Test
    public void testDeleteUser_ReturnsRowAffected() throws UserNotFoundException {
        when(userDao.deleteUser(USER.getId())).thenReturn(1);

        assertThat(mockUserService.deleteUser(USER.getId())).isEqualTo(1);
    }

    @Test
    public void testUpdateUserById_ReturnsRowsAffected() throws UserNotFoundException {
        when(userDao.updateUserById(USER.getId(), USER)).thenReturn(1);

        assertThat(mockUserService.updateUserById(USER.getId(), USER)).isEqualTo(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteUser_NullId_ThrowsIllegalArgumentException() throws UserNotFoundException {
        userService.deleteUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserById_NullId_ThrowsIllegalArgumentException() throws UserNotFoundException {
        userService.updateUserById(null, USER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserById_NullUser_ThrowsIllegalArgumentException() throws UserNotFoundException {
        userService.updateUserById(USER.getId(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUser_NullFirstName_ThrowsIllegalArgumentException() throws FailedToInsertException {
        userService.addUser(new User(null, null, "Test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUser_NullLastName_ThrowsIllegalArgumentException() throws FailedToInsertException {
        userService.addUser(new User(null, "Test", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUserById_NullId_ThrowsIllegalArgumentException() throws UserNotFoundException {
        userService.getUserById(null);
    }
}
