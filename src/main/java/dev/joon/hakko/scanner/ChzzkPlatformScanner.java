package dev.joon.hakko.scanner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import dev.joon.hakko.stream.LiveStream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ChzzkPlatformScanner implements PlatformScanner {

    private static final String BASE_URL = "https://api.chzzk.naver.com/service/v1/lives";
    private static final int PAGE_SIZE = 20;
    private static final long REQUEST_DELAY = 1000;

    private final RestTemplate restTemplate;

    public ChzzkPlatformScanner(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<LiveStream> scan(int maxViewerCount) {
        List<LiveStream> streams = new ArrayList<>();
        String nextUrl = String.format("%s?size=%d&sortType=LATEST", BASE_URL, PAGE_SIZE);

        while (nextUrl != null) {
            try {
                Thread.sleep(REQUEST_DELAY);

                ResponseEntity<ChzzkResponse> response = restTemplate.getForEntity(nextUrl, ChzzkResponse.class);
                if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                    break;
                }

                var lives = response.getBody().content.data;
                for (var live : lives) {
                    if (live.concurrentUserCount() <= maxViewerCount) {
                        streams.add(new ChzzkLiveStream(live));
                    }
                }

                var next = response.getBody().content.page.next;
                if (next != null && next.concurrentUserCount >= 20) {
                    nextUrl = String.format("%s?concurrentUserCount=%d&liveId=%d&size=%d&sortType=LATEST",
                            BASE_URL, next.concurrentUserCount, next.liveId, PAGE_SIZE);
                } else {
                    nextUrl = null;
                }
            } catch (Exception e) {
                log.error("An error occurred: {}", e.getMessage(), e);
                break;
            }
        }
        return streams;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ChzzkResponse(
            int code,
            @JsonProperty("content") ChzzkContent content
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ChzzkContent(
            @JsonProperty("data") List<ChzzkLive> data,
            @JsonProperty("page") ChzzkPage page
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ChzzkLive(
            @JsonProperty("liveId") String liveId,
            @JsonProperty("liveTitle") String liveTitle,
            @JsonProperty("liveImageUrl") String liveImageUrl,
            @JsonProperty("concurrentUserCount") int concurrentUserCount,
            @JsonProperty("liveCategoryValue") String liveCategoryValue,
            @JsonProperty("channel") ChzzkChannel channel
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ChzzkChannel(
            @JsonProperty("channelId") String channelId,
            @JsonProperty("channelName") String channelName
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ChzzkPage(
            @JsonProperty("next") ChzzkNextPage next
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ChzzkNextPage(
            @JsonProperty("concurrentUserCount") int concurrentUserCount,
            @JsonProperty("liveId") long liveId
    ) {}
}
