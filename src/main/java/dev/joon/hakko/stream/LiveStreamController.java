package dev.joon.hakko.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class LiveStreamController {

    private final LiveStreamRepository liveStreamRepository;

    public LiveStreamController(LiveStreamRepository liveStreamRepository) {
        this.liveStreamRepository = liveStreamRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/{platform}")
    public String platformPage(
            @PathVariable String platform,
            @RequestParam(required = false) String channelId,
            Model model
    ) {
        model.addAttribute("platform", platform.toUpperCase());

        LiveStream liveStream;
        if (channelId != null) {
            liveStream = liveStreamRepository.getLiveStream(platform, channelId).orElse(null);
            if (liveStream == null) {
                model.addAttribute("error", "해당 스트리밍 정보를 찾을 수 없습니다. 현재 스트리밍 중이 아니거나, 존재하지 않는 스트리머입니다.");
                model.addAttribute("error_btn", "다른 스트리머 보기");
            }
        } else {
            liveStream = liveStreamRepository.getRandomLiveStream(platform).orElse(null);
            if (liveStream == null) {
                model.addAttribute("error", "현재 시청 가능한 스트리머가 없습니다.");
                model.addAttribute("error_btn", "다시 시도");
            }
        }

        model.addAttribute("liveStream", liveStream);
        log.debug("live stream: {}", liveStream);

        return "stream";
    }


    @GetMapping("/fragment/{platform}/live-stream")
    public String getLiveStreamFragment(@PathVariable String platform, Model model) {
        model.addAttribute("platform", platform.toUpperCase());

        LiveStream liveStream = liveStreamRepository.getRandomLiveStream(platform).orElse(null);
        if (liveStream == null) {
            model.addAttribute("error", "현재 시청 가능한 스트리머가 없습니다.");
            model.addAttribute("error_btn", "다시 시도");
        }
        model.addAttribute("liveStream", liveStream);

        return "fragments/stream-content :: streamContent";
    }
}
