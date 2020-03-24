package com.pedigo.Kontraktor.api;

import com.pedigo.Kontraktor.exception.FailedToInsertException;
import com.pedigo.Kontraktor.exception.UserNotFoundException;
import com.pedigo.Kontraktor.model.User;
import com.pedigo.Kontraktor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/user")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) throws IllegalArgumentException {
        if (userService == null) {
            throw new IllegalArgumentException();
        }
        this.userService = userService;
    }

    @PostMapping
    @ResponseBody
    public int addUser(@Valid @NotNull @RequestBody User user) throws FailedToInsertException {
        return userService.addUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "{id}")
    public User getUserById(@NotNull @PathVariable("id") UUID id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @DeleteMapping(path = "{id}")
    public int deleteUser(@NotNull @PathVariable("id") UUID id) throws UserNotFoundException {
        return userService.deleteUser(id);
    }

    @PutMapping(path = "{id}")
    public int updateUserById(@NotNull @PathVariable("id") UUID id, @Valid @NotNull @RequestBody User user)
            throws UserNotFoundException {
        return userService.updateUserById(id, user);
    }
}
