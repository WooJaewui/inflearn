
 # 데이터 접근 기술 - 스프링 JdbcTemplate
 
---------------------------------------------------------------------------------------------------------------------------------

> ## JdbcTemplate 소개와 설정

### JdbcTemplate 특징
- 장점
  - 설정의 편리함
    - JdbcTemplate은 spring-jdbc 라이브러리에 포함되어 있는데, 이 라이브러리는 스프링으로 JDBC를 사용할 때 기본으로 사용되는 라이브러리다
  - 반복 문제 해결
    - 개발자는 SQL을 작성하고, 전달할 파라미터를 정의하고, 응답 값을 매핑하기만 하면 된다 
    - 우리가 생각할 수 있는 대부분의 반복 작업을 대신 처리해준다
- 단점
  - 동적 SQL을 해결하기 어렵다


### JdbcTemplate 설정
    //JdbcTemplate 추가
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
    //H2 데이터베이스 추가
    runtimeOnly 'com.h2database:h2'
- org.springframework.boot:spring-boot-starter-jdbc를 추가하면 JdbcTemplate이 들어있는 spring-jdbc가 라이브러리에 포함된다

---------------------------------------------------------------------------------------------------------------------------------

> ## 기본

### KeyHolder
- INSERT 쿼리 실행 이후에 데이터베이스에서 생성된 ID 값을 조회할 수 있다


### queryForObject()
- 결과 로우가 하나일 때 사용한다
- 결과가 없으면 EmptyResultDataAccessException 예외가 발생한다
- 결과가 둘 이상이면 IncorrectResultSizeDataAccessException 예외가 발생한다


### itemRowMapper()
- 데이터베이스의 조회 결과를 객체로 변환할 때 사용한다

---------------------------------------------------------------------------------------------------------------------------------

> ## 동적 쿼리 문제

### MyBatis
- JdbcTemplate 이후에 설명할 MyBatis의 가장 큰 장점은 SQL을 직접 사용할 때 동적 쿼리를 쉽게 작성할 수 있다는 점이다

---------------------------------------------------------------------------------------------------------------------------------

> ## 구성과 실행

### application.properties
- src/test, src/main 둘 다 application.properties가 존재한다
- test, main을 따로 설정할 수 있다

---------------------------------------------------------------------------------------------------------------------------------

> ## 이름 지정 파라미터 1

### 순서대로 바인딩
- JdbcTemplate을 기본으로 사용하면 파라미터를 순서대로 바인딩 한다
- (?, ?, ?, ...) -> itemName, price, ...


### 참고
- 버그 중에 가장 고치기 힘든 버그는 데이터베이스에 데이터가 잘못 들어간느 버그다
- 이것은 코드만 고치는 수준이 아니라 데이터베이스의 데이터를 복구해야 하기 때문에 버그를 해결하는데 들어가는 리소스가 엄청 크다


### NamedParameterJdbcTemplate
- 이름으로 파리미터를 바인딩 하는 JdbcTemplate 방식이다

---------------------------------------------------------------------------------------------------------------------------------

> ## 이름 지정 파라미터 2

### 이름 지정 파라미터
- 파라미터를 전달하려면 Map 처럼 key, value 데이터 구조를 만들어서 전달해야 한다
- 여기서 key는 ":파라미터이름"으로 지정한,파라미터의 이름이고, value는 해당 파라미터의 값이 된다


### 이름 지정 바인딩에서 자주 사용하는 파라미터의 종류.
- Map
- SqlParameterSource
  - MapSqlParameterSource
  - BeanPropertySqlParameterSource


### Map
    Map.of(키, 값);
    template.queryForObject(sql, param, itemRowMapper());
- 단순히 Map을 사용하면 된다


### MapSqlParameterSource
    SqlParamterSource param = new MapSqlparameterSource
                                  .addValue(키1, 값1)
                                  .addValue(키2, 값2)
                                  ...
    template.update(sql, param);
- Map과 유사한데, SQL 타입을 지정할 수 있는 등 SQL에 좀 더 특화된 기능을 제공한다
- SqlParameterSource 인터페이스의 구현체이다
- MapSqlParamterSource는 메서드 체인을 통해 편리한 사용법도 제공한다


### BeanPropertySqlParameterSource
    SqlParamterSource pram = new BeanPropertySqlParameterSource(item);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql, param, keyHolder);
- 자바빈 프로퍼티 규약을 통해서 자동으로 파라미터 객체를 생성한다
- 예를 들어서 getItemName(), getPrice()가 있다면 데이터를 자동으로 만들어 낸다
  - key = itemName, value = 상품명 값
  - key = price, value = 가격 값


### 정리
- BeanPropertySqlParameterSource를 매번 사용하는 게 좋아보이지만, 항상 사용할 수 있는 것은 아니다
  - DTO에 파라미터에 해당하는 값이 없는 경우 사용할 수 없다


### BeanPropertyRowMapper
- ResultSet의 결과를 받아서 자바빈 규약에 맞추어 데이터를 변환한다
- 실제로는 리플렉션 같은 기능을 사용한다


### 별칭
- DB 컬럼명과 DTO 변수 명이 다른 경우 SQL 별칭 AS를 사용해서 이름을 변경하는 방법을 사용한다
- JdbcTemplate, Mybatis 같ㅇ느 기술에서도 자주 사용된다


### 관례의 불일치
- 자바 객체는 카멜(camelCase) 표기법을 사용한다, 반면에 데이터베이스에서는 주로 언더스코어(snake_case)를 사용한다
- 이 부분을 관례로 많이 사용하다 보니 "BeanPropertyRowMapper"는 언더스코어 표기법을 카멜로 자동 변환해준다

---------------------------------------------------------------------------------------------------------------------------------

> ## 이름 지정 파라미터 3

### 정리
- JdbcTemplateItemRepositoryV2, JdbcTemplateV2Config, ItemServiceApplication 코드 변경

---------------------------------------------------------------------------------------------------------------------------------

> ## SimpleJdbcInsert

### SimpleJdbcInsert
- dataSOurce를 받고 내부에서 SimpleJdbcInsert를 생성해서 가지고 있다
- JdbcTemplate 관련 기능을 사용할 때 관례상 이 방법을 많이 사용한다

---------------------------------------------------------------------------------------------------------------------------------

> ## JdbcTemplate 기능 정리

### 주요 기능
- JdbcTemplate
  - 순서 기반 파라미터 바인딩을 지원
- NamedParameterJdbcTemplate
  - 이름 기반 파라미터 바인딩을 지원한다 (권장)
- SimpleJdbcInsert
  - INSERT SQL을 편리하게 사용할 수 있다
- SimpleJdbcCall
  - 스토어드 프로시저를 편리하게 호출할 수 있다


### 조회
- 객체 하나를 조회
  - template.queryForObject(...)
- 여러 로우를 조회
  - template.query(...)























