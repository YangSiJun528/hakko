package dev.joon.hakko.scanner;

import dev.joon.hakko.stream.LiveStream;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DelegatePlatformScanner implements PlatformScanner {

    private final List<PlatformScanner> scanners;

    public DelegatePlatformScanner(ApplicationContext applicationContext) {
        Map<String, PlatformScanner> scannersMap = applicationContext.getBeansOfType(PlatformScanner.class);
        this.scanners = scannersMap.values().stream()
                .filter(scanner -> !(scanner instanceof DelegatePlatformScanner))
                .toList();
    }

    @Override
    public List<LiveStream> scan(int maxViewerCount) {
        List<LiveStream> streams = new ArrayList<>();
        for (PlatformScanner scanner : scanners) {
            streams.addAll(scanner.scan(maxViewerCount));
        }
        return streams;
    }
}
