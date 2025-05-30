package org.springfeed.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "posts")
@Getter
public class Post extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Setter
    private User author;

    @Column(nullable = false, length = 50)
    @Setter
    private String title;

    @Column(nullable = false, length = 500)
    @Setter
    private String contents;

    public Post() {
    }

    public Post(String title, String content) {
        this.title = title;
        this.contents = content;
    }
}
