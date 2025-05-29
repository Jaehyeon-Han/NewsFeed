package org.springfeed.newsfeed.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springfeed.newsfeed.global.config.SessionType;
import org.springfeed.newsfeed.global.error.dto.ErrorResponse;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class LoginFilter implements Filter {

    // 팔로우 조회에도 로그인 필요?
    private static final String[] WHITE_LIST = {"/signup", "/login", "/posts"};
    private static final int UNAUTHORIZED_HTTP_STATUS_CODE = 401;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if (!(isWhiteList(requestURI) || ("GET".equals(method) && requestURI.matches("^/posts/\\d+$")))) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(SessionType.USER) == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                ObjectMapper objectMapper = new ObjectMapper();
                ErrorResponse errorResponse = new ErrorResponse(UNAUTHORIZED_HTTP_STATUS_CODE, "로그인이 필요합니다.");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
