package org.springfeed.newsfeed.global.jwt.blacklist;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springfeed.newsfeed.global.jwt.blacklist.jpa.Blacklist;
import org.springfeed.newsfeed.global.jwt.blacklist.jpa.BlacklistRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//@Component
//@Primary
@RequiredArgsConstructor
@Slf4j
public class JpaBlacklistManager implements BlacklistManager {

    private final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    private final BlacklistRepository blacklistRepository;

    @Override
    @Transactional
    public void add(String token, long remainingTimeInSec) {
        Blacklist blacklist = new Blacklist(token);
        Blacklist saved = blacklistRepository.save(blacklist);

        ses.schedule(() -> {
                Optional<Blacklist> optionalToDelete = blacklistRepository.findById(saved.getId());
                if (optionalToDelete.isEmpty()) {
                    return;
                }

                blacklistRepository.delete(optionalToDelete.get());
            }, remainingTimeInSec, TimeUnit.SECONDS
        );
        log.info("token {} 을 blacklist 에 추가", token);
    }

    @Override
    public boolean contains(String token) {
        return blacklistRepository.existsByToken(token);
    }

    @PreDestroy
    public void shutdownExecutor() {
        ses.shutdown();
    }
}
