package org.springfeed.newsfeed.global.jwt.blacklist;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SizedSetBlacklistManager implements BlacklistManager {

    private final ScheduledExecutorService ses;
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    private final int maxSize;

    public SizedSetBlacklistManager(@Value("${BlacklistSize}") int maxSize) {
        this.maxSize = maxSize;
        this.ses = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void add(String token, long remainingTimeInSecond) {

        // blacklist가 무한히 커지는 것을 막는다
        if(blacklist.size() > maxSize) {
            throw new RuntimeException();
        }

        blacklist.add(token);

        ses.schedule(() -> {
                blacklist.remove(token);
            }, remainingTimeInSecond, TimeUnit.SECONDS
        );
        log.debug("token {} 을 blacklist 에 추가", token);
    }

    @Override
    public boolean contains(String token) {
        return blacklist.contains(token);
    }

    @PreDestroy
    public void shutdownExecutor() {
        System.out.println("Shutting down executor...");
        ses.shutdown();
    }
}
