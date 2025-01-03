package online.online.hakko.stream;

public interface LiveStream {

    String getPlatform();
    String getChannelId();
    String getChannelName();
    String getChannelUrl();
    String getStreamingTitle();
    String getStreamingUrl();
    int getViewerCount();
}
