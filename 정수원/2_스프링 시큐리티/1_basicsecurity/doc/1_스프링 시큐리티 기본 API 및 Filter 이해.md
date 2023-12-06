
# 스프링 시큐리티 기본 API 및 Filter 이해

------------------------------------------------------------------------------------------------------------------------

> ## 프로젝트 구성 및 의존성 추가

### 스프링 시큐리티의 의존성 추가 시 일어나는 일들
- 서버가 기동되면 스프링 시큐리티의 초기화 작업 및 보안 설정이 이루어진다
- 별도의 설정이나 구현을 하지 않아도 기본적인 웹 보안 기능이 현재 시스템에 연동되어 작동함
    1. 모든 요청은 인증이 되어야 자원에 접근이 가능하다
    2. 인증 방식은 폼 로그인 방식과 httpBasic 로그인 방식을 제공한다
    3. 기본 로그인 페이지를 제공한다
    4. 기본 계정 한 개 제공한다 - username : user / password : 랜덤 문자열
- 앞으로 구현해야 하는 부분
    - 계정 추가, 권한 추가, DB 연동 등이 아직 없다
    - 기본적인 보안 기능 외에 시스템에서 필요로 하는 더 세부적이고 추가적인 보안 기능이 필요

------------------------------------------------------------------------------------------------------------------------

> ## 사용자 정의 보안 기능 구현

### WebSecurityConfigurerAdapter
- WebSecurityConfigurerAdapter
  - 스프링 시큐리티의 웹 보안 기능 초기화 및 설정
  - SecurityConfig를 상속받고 있다
- SecurityConfig 
  - 사용자 정의 보안 설정 클래스 
  - WebSecurityConfigurerAdapter를 상속하고 있다
- HttpSecurity
  - 세부적인 보안 기능을 설정할 수 있는 API 제공
  - 인증 API
  - 인가 API


### 스프링 시큐리티 기본 사용자, 비밀번호 설정
    // application.yml
    spring:
      security:
        user:
          name: user
          password: 1111


### 인증 설정
    // bean 등록
    @Configuration
    @EanableWebSecurity
    public class SecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated());

            http
                .formLogin();
        
            return http.build();
        }
    }
- anyRequest().authenticated() : 모든 요청에 대해 인증을 함
- formLogin() : form 형식으로 로그인을 구성하는 데 사용되는 메서드이다

------------------------------------------------------------------------------------------------------------------------

> ## Form Login 인증

### 인증 API - Form 인증
- 인증이 안되면 로그인 페이지로 리다이렉트
- client에서 Form 형태로 username + password를 POST 방식으로 전송
- 서버에서 클라이언트가 전송한 username + password를 통해 session 및 인증 토근 생성/저장
- 세션에 저장된 인증 토큰으로 접근/인증 유지


### 인증 API - Form Login 인증
    // Form 로그인 인증 기능이 작동함
    http.formLogin()
        .loginPage("/login.html")                 // 사용자 정의 로그인 페이지
        .defaultSuccessUrl("/home")               // 로그인 성공 후 이동 페이지
        .failureUrl("/login.html?error=true")     // 로그인 실패 후 이동 페이지
        .usernameParameter("username")            // 아이디 파라미터명 설정 (UI form에서 설정한 값)
        .passwordParameter("password")            // 패스워드 파라미터명 설정 (UI form에서 설정한 값)
        .loginProcessingUrl("/login")             // 로그인 Form Action Url (UI form에서 설정한 값)
        .successHandler(loginSuccessHandler())    // 로그인 성공 후 핸들러
        .failureHandler(loginFailureHandler())    // 로그인 실패 후 핸들러
- 성공/실패 실행 순서
  - handler()들이 실행된 후, defaultSuccessUrl(), failureUrl() 메서드들이 동작한다
  - handler()에서 response.redirect()를 통해 redirect하면, defaultSuccessUrl(), failureUrl()은 동작하지 않는다

------------------------------------------------------------------------------------------------------------------------

> ## Form Login 인증 필터 : UsernamePasswordAuthenticationFilter

### Login Form 인증
- UsernamePasswordAuthenticationFilter
  - AntPathRequestMatcher(/login) : URL 정보 확인
    - No : chain.doFilter
    - YES : Authentication에 username + password 저장
      - AuthenticationManage
        - AuthenticationProvider를 통해 인증을 확인한다
        - 인증 실패 시 AuthenticationException에 의해 UsernamePasswordAuthenticationFilter로 돌아감
        - 인증 성공 시 Authentication에 username + Authorities 정보를 SecurityContext에 저장
          - SecurityContext
            - Session에 저장
            - SuccessHandler를 호출

        
### request 관련
    request.getRemoteAddr()
- 요청 IP 반환


### Authentication 얻는 방법
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
- SecurityContext 클래스를 통해 Authentication 클래스를 얻고, 로그인한 데이터를 전역적으로 확인할 수 있다


### FilterChainProxy
- 필터들을 관리하는 클래스
- 여러 필터들이 순서대로 동작하도록 만들어주는 클래스이다
- doFilter() 메서드에서 


### 실행 순서
1. AbstractAuthenticationProcessingFilter에 doFilter()를 통해 loginProcess Url 확인
2. 위에 클래스를 상속받은 UsernamePasswordProviderFilter에 attemptAuthentication()에서 Authentication을 확인 
   - AuthenticationManager, AuthenticationProvider를 통해 Authentication을 확인한다
   - 틀리면, AuthenticationException을 통해 오류를 반환함
   - 맞으면, 다음 단계로 이동
3. attemptAuthentication()을 통해 반환한 Authentication을 SecurityContext에 저장함
4. AbstractAuthenticationProcessingFilter에 successfulAuthentication()를 통해 successHandler() 호출

------------------------------------------------------------------------------------------------------------------------

> ## Logout 처리, LogoutFilter

### Logout
- 세션 무효화, 인증토큰 삭제, SecurityContext 데이터 삭제, 쿠키정보 삭제, 로그인 페이지로 리다이렉트


### 인증 API - Logout
    http.logout()
          .logoutUrl("/lotout")                             // 로그아웃 처리 URL
          .logoutSuccessUrl("/login")                       // 로그아웃 성공 후 이동페이지
          .deleteCookies("JSESSIONID", "remember-me")       // 로그아웃 성공 후 쿠키 삭제
          .addLogoutHandler(logoutHandler())                // 로그아웃 핸들러
          .logoutSuccessHandler(logoutSuccessHandler())     // 로그아웃 성공 후 핸들러
- 기본적으로 post 방식으로 로그아웃 처리 된다 (get 방식으로 설정할 수 있다) 


### LogoutFilter
- LogoutFilter
  - logout process url 체크
    - 일치하지 않으면, 다음 filter로 이동
    - 일치하면 SecurityContext에서 Authentication을 조회
      - SecurityContextLogoutHandler를 통해 세션 무효화, 쿠키 삭제, SecurityContextHolder.clearContext() 함
      - SimpleUrlLogoutSuccessHandler()를 통해 login 페이지로 이동


### 실행 순서
1. LogoutFilter에 doFilter() 메서드를 통해 logoutProcess url 확인
2. doFilter() 메서드에서 SecurityContext에서 Authentication 데이터를 반환
3. CompositeLogoutHandler() logout() 메서드를 통해 모든 LogoutHandler의 logout() 호출
4. SecurityContextLogoutHandler logout() 메서드를 통해 세션 정리, SecurityContext 초기화
5. logoutSuccessHandler() 수행

------------------------------------------------------------------------------------------------------------------------

> ## Remember Me 인증

### Remember Me 인증
- 세션이 만료되고 웹 브라우저가 종료된 후에도 어플리케이션이 사용자를 기억하는 기능
- Remember-me 쿠키에 대한 Http 요청을 확인한 후 토큰 기반 인증을 사용해 유효성을 검사하고 토큰이 검증되면 사용자는 로그인 된다


### 사용자 라이프 사이클
- 인증 성공 (Remember-Me 쿠키 설정)
- 인증 실패 (쿠키가 존재하면 쿠키 무효화)
- 로그아웃 (쿠키가 존재하면 쿠키 무효화)


### API
    http.rememberMe(
        remember -> remember
                          .rememberMeParameter("remember")          // 파라미터 이름 설정 (기본값 remember-me)
                          .tokenValiditySeconds(3600)               // 쿠키 만료시간 설정 (기본값 14일
                          .alwaysRemember(true)                     // remember-me 체크를 하지 않아도 항상 적용
                          .userDetailsService(userDetailsService)   // userDetailsService 클래스를 넣어줘야 함
    );

------------------------------------------------------------------------------------------------------------------------

> ## Remember Me 인증 필터 : RememberMeAuthenticationFilter

### 인증 API
- RememberMeAuthenticationFilter
  - 인증을 받은 사용자가 session이 만료되거나, 활성화되지 않는 경우 실행된다
- RememberServices 인터페이스
  - 구현체
    - TokenBasedRememberMeService
      - 메모리에 토큰을 저장
    - PersistentTokenBasedRememberMeService
      - DB에 토큰을 저장
    - Token Cookie 추출
- Token 쿠키 존재 유무
  - 존재 하지 않으면 chain.doFilter
  - 존재하면 Decode Token, 일치하는 지 확인, User 계정 확인, 새로운 Authentication 생성, AuthenticationManager


### 실행 순서 - Authentication이 존재하는 경우
1. RememberMeAuthenticationFilter doFilter(...) 호출 Authentication이 존재하면 아래 수행
2. AbstractAuthenticationProcessingFilter successfulAuthentication(...) 실행
3. RememberMeServices 구현체 AbstractRememberMeServices loginSuccess(...) 호출
4. AbstractRememberMeServices 구현체 TokenBasedRememberMeService, PersistentTokenBasedRememberMeService onLoginSuccess(...) 호출


### 실행 순서 - Authentication이 존재하지 않는 경우
1. RememberMeAuthenticationFilter doFilter(...) 호출 Authentication이 없으면 아래 수행
2. RememberMeServices 구현체 AbstractRememberMeServices autoLogin(..) 호출
3. autoLogin(...)에서 remember-me 쿠키를 확인한 후 존재하면 createSuccessfulAuthentication(...) 메서드 호출
4. RememberMeAuthenticationFilter doFilter(...)로 돌아와서 새로 만들어진 Authentication을 AuthenticationManager를 통해 SecurityContext에 담아줌


### 인증 토큰 access token
- 클라이언트가 서버에 대한 신원을 확인하고 리소스에 접근할 권한을 부여하는 데 사용된다
- 토큰 안에 있는 정보
  - 사용자 식별자 User Identifier : 사용자를 식별하는 정보
  - 권한 및 범위 Scopes : 사용자에 대한 권한 정보
  - 만료 시간 Expiration Time : 토큰의 유효시간
  - 서명 Signature : 토큰이 변조되지 않았음을 보장하기 위한 디지털 서명

------------------------------------------------------------------------------------------------------------------------

> ## 익명사용자 인증 필터 : AnonymousAuthenticationFilter

### AnonymousAuthenticationFilter
- 익명사용자 인증 처리 필터
- 익명사용자와 인증 사용자를 구분해서 처리하기 위한 용도로 사용
- 화면에서 인증 여부를 구현할 때 isAnonymous()와 isAuthenticated()로 구분해서 사용
  - 인증 받은 Authentication이 존재하는 경우, 다음 필터로 이동
- 인증객체를 세션에 저장하지 않는다

------------------------------------------------------------------------------------------------------------------------

> ## 동시 세션 제어, 세션 고정 보호, 세션 정책

### 동시 세션 제어
1. 이전 사용자 세션 만료
2. 현재 사용자 인증 실패


### 동시 세션 제어 API
    http.sessionManagement(
        session -> session
                        .maximumSessions(1)                     // 최대 허용 가능 수 (-1: 무한)
                        .maxSessionsPreventsLogin(true)         // 동시 로그인 차단함 (false : 기존 세션 만료 default)
                        .invalidSessionUrl("/invalid")          // 세션이 유효하지 않을 때 이동 할 페이지 - 사라짐
                        .expiredUrl("/expired")                 // 세션이 만료된 경우 이동 할 페이지
    )
- invalidSessionUrl(...)은 삭제됨 


### 세션 고정 보호
- 인증에 성공할 때마다 세션이 새로 만들어지는 방법


### 세션 고정 보호 API
    http.sessionManagement(
          session -> session.sessionFixation().changeSessionId()    // 기본값
    )                
- none(), migrateSession(), newSession() 등이 있다
- none()는 세션 고정 보호를 받을 수 없다
- changeSessionId()은 세션 데이터를 그대로 두고, sessionId만 변경한다
- migrateSession()은 세션 데이터를 그대로 새로운 세션으로 이전할 때 사용한다  
- newSession()은 이전 세션 데이터가 초기화되고 새로운 세션에 데이터가 저장된다


### 세션 정책 API
    http.sessionManagement(
          session -> session
                          .sessionCreationPolicy(SessionCreationPolicy.If_Required)
    )
- Always : 스프링 시큐리티가 항상 세션 생성
- If_Required : 스프링 시큐리티가 필요시 생성 (기본값)
- Never : 스프링 시큐리티가 생성하지 않지만 이미 존재하면 사용
- Stateless : 스프링 시큐리티가 생성하지 않고 존재해도 사용하지 않음 (JWT를 사용할 때 적용)

------------------------------------------------------------------------------------------------------------------------

> ## 세션 제어 필터 : SessionManagementFilter, ConcurrentSessionFilter

### SessionManagementFilter
- 세션 관리
  - 인증 시 사용자 세션정보를 등록, 조회, 삭제 등의 세션 이력을 관리
- 동시적 세션 제어
  - 동일 계정으로 접속이 허용되는 최대 세션 수를 제한
- 세션 고정 보호
  - 인증 할 때마다 세션 쿠키를 새로 발급하여 공격자의 쿠키 조작을 방지 
- 세션 생성 정책
  - Always, If_Required, Never, Stateless


### ConcurrentSessionFilter
- 매 요청마다 현재 사용자의 세션 만료 여부 체크
- 세션이 만료되었을 경우 즉시 만료 처리
- session.isExpired() == true
  - 로그아웃 처리
  - 즉시 오류 페이지 응답


### 동시 제어
- 이전 사용자가 로그인 한 상태에서, 새로운 사용자가 같은 ID로 로그인하는 경우, 최대 세션 허용 개수를 초과하면 이전 사용자 세션 만료
- 이전 사용자의 요청이 있는 경우 ConcurrentSessionFilter에서 이전 사용자의 세션이 만료된 것을 확인하여 로그아웃 처리


### 실행 순서
- UserPasswordAuthenticationFilter에서 로그인을 확인하여 통과한 후, ConcurrentSessionControlAuthenticationStrategy에서 같은 user의 세션 count를 체크
  - 만약 최대 허용 개수를 넘으면 세션을 만료 시키거나, 
- ChangeSessionIdAuthenticationStrategy를 통해 최대 허용 개수를 초과한 경우 session ID만 변경
- RegisterSessionAuthenticationStrategy를 통해 세션을 등록


### 영어 단어
- exceed : 초과하다
- exceeded : 초과된

------------------------------------------------------------------------------------------------------------------------

> ## 권한설정과 표현식

### 인가 API - 선언적 방식
    // URL
    http.antMachers("/users/**").hasRole("USER")
    
    // Method
    @PreAuthorize("hasRole('USER')")
    public void user() {
      ...
    }


### 인가 API - 동적 방식 (DB 연동 프로그래밍)
- 실전 프로젝트 인가 편에서 자세히 배움


### 인가 API - 예시 코드
    http.authorizeHttpRequests(
          requestMatcherRegistry -> requestMatcherRegistry
                                                .requestMatchers("/url1").permitAll()
                                                .requestMatchers("/url2").hasRole("USER")
    )
- 설정 시 구체적인 경로가 먼저 오고 그것보다 큰 범위의 경로가 뒤에 오도록 해야 한다 (코드를 작성한 순서대로 확인)


### 표현식
- authenticated() : 인증된 사용자의 접근을 허용
- fullyAuthenticated() : 인증된 사용자의 접근을 허용, rememberMe 인증 제외
- permitAll() : 무조건 접근을 허용
- denyAll() : 무조건 접근을 허용하지 않음
- anonymous() : 익명 사용자의 접근을 허용
- rememberMe() : 기억하기를 통해 인증된 사용자의 접근을 허용
- access(String) : 주어진 SpEL 표현식의 평가 결과가 true이면 접근을 허용
- hasRole(String) : 사용자가 주어진 역할이 있다면 접근을 허용
- hasAuthority(String) : 사용자가 주어진 권한이 있다면 접근을 허용
- hasAnyRole(String...) : 사용자가 주어진 권한 중 어떤 것이라도 있다면 접근을 허용
- hasAnyAuthority(String...) : 사용자가 주어진 권한 중 어떤 것이라도 있다면 접근을 허용
- hasIpAddress(String) : 주어진 IP로부터 요청이 왔다면 접근을 허용

------------------------------------------------------------------------------------------------------------------------

> ## 예외 처리 및 요청 캐시 필터 : ExceptionTranslationFilter, RequestCacheAwareFilter

### ExceptionTranslationFilter
- AuthenticationException
  - 인증 예외 처리
    - AuthenticationEntryPoint 호출 : 로그인 페이지 이동, 401 오류 코드 전달 등
  - 인증 예외가 발생하기 전의 요청 정보를 저장
    - RequestCache : 사용자의 이전 요청 정보를 세션에 저장하고 이를 꺼내 오는 캐시 메커니즘
    - SavedRequest : 사용자가 요청했던 request 파라미터 값들, 그 당시의 헤더값들 등이 저장
- AccessDeniedException 
  - 인가 예외 처리
    - AccessDeniedHandler에서 예외 처리하도록 제공


### API 설정
    http.exceptionHandling(
        exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                              // 인증 예외가 발생했을 때 실행할 코드 작성
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                              // 인가 예외가 발생했을 때 실행할 코드 작성 
                        })
    )
- authenticationEntryPoint(...) : 인증 예외가 발생했을 때 실행할 코드 설정
- accessDeniedHandler(...) : 인가 예외가 발생했을 때 실행할 코드 설정


### 세션에서 예외 전 데이터를 얻는 방법
    HttpRequestCache requestCache = new HttpRequestCache();
    SavedRequest savedRequest = requestCache.getRequest(request, response);
- savedRequest 메서드
  - getRedirectUrl() : 이전 요청에 페이지 주소
  - getCookies() : 이전 요청에 쿠키 정보
  - getMethod() : 이전 요청에 method 정보
  - getHeaderValues(String name) : 이전 요청에 헤더 정보
  - getHeaderNames() : 이전 요청에 헤더 이름 정보
  - getLocales() : 이전 요청에 다국어 정보
  - getParameterValues(String name) : 이전 요청에 파라미터 값들
  - getParameterMap() : 이전 요청에 파라미터 값들 Map 반환 

------------------------------------------------------------------------------------------------------------------------

> ## 사이트 간 요청 위조 - CSRF, CsrfFilter

### CSRF Cross Site Request Forgery (사이트 간 요청 위조)
- 인터넷 사용자가 자신의 의지와는 무관하게 공격자가 의도한 행위를 특정 웹사이트에 요청하게 만드는 공격이다


### Form 인증 - CsrfFilter
- 모든 요청에 랜덤하게 생성된 토큰을 HTTP 파라미터로 요구
- 요청 시 전달되는 토큰 값과 서버에 저장된 실제 값과 비교한 후 만약 일치하지 않으면 요청은 실패한다
- GET 메서드는 따로 인증하지 않아도 된다 (조회에서는 인증하지 않아도 됨)
- Spring Security는 http.csrf() 기본 활성화되어 있음


### 설정 방법
    http.csrf()           // csrf 설정
    http.csrf().diabled   // csrf 설정 X


### 영어 단어
- deferred : 연기된


### Form 생성
    <input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }">














