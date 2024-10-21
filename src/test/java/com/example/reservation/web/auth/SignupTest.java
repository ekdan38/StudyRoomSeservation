package com.example.reservation.web.auth;

import com.example.reservation.domain.account.dto.AccountDto;
import com.example.reservation.domain.account.entity.AccountRole;
import com.example.reservation.domain.account.repository.AccountRepository;
import com.example.reservation.domain.account.sevice.AccountService;
import com.example.reservation.domain.mapper.AccountMapper;
import com.example.reservation.web.auth.dto.AccountSignupRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class SignupTest {

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

    private ResultActions performSignup(AccountSignupRequestDto requestDto) throws Exception {
        return mockMvc.perform(post("/signup")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
    }

    private void expectSignupFieldErrors(ResultActions perform) throws Exception {
        perform
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0]").exists())
                .andExpect(jsonPath("errors[0]").exists())
                .andExpect(jsonPath("errors[0]").exists())
        ;
    }


    @Test
    @Transactional
    @DisplayName("회원가입 성공")
    public void sigunup_success() throws Exception {
        //given
        AccountSignupRequestDto requestDto = new AccountSignupRequestDto(
                "testId",
                "testpwd@",
                "test",
                "010-0000-0000",
                "test@gmail.com",
                "USER");

        //when
        ResultActions perform = mockMvc.perform(post("/signup")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("message").value("Success Signup"))
                .andExpect(jsonPath("account.id").exists())
                .andExpect(jsonPath("account.loginId").value("testId"))
                .andExpect(jsonPath("account.name").value("test"))
                .andExpect(jsonPath("account.email").value("test@gmail.com"));
    }

    @Test
    @Transactional
    @DisplayName("회원 가입 실패_logId 가 이미 존재함")
    public void sigunup_fail_exists_loginId() throws Exception {
        //given
        AccountDto accountDto = new AccountDto("testId",
                "testpwd@",
                "test",
                "010-0000-0000",
                "test@gmail.com",
                AccountRole.USER);
        accountService.accountSignup(accountDto);

        AccountSignupRequestDto requestDto = new AccountSignupRequestDto(
                "testId",
                "testpwd@",
                "test",
                "010-0000-0000",
                "test@gmail.com",
                "USER");

        //when
        ResultActions perform = mockMvc.perform(post("/signup")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @ParameterizedTest
    @CsvSource({
            ",",
            " ,",
            "Id",
            "testUserIdtestUserIdtestUserId",
            "testloginId@"

    })
    @Transactional
    @DisplayName("회원가입 실패_loginIdField 오류")
    public void signup_fail_invalid_loginIdField(String loginId) throws Exception {
        //given
        AccountSignupRequestDto requestDto = new AccountSignupRequestDto(
                loginId,
                "testpassword@",
                "test",
                "010-0000-0000",
                "test@gmail.com",
                "USER");

        //when
        ResultActions resultActions = performSignup(requestDto);

        //then
        expectSignupFieldErrors(resultActions);
    }

    @ParameterizedTest
    @CsvSource({
            ", ",
            " ,",
            "pwd",
            "testPwdtestPwdtestPwd",
            "testPassworld"
    })
    @Transactional
    @DisplayName("회원가입 실패_passwordField 오류")
    public void signup_fail_invalid_passwordField(String password) throws Exception {
        //given
        AccountSignupRequestDto requestDto = new AccountSignupRequestDto(
                "testId",
                password,
                "test",
                "010-0000-0000",
                "test@gmail.com",
                "USER");

        //when
        ResultActions resultActions = performSignup(requestDto);

        //then
        expectSignupFieldErrors(resultActions);

    }

    @ParameterizedTest
    @CsvSource({
            ", ",
            " ,",
            "n",
            "testnameistolong",
    })
    @Transactional
    @DisplayName("회원가입 실패_nameField 오류")
    public void signup_fail_invalid_nameField(String name) throws Exception {
        //given
        AccountSignupRequestDto requestDto = new AccountSignupRequestDto(
                "testId",
                "testpassword@",
                name,
                "010-0000-0000",
                "test@gmail.com",
                "USER");

        //when
        ResultActions resultActions = performSignup(requestDto);

        //then
        expectSignupFieldErrors(resultActions);
    }

    @ParameterizedTest
    @CsvSource({
            ", ",
            " ,",
            "testEmail",
    })
    @Transactional
    @DisplayName("회원가입 실패_emailField 오류")
    public void signup_fail_invalid_emailField(String email) throws Exception {
        //given
        AccountSignupRequestDto requestDto = new AccountSignupRequestDto(
                "testId",
                "testpassword@",
                "test",
                "010-0000-0000",
                email,
                "USER");

        //when
        ResultActions resultActions = performSignup(requestDto);

        //then
        expectSignupFieldErrors(resultActions);
    }

    @ParameterizedTest
    @CsvSource({
            ", ",
            " ,",
            "01000000000",
            "010-0000",
    })
    @Transactional
    @DisplayName("회원가입 실패_phoneNumber 오류")
    public void signup_fail_invalid_phoneNumberField(String email) throws Exception {
        //given
        AccountSignupRequestDto requestDto = new AccountSignupRequestDto(
                "testId",
                "testpassword@",
                "test",
                "010-0000-0000",
                email,
                "USER");

        //when
        ResultActions resultActions = performSignup(requestDto);

        //then
        expectSignupFieldErrors(resultActions);
    }

    @ParameterizedTest
    @CsvSource({
            ", ",
            " ,",
    })
    @Transactional
    @DisplayName("회원가입 실패_rolesField 오류")
    public void signup_fail_invalid_rolesField(String roles) throws Exception {
        //given
        AccountSignupRequestDto requestDto = new AccountSignupRequestDto(
                "testId",
                "testpassword@",
                "test",
                "010-0000-0000",
                "testpassword@",
                roles);

        //when
        ResultActions resultActions = performSignup(requestDto);

        //then
        expectSignupFieldErrors(resultActions);
    }




}