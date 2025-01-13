#!/bin/bash
set -e

# Docker 이미지 태그를 인자로 받음
IMAGE=$1

# 현재 활성 서비스 확인
ACTIVE_SERVICE=$(docker ps --filter "name=hakko-app-blue" --filter "name=hakko-app-green" --format "{{.Names}}" | grep "hakko-app" || echo "")
if [ "$ACTIVE_SERVICE" == "hakko-app-blue" ]; then
  NEW_SERVICE="green"
  OLD_SERVICE="blue"
  NEW_PORT=8082
else
  NEW_SERVICE="blue"
  OLD_SERVICE="green"
  NEW_PORT=8081
fi

echo "현재 활성 서비스: $ACTIVE_SERVICE"
echo "새 서비스로 전환 중: hakko-app-$NEW_SERVICE"

# 새 컨테이너 실행
echo "새 서비스 컨테이너 실행 중..."
docker run -d --name hakko-app-$NEW_SERVICE -p $NEW_PORT:8080 $IMAGE

# Nginx 설정 업데이트
echo "Nginx 설정 업데이트 중..."
sed -i "s/server hakko-app-$OLD_SERVICE/server hakko-app-$NEW_SERVICE/" /path/to/nginx.conf

# Nginx 리로드
echo "Nginx 리로드 중..."
docker exec nginx nginx -s reload

# 이전 서비스 중지 및 제거
echo "이전 서비스 컨테이너 중지 및 제거..."
docker stop hakko-app-$OLD_SERVICE || true
docker rm hakko-app-$OLD_SERVICE || true

echo "배포 완료! 현재 활성 서비스: hakko-app-$NEW_SERVICE"
