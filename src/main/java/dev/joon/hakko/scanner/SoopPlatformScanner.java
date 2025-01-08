package dev.joon.hakko.scanner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import dev.joon.hakko.stream.LiveStream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                    if (broadcast == null) {
                        continue;
                    }
                    if (broadcast.currentViewCnt() > maxViewerCount) {
                        shouldStop = true;
                        break;
                    }
                    if (Objects.equals(broadcast.isPassword, "Y")) {
                        continue;
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
            @JsonProperty("broad") List<SoopBroadcast> broad,
            @JsonProperty("time") long time,
            @JsonProperty("is_wp") List<String> isWp
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record SoopBroadcast(
            @JsonProperty("broad_no") String broadNo,
            @JsonProperty("parent_broad_no") String parentBroadNo,
            @JsonProperty("user_id") String userId,
            @JsonProperty("user_nick") String userNick,
            @JsonProperty("broad_title") String broadTitle,
            @JsonProperty("broad_thumb") String broadThumb,
            @JsonProperty("broad_start") String broadStart,
            @JsonProperty("broad_grade") String broadGrade,
            @JsonProperty("broad_bps") String broadBps,
            @JsonProperty("broad_resolution") String broadResolution,
            @JsonProperty("visit_broad_type") String visitBroadType,
            @JsonProperty("broad_type") String broadType,
            @JsonProperty("station_name") String stationName,
            @JsonProperty("broad_memo") String broadMemo,
            @JsonProperty("current_view_cnt") int currentViewCnt,
            @JsonProperty("m_current_view_cnt") String mobileCurrentViewCnt,
            @JsonProperty("allowed_view_cnt") int allowedViewCnt,
            @JsonProperty("is_password") String isPassword, // "Y", "N"
            @JsonProperty("rank") String rank,
            @JsonProperty("broad_cate_no") String broadCateNo,
            @JsonProperty("total_view_cnt") int totalViewCnt,
            @JsonProperty("pc_view_cnt") int pcViewCnt,
            @JsonProperty("mobile_view_cnt") int mobileViewCnt,
            @JsonProperty("is_drops") int isDrops,
            @JsonProperty("auto_hashtags") List<String> autoHashtags,
            @JsonProperty("category_tags") List<String> categoryTags,
            @JsonProperty("category_name") String categoryName,
            @JsonProperty("hash_tags") List<String> hashTags
    ) {
    }
}
