
# 데이터 타입 (Data Type) 알아보기

------------------------------------------------------------------------------------------------------------------------

> ## Strings

### Strings
    // 데이터 저장, 얻기
    SET lecture inflearn-redis
    GET lecture

    // 여러 데이터를 한번에 등록하기 (price, langeuge)
    MSET price 100 language ko

    // 여러 키로 값을 배열로 얻기 (키 : lecture, price, langauge)
    MGET lecture price language

    // 증감 명령어
    INCR price          (1 더하기)
    INCRBY price 10     (10 더하기)

    // 키는 의미별로 ":"를 사용해서 구분한다
    SET inflearn-redis '{"price": 100, "language": "ko"}'
- 문자열, 숫자, serialized object(JSON string) 등 저장

------------------------------------------------------------------------------------------------------------------------

> ## Lists

### Lists
    // queue처럼 사용
    LPUSH queue job1 job2 job3
    RPOP queue

    // stack처럼 사용   
    LPUSH stack job1 job2 job3
    LPOP stack

    LPUSH queue job1 job2 job3
    LRANGE queue -2 -1
    LTRIM queue 0 0
- String을 Linked List로 저장 -> push/pop에 최적화 O(1)
- Queue, Stack 구현에 사용


### List 응용
    // 모든 데이터 확인
    LRANGE queue 0 -1
- 양수 인덱스 : List에 0번부터 n번까지 데이터를 확인
- 음수 인덱스 : List에 -1은 List에 마지막 요소를 나타내고, 뒤에서부터 -1씩 추가됨 (-1, -2, ...)

------------------------------------------------------------------------------------------------------------------------

> ## Sets

### Sets
- 중복 없는 String 값을 저장하는 정렬되지 않은 집합
- Set Operation 사용 가능 (intersection, union, difference)


### 명령어
    // 요소 저장
    SADD user:1:fruits apple banana orange orange

    // 모든 요소 조회
    SMEMBERS user:1:fruits
    
    // 고유한 아이템의 개수
    SCARD user:1:fruits

    // 요소가 포함되어 있는지 (레몬이 포함되어 있는지)
    SISMEMBER user:1:fruits lemon


### 응용
    SADD user:1:fruits apple banana orange orange
    SADD user:2:fruits apple lemon

    // user1, user2의 공통 과일 (user1 and user2)
    SINTER user:1:fruits user:2:fruits

    // user1에는 포함되고, user2에는 포함되지 않는 과일 (user1-user2)
    SDIFF user:1:fruits user:2:fruits

    // user1 또는 user2에 포함되는 과일 (user1 or user2)
    SUNION user:1fruits user:2:fruits

------------------------------------------------------------------------------------------------------------------------

> ## Hashes

### Hashes
- field-value 구조를 갖는 데이터 타입
- 다양한 속성을 갖는 객체의 데이터를 저장할 때 유용


### 명령어
    // lecture에 데이터 저장
    HSET lecture name inflearn-redis price 100 language ko

    // name key에 해당하는 value를 조회
    HGET lecture name

    // 여러 key에 데이터 조회
    HMGET lecture price language invalid

    // 모든 요소 조회
    HGETALL lecture

    // price 데이터에 +10
    HINCRBY lecture price 10

------------------------------------------------------------------------------------------------------------------------

> ## Sorted Sets

### Sorted Sets 
- score를 통해 정렬된 집합 (Set의 기능 + 추가로 score 속성 저장)
- 내부적으로 Skip List + Hash Table로 이루어져 있고, score 값에 따라 정렬 유지
- score가 동일하면 사전순으로 정렬
- 'ZSets' 이라고 불린다


### 명령어
    // 데이터 저장 (key [score1 value1 score2 value2 ...])
    ZADD points 10 TeamA 10 TeamB 50 TeamC

    // 인덱스 0부터 -1까지 조회    
    ZRANGE points 0 -1
    
    // 스코어를 기준으로 역순으로 조회 (withscores : 스코어도 같이 조회, rev : 거꾸로) 
    ZRANGE points 0 -1 REV WITHSCORES

    // 인덱스를 반환
    ZRANK points TeamA

------------------------------------------------------------------------------------------------------------------------

> ## Streams

### Stream
- append-only log에 consumer groups과 같은 기능을 더한 자료 구조
- unique id를 통해 하나의 entry를 읽을 때, O(1) 시간 복잡도
- Consumer Group을 통해 분산 시스템에서 다수의 consumer가 event 처리


### 명령어
    // 이벤트 추가
    XADD events * action like user_id 1 product_id 1

    // 이벤트 삭제
    XDEL events 123897283123-0 (이벤트 ID)

    // 다수의 메시지 조회
    XRANGE events - +

------------------------------------------------------------------------------------------------------------------------

> ## Geospatial

### Geospatial
- 좌표를 저장하고, 검색하는 데이터 타입
- 거리 계산, 범위 탐색 등 지원


### 명령어
    // 강남 홍대 위치 등록
    GEOADD seoul:station 126.923287(위도) 37.723834(경도) hong-dae 127.032323 37.49834893 gang-name 

    // 강남 홍대 거리 조회
    GEODIST seoul:station hong-dae gang-nam km

------------------------------------------------------------------------------------------------------------------------

> ## Bitmaps

### Bitmaps
- 실제 데이터 타입은 아니고, String에 binary operation을 적용한 것
- 최대 42억개 binary 데이터 표현 = 2**32


### 명령어
    // 저장
    SETBIT user:log-in:23-01-01 유저이름 1

    // 개수 조회
    BITCOUNT user:log-in:23-01-01
    1

    // BIT 연산자 수행하고 result로 결과값 저장
    BITOP AND result user:log-in:23-01-01 user:log-in:23-01-02

------------------------------------------------------------------------------------------------------------------------

> ## HyperLogLog

### cardinality
- 고유한 값의 수


### HyperLogLog
- 집합의 cardinality를 추정할 수 있는 확률형 자료구조
- 정확성을 일부 포기하는 대신 저장공간을 효율적으로 사용 (평균 에러 0.81%)
- 실제 값을 저장하지 않기 때문에 매우 적은 메모리


### 명령어
    // 저장
    PFADD fruits apple orange grape kiwi
    
    // 요소의 개수 조회
    PFCOUNT fruits 

------------------------------------------------------------------------------------------------------------------------

> ## BloomFilter

### BloomFilter
- element가 집합 안에 포함되었는지 확인할 수 있는 확률형 자료 구조
- 정확성을 이부 포기하는 대신 저장공간을 효율적으로 사용
- false positive
  - element가 집합에 실제로 포함되지 않는데 포함되었다고 잘못 예측하는 경우
- 실제 값을 저장하지 않기 때문에 매우 적은 메모리 사용 (Set에 비해 적다)


### 명령어
    
    BF.MADD fruits apple orange
    BF.EXISTS fruits kiwi










