package dev.joon.hakko.stream;

public interface LiveStream {

    Platform getPlatform();
    String getChannelId();
    String getChannelName();
    String getChannelUrl();
    String getStreamingTitle();
    String getStreamingUrl();
    int getViewerCount();
    // TODO: 방송 시작시간 추가해서 최근 방송 시작한 사람을 제외하고 가져오기 (인기있는 사용자가 방송을 킨 직후는 해당되지 않게)
}
