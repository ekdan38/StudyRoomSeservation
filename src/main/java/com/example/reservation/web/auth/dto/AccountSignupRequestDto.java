package com.example.reservation.web.auth.dto;

import com.example.reservation.domain.account.entity.AccountRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountSignupRequestDto {
    @NotBlank(message = "loginId가 입력되지 않았습니다.")
    @Size(min = 6, max = 20, message = "loginId는 최소 6, 최대20 글자 허용")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username은 영어 대소문자와 숫자만 허용됩니다.") // 영어 대소문자, 숫자만 허용
    private String loginId;

    @NotBlank(message = "password가 입력되지 않았습니다.")
    @Size(min = 8, max = 20, message = "password는 최소 8, 최대20 글자 허용")
    @Pattern(
            regexp = "^(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "password는 특수문자가 1개 이상 포함되어야 합니다."
    ) // 대문자, 소문자 허용 + 특수문자 필수
    private String password;

    @NotBlank(message = "name이 입력되지 않았습니다.")
    @Size(min = 2, max = 10, message = "name은 최소 2, 최대 10글자 허용")
    private String name;

    @NotBlank(message = "phoneNumber이 입력되지 않았습니다.")
    @Pattern(
            regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",  // 한국식 전화번호 형식 (예: 010-1234-5678)
            message = "전화번호는 010-1234-5678 형식이어야 합니다."
    )  // 전화번호 형식 제약 추가
    private String phoneNumber;

    @NotBlank(message = "email이 입력되지 않았습니다.")
    @Email(message = "이메일 주소의 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "accountRole을 요청해 주세요. ex)USER")
    private String accountRole;

    public AccountSignupRequestDto(String loginId, String password, String name, String phoneNumber, String email, String accountRole) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accountRole = accountRole;
    }
}
