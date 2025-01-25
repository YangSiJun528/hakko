# 프로젝트 소개

## 주요 기능
- 시청자가 적은 스트리머 소개
- 실시간 플랫폼 전환 (HTMX 활용)
- 임베디드 스트림 플레이어
- 주기적인 자동 데이터 업데이트

## 기술 스택
- Backend
    - Java 21
    - Spring Boot 3.4.1
    - Gradle
- Frontend
    - Thymeleaf
    - HTMX
    - 순수 CSS

## 아키텍처

### 핵심 컴포넌트
- `LiveStreamRepository`: ConcurrentHashMap 기반 인메모리 저장소
- `PlatformScanner`: 스트리밍 플랫폼 스캐닝 인터페이스
- `ScheduleTask`: 주기적 데이터 업데이트
- `LiveStreamController`: 웹 요청 처리
