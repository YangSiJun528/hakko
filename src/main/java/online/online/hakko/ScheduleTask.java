package online.online.hakko;

import lombok.extern.slf4j.Slf4j;
import online.online.hakko.scanner.DelegatePlatformScanner;
import online.online.hakko.stream.LiveStream;
import online.online.hakko.stream.LiveStreamRepository;
import online.online.hakko.stream.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Async
    @Scheduled(cron = "0 0/5 * * * *")
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
