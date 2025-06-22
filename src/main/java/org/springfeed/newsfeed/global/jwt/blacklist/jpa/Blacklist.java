package org.springfeed.newsfeed.global.jwt.blacklist.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blacklist")
@Getter
@NoArgsConstructor
public class Blacklist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    public Blacklist(String token) {
        this.token = token;
    }
}
