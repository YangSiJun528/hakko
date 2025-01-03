package online.online.hakko.config;

import online.online.hakko.stream.LiveStreamRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiveStreamConfig {

    @Bean
    public LiveStreamRepository liveStreamRepository() {
        return new LiveStreamRepository().init();
    }
}
