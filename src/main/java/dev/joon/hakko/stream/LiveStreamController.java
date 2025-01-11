package dev.joon.hakko.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public String platformPage(@PathVariable String platform, Model model) {
        model.addAttribute("platform", platform.toUpperCase());

        LiveStream liveStream = liveStreamRepository.getRandomLiveStream(platform).orElse(null);
        if (liveStream == null) {
            model.addAttribute("error", "현재 시청 가능한 스트리머가 없습니다.");
        }
        model.addAttribute("liveStream", liveStream);

        System.out.println(liveStream);
        return "stream";
    }

    @GetMapping("/fragment/{platform}/live-stream")
    public String getLiveStreamFragment(@PathVariable String platform, Model model) {
        model.addAttribute("platform", platform.toUpperCase());

        LiveStream liveStream = liveStreamRepository.getRandomLiveStream(platform).orElse(null);
        if (liveStream == null) {
            model.addAttribute("error", "현재 시청 가능한 스트리머가 없습니다.");
        }
        model.addAttribute("liveStream", liveStream);

        return "fragments/stream :: streamContent";
    }
}
