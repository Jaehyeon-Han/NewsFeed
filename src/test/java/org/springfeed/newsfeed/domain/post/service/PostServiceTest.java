package org.springfeed.newsfeed.domain.post.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springfeed.newsfeed.domain.entity.Post;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.post.dto.response.PostResponse;
import org.springfeed.newsfeed.domain.post.repository.PostRepository;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.config.PasswordEncoder;
import org.springfeed.newsfeed.global.error.exception.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springfeed.newsfeed.constant.UserConstant.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    PasswordEncoder passwordEncoder = new PasswordEncoder();

    PostService postService;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository);
    }

    private static final long LOGIN_USER_ID = 1L;
    private static final long ANOTHER_USER_ID = 9999L;
    private static final long NEW_POST_ID = 1L;
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String NEW_TITLE = "new title";
    private static final String NEW_CONTENT = "new content";

    /* 게시글 작성 테스트
     * 사실 이 경우, 단순 반환이기에 실제로 테스트하는 건
     * new 생성 시 인자를 잘 넣었는가 정도에 불과
     */
    @Test
    @DisplayName("정상 작성")
    void should_createPost_when_LoggedInUserSavePost() {
        // given
        User mockUser = getMockUserWithLoginUserId();
        given(userRepository.findByIdOrElseThrow(LOGIN_USER_ID)).willReturn(mockUser);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        given(postRepository.save(any(Post.class))).willAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            ReflectionTestUtils.setField(p, "id", NEW_POST_ID);
            return p;
        });

        // when
        PostResponse response = postService.save(TITLE, CONTENT, LOGIN_USER_ID);

        // then
        then(postRepository).should().save(postCaptor.capture());
        Post saved = postCaptor.getValue();

        assertThat(saved.getTitle()).isEqualTo(TITLE);
        assertThat(saved.getContents()).isEqualTo(CONTENT);
        assertThat(saved.getAuthor()).isEqualTo(mockUser);

        assertThat(response.getPostId()).isEqualTo(NEW_POST_ID);
        assertThat(response.getAuthor()).isEqualTo(mockUser.getNickname());
    }

    // 게시물 단건 조회와 페이징 조회도 단순 DTO 생성에 불과

    // 게시물 수정
    @Test
    @DisplayName("정상 수정")
    void should_updatePost_when_authorUpdatesPost() {
        // given
        User mockUser = getMockUserWithLoginUserId();
        Post mockPost = new Post(TITLE, CONTENT);
        ReflectionTestUtils.setField(mockPost, "author", mockUser);
        ReflectionTestUtils.setField(mockPost, "id", NEW_POST_ID);
        given(postRepository.findPostByIdOrElseThrow(NEW_POST_ID)).willReturn(mockPost);

        // when
        PostResponse postResponse = postService.updateById(NEW_POST_ID, LOGIN_USER_ID, NEW_TITLE, NEW_CONTENT);

        // then
        assertThat(postResponse.getTitle()).isEqualTo(NEW_TITLE);
        assertThat(postResponse.getContent()).isEqualTo(NEW_CONTENT);
    }

    @Test
    @DisplayName("다른 사용자의 게시글 수정")
    // private 메소드 verifyAuthorOrThrow() 검증
    void should_throwAccessDeniedException_when_updatePostOfOtherUser() {
        // given
        User mockUser = getMockUserWithLoginUserId();
        Post mockPost = new Post(TITLE, CONTENT);
        ReflectionTestUtils.setField(mockPost, "author", mockUser);
        ReflectionTestUtils.setField(mockPost, "id", NEW_POST_ID);
        given(postRepository.findPostByIdOrElseThrow(NEW_POST_ID)).willReturn(mockPost);

        // when-then
        assertThatThrownBy(() -> postService.updateById(NEW_POST_ID, ANOTHER_USER_ID, NEW_TITLE, NEW_CONTENT))
            .isInstanceOf(AccessDeniedException.class);
    }

    private User getMockUserWithLoginUserId() {
        User mockUser = new User(EMAIL, passwordEncoder.encode(PASSWORD), NICKNAME, INTRODUCTION);
        ReflectionTestUtils.setField(mockUser, "id", LOGIN_USER_ID);
        return mockUser;
    }
}