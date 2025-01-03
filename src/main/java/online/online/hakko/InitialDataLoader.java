package online.online.hakko;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
// 테스트 중에 실행하면 바로 등록되어야 하니까 사용.
public class InitialDataLoader implements CommandLineRunner {

    private final ScheduleTask scheduleTask;

    public InitialDataLoader(ScheduleTask scheduleTask) {
        this.scheduleTask = scheduleTask;
    }

    @Override
    public void run(String... args) {
        log.info("Performing initial scan...");
        scheduleTask.scheduledTask();
    }
}
