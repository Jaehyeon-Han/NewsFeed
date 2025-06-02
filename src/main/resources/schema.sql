CREATE TABLE `Users`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '고유 식별자',
    `email`            VARCHAR(255) NOT NULL COMMENT '아이디 역할 이메일',
    `password_hash`    VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `nickname`         VARCHAR(20)  NOT NULL COMMENT '닉네임',
    `introduction`     VARCHAR(500) DEFAULT '' COMMENT '자기소개',
    `created_at`       DATETIME     NOT NULL COMMENT '회원 가입 시각',
    `last_modified_at` DATETIME     NOT NULL COMMENT '마지막 정보 수정 시각',
    PRIMARY KEY (`id`),
    UNIQUE (`email`)
);

CREATE TABLE `Posts`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '게시글 고유 식별자',
    `author_id`        BIGINT       NOT NULL COMMENT '사용자 고유 식별자',
    `title`            VARCHAR(50)  NOT NULL COMMENT '제목',
    `content`          VARCHAR(500) NOT NULL COMMENT '내용',
    `created_at`       DATETIME     NOT NULL COMMENT '최초 글 작성 시각',
    `last_modified_at` DATETIME     NOT NULL COMMENT '마지막 글 수정 시각',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `Follows`
(
    `id`                   BIGINT   NOT NULL AUTO_INCREMENT COMMENT '팔로우 고유 식별자',
    `follower_id`          BIGINT   NOT NULL COMMENT '팔로우 건 사람 (User)',
    `following_id`         BIGINT   NOT NULL COMMENT '팔로우 당한 사람 (User)',
    `started_following_at` DATETIME NOT NULL COMMENT '팔로우 시작 시각',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`follower_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`following_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `Comments`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '댓글 고유 식별자',
    `post_id`          BIGINT       NOT NULL COMMENT '게시글 고유 식별자',
    `author_id`        BIGINT       NOT NULL COMMENT '사용자 고유 식별자',
    `comment`          VARCHAR(100) NOT NULL COMMENT '내용',
    `created_at`       DATETIME     NOT NULL COMMENT '최초 댓글 작성 시각',
    `last_modified_at` DATETIME     NOT NULL COMMENT '마지막 댓글 수정 시각',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`post_id`) REFERENCES `Posts` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
);
