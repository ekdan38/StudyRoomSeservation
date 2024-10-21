package com.example.reservation.web.auth;

import com.example.reservation.domain.account.dto.AccountDto;
import com.example.reservation.domain.account.entity.AccountRole;
import com.example.reservation.domain.account.repository.AccountRepository;
import com.example.reservation.domain.account.sevice.AccountService;
import com.example.reservation.domain.mapper.AccountMapper;
import com.example.reservation.domain.security.dto.AccountLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void saveAccount() {
        AccountDto accountDto = new AccountDto(
                "testId",
                "testpwd@",
                "test",
                "010-0000-0000",
                "test@gmail.com",
                AccountRole.USER);
        accountService.accountSignup(accountDto);
    }

    private ResultActions performLogin(AccountLoginDto loginDto) throws Exception {
        return mockMvc.perform(post("/login")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Requested-With", "XMLHttpRequest")
                .content(objectMapper.writeValueAsString(loginDto)));
    }

    private void expectLoginFail(String message, ResultActions perform) throws Exception {
        perform.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("errors").value("Authentication failed"))
                .andExpect(jsonPath("message").value(message));
    }

    @Test
    public void getCsrfToken() throws Exception {
        //given
        ResultActions perform = mockMvc.perform(get("/login"));

        //when & then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공")
    public void login_success() throws Exception {
        //given
        AccountLoginDto loginDto = new AccountLoginDto("testId", "testpwd@");

        //when
        ResultActions perform = mockMvc.perform(post("/login")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Requested-With", "XMLHttpRequest")
                .content(objectMapper.writeValueAsString(loginDto)));

        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("Success Login"));
    }


    @Test
    @Transactional
    @DisplayName("로그인 실패_loginId가 존재하지 않음(잘못 입력)")
    public void login_fail_UsernameNotFound() throws Exception {
        //given
        AccountLoginDto loginDto = new AccountLoginDto("failLoginId", "testpwd@");

        //when
        ResultActions perform = performLogin(loginDto);

        //then
        expectLoginFail("Can't find by loginId",perform);
    }

    @Test
    @Transactional
    @DisplayName("로그인 실패_password 틀림")
    public void login_fail_BadCredentials() throws Exception {
        //given
        AccountLoginDto loginDto = new AccountLoginDto("testId", "wrongPassword");

        //when
        ResultActions perform = performLogin(loginDto);


        //then
        expectLoginFail("Invalid password",perform);
    }

    @Test
    @Transactional
    @DisplayName("로그인 실패_ajaxHeader 존재 하지 않음")
    public void login_fail_MissingHeader() throws Exception {
        //given
        AccountLoginDto loginDto = new AccountLoginDto("failLoginId", "testpwd@");

        //when
        ResultActions perform = mockMvc.perform(post("/login")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        //then
        expectLoginFail("Missing X-Requested-With Header",perform);
    }

    @Test
    @Transactional
    @DisplayName("로그인 실패_ajaxHeader 값이 불일치")
    public void login_fail_UnsupportedHeader() throws Exception {
        //given
        AccountLoginDto loginDto = new AccountLoginDto("failLoginId", "testpwd@");

        //when
        ResultActions perform = mockMvc.perform(post("/login")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Requested-With", "XMLHttp")
                .content(objectMapper.writeValueAsString(loginDto)));

        //then
        expectLoginFail("This Header is not Supported",perform);
    }





}