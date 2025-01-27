package dev.joon.hakko;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ErrorPageController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        logError(request);
        return "error";
    }

    private void logError(HttpServletRequest request) {
        StringBuilder logMessage = new StringBuilder("\n=== Error Details ===\n");

        // 기본 에러 정보
        logMessage.append("URI: ").append(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI)).append("\n")
                .append("Status: ").append(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).append("\n")
                .append("Message: ").append(request.getAttribute(RequestDispatcher.ERROR_MESSAGE)).append("\n");

        // 스택트레이스
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (exception != null) {
            Throwable throwable = (Throwable) exception;
            logMessage.append("Exception: ").append(throwable.getClass().getName()).append("\n")
                    .append("Stack Trace:\n");
            for (StackTraceElement element : throwable.getStackTrace()) {
                logMessage.append("\tat ").append(element.toString()).append("\n");
            }
        }

        log.error(logMessage.toString());
    }
}
