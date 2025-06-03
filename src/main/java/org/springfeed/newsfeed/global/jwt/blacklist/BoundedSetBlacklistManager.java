package org.springfeed.newsfeed.global.jwt.blacklist;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class BoundedSetBlacklistManager implements BlacklistManager {

    private final ScheduledExecutorService ses;
    private final SizedConcurrentSet<String> blacklist;

    public BoundedSetBlacklistManager(@Value("${BlacklistSize}") int size) {
        this.blacklist = new SizedConcurrentSet<>(size);
        this.ses = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void add(String token, long remainingTime) {
        blacklist.add(token);

        ses.schedule(() -> {
                blacklist.remove(token);
            }, remainingTime, TimeUnit.SECONDS
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
