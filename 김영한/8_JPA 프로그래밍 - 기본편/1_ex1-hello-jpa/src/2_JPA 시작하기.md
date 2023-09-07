
# JPA 시작하기

----------------------------------------------------------------------------------------------------------------------------------

> ## Hello JPA - 프로젝트 생성

### 버전 선택 방법
- 스프링 공홈 -> boot 프로젝트 -> LEARN 선택 -> 사용하려는 부트 버전에 Reference Doc 선택 -> Dependency Versions 


### persistence.xml 
    <?xml version="1.0" encoding="UTF-8"?>
    <persistence version="2.2"
        xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">

        <persistence-unit name="hello">
            <properties>
                <!-- 필수 속성 -->
                <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
                <property name="javax.persistence.jdbc.user" value="sa"/>
                <property name="javax.persistence.jdbc.password" value=""/>
                <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
                <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
            
                <!-- 옵션 -->
                <property name="hibernate.show_sql" value="true"/>
                <property name="hibernate.format_sql" value="true"/>
                <property name="hibernate.use_sql_comments" value="true"/>
                <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
            </properties>
        </persistence-unit>
    </persistence>
- JPA 설정 파일
- resources/META-INF/persistence.xml 위치에 넣어야 한다
- JPA는 특정 언어에 종속적이지 않기 때문에 dialect를 설정해줘야 한다


### 데이터베이스 방언
- JPA는 특정 데이터베이스에 종속 X
- 각각의 데이터베이스가 제공하는 SQL 문법과 함수는 조금씩 다름
  - 가변 문자 : MySQL은 VARCHAR, Oracle은 VARCHAR2
  - 문자열을 자르는 함수 : SQL 표준은 SUBSTRING(), Oracle은 SUBSTR()
  - 페이징 : MySQL LIMIT, Oracle은 ROWNUM
- 방언 : SQL 표준을 지키지 않는 특정 테이터베이스만의 고유한 기능
- 하이버네이트는 40여가지의 방언을 제공한다


### 라이브러리
- javax.xxx : 자바 표준 (다른 구현체에서 사용 가능) 
- hibernate.xxx : 하이버네이트 구현체 전용 옵션

----------------------------------------------------------------------------------------------------------------------------------

> ## Hello JPA - 어플리케이션 개발

### JPA 구동 방식
- Persistence 클래스
    - 설정 정보 조회 (META-INF/persistence.xml)
    - 생성 (EntityManagerFactory)
    - 생성 (EntityManager) 


### pom.xml
    <dependency>
      <groupId>javax.xml.bind</groupId>
      <artifactId>jaxb-api</artifactId>
      <version>2.3.0</version>
    </dependency>
- xml.bind 설정 추가하기


### 조회하기
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    // 저장
    try {
        Member member = new Member();
        member.setId(2L);
        member.setName("HelloB");
        entityManager.persist(member);

        transaction.commit();
    } catch (Exception e) {
        transaction.rollback();
    } finally {
        entityManager.close();
        entityManagerFactory.close();
    }


### 수정하기
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    // 수정
    try {
        Member findMember = entityManager.find(Member.class, 1L);
        findMember.setName("HelloJPA");

        transaction.commit();
    } catch (Exception e) {
        transaction.rollback();
    } finally {
        entityManager.close();
        entityManagerFactory.close();
    }


### 주의
- 엔티티 매니저 팩토리는 하나만 생성해서 어플리케이션 전체에서 공유
- 엔티티 매니저는 쓰레드간에 공유 X (사용하고 버려야 한다)
- JPA의 모든 데이터 변경은 트랜잭션 안에서 실행


### JPQL 소개
- 가장 단순한 조회 방법
  - EntityManager.find()
  - 객체 그래프 탐색(a.getB().getC())
- 나이가 18살 이상인 회원을 모두 검색하고 싶다면??


### JPQL Java Persistence Query Language
- JPA를 사용하면 엔티티 객체를 중심으로 개발
- 문제는 검색 쿼리
- 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
- 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
- 어플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함도니 SQL이 필요
- JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공
- SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
- JPQL은 엔티티 객체를 대상으로 쿼리
- SQL은 데이터베이스 테이블을 대상으로 쿼리
- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
- SQL을 추상화해서 특정 데이터베이스 SQL에 의존하지 않음
- JPQL을 한마디로 정의하면 객체 지향 SQL
- JPQL은 뒤에서 아주 자세히 다룸


### JPQL 예시
    List<Member> resultList = entityManager.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
- setFirstResult(0) : 0번째 데이터부터 조회 (ROWNUM > 1)
- setMaxResults(10) : 10번째 데이터까지 조회 (ROWNUM < 10)















