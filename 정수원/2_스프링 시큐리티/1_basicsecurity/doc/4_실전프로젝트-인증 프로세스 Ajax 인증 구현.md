
# 실전프로젝트 - 인증 프로세스 Ajax 인증 구현

------------------------------------------------------------------------------------------------------------------------

> ## 흐름 및 개요

### Ajax 관련 클래스
- AjaxAuthenticationFilter
- AjaxAuthenticationToken
- AuthenticationManager
- AjaxAuthenticationProvider
- AjaxAuthenticationSuccessHandler
- AjaxAuthenticationFailureHandler
- AjaxUrlAuthenticationEntryPoint
- AjaxAccessDeniedHandler

------------------------------------------------------------------------------------------------------------------------

> ## 인증 필터 - AjaxAuthenticationFilter

### AbstractAuthenticationProcessingFilter 커스텀
- 필터 작동 조건 : requestMatcher("/url")으로 요청정보와 매칭하고 요청 방식이 Ajax이면 필터 작동
- AjaxAuthenticationToken을 생성하여 AuthenticationManager에게 전달하여 인증처리
- Filter 추가


### 주의
- post 요청을 하기 위해서는 csrf.disabled() 해야 한다
- Ajax...Provider 까지 만들어서 인증 방법을 변경해줘야 문제 없이 동작한다

------------------------------------------------------------------------------------------------------------------------

> ## 인증 처리자 - AjaxAuthenticationProvider

### AjaxAuthenticationProvider
- authenticate(...) 메서드에서 인증과 관련된 코드를 작성
- form AuthenticationProvider와 크게 다른게 없다 (Token만 다름)

------------------------------------------------------------------------------------------------------------------------

> ## 인증 핸들러 - AjaxAuthenticationSuccessHandler, AjaxAuthenticationFailureHandler

### AuthenticationSuccessHandler, AuthenticationFailureHandler
- Form 형식과 설정 방식이 똑같다
- Ajax, Form 형식에 따라 다르게 처리되는 부분에 대해서만 코드를 작성

------------------------------------------------------------------------------------------------------------------------

> ## 인증 및 인가 예외 처리 - AjaxLoginUrlAuthenticationEntryPoint, AjaxAccessDeniedHandler

### 클래스
- Form 형식과 설정 방식이 같다

------------------------------------------------------------------------------------------------------------------------

> ## Ajax Custom DSLs 구현하기

### DSL Domain Specific Language
- 특정한 도메인에서 사용하는 언어


### Custom DSLs
- AbstractHttpConfigurer
  - 스프링 시큐리티 초기화 설정 클래스
  - 필터, 핸들러, 메서드, 속성 등을 한 곳에 정의하여 처리할 수 있는 편리함 제공
  - init(...) throws Exception : 초기화
  - configure(H http) : 설정
- HttpSecurity의 apply(c configurer) 메서드 사용

------------------------------------------------------------------------------------------------------------------------

> ## Ajax 로그인 구현 & CSRF 설정
















