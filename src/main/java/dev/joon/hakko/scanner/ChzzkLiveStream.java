package dev.joon.hakko.scanner;

import dev.joon.hakko.stream.LiveStream;
import dev.joon.hakko.stream.Platform;

public record ChzzkLiveStream(
        ChzzkPlatformScanner.ChzzkLive live
) implements LiveStream {

    @Override
    public String getPlatform() {
        return Platform.CHZZK;
    }

    @Override
    public String getChannelId() {
        return live.channel().channelId();
    }

    @Override
    public String getChannelName() {
        return live.channel().channelName();
    }

    @Override
    public String getChannelUrl() {
        return "https://chzzk.naver.com/" + live.channel().channelId();
    }

    @Override
    public String getStreamingTitle() {
        return live.liveTitle();
    }

    @Override
    public String getStreamingUrl() {
        return "https://chzzk.naver.com/live/" + live.channel().channelId();
    }

    @Override
    public int getViewerCount() {
        return live.concurrentUserCount();
    }
}
