
# API 개발 고급 - 실무 필수 최적화

----------------------------------------------------------------------------------------------------------------------------------

> ## OSIV와 성능 최적화

### OSIV Open Session In View
- Open Session In View : 하이버네이트
- Open EntityManager In View : JPA
- 관례상 OSIV라 한다


### 스프링 설정
    spring.jpa.open-in-view : true (기본값)
    spring.jpa.open-in-view : false


### OSIV가 true
- API : 클라이언트에게 데이터를 반환할 때까지 DB Connection을 가지고 있다
- 화면 : 클라이언트에게 화면이 뿌려질 때까지 DB Connection을 가지고 있다


### OSIV true 단점
- 너무 오랜시간동안 DB 커넥션 리소스를 사용하기 때문에, 실시간 트래픽이 중요한 어플리케이션에서는 커넥션이 모자랄 수 있다 (장애 발생)


### OSIV false
- 트랜잭션이 종료되는 순간 영속성 컨텍스트를 닫고, DB Connection을 반환한다


### OSIV false 단점
- 모든 지연로딩을 트랜잭션 안에서 처리해야 한다
- view template에서 지연로딩이 동작하지 않는다


### 커맨드와 쿼리 분리
- OrderService : 핵심 비즈니스 로직
- OrderQueryService : 화면이나 API에 맞춘 서비스 (주로 읽기 전용 트랜잭션 사용)
- 작은 서비스는 OrderService만 사용


### 참고
- 고객 서비스의 실시간 API는 OSIV를 끄고, ADMIN처럼 커넥션을 많이 사용하지 않는 곳에서는 OSIV를 켠다
- 책 ORM 표준 JPA 프로그래밍 13장 참고 










