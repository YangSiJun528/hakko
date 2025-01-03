package online.online.hakko.scanner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import online.online.hakko.stream.LiveStream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SoopPlatformScanner implements PlatformScanner {

    private static final String BASE_URL = "https://live.sooplive.co.kr/api/main_broad_list_api.php";
    private static final long REQUEST_DELAY = 1000;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SoopPlatformScanner(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<LiveStream> scan(int maxViewerCount) {
        List<LiveStream> streams = new ArrayList<>();
        int pageNo = 1;
        boolean shouldStop = false;

        while (!shouldStop) {
            try {
                Thread.sleep(REQUEST_DELAY);

                String url = String.format("%s?selectType=action&selectValue=all&orderType=view_cnt_asc&pageNo=%d&lang=ko_KR",
                        BASE_URL, pageNo);

                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                    break;
                }

                SoopResponse soopResponse = objectMapper.readValue(response.getBody(), SoopResponse.class);
                var broadcasts = soopResponse.broad();

                if (broadcasts == null || broadcasts.isEmpty()) {
                    break;
                }

                for (var broadcast : broadcasts) {
                    if (broadcast.currentViewCount() > maxViewerCount) {
                        shouldStop = true;
                        break;
                    }
                    streams.add(new SoopLiveStream(broadcast));
                }

                pageNo++;
            } catch (Exception e) {
                log.error("An error occurred: {}", e.getMessage(), e);
                break;
            }
        }
        return streams;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record SoopResponse(
            @JsonProperty("total_cnt") String totalCnt,
            @JsonProperty("cnt") int cnt,
            @JsonProperty("broad") List<SoopBroadcast> broad
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record SoopBroadcast(
            @JsonProperty("broad_no") String broadNo,
            @JsonProperty("user_id") String userId,
            @JsonProperty("user_nick") String userNick,
            @JsonProperty("broad_title") String broadTitle,
            @JsonProperty("broad_thumb") String broadThumb,
            @JsonProperty("current_view_cnt") int currentViewCount,
            @JsonProperty("category_name") String categoryName
    ) {
    }
}
