package org.springfeed.newsfeed.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.global.error.dto.ErrorResponse;
import org.springfeed.newsfeed.global.jwt.blacklist.BlacklistManager;
import org.springfeed.newsfeed.global.jwt.JwtUtil;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    private static final String[] WHITE_LIST = {"/signup", "/login", "/posts"};

    @Override
    public void doFilter(ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUri = httpRequest.getRequestURI();
        String requestMethod = httpRequest.getMethod();
        boolean isFindingPost = "GET".equals(requestMethod) && requestUri.matches("^/posts/\\d+$");

        // 정상 요청
        if (inWhiteList(requestUri) || isFindingPost) {
            chain.doFilter(request, response);
            return;
        }

        // 인증 정보 없음
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            error(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return;
        }

        // 잘못된 토큰
        String jwt = authorizationHeader.substring(7);
        if (!jwtUtil.validateToken(jwt)) {
            error(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "잘못된 토큰입니다.");
            return;
        }

        if (jwtUtil.isInBlacklist(jwt)) {
            error(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "이미 로그아웃되었습니다.");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean inWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

    private void error(HttpServletResponse httpResponse, int status, String message) throws IOException {

        httpResponse.setStatus(status);
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse(status, message);
        httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
