package com.spock.test_demo.common.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String name;
    private String password;
    private String phoneNumber;
}
