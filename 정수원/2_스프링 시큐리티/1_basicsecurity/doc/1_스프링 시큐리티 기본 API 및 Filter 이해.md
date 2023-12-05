
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













