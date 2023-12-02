
# Redis 설치

------------------------------------------------------------------------------------------------------------------------

> ## MacOS

### 설치 경로
https://redis.io/docs/getting-started/installation/install-redis-on-mac-os/


### 설치 순서
1. HomeBrew 설치 
    > /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
2. Redis 설치
    > brew install redis
3. Redis 실행
    > brew services start redis
4. Redis 종료
    > brew services stop redis

------------------------------------------------------------------------------------------------------------------------
> ### Windows

### 설치 경로
https://redis.io/docs/getting-started/installation/install-redis-on-windows/


### 다운로드
- https://github.com/microsoftarchive/redis


### 설치 순서
1. Redis 설치
    > curl -fsSL https://packages.redis.io/gpg | sudo gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg <br>
      echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/redis.list <br>
      sudo apt-get update <br>
      sudo apt-get install redis

2. Redis 실행
    > sudo service redis-server start


### 포트 번호
- 기본 포트 번호 : 6379


### 참고
- https://pamyferret.tistory.com/9