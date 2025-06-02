package org.springfeed.newsfeed.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.global.error.dto.ErrorResponse;
import org.springfeed.newsfeed.global.jwt.JwtUtil;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private static final String[] WHITE_LIST = {"/signup", "/login", "/posts"};
    private static final int UNAUTHORIZED_HTTP_STATUS_CODE = 401;

    @Override
    public void doFilter(ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestUri = httpRequest.getRequestURI();
        String requestMethod = httpRequest.getMethod();
        if (isWhiteList(requestUri) || ("GET".equals(requestMethod) && requestUri.matches("^/posts/\\d+$"))) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            error(httpResponse, "로그인이 필요합니다.");
            return;
        }

        String jwt = authorizationHeader.substring(7);
        if (!jwtUtil.validateToken(jwt)) {
            error(httpResponse, "잘못된 토큰입니다.");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

    private void error(HttpServletResponse httpResponse, String message) throws IOException {

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse(UNAUTHORIZED_HTTP_STATUS_CODE, message);
        httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
