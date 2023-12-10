
# 실전프로젝트 - 인증 프로세스 Form 인증 구현

------------------------------------------------------------------------------------------------------------------------

## 정적 자원 ignore 하기

### 예시 코드
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
- httpSecurity requestMatcher()와 차이점
  - httpSecurity는 Filter들을 호출하게 되고, WebSecurity는 Filter를 거치지 않음


### 출처
- https://velog.io/@yevini118/Spring-Security-%EC%A0%95%EC%A0%81-%EC%9E%90%EC%9B%90-ignore%ED%95%98%EA%B8%B0

------------------------------------------------------------------------------------------------------------------------

> ## 사용자 DB 등록 및 PasswordEncoder

### PasswordEncoder
- 비밀번호를 안전하게 저장할 수 있도록 비밀번호의 암호화를 지원하는 인터페이스


### 비밀번호 암호화 방식 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
    }


### PasswordEncoder 구현체
- BcryptPasswordEncoder : BCrypt 해시 함수를 사용해 비밀번호를 암호화
- Argon2PasswordEncoder : Argon2 해시 함수를 사용해 비밀번호를 암호화
- Pbkdf2PasswordEncoder : PBKDF2 해시 함수를 사용해 비밀번호를 암호화
- ScryptPasswordEncoder : SCrypt 해시 함수를 사용해 비밀번호를 암호화
- PasswordEncoder 인터페이스를 직접 구현해서 사용할 수 있다

------------------------------------------------------------------------------------------------------------------------

> ## DB 연동 인증 처리(1) CustomUserDetailsService

### UserDetailsService
- 사용자 로그인 정보를 조회하는 loadUserByUsername(String username) 메서드를 가지고 있는 인터페이스
- UserDetailsService 구현체를 만들어 조회 시 여러 작업을 수행하도록 만들 수 있다
- 특별한 처리 하지 않으면, 기본 값으로 InmemoryUserDetailsManager를 사용한다


### 구현 코드
    @Service
    public class UserService implements UserDetailsService {
        private final UserRepository userRepository;
        
        public UserDetails loadUserByUsername(String username) {
            Account account = userRepository.findByUsername(username);

            if (account == null) {
                
            }
        }
    }
- loadUserByUsername(String name) 메서드를 통해 DB에 저장된 로그인 정보를 확인

------------------------------------------------------------------------------------------------------------------------

> ## DB 연동 인증 처리(2) CustomAuthenticationProvider

### AuthenticationProvider
- 로그인 아이디의 인증을 담당하는 클래스
- authenticate(...), supports(...) 메서드를 구현해야 한다


### CustomAuthenticationProvider 코드
    @Component
    public class CustomAuthenticationProvider implements AuthenticationProvider {
        @Autowired
        private UserRepository userRepository

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String name = authentication.getName();
            String password = authenticateion.getCredentials();

            AccountContext context = (AccountContext) userRepository.findByUsername(name);

            if (!passwordEncoder.match(password, cotext.getAccount().getCredentials())) {
                throw new BadCredentialsException("BadCredentialsException");
            }

            UsernamePasswordAuthenticationToken() token 
                            = new UsernamePasswordAuthenticationToken(context.getAccount(), null, context.getAuthorities);

            return token;
        }

        @Override
        boolean supports(Class<?> authentication) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }

------------------------------------------------------------------------------------------------------------------------

> ## 로그아웃 및 인증에 따른 화면 보안 처리

### 로그아웃 방법
1. <form> 태그를 사용해서 POST로 요청
2. <a> 태그를 사용해서 GET으로 요청 SecurityContextLogoutHandler 활용


### SecurityContextLogoutHandler
- logout(request, response, authentication)을 통해 로그아웃 처리를 할 수 있다


### 예시
1. logout Controller를 만듬
2. logout Controller에서 request, response를 받고 SecurityContext에서 authentication을 꺼내 logout 처리

------------------------------------------------------------------------------------------------------------------------

> ## 인증 부가 기능 - WebAuthenticationDetails, AuthenticationDetailsSource

### WebAuthenticationDetails
- 인증 과정 중 전달된 데이터를 저장
- Authentication의 details 변수에 저장됨


### AuthenticationDetailsSource
- WebAuthenticationDetails 객체를 생성


### 예시 코드
    public CustomWebAuthenticationDetails extends WebAuthenticationDetails {
        private String param;
        public CustomWebAuthenticationDetails(HttpServletRequest request) {
            super(request);
            this.param = request.getParameter("key");
        }

        ...
    }

    public CustomAuthenticationDetailsSource implements AuthencationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
        @Override
        public WebAuthenticationDetails buildDetails(HttpServletReqeust request) {
            return new CustomWebAuthenticationDetails(request);
        }
    }

------------------------------------------------------------------------------------------------------------------------

> ## 인증 성공 핸들러 : CustomAuthenticationSuccessHandler

### RequestCache
- 이전 요청에 대한 정보를 담고 있는 클래스
- 구현체 
  - HttpSessionRequestCache 클래스
    - getRequest(request, response)를 통해 이전 request 정보를 읽어올 수 있다


### AuthenticationSuccessHandler
- onAuthenticationSuccess(request, response, authentication) 메서드에 로그인 성공 후 실행할 코드를 작성하면 된다
- 이전 요청에 대한 정보가 필요함녀 RequestCache 스프링 주입 받기

------------------------------------------------------------------------------------------------------------------------

> ## 인증 실패 핸들러 : CustomAuthenticationFailureHandler

### AuthenticationFailureHandler
- onAuthenticationFailure(request, response, authentication) 메서드에 로그인 실패 후 실행할 코드를 작성하면 된다


### 주의 사항
- QueryString으로 파라미터를 넘기는 경우 requestMatcherUrl 설정에서 "/login*"와 같이 쿼리 스트링에 대한 권한을 허용해야 한다
- 예 : /login?username=abcd, http.antMatchers("/login*").permitAll()

------------------------------------------------------------------------------------------------------------------------

> ## 인증 거부 처리 - Access Denied

### ExceptionTranslationFilter
- 예외가 발생했을 때 예외 처리를 하는 필터


### AbstractSecurityInterceptor
- 권한에 대한 접근 처리를 하는 필터






















