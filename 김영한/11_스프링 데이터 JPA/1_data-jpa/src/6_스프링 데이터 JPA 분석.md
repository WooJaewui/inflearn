
# 스프링 데이터 JPA 분석

--------------------------------------------------------------------------------------------------------------------------------

> ## 스프링 데이터 JPA 구현체 분석

### SimpleJpaRepository
    @Repository
    @Transcational(readOnly = true)
    public class SimpleJpaRpository<T, ID> implements JpaRepositoryImplementation<T, ID> { ... }
- Spring Data JPA 구현체
- @Repository
  - Spring Bean으로 등록한다
  - Repository에서 Exception이 발생하면 Spring DB Exception으로 변환된다
- @Transactional(readOnly = true)
  - 트랜잭션을 따로 설정하지 않으면 Repository에서 트랜잭션을 검
  - 조회가 많기 때문에 클래스 레벨에는 readOnly=true, save() 같은 메서드에는 @Transactional 어노테이션이 붙어있다


### @Transactional(readOnly = true)
- readOnly=true 옵션을 사용하면 플러시를 생략해서 약간의 성능 향상을 얻을 수 있음 (변경 감지 X)
- 자세한 내용은 JPA 책 15.4.2 읽기 전용 쿼리의 성능 최적화 참조


### save() 메서드
- 새로운 엔티티면 저장 (persist)
- 새로운 엔티티가 아니면 병합 (merge)


### merge 활용
- 영속 상태의 엔티티가 영속 상태에서 벗어나는 경우에 다시 영속상태로 만들 때 사용

--------------------------------------------------------------------------------------------------------------------------------

> ## 새로운 엔티티를 구별하는 방법

### 새로운 엔티티를 판단하는 기본 전략 
    entityInformation.isNew(entity)
- 식별자가 객체일 때, "null"로 판단
- 식별자가 기본 타입일 때, "0"으로 판단
- Persistable<T>를 implements한 후, isNew() 메서드를 구현한다 


### 참고
- JPA 식별자 생성 전략이 @Id만 사용해서 직접 할당이면 이미 식별자 값이 있는 상태로 save()를 호출한다
- 이런 경우, Persistable<T> 인터페이스를 구현해서 isNew() 메서드를 구현해서 사용하는 것이 좋다























