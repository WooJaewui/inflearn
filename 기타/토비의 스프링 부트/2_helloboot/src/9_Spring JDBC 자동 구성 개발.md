
# Spring JDBC 자동 구성 개발

---------------------------------------------------------------------------------------------------------------------------------

> ## 자동 구성 클래스와 빈 설계

### JdbcOperations
- JdbcTemplate가 구현한 인터페이스


### DataSource 인터페이스
- 구현체
  - SimpleDriverDataSource
  - HikariDataSource : 최근에 가장 많이 사용하는 DataSource (com.zaxxer.hikari.HikariDataSource)


### MyAutoConfigurationDataSourceConfig
- DataSource 인터페이스
  - SimpleDriverDataSource
  - HikariDataSource
    - DataSourceProperties
- JdbcTemplate
- JdbcTransactionManager

---------------------------------------------------------------------------------------------------------------------------------

> ## DataSource 자동 구성 클래스

### @ExtendWith
    @ExtendWith("SpringExtension.class")
- 스프링 컨텍스트를 이용하는 스프링 테스트를 이용할 수 있다


### @ContextConfiguration
    @ContextConfiguration(classes = HellobootApplication.class)
- HellobootApplication 클래스에서 설정한 빈들을 Context 넣고 테스트를 진행할 수 있다


### @TestPropertySource
    @TestPropertySource("classpath:/application.properties")
- 테스트에서 사용할 프로퍼티 설정을 불러올 수 있다


### 테스트 클래스
    @ExtendWith("SpringExtention.class")
    @ContextConfiguration(classes=HellobootApplication.class)
    @TestProperties("classpath:/application.properties")
    public class DataSourceConfig {
      ...
    }


### @ConditionalOnMissingBean
- 같은 타입의 빈이 없을 때만 빈으로 등록한다

---------------------------------------------------------------------------------------------------------------------------------

> ## JdbcTemplate과 트랜잭션 매니저 구성

### JdbcTemplate
- Jdbc 구성을 쉽게 간결하게 사용할 수 있도록 만든 Template 이다


### JdbcTransactionManager
- 스프링 트랜잭션 추상화를 위해 만든 스프링 빈이다


### @ConditionalOnSingleCandidate
    @ConditionalOnSingleCandidate(DataSource.class)
- DataSource 클래스가 빈으로 1개만 등록되어 있는 경우에만 빈으로 등록하겠다라는 뜻


### @EnableTransactionManagement
    @EnableTransactionManagement
- @Transactional 어노테이션을 사용할 수 있다 (AOP 기능 추가)


### Test에서 Transactional
- 테스트에서 @Rollback 어노테이션이 따로 없다면 무조건 롤백한다
- TransactionalTestExecutionListener 클래스에서 isDefaultRollback() 메서드 참조
- 추가
  - @BeforeEach, @AfterEach는 @Test와 같은 Transaction을 공유한다
  - @BeforeTestClass, @AfterTestClass는 @Test와 Transaction을 공유하지 않는다

---------------------------------------------------------------------------------------------------------------------------------

> ## Hello 리포지토리

### 인터페이스의 default 메서드
- JDK 1.8 버전부터 생긴 기능
- Comparable 인터페이스를 보면서 공부해보자

---------------------------------------------------------------------------------------------------------------------------------

> ## 리포지토리를 사용하는 HelloService

### 테스트 대역
- 테스트하려는 객체가 다른 객체들이 여러 관계가 엮여있어 사용하기 힘들 때, 대체할 수 있는 객체를 의미한다
- Dummy, Stub, Spy, Mock, Fake로 나눠진다 


### Stub
- https://azderica.github.io/00-test-mock-and-stub/ 보고 다시 정리하기 











