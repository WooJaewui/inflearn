
# 실전프로젝트 - 인가 프로세스 DB 연동 서비스 계층 구현

------------------------------------------------------------------------------------------------------------------------

> ## Method 방식 개요

### 서비스 계층의 인가처리 방식
- 화면 메뉴 단위가 아닌 기능 단위로 인가 처리
- 메소드 처리 전 후로 보안 검사 수행하여 인가처리


### AOP 기반으로 동작
- 프록시와 어드바이스로 메소드 인가처리 수행


### 보안 설정 방식
- 어노테이션 권한 설정 방식
  - @PreAuthorize("hasRole('user')"), @PostAuthorize("hasRole('user')"), @Secured("ROLE_USER")
- 맵 기반 권한 설정 방식
  - 맵 기반 방식으로 외부와 연동하여 메소드 보안 설정 구현

------------------------------------------------------------------------------------------------------------------------

> ## 어노테이션 권한 설정

### Method 방식 - 어노테이션 권한 설정
- 보안이 필요한 메서드에 설정한다
- @PreAuthorize, @PostAuthorize
  - SpEL 지원
  - PrePostAnnotationSecurityMetadataSource가 담당
- @Secured, @RolesAllowed
  - SpEL 미지원
  - SecuredAnnotationSecurityMetadataSource, Jsr250MethodSecurityMetadataSource가 담당
- @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
























