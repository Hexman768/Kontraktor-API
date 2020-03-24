package com.pedigo.Kontraktor.api;

import com.pedigo.Kontraktor.exception.UserNotFoundException;
import com.pedigo.Kontraktor.model.User;
import com.pedigo.Kontraktor.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testAddUser_ReturnsOkStatus() throws Exception {
        String requestJson = "{\"firstName\":\"John\", \"lastName\":\"Doe\"}";

        mockMvc.perform(post("/api/v1/user/")
                .content(requestJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testAddUser_BlankFirstName_ReturnsBadRequest() throws Exception {
        String requestJson = "{\"firstName\":\"\", \"lastName\":\"Doe\"}";

        mockMvc.perform(post("/api/v1/user/")
                .content(requestJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddUser_BlankLastName_ReturnsBadRequest() throws Exception {
        String requestJson = "{\"firstName\":\"John\", \"\":\"Doe\"}";

        mockMvc.perform(post("/api/v1/user/")
                .content(requestJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllUsers_ReturnsAllUsersJson() throws Exception {
        User user = new User(UUID.randomUUID(), "John", "Doe");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

        mockMvc.perform( MockMvcRequestBuilders
                .get("/api/v1/user/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':"
                        + user.getId().toString()
                        + ",'firstName':'John', 'lastName':'Doe'}]"));
    }

    @Test
    public void testGetUserById_ReturnsUserJson() throws Exception {
        User user = new User(UUID.randomUUID(), "John", "Doe");

        when(userService.getUserById(user.getId())).thenReturn(user);

        mockMvc.perform( MockMvcRequestBuilders
                .get("/api/v1/user/" + user.getId().toString() + "/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':"
                        + user.getId().toString()
                        + ",'firstName':'John', 'lastName':'Doe'}"));
    }

    @Test(expected = NestedServletException.class)
    public void testGetUserById_InvalidId_ReturnsInternalServerError() throws Exception {
        when(userService.getUserById(any(UUID.class))).thenThrow(new UserNotFoundException("MockId"));

        mockMvc.perform( MockMvcRequestBuilders
                .get("/api/v1/user/" + UUID.randomUUID() + "/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteUserById_ReturnsOkStatus() throws Exception {
        when(userService.deleteUser(any(UUID.class))).thenReturn(1);

        mockMvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/user/" + UUID.randomUUID() + "/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void testDeleteUserById_InvalidId_ReturnsInternalServerError() throws Exception {
        when(userService.deleteUser(any(UUID.class))).thenThrow(new UserNotFoundException("MockId"));

        mockMvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/user/" + UUID.randomUUID() + "/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateUserById_ReturnsOkStatus() throws Exception {
        String requestJson = "{\"firstName\":\"John\", \"lastName\":\"Doe\"}";

        when(userService.updateUserById(any(UUID.class), any(User.class))).thenReturn(1);

        mockMvc.perform(put("/api/v1/user/" + UUID.randomUUID().toString())
                .content(requestJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void testUpdateUserById_InvalidId_ReturnsInternalServerError() throws Exception {
        String requestJson = "{\"firstName\":\"John\", \"lastName\":\"Doe\"}";

        when(userService.updateUserById(any(UUID.class), any(User.class))).thenThrow(new UserNotFoundException("MockId"));

        mockMvc.perform(put("/api/v1/user/" + UUID.randomUUID().toString())
                .content(requestJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullValue_ThrowsIllegalArgumentException() throws IllegalArgumentException {
        new UserController(null);
    }
}
