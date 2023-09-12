
# API 개발 고급 - 지연 로딩과 조회 성능 최적화

----------------------------------------------------------------------------------------------------------------------------------

> ## 간단한 주문 조회 V1 : 엔티티를 직접 노출

### 양방향
- 양방향 관계인 경우, 관계 주인이 아닌 필드에 @JsonIgnore 해줘야 한다 (무한 루프 발생)


### 프록시 오류
- 엔티티를 Response로 보내는 경우 지연 로딩시 발생하는 오류 (org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor) 


### bytebuddy 해결 방법 
    implementation 'com.fasterxml.jackson.datatype:jackson-datatype-hibernate5'
- hibernate5 의존성을 추가하고, 빈으로 등록


### hibernate5 Bean
    @Bean
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5Module;
	}
- 강제로 지연 로딩을 수행하도록 만든다
- 엔티티에 연관되어 있는 모든 엔티티를 조회한다 (불필요한 조회 - 성능 저하)


### EAGER
- N+1 문제 발생
- 다른 API 문제 (성능 최적화 불가)
- fetch join을 사용해라! (V3 확인)

----------------------------------------------------------------------------------------------------------------------------------

> ## 간단한 주문 조회 V2 : 엔티티를 DTO로 변환

### DTO 변경
- 쿼리가 1 + N 번 실행된다

----------------------------------------------------------------------------------------------------------------------------------

> ## 간단한 주문 조회 V3 : 엔티티를 DT로 변환 - 페치 조인 최적화

### fetch join
- 1 + N 쿼리 문제를 해결

----------------------------------------------------------------------------------------------------------------------------------

> ## 간단한 주문 조회 V4 : JPA에서 DTO로 바로 조회

### 쿼리 작성
    em.createQuery("select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderSimpleQueryDto.class).getResultList();
- DTO를 만들어서 쿼리를 조회
- 내가 원하는 컬럼만 조회한다
- V3는 여러 곳에서 사용할 수 있다 (코드 재활용 가능, 성능이 V4에 비해 안 좋음)
- V4는 여러 곳에서 사용할 수 없다 (코드 재활용 불가능, 성능이 좋다, Dto는 데이터를 변경할 수 없음)


### 정리
- 엔티티를 DTO로 변경하거나, DTO로 바로 조회하는 두 가지 방법은 각각 장단점이 있다
- 둘 중 상황에 따라 더 나은 방법을 선택하면 된다


### V4 단점
- API 스펙이 쿼리에 들어와있기 때문에, API 스펙이 변경되면 쿼리도 변경해야 된다


### 일반 Repository, Dto 의존 Repository
- 다른 패키지를 만들어서, 구분해서 Repository를 만드는 것을 추천한다
- 일반 Repository는 Entity만 사용, Dto 의존 Repository는 다른 Repository, 패키지를 만들어서 사용


### 쿼리 방식 선택 권장 순서
1. 우선 엔티티를 DTO로 변환하는 방법을 선택
2. 필요하면 fetch join으로 성능을 최적화 -> 대부분의 성능 이슈가 해결
3. 그래도 안되면 DTO로 직접 조회나는 방법을 사용
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다














