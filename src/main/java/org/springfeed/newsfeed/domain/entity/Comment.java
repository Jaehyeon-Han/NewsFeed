package org.springfeed.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "comments")
@Getter
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, length = 100)
    private String comment;

}
