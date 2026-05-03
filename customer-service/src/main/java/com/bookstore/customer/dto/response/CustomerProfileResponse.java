package com.bookstore.customer.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfileResponse {

    private Long id;
    private Long userId;
    private String phone;
    private String preferences;
}