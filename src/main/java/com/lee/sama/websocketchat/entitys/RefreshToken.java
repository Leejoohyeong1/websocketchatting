package com.lee.sama.websocketchat.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    private Long userId;

    private String refreshToken;

}
