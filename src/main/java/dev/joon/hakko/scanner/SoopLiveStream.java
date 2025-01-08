package dev.joon.hakko.scanner;

import dev.joon.hakko.stream.LiveStream;
import dev.joon.hakko.stream.Platform;

public record SoopLiveStream(
        SoopPlatformScanner.SoopBroadcast broadcast
) implements LiveStream {

    @Override
    public String getPlatform() {
        return Platform.SOOP;
    }

    @Override
    public String getChannelId() {
        return broadcast.userId();
    }

    @Override
    public String getChannelName() {
        return broadcast.userNick();
    }

    @Override
    public String getChannelUrl() {
        return "https://ch.sooplive.co.kr/" + broadcast.userId();
    }

    @Override
    public String getStreamingTitle() {
        return broadcast.broadTitle();
    }

    @Override
    public String getStreamingUrl() {
        return "https://play.sooplive.co.kr/" + broadcast.userId() + "/" + broadcast.broadNo();
    }

    @Override
    public int getViewerCount() {
        return broadcast.currentViewCnt();
    }
}
