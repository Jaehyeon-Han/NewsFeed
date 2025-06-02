package org.springfeed.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "follows")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User follower; // 팔로우를 건 사람 (나)

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User following; // 팔로우 당한 사람 (상대방)

    @CreatedDate
    @Column(
        updatable = false,
        nullable = false
    )
    private LocalDateTime startedFollowingAt;

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}
