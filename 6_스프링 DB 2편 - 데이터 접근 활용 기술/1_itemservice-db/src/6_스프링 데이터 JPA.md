
# 스프링 데이터 JPA

---------------------------------------------------------------------------------------------------------------------------------

> ## 등장 이유

### POJO
- Plain Old Java Object


### 스프링
- 현재 EJB 컨테이너 대체
- 단순함의 승리


### 하이버네이트
- EJB 엔티티빈 기술을 대체
- JPA(Java Persistence API)라는 새로운 표준 정의
- JPA 구현체들
  - 하이버네이트, EclipseLink, OpenJPA


### Spring Data
- DB 공통적인 인터페이스르 제공함
- 종류
  - Spring Data JPA
  - Spring Data mongo
  - Spring Data Redis
  - ...
- 단순한 통합 그 이상
  - CRUD + 쿼리
  - 동일한 인터페이스
  - 페이징 처리
  - 메서드 이름으로 쿼리 생성
  - 스프링 MVC에서 ID 값만 넘겨도 도메인 클래스로 바인딩


### Spring Data만 알면 되나요?
- 자바를 모르고 스프링을 사용하는 것과 같음
- 해당 기술을 아는 사람이 편하게 사용하려고 쓰는 것

---------------------------------------------------------------------------------------------------------------------------------

> ## 기능

### JpaRepository 인터페이스
    <S extends T> S save(S entity)
    void delete(ID id)
    Optional<T> findById(ID id)
    Iterable<T> findAll()
    Long count()
    기타 등등...
- JpaRepository 인터페이스를 "extends"한 구현 인터페이스를 만들면 된다
- 메서드 이름으로 쿼리 생성


### Spring Data JPA 장점
- 코딩량
- 도메인 클래스를 중요하게 다룸
- 비즈니스 로직 이해 쉬움
- 더 많은 테스트 케이스 작성 가능
- 너무 복잡할 땐 SQL 사용


### 주의사항
- JPA(하이버네이트) 이해 필요
- 본인 먼저 JPA 이해
- 데이터베이스 설계 이해
- 대부분의 문제는 JPA를 모르고 사용해서 발생

---------------------------------------------------------------------------------------------------------------------------------

> ## 주요 기능

### 스프링 데이터 JPA 주요 기능
- 스프링 데이터 JPA는 JPA를 편리하게 사용할 수 있도록 도와주는 라이브러리이다
- 수많은 편리한 기능을 제공하지만 가장 대표적인 기능은 다음과 같다
  - 공통 인터페이스 기능
  - 쿼리 메서드 기능


### 공통 인터페이스 기능
- 기본적인 CRUD 기능 제공
- 공통화 가능한 기능이 거의 모두 포함(페이징, 카운트, ...)


### JpaRepository 사용법
    public interface ItemRepository extends JpaRepository<Item, Long> {
 
    }
- JpaRepository 인터페이스를 인터페이스 상속 받고, 제네릭에 관리할 <엔티티, 엔티티 ID>를 주면 된다
- 이 설정만 함녀 JpaRepository가 제공하는 기본 CRUD 기능을 모두 사용할 수 있다


### 스프링 데이터 JPA가 구현 클래스를 대신 생성
- JpaRepository 인터페이스만 상속받으면 스프링 데이터 JPA가 프록시 기술을 사용해서 구현 클래스를 만들어준다
- 그리고 만든 구현 클래스의 인스턴스를 만들어서 스프링 빈으로 등록한다


### 쿼리 메서드 기능
- 스프링 데이터 JPA는 인터페이스에 메서드만 적어두면, 메서드 이름을 분석해서 쿼리를 자동으로 만들고 실행해주는 기능을 제공한다


### 스프링 데이터 JPA 예시
    public interface MemberRepository extends JpaRepository<Member, Long> {
        List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    }
- 스프링 데이터 JPA는 메서드 이름을 분석해 필요한 JPQL을 만들고 실행해준다


### 스프링 데이터 JPA가 제공하는 쿼리 메서드 기능
- 조회
  - find_By, read_By, query_By, get_By
  - 예 : findHelloBy
- COUNT
  - count_By
  - 반환타입 long
- EXISTS
  - exists_By
  - 반환타입 boolean
- 삭제
  - delete_By, remove_By
  - 반환타입 long
- DISTINCT
  - findDistinct, findMemberDistinctBy
- LIMIT
  - findFirst3, findFirst, findTop, findTop3


### JPQL 직접 사용하기
        public interface MemberRepository extends JpaRepository<Member, Long> {
            @Query("select i from item i where i.itemName like :itemName and i.price <= :price")
            List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);
        }
- 스프링 데이터 JPA는 JPQL뿐만 아니라 JPA의 네이티브 쿼리 기능도 지원하는데, JPQL 대신에 SQL을 직접 작성할 수 있다
- @Query를 작성하면 메서드 이름으로 실행하는 규칙은 무시된다

---------------------------------------------------------------------------------------------------------------------------------

> ## 적용

### 의존관계와 구조
- ItemService는 ItemRepository에 의존하기 때문에 ItemService에서 SpringDataJpaItemRepository를 그대로 사용할 수 없다


### 런타임 객체 의존 관계
    ItemService -> JpaItemRepositoryV2 -> <<proxy>> 스프링 데이터 JPA


### findAll()
- 모든 데이터 조회
- 이름 조회
- 가격 조회
- 이름 + 가격 조회


### 메서드 이름 단점
- 조회 조건이 2개만 되어도 이름이 너무 길어지는 단점이 있다


### 하이버네이트 버전 오류
    // gradle
    ext["hibernate.version"] = "5.6.5.Final"
- 하이버네이트 5.5.6 ~ 5.5.7 버전에서는 "Like" 쿼리 오류가 발생하여 5.6.5 버전으로 변경함

---------------------------------------------------------------------------------------------------------------------------------

> ## Querydsl

### QUERY의 문제점
- QUQERY는 문자, Type-check 불가능
- 실행하기 전까지 작동여부 확인 불가


### 에러의 종류
- 컴파일 에러 (좋은 에러)
  - 문제가 터지기 전에 인지할 수 있다
- 런타임 에러 (나쁜 에러)


### Type-safe
- 컴파일시 에러 체크 가능
- Code-assistant x 100!!


### QueryDSL
- 쿼리를 Java로 type-safe하게 개발할 수 있게 지원하는 프레임워크
- 주로 JPA 쿼리(JPQL)에 사용


### JPA에서 Query 방법은 크게 3가지
1. JPQL(HQL)
2. Criteria API
3. MetaModel Criteria API(type-safe)


### MetaModel Criteria API
- Criteria API + MetaModel
- Criteria API와 거의 동일
- type-safe
- 복잡하긴 마찬가지

---------------------------------------------------------------------------------------------------------------------------------

> ## 해결

### DSL Domain-Specific Language
- 도메인 + 특화 + 언어
- 특정한 도메인에 초점을 맞춘 제한적인 표현력을 가진 컴퓨터 프로그래밍 언어
- 특징 : 단순, 간결, 유창


### QueryDSL
- 쿼리 + 도메인 + 특화 + 언어
- 쿼리에 특화된 프로그래밍 언어
- 단순, 간결, 유창
- 다양한 저장소 쿼리 기능 통합
- JPA, MongoDS, SQL 같은 기술들을 위해 type-safe SQL을 만드는 프레임워크


### 작동 방식
- QUERYDSL -> 생성 -> JPQL -> 생성 -> SQL


### QueryDSL-JPA
- 장점
  - type-safe
  - 단순함
  - 쉬움
- 단점
  - Q코드 생성을 위한 APT를 설정해야 함


### APT Annotation Process Tool
- 어노테이션을 처리하는 툴


### QueryDSL 결론
- 한번 써보면 돌아 올 수 없음
- 감동의 컴파일 에러
- 감동의 IDE 지원, ctrl+space, Code-assistant
- JPQL로 해결하기 어려운 복잡한 쿼리는 네이티브 SQL 쿼리 사용(JdbcTemplate, MyBatis)

---------------------------------------------------------------------------------------------------------------------------------

> ## QueryDsl 설정

### build.gradle
    //Querydsl 추가
	implementation 'com.querydsl:querydsl-jpa'
	annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jpa"
	annotationProcessor "jakarta.annotation:jakarta.annotation-api"
	annotationProcessor "jakarta.persistence:jakarta.persistence-api"

  
    //Querydsl 추가, 자동 생성된 Q클래스 gradle clean으로 제거
    clean {
      delete file('src/main/generated')
    }


### Gradle 설정하기
- setting -> gradle -> gralde로 설정한 경우
    1. Gradle -> Tasks-> build -> clean (build 파일 삭제) 
    2. Gradle -> Tasks -> other -> compileJava
- 확인방법
  - 프로젝트 -> build -> generated -> sources -> annotationProcessor -> java -> 패키지경로 -> Q 클래스이름 
- setting -> gradle -> IntelliJIDE로 설정한 경우
    1. 프로젝트에 main() 메서드 역할을 하는 메서드를 실행한다
    2. 다른 방법으로는 Build -> Rebuild를 실행한다
- 확인방법
  - 프로젝트 -> main -> generated -> 패키지경로 -> Q 클래스이름
  - GIT에 배포되지 않도록 ignore 설정하기
  

### 참고
- Q 타입은 컴파일 시점에 자동 새성되므로 버전관리(GIT)에 포함하지 않는 것이 좋다
- QueryDSL은 이렇게 설정하는 부분이 사용하면서 조금 귀찮은 부분이다
- 설정에 수고로움이 있지만 "queryDSL gradle"로 검색하면 본인 환경에 맞는 대안을 금방 찾을 수 있다

---------------------------------------------------------------------------------------------------------------------------------

> ## QueryDsl 적용

### 공통
- QueryDSL을 사용하려면 JPAQueryFactory가 필요하다
- JPAQueryFactory는 JPA 쿼리인 JPQL을 만들기 때문에 EntityManager가 필요하다
- 참고로 JPAQueryFactory를 스프링 빈으로 등록해서 사용해도 된다


### 예외 변환
- QueryDSL은 별도의 스프링 예외 추상화를 지원하지 않는다. 대신에 JPA에서 학습한 것처럼 @Repository에서 스프링 예외 추상화를 처리해준다



























