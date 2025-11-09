package org.LosenkovIvan.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.LosenkovIvan.userService.dto.UserDto;
import org.LosenkovIvan.userService.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void create_returns_created() throws Exception {
        UserDto req = new UserDto(null, "Misha", "misha@example.com", 20, LocalDateTime.now());
        UserDto resp = new UserDto(1L, req.getName(), req.getEmail(), req.getAge(), req.getCreatedAt());
        given(userService.create(any(UserDto.class))).willReturn(resp);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
        verify(userService).create(any(UserDto.class));
    }

    @Test
    void get_returns_ok() throws Exception {
        UserDto resp = new UserDto(2L, "Alexander", "alexander@example.com", 42, LocalDateTime.now());
        given(userService.get(2L)).willReturn(resp);
        mockMvc.perform(get("/api/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alexander@example.com"));
        verify(userService).get(2L);
    }

    @Test
    void list_returns_ok() throws Exception {
        List<UserDto> resp = List.of(
                new UserDto(1L, "M", "m@e.com", 30, LocalDateTime.now()),
                new UserDto(2L, "A", "a@e.com", 45, LocalDateTime.now())
        );
        given(userService.list()).willReturn(resp);
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
        verify(userService).list();
    }

    @Test
    void update_returns_ok() throws Exception {
        UserDto req = new UserDto(null, "New", null, 33, null);
        UserDto resp = new UserDto(3L, "New", "c@e.com", 33, LocalDateTime.now());
        given(userService.update(eq(3L), any(UserDto.class))).willReturn(resp);
        mockMvc.perform(put("/api/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"));
        verify(userService).update(eq(3L), any(UserDto.class));
    }

    @Test
    void delete_returns_no_content() throws Exception {
        mockMvc.perform(delete("/api/users/4"))
                .andExpect(status().isNoContent());
        verify(userService).delete(4L);
    }
}