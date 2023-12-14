
# 실전프로젝트 - 인가 프로세스 DB 연동 웹 계층 구현

------------------------------------------------------------------------------------------------------------------------

> ## 스프링 시큐리티 인가 개요

### 개요
- DB와 연동하여 자원 및 권한을 설정하고 제어함으로 동적 권한 관리가 가능하도록 한다
- 설정 클래스 소스에서 설정 관련 코드 모두 제거


### 관리자 시스템 구축
- 회원 관리 : 권한 부여
- 권한 관리 : 권한 생성, 삭제
- 자원 관리 : 자원 생성, 삭제, 수정, 권한 매핑


### 권한 계층 구현
- URL : URL 요청 시 인가 처리
- Method : 메소드 호출 시 인가 처리 (Method, Pointcut)

------------------------------------------------------------------------------------------------------------------------

> ## 웹 기반 인가 처리 DB 연동 - 주요 아키텍처 이해

### SecurityMetadataSource
- 구현체
  - FilterInvocationSecurityMetadataSource : URL 권한 정보 추출
  - MethodSecurityMetadataSource : Method 권한 정보 추출

------------------------------------------------------------------------------------------------------------------------

> ## 웹 기반 인가 처리 DB 연동 - FilterInvocationSecurityMetadataSource(1)

### FilterInvocationSecurityMetadataSource 인터페이스
- 사용자가 접근하고자 하는 URL 자원에 대한 권한 정보 추출
- AccessDecisionManager에게 전달하여 인가처리 수행
- DB로부터 자원 빛 권한 정보를 매핑하여 맵으로 관리
- 사용자의 매 요청마다 요청정보에 매핑된 권한 정보 확인


### SecurityMetadataSource 인터페이스
- FilterInvocationSecurityMetadataSource 부모 인터페이스


### 흐름
- 사용자의 요청이 들어오면 FilterSecurityInterceptor Filter 동작
- FilterInvocationSecurityMetadataSource를 통해 권한 정보 조회
  - RequestMap 
    - key : request URL
    - value : 권한 정보
  - 요청정보와 매칭되는 권한목록이 존재하는지 확인
    - 권한 목록이 존재하지 않으면, 인가 처리 하지 않음
    - 권한 목록이 존재하면, AccessDecisionManager decide() 메서드 수행


### 주의
- 인가 처리 클래스가 deprecated되고, AuthorizationManager로 대체되었다

------------------------------------------------------------------------------------------------------------------------

> ## 웹 기반 인가처리 DB 연동 - FilterInvocationSecurityMetadataSource(2)














