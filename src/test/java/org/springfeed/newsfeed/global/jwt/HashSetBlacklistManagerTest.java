package org.springfeed.newsfeed.global.jwt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springfeed.newsfeed.global.jwt.blacklist.BoundedSetBlacklistManager;

import static org.junit.jupiter.api.Assertions.*;

class HashSetBlacklistManagerTest {

    BoundedSetBlacklistManager blacklistManager = new BoundedSetBlacklistManager(100);

    @AfterEach
    void shutdown() {
        blacklistManager.shutdownExecutor();
    }

    @Test
    void should_beRemovedFromBlackList_when_tokenExpires() throws Exception {
        // given
        final String token = "ThisIsToken";
        final long remainingTime = 1L;

        // when
        blacklistManager.add(token, remainingTime);

        // then
        assertTrue(blacklistManager.contains(token));

        Thread.sleep(1500);
        assertFalse(blacklistManager.contains(token));
    }
}