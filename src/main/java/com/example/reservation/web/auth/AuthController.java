package com.example.reservation.web.auth;

import com.example.reservation.domain.account.dto.AccountDto;
import com.example.reservation.domain.account.entity.Account;
import com.example.reservation.domain.account.sevice.AccountService;
import com.example.reservation.domain.mapper.AccountMapper;
import com.example.reservation.web.auth.dto.AccountSignupRequestDto;
import com.example.reservation.web.auth.dto.AccountSignupResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping({"/signup", "/login", "/logout"})
    public ResponseEntity<?> getLoginCsrfToken(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Validated @RequestBody AccountSignupRequestDto requestDto, Errors errors) {
        if(errors.hasFieldErrors()){
            log.info("validation Errors! = {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }
        Map<String, Object> response = new HashMap<>();
        Account account = accountService.accountSignup(accountMapper.requestToDto(requestDto));
        AccountSignupResponseDto accountSignupResponseDto =
                new AccountSignupResponseDto(account.getId(), account.getName(), account.getLoginId(), account.getEmail());
        response.put("message", "Success Signup");
        response.put("account", accountSignupResponseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal AccountDto accountDto){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Success Logout");
        response.put("account", accountDto.getId());
        return ResponseEntity.ok(response);
    }


}
