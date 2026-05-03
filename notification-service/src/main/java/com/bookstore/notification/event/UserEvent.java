package com.bookstore.notification.event;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvent {

    private Long userId;
    private String email;
    private String type;
    private LocalDateTime timestamp;
}