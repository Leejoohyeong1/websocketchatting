package com.lee.sama.websocketchat.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
//@SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "USER_SEQ", initialValue = 1)
public class UserInfo {

    @Id
    Long userId;

    String nickName;


}
