package com.example.reservation.web.auth;


import com.example.reservation.domain.account.repository.AccountRepository;
import com.example.reservation.domain.account.sevice.AccountService;
import com.example.reservation.domain.mapper.AccountMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("인증 정보가 없어서 EntryPoint 호출")
    public void required_Authentication() throws Exception {
        //given
        ResultActions resultActions = mockMvc.perform(get("/protected")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        //when & then
        resultActions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("errors").value("Authentication required"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("redirectUrl").value("/protected"));

    }

    @Test
    @DisplayName("인증 후 권한 불일치 DeniedHandler 호출")
    @Transactional
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void DeniedHandler() throws Exception {
        //given
        ResultActions resultActions = mockMvc.perform(get("/manager")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        //when & then
        resultActions.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("errors").value("Insufficient permissions"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("redirectUrl").value("/manager"));
    }


}
