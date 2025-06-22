package org.springfeed.newsfeed.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springfeed.newsfeed.global.jwt.blacklist.BlacklistManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final static long TOKEN_TIME = 1 * 60 * 1000L; // 30분?
    private final static SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    private final BlacklistManager blacklistManager;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    // 토큰 초기화
    @PostConstruct
    public void init() {

        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(Long userId) {

        Date date = new Date();

        return Jwts.builder()
            .setSubject(userId.toString())
            .setExpiration(new Date(date.getTime() + TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, ALGORITHM)
            .compact();
    }

    //토큰 유효성 검사
    public boolean validateToken(String token) {

        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {       // 토큰이 만료된 경우
            log.error("만료된 토큰입니다.", e);
        } catch (SignatureException e) {        // 토큰 서명이 잘못된 경우
            log.error("서명이 유효하지 않은 토큰입니다.", e);
        } catch (MalformedJwtException | UnsupportedJwtException | SecurityException e) {     // 잘못된 형식의 JWT가 전달된 경우
            log.error("잘못된 형식의 토큰입니다.", e);
        } catch (IllegalArgumentException e) {  // 토큰이 없는 경우
            log.error("잘못된 토큰 입니다.", e);
        }

        return false;
    }

    // JWT에 있는 userId값 반환
    public Long getUserId(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String userId = parseClaims(token).getSubject();

        return Long.valueOf(userId);
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    public void invalidateToken(String token) {
        blacklistManager.add(token, getTokenRemainingTimeInSecond(token));
    }

    public boolean isInBlacklist(String token) {
        return blacklistManager.contains(token);
    }

    private long getTokenRemainingTimeInSecond(String token) {
        validateToken(token);

        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();
        Date now = new Date();

        long diffMillis = expiration.getTime() - now.getTime();
        long diffSeconds = diffMillis / 1000;

        return diffSeconds;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)           // 서명 검증을 위한 키
            .build()
            .parseClaimsJws(token)        // 유효성 검사 및 파싱
            .getBody();
    }
}
