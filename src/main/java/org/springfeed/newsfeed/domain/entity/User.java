package org.springfeed.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
//@Setter(필요 시 주석 해제)
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(length = 500)
    private String introduction;

    public User(String email, String encodePassword, String nickname, String introduction) {
        this.email = email;
        this.passwordHash = encodePassword;
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public User() {

    }
}
