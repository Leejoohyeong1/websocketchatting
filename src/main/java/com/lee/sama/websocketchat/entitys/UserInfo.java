package com.lee.sama.websocketchat.entitys;

import com.lee.sama.websocketchat.auth.dto.JoinResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@NoArgsConstructor
@Entity
@Getter
@SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "USER_SEQ", initialValue = 1)
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
    private Long userId;

    private String email;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private UserRule rule;

    private String password;

    public UserInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserInfo(String email, String nickName, String password) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
    }

    public void haveAuthority(UserRule userRule){
        this.rule = userRule;
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

    public JoinResponse toJoinResponse(){
        return new JoinResponse(this.userId,this.email,this.nickName);
    }
}
