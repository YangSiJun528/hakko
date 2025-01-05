package dev.joon.hakko.scanner;

import dev.joon.hakko.stream.LiveStream;

import java.util.List;

public interface PlatformScanner {

    List<LiveStream> scan(int maxViewerCount);
}
