package online.online.hakko.scanner;

import online.online.hakko.stream.LiveStream;

import java.util.List;

public interface PlatformScanner {

    List<LiveStream> scan(int maxViewerCount);
}
