
# JPA
 
--------------------------------------------------------------------------------------------------------------------------------------

> ## JPA 시작

### 개요
- JPA는 스프링 만큼이나 방대하고, 학습해야할 분량도 많다. 하지만 한번 배워두면 데이터 접근 기술에서 매우 큰 생산성 향상을 얻을 수 있다
- JPA를 사용하면 SQL도 JPA가 대신 작성하고 처리해준다
- 이 강의에서는 JPA와 스프링 데이터 JPA, 그리고 Querydsl로 이어지는 전체 그림을 볼 것이다


### 실무
- JPA를 더욱 편리하게 사용하기 위해 스프링 데이터 JPA와 Querydsl이라는 기술을 함께 사용한다


### 참고
- 각각의 기술들은 별도의 강의로 다룰 정도로 내용이 방대하다.
- 여기서는 해당 기술들의 기본 기능과, 왜 사용해야 하는지 각각의 장단점을 알아본다

--------------------------------------------------------------------------------------------------------------------------------------

> ## SQL 중심적인 개발의 문제점

### 객체와 관계형 데이터베이스의 차이
- 상속
- 연관관계
- 데이터 타입
- 데이터 식별 방법


### 연관관계
- 객체는 참조를 사용 : member.getTeam()
- 테이블은 외래 키를 사용 : JOIN ON M.TEAM_ID = T.TEAM_ID


### 엔티티 신뢰 문제
- 계층형 아키텍처는 진정한 의미의 계층 분할이 어렵다


### 비교하기 문제
- Java Collection에서 같은 시퀀스의 클래스를 비교하면 같지만, SQL에서 같은 시퀀스의 조회 결과를 가진 두 클래스를 비교하면 false가 나온다


### 결론
- 객체를 자바 컬렉션에 저장하듯이 DB에 저장할 수 없을까 ? => JPA

-----------------------------------------------------------------------------------------------------------------------------------

> ## JPA 소개

### JPA
- Java Persistence API
- 자바 진영의 ORM 기술 표준 (인터페이스)


### ORM
- Object-Relational Mapping(객체 관계 매핑)
- 객체는 객체대로 설계
- 관계형 데이터베이스는 관계형 데이터베이스대로 설계
- ORM 프레임워크가 중간에서 매핑
- 대중적인 언어에는 대부분 ORM 기술이 존재


### JPA는 표준 명세
- JPA는 인터페이스의 모음
- JPA 2.1 표준 명세를 구현한 3가지 구현체
- 하이버네이트(대부분), EclipseLink, DataNucleus


### JPA 버전
- JPA 1.0(JSR 220) 2006년 : 초기 버전. 복합 키와 연관관계 기능이 부족
- JPA 2.0(JSR 317) 2009년 : 대부분의 ORM 기능을 포함, JPA Criteria 추가
- JPA 2.1(JSR 338) 2013년 : 스토어드 프로시저 접근, 컨버터, 엔티티 그래프 기능이 추가


### JPA를 왜 사용해야 하는가?
- SQL 중심적인 개발에서 객체 중심으로 개발
- 생산성
- 유지보수
- 패러다임의 불일치 해결
- 성능
- 데이터 접근 추상화와 벤더 독립성
- 표준


### JPA의 성능 최적화 기능
1. 1차 캐시와 동일성 identity 보장
2. 트랜잭션을 지원하는 쓰기 지연 transactional write-behind
3. 지연 로딩 Lazy Loading


### 1차 캐시와 동일성 보장
1. 같은 트랜잭션 안에서는 같은 엔티티를 반환 - 약간의 조회 성능 향상
2. DB Isolation Level이 Read Commit이어도 어플리케이션에서 Repeatable Read 보장


### 지연로딩과 즉시 로딩
- 지연 로딩 : 객체가 실제 사용될 때 로딩
- 즉시 로딩 : JOIN SQL로 한번에 연관된 객체까지 미리 조회

---------------------------------------------------------------------------------------------------------------------------------------

> ## JPA 설정

### build.gradle 설정
    //JPA, 스프링 데이터 JPA 추가
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'


### 확인방법
- External Libraries
  - hibernate
  - spring-data-jpa


### JPA log 설정
    logging.level.org.hibernate.SQL=DEBUG
    logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
- org.hibernate.SQL=DEBUG : 하이버네이트가 생성되고 실행하는 SQL을 확인할 수 있다
- org.hibernate.type.descriptor.sql.BasicBinder=TRACE : SQL에 바인딩되는 파라미터를 확인할 수 있다
- spring.jpa.show-sql=true : 참고로 이런 설정도 있다. 이전 설정은 "logger"를 통해서 SQL이 출력된다 (System.out을 통해 SQL이 출력)
    - 위 설정을 둘 다 켜면 logger, System.out 둘 다 로그가 출력되어 같은 로그가 중복해서 출력된다 

---------------------------------------------------------------------------------------------------------------------------------------

> ## 개발

### 개요
- JPA에서 가장 중요한 부분은 객체와 테이블을 매핑하는 것이다.


### 객체 매핑
- @Entity
  - JPA가 사용하는 객체라는 뜻이다
  - 이 어노테이션이 있어야 JPA가 인식할 수 있다
- @Id
  - 테이블의 PK와 해당 필드를 매핑한다
- @GeneratedValue(strategy = GenerationType.IDENTITY)
  - PK 생성 값을 데이터베이스에서 생성하는 IDENTITY 방식을 사용한다
- @Column
  - 객체의 필드를 테이블의 컬럼과 매핑한다
  - name = "item_name" : 객체는 itemName이지만 테이블의 컬럼은 item_name이므로 이렇게 매핑한다
  - length = 10 : JPA 매핑 정보로 DDL도 생성할 수 있는데, 그 때 컬럼의 길이 값으로 활용된다 (varchar 10)
  - @Column을 생략할 경우 필드의 이름을 테이블 컬럼 이름으로 사용한다
  - 참고로 지금처럼 스프링 부트와 통합해서 사용하면 필드 이름을 테이블 컬럼 명으로 변경할 때 카멜 -> 스네이크 방식으로 변경해준다
  - itemName -> item_name 생략가능
- JPA는 public 또는 protected 기본 생성자가 필수이다 (기본생성자 꼭 넣기)


### 참고
- JPA를 설정하려면 EntityManagerFactory, JPA 트랜잭션 매니저, 데이터소스 등등 다양한 설정을 해야 한다
- 스프링 부트는 이 과정을 모두 자동화 해준다

---------------------------------------------------------------------------------------------------------------------------------------

> ## 리포지토리 분석

### save() 저장
    em-persist(item)
- JPA에서 객체를 테이블에 저장할 때는 엔티티 매니저가 제공하는 persist() 메서드를 사용하면 된다
- JPA가 만들어서 실행한 SQL을 보면 id에 값이 빠져있는 것을 확인할 수 있다
- PK 키 생성 전략을 IDENTITY로 사용했기 때문에 JPA가 이런 쿼리를 만들어서 실행한 것이다
- JPA가 INSERT SQL 실행 이후에 생성된 ID 결과를 받아서 넣어준다


### update() 수정
- em.update() 같은 메서드를 전혀 호출하지 않았다
- JPA가 어떻게 변경된 엔티티 객체를 찾는지 명확하게 이해하려면 영속성 컨텍스트라는 JPA 내부 원리를 이해해야 한다
- 테스트의 경우 마지막에 트랜잭션이 롤백되기 때문에 JPA는 UPDATE SQL을 실행하지 않는다


### findById() 단건 조회
    Item item = em.find(Item.class, id);
- JPA에서 엔티티 객체를 PK를 기준으로 조회할 때는 find()를 사용하고 조회 타입과, PK 값을 주면 된다


### findAll() 목록 조회
- JPQL
  - Java Persistence Query Language라는 객체지향 쿼리 언어를 제공한다
  - 주로 여러 데이터를 복잡한 조건으로 조회할 때 사용한다
  - SQL이 테이블을 대상으로 한다면, JPQL은 엔티티 객체를 대상으로 SQL을 실행한다 생각하면 된다
  


### 동적쿼리 문제
- JPA를 사용해도 동적 쿼리 문제가 남아있다
- 동적 쿼리는 뒤에서 설명하는 Querydsl이라는 기술을 활용하면 매우 깔끔하게 사용할 수 있다


### 참고
- JPQL에 대한 자세한 내용은 JPA 기본편 강의를 참고하자

---------------------------------------------------------------------------------------------------------------------------------------

> ## 예외 변환

### 개요
- JPA의 경우 예외가 발생하면 JPA 에외가 발생하게 된다
- JPA는 PersistenceException과 그 하위 예외를 발생시킨다
  - 추가로 JPA는 IllegalStateException, IllegalArgumentException을 발생시킬 수 있다
- @Repository를 통해 DataAccessException으로 변환할 수 있다


### @Repository의 기능
- "@Repository"가 붙은 클래스는 컴포넌트 스캔의 대상이 된다
- "@Repository"가 붙은 클래스는 예외 변환 AOP의 적용 대상이 된다
  - 스프링과 JPA를 함께 사용하는 경우 스프링은 JPA 예외 변환기(PersistenceExceptionTranslator)를 등록한다
  - 예외 변환 AOP 프록시는 JPA 관련 예외가 발생하면 JPA 예외 변환기를 통해 발생한 예외를 스프링 데이터 접근 예외로 변환한다


### 예외 변환 동작 순서
    결과적으로 리포지포트에 @Repository 어노테이션만 있으면 스프링이 예외 변환을 처리하는 AOP를 만들어준다
1. EntityMange - JPA 예외 발생
2. JpaItemRepository - PersistenceException 전달
3. 예외 변환 AOP Proxy - PersistenceException 전달
4. JPA 예외를 스프링 예외 추상화로 변환
5. 서비스 계층 - 스프링 예외로 변환된 Exception 전달
6. 서비스 계층 - 스프링 예외 추상화에 의존


### 스프링 부트
- PersistenceExceptionTranslationPostProcess 클래스를 자동으로 등록한다








