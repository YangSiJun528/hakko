package dev.joon.hakko;

import lombok.extern.slf4j.Slf4j;
import dev.joon.hakko.scanner.DelegatePlatformScanner;
import dev.joon.hakko.stream.LiveStream;
import dev.joon.hakko.stream.LiveStreamRepository;
import dev.joon.hakko.stream.Platform;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ScheduleTask {
    
    private static final int MAX_VIEWER_COUNT = 0;

    private final DelegatePlatformScanner platformScanner;
    private final LiveStreamRepository liveStreamRepository;

    public ScheduleTask(DelegatePlatformScanner platformScanner, LiveStreamRepository liveStreamRepository) {
        this.platformScanner = platformScanner;
        this.liveStreamRepository = liveStreamRepository;
    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void scheduledTask() {
        log.info("Starting scheduled scan at: {}", LocalDateTime.now());

        try {
            // CHZZK 스캔
            liveStreamRepository.clearPlatform(Platform.CHZZK);
            List<LiveStream> chzzkStreamers = platformScanner.scan(MAX_VIEWER_COUNT);
            liveStreamRepository.saveLiveStreams(chzzkStreamers);
            log.info("Updated CHZZK streamers. Count: {}", chzzkStreamers.size());

            // SOOP 스캔
             liveStreamRepository.clearPlatform(Platform.SOOP);
             List<LiveStream> soopStreamers = platformScanner.scan(MAX_VIEWER_COUNT);
             liveStreamRepository.saveLiveStreams(soopStreamers);
             log.info("Updated SOOP streamers. Count: {}", soopStreamers.size());

        } catch (Exception e) {
            log.error("Error during scheduled scan", e);
        }
    }
}
