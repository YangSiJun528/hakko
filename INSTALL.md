# 설치 가이드

## 시스템 요구사항
- Java 21 이상

## 설치 및 실행

1. 저장소 클론
```bash
git clone https://github.com/YangSiJun528/hakko.git
cd hakko
```

2. 프로젝트 실행
```bash
./gradlew bootRun
```

또는 JAR 파일로 실행:
```bash
./gradlew build
java -jar build/libs/hakko.jar
```

## 설정

### 포트 변경
기본 포트(8080)를 변경하는 방법:

1. `application.properties` 수정
```properties
server.port=원하는_포트
```

2. 실행 시 포트 지정
```bash
java -jar build/libs/hakko.jar --server.port=원하는_포트
```
