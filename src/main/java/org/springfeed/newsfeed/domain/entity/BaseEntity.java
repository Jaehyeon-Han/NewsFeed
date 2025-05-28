package org.springfeed.newsfeed.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 생성, 수정시각을 위한 기본 엔티티 
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @Column(
        name = "created_at",
        updatable = false,
        nullable = false
    )
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(
        name = "last_modified_at",
        nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

}
