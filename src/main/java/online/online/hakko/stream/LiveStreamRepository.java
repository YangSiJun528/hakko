package online.online.hakko.stream;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LiveStreamRepository {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, LiveStream>> repository = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public LiveStreamRepository init() {
        repository.put(Platform.CHZZK, new ConcurrentHashMap<>());
        repository.put(Platform.SOOP, new ConcurrentHashMap<>());
        return this;
    }

    public void saveLiveStreams(LiveStream streamer) {
        repository.get(streamer.getPlatform())
                .put(streamer.getChannelId(), streamer);
    }

    public void saveLiveStreams(List<LiveStream> streamers) {
        streamers.forEach(this::saveLiveStreams);
    }

    public Optional<LiveStream> getRandomLiveStream(String platform) {
        ConcurrentHashMap<String, LiveStream> platformStreamers = this.repository.get(platform.toUpperCase());
        if (platformStreamers == null || platformStreamers.isEmpty()) {
            return Optional.empty();
        }

        List<LiveStream> streamers = new ArrayList<>(platformStreamers.values());
        return Optional.of(streamers.get(this.random.nextInt(streamers.size())));
    }

    public void clearPlatform(String platform) {
        this.repository.get(platform).clear();
    }

    public int getCountLiveStream(String platform) {
        return this.repository.get(platform).size();
    }
}
