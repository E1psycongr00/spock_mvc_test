package com.spock.test_demo.common.dto.member;

import com.spock.test_demo.validation.annotation.Phone;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RequestMemberDto {

    @Email(
        message = "email 형식에 맞게 입력해주세요"
    )
    @NotBlank(
        message = "email 은 빈칸 또는 비어 있을 수 없습니다. 반드시 입력해주세요"
    )
    private final String email;
    @Length(min = 2, max = 10)
    @NotBlank
    private final String name;
    @NotBlank
    private final String password;
    @Phone
    private final String phoneNumber;
}
