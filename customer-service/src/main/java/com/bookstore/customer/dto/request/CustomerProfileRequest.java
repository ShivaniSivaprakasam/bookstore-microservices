package com.bookstore.customer.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfileRequest {

    private String phone;
    private String preferences;
}