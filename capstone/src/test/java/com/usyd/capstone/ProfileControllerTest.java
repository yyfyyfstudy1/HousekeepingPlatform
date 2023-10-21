package com.usyd.capstone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.VO.ProfileUpdate;
import com.usyd.capstone.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

// Static imports
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
//    @MockBean
//    private UserService userService;
    @Test
    public void testShowProfile() throws Exception {
        mockMvc.perform(get("/user/profile").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Ishikawa Hikaru"));

    }

    @Test
    public void testUpdateProfile() throws Exception {
        // Given
        ProfileUpdate profileUpdate = new ProfileUpdate();
        profileUpdate.setId(1L);
        profileUpdate.setName("Jacoco");
        // Set other fields if needed, like phone, address, etc.

        // Mocking the service call to return true when `updateProfile` is called
//        when(userService.updateProfile(profileUpdate)).thenReturn(Result.suc(true));

        // When & Then
        mockMvc.perform(post("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(profileUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.data").value(true));
 
        profileUpdate.setId(1L);
        profileUpdate.setName("Ishikawa Hikaru");
        mockMvc.perform(post("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(profileUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.data").value(true));
    }

    // 将对象转换为 JSON 字符串的辅助方法
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
