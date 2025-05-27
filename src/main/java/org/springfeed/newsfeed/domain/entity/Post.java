package org.springfeed.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "posts")
@Getter
// @Setter (필요 시 주석 해제)
public class Post extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;
}
