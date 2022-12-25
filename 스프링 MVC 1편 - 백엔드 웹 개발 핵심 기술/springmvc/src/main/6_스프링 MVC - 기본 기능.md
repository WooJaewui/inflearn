
# 스프링 MVC - 기본 기능.

-------------------------------------------------------------------------------------------------------------------------------

> ## 프로젝트 생성.

### JAR vs WAR
- WAR.
  - 톰캣을 별도로 설치하고 자바를 돌릴 때.
  - JSP를 사용하려면 써야 된다.
- JAR.
  - 내장 톰캣을 최적화되어 사용할 때 JAR를 사용한다.
  - 최근에는 부트를 사용할 경우 JAR를 대부분 사용한다.

### index 페이지.
- 스프링 부트 jar 파일을 사용하면 'resources/static/index.html' 을 통해 index 페이지를 설정할 수 있다.

-------------------------------------------------------------------------------------------------------------------------------

> ## 로깅 간단히 알아보기.

### 개요.
- 앞으로 로그를 사용할 것이기 때문에, 이번시간에는 로그에 대해서 간단히 알아보자.
- 운영 시스템에서는 System.out.println()같은 시스템 콘솔을 사용해서 정보를 출력하지 않고, 별도의 로깅 라이브러리를 사용해서 로그를 출력한다.
- 최소한의 사용 방법만 알아본다.

### Logback.
- slf4j 인터페이스 + Logback를 사용한다.
- slf4j 인터페이스를 사용하려면 Logger를 org.slf4j 라이브러리에 있는 것을 사용해야 한다.

### @RestController.
- @Controller를 사용하면 반환타입이 String일 경우 viewResolver를 통해 view로 전달한다.
- @RestController를 사용하면 반환타입이 String이여도 viewResolver를 통하지 않고 String 자체를 반환한다.

### 로깅 남기는 방법.
    log.trace("메시지 {}", 대괄호에 들어갈 문장);
    log.debug("메시지 {}", 대괄호에 들어갈 문장);
    log.info("메시지 {}", 대괄호에 들어갈 문장);
    log.warn("메시지 {}", 대괄호에 들어갈 문장);
    log.error("메시지 {}", 대괄호에 들어갈 문장);
- info, warn, error만 콘솔창에 찍힌다.

### 로깅 레벨 설정.
    // 전체 로그 레벨 설정.
    logging.level.root=info

    // 내가 원하는 패키지 그 하위 로그 레벨 설정.
    logging.level.hello.springmvc=trace
    logging.level.hello.springmvc=debug
    logging.level.hello.springmvc=info
    logging.level.hello.springmvc=warn
    logging.level.hello.springmvc=error
- 디폴트 설정은 info이다.
- 운영서버에 로그를 다 남기면 너무 많은 로그를 남기게된다. (개발 서버 - debug / 운영 서버 - info 출력)
- trace > debug > info > warn > error

### 로그를 선언하는 방법.
    private Logger log = LoggerFactory.getLogger(getClass());
    private static final Logger log = LoggerFactory.getLogger(Xxx.class);
    @Slf4j 클래스에 붙여주면 위에 Logger를 자동으로 생성해준다.
- @Slf4j 롬복 사용가능.

### 올바른 로그 사용방법.
    // 잘못된 로깅 사용방법.
    log.trace("trace log = " + name);

    // 제대로 사용.
    log.trace("trace log ={}", name); 
- 첫번째 방법은 + 연산에 우선 순위 때문에 trace가 사용되지 않더라도 + 연산을 수행한다.
- 반면에, 두 번째 방법은 파라미터로 넘겨서 더하기 때문에 trace가 사용되지 않으면 연산을 수행하지 않는다.

### 로그 사용시 장점.
- 쓰레드 정보, 클래스 이름 같은 부가 정보를 함께 볼 수 있고, 출력 모양을 조정할 수 있다.
- 로그 레벨에 따라 개발 서버, 운영 서버 등을 쉽게 조절할 수 있다.
- 시스템 아웃 콘솔에만 출력하는 것이 아니라, 파일 네트워크 등 로그를 별도의 위치에 남길 수 있다. (파일로 남길 때 일별, 특정 용량 등)
- 성능도 일반 System.out보다 좋다. (내부 버퍼링, 멀티 쓰레드 등)

-------------------------------------------------------------------------------------------------------------------------------

> ## 요청 매핑.

### @RestController.
- @RestController를 붙이면 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력한다.
- 따라서 실행 결과로 ok 메시지를 받을 수 있다.
- @ResponseBody와 관련이 있는데, 뒤에서 더 자세히 설명한다.

### @RequestMapping 팁.
    @RequestMapping({"/url1", "/url2"})
- url을 2개 이상으로 설정할 수 있다.
- /url1과 /url1/은 원래 서로 다른 url이지만, 스프링은 같은 url로 인식해준다.
- method를 따로 설정하지 않으면 어떤 method던지 상관없이 url만 맞으면 호출된다. (405 = Method Not Allowed)

### PathVariable.
    @RequestMapping("/url/{var}")
    public String method(@PathVariable String var) { ... }
- 경로 변수라고 한다.
- 경로에 변수를 넣어서 사용할 수 있다.

### 다중 PathVariable.
    @RequestMapping("/url/{var1}/{var2}")
    public String method(@PathVariable String var1, @PathVaraible var2) { ... }
- 경로 변수를 여러개 사용할 수 있다.

### 파라미터로 추가 매핑.
    @RequestMapping(value="/url", params="mode=debug")
    public String method(...) { ... }
- 파라미터에 /url?mode=debug 와 같이 'mode=debug' 값이 들어와야만 메서드가 매핑된다.
- 파라미터가 없어야 된다, 있어야 된다 등 파라미터에 조건을 걸어서 사용할 수 있다.

### 특정 헤더 조건을 추가 매핑.
    @RequestMapping(value="/url", headers="mode=debug")
    public String method(...) { ... }
- 헤더에 내가 원하는 값이 존재해야 메서드가 매핑되도록 만들 수 있다.
- 헤더에 여러가지 조건을 걸어서 사용할 수 있다.

### 미디어 타입 조건 매핑.
    @RequestMapping(value="/url", consumes = "application/json")
    public String method(...) { ... }
- Content-Type 헤더를 기반으로 미디어 타입을 매핑한다.
- 맞지 않으면 HTTP 415 상태코드(Unsupported Media Type)을 반환한다.
- MediaType.APPLICATION_JSON_VALUE 과 같이 미리 정의된 상수를 사용해도 된다.

### 미디터 타입 조건 매핑.
    @RequsetMapping(value="/url", produces="text/html")
    public String method(...) {...}
- Accept 헤더를 기반으로 미디어 타입을 매핑한다.
- 맞지 않으면 HTTP 406 상태코드(Not Acceptable)을 반환한다.
- Accept는 클라이언트(브라우저)가 Response로 받을 수 있는 타입을 의미한다.
- MediaType.APPLICATION_JSON_VALUE 과 같이 미리 정의된 상수를 사용해도 된다.

-------------------------------------------------------------------------------------------------------------------------------

> ## 요청 매핑 - API 예시.

### 개요.
- 회원 관리를 HTTP API로 만든다 생각하고 매핑을 어떻게 하는지 알아보자.

### 정리.
- 매핑 방법을 이해했으니, 이제부터 HTTP 요청이 보내는 데이터들을 스프링 MVC로 어떻게 조회하는지 알아보자.

-------------------------------------------------------------------------------------------------------------------------------

> ## HTTP 요청 - 기본, 헤더 조회.

### 개요.
- 어노테이션 기반의 스플이 컨트롤러는 다양한 파라미터를 지원한다.
- 이번 시간에는 HTTP 헤더 정보를 조회하는 방법을 알아보자.

### 파라미터로 받을 수 있는 것들.
- HttpServletRequest.
- HttpServletResponse.
- HttpMethod.
- Locale.
- @RequestHeader MultiValueMap<String, String>
- @RequestHeader("헤더이름") String var
- @CookieValue(value="쿠키이름", required=false) String cookie

### MultiValueMap.
- Map과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
- HTTP Header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.

### 참고.
- spring 공식 메뉴얼에 파라미터로 넘어올 수 있는 것들이 자세히 정리되어 있다.
- spring 공식 메뉴얼에 반환 타입에 따라 어떻게 수행되는지 정리되어 있다.

-------------------------------------------------------------------------------------------------------------------------------

> ## HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form.

### 개요.
- 서블릿으로 학습했던 내용을 스프링이 얼마나 깔끔하고 효율적으로 바꾸어주는지 알아보자.

### 요청 전달 3가지 방법.
1. GET - 쿼리 파라미터.
2. POST - HTML Form.
   - content-type : application/x-www-form-urlencoded.
   - 메시지 바디에 쿼리 파라미터 형식으로 전달.
3. HTTP message body.
   - HTTP API에서 주로 사용, JSON, XML, TEXT
   - 데이터 형식은 주로 JSON 사용.

-------------------------------------------------------------------------------------------------------------------------------

> ## HTTP 요청 파라미터 - @RequestParam.

### 개요.
- 스프링이 제공하는 @RequestParam을 사용하면 요청 파라미터를 쉽게 처리할 수 있다.

### @ResponseBody.
- 리턴타입을 바로 Response Body에 출력해준다.
- @RestController와 같은 효과. (컨트롤 단위 설정)
- 메서드 위에 붙여서 사용한다. (메서드 단위 설정)

### @RequestParam.
    public String method(@RequestParam("파라미터이름") String var) { ... }
- @RequestParam = request.getParameter("파라미터이름")과 같다.
- String, int, Integer 등의 단순 타입이면 @RequestParam을 생략할 수 있다.

### 참고.
- 너무 어노테이션을 완전히 생략하는 것도 가독성이 떨어지는 것 같다는 주장도 있다.

### 필수 파라미터 여부.
    public String method(@RequestParam(required=false) String var) {...}
- 파라미터 값이 넘어오지 않아도 오류를 발생하지 않는다.
- 매개변수를 null을 받을 수 있는 참조형으로 사용해야 한다. (기본형 -> 래퍼 클래스)

### 팁.
- null과 빈문자열("")을 잘 구분해야 한다.

### defaultValue.
    public String method(@RequestParam(defaultValue="1") String var) {...}
- 파라미터에 값이 넘어오지 않았을 때 사용할 기본값을 defaultValue로 설정해줄 수 있다.
- 빈 문자열에 경우에도 defaultValue가 설정된다.

### 파라미터를 Map으로 조회하기.
    public String method(@RequestParam Map<String, Object> map) { ... }
- 모든 파라미터를 Map형태로 받을 수 있다.
- 파라미터의 값이 1개가 확실하다면 Map을 사용해도 되지만, 그렇지 않다면 MultiValueMap을 사용해야 한다.

-------------------------------------------------------------------------------------------------------------------------------

> ## HTTP 요청 파라미터 - @ModelAttribute.

### 개요.
- 실제 개발을 하면 요청 파라미터를 받아서 필요한 객체를 만들고 그 객체에 값을 넣어주어야 한다.

### @Data.
- @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 자동으로 적용해준다.

### @ModelAttribute 실행 순서.
- HelloData 객체를 생성한다.
- 요청 파라미터의 이름으로 HelloData 객체의 프로터티를 찾는다. (Getter, Setter)
- 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 바인딩 한다.
- 예) 파라미터 이름이 username이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.

### 바인딩 오류.
- BindingException : 파라미터와 파라미터를 받는 매개변수의 타입이 다를 때 발생한다.

### 스프링 어노테이션 생략 규칙.
- String, int, Integer 같은 단순 타입 => @RequestParam.
- 나머지는 => @ModelAttribute
- argument resolver로 지정해 둔 타입은 @ModelAttribute/@RequestParam에서 제외된다.

-------------------------------------------------------------------------------------------------------------------------------

> ## HTTP 요청 메시지 - 단순 텍스트.

### 개요.
- HTTP message bodyt에 데이터를 직접 담아서 요청.
- HTTP 메시지 바디를 통해 데이터가 직접 데이터가 넘어오는 경우는 @RequestParam, @ModelAttribute를 사용할 수 없다.
- 물론 HTML Form 형식으로 전달되는 경우는 요청 파라미터로 인정된다.

### HttpEntity<T>
- HTTP header, body 정보를 편리하게 조회할 수 있는 클래스.
  - 메시지 바디 정보를 직접 조회.
  - 요청 파라미터를 조회하는 기능과 관계 없음. (@RequestParam, @ModelAttribute)
- HttpEntity는 응답에도 사용 가능.
  - 메시지 바디 정보 직접 반환.
  - 헤더 정보 포함 가능.
  - view 조회 X.

### HttpEntity<T> 상속 클래스.
- RequestEntity.
  - HttpMethod, url 정보가 추가, 요청에서 사용.
- ResponseEntity.
  - HTTP 상태 코드 설정 가능, 응답에서 사용.

### 참고.
- 스프링 MVC 내부에서 HTTP 메시지 바디를 읽어서 문자나 객체를 변환해서 전달해준다.
- HTTP 메시지 컨버터라는 기능을 사용한다.
- 이것은 조금 뒤에 HTTP 메시지 컨버터에서 자세히 설명한다.

### @RequestBody.
- HTTP Request 바디 정보를 편리하게 조회할 수 있다.

### @RequestHeader.
- HTTP Request 헤더 정보를 편리하게 조회할 수 있다.

### @ResponseBody.
- HTTP Response 바디 정보를 편리하게 작성할 수 있다.
- view를 사용하지 않는다.

### 요청 파라미터 vs HTTP 메시지 바디.
- @RequestParam, @ModelAttribute : 요청 파라미터를 조회하는 기능.
- @RequestBody : HTTP Request 메시지 바디를 직접 조회하는 기능.

-------------------------------------------------------------------------------------------------------------------------------

> ## HTTP 요청 메시지 - JSON.

### @RequestBody 객체 파라미터.
- HttpEntity<T>, @RequestBody를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다.
- HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON도 객체로 변환해주는데, 우리가 방금 V2에서 했던 작업을 대신 처리해준다.
- 참조 타입인 경우 @ReuqestBody를 생략할 수 없다. (생략하는 경우 @ModelAttribute가 붙는다)

### 주의사항.
- HTTP 요청시에 content-type이 application/json인지 꼭 확인해야 한다.
- 그래야 JSON을 처리할 수 있는 HTTP 메시지 컨버터가 실행된다.

### @RequestBody 요청.
- JSON 요청 -> HTTP 메시지 컨버터 -> 객체.
- content-type : application/json.

### @ResponseBody 응답.
- 객체 -> HTTP 메시지 컨버터 -> JSON 응답.
- Accept: application/json.

-------------------------------------------------------------------------------------------------------------------------------

> ## 응답 - 정적 리소스, 뷰 템플릿.

### 응답 데이터를 만드는 방법 3가지.
1. 정적 리소스.
   - 예) 웹 브라우저에 정적인 HTML, css, js을 제공할 때.
2. 뷰 템플릿 사용.
   - 예) 웹 브라우저에 동적인 HTML을 제공할 때는 뷰 템플릿을 사용한다.
3. HTML 메시지 사용.
   - HTTP API를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로, HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다.
   
### 정적 리소스.
- 스프링 부트는 클래스패스의 다음 디렉토리에 있는 정적 리소스를 제공한다.
- '/static', '/public', '/resources', '/META-INF/resources'
- 정적 리소스는 해당 파일을 변경 없이 그대로 서비스하는 것이다.

### 뷰 템플릿.
- 뷰 템플릿을 거쳐서 HTML이 생성되고, 뷰가 응답을 만들어서 전달한다.
- 일반적으로 HTML을 동적으로 생성하는 용도로 사용하지만, 다른 것들도 가능하다.
- 뷰 템플릿이 만들 수 있는 것이라면 뭐든지 가능하다.

### 뷰 템플릿 경로.
- 타임리프 '/src/resources/templates'
- html에 <html xmlns:th="http://www.thymeleaf.org">를 설정해야 한다.

### 뷰 템플릿 사용 방법.
1. String을 반환하는 경우.
   - @ResponseBody가 없으면 response/hello로 뷰 리졸버가 실행되어서 뷰를 찾고, 렌더링 한다.
   - @ResponseBody가 있으면 뷰 리졸버를 실행하지 않고, HTTP 메시지 바디에 직접 response/hello 라는 문자가 입력된다.
2. void를 반환하는 경우.
   - @Controller를 사용하고, HTTP 메시지 바디를 처리하는 파라미터가 없으면 요청 URL을 참고해서 논리 뷰 이름으로 사용.
   - 참고로 이 방식은 명시성이 너무 떨어지고 이렇게 딱 맞는 경우도 많이 없어서, 권장하지 않는다.

### Thymeleaf 스프링 부트 설정.
- 스프링 부트가 자동으로 ThymeleafViewResolver와 필요한 스프링 빈들을 등록한다.
- 그리고 prefix/suffix 설정도 같이 해준다. (prefix="resources/templates/", suffx=".html")

### 참고.
- 스프링 부트의 타임리프 관련 추가 설정은 공식 사이트를 참고하자. (페이지 안에서 thymeleaf 검색)

-------------------------------------------------------------------------------------------------------------------------------

> ## HTTP 응답 - HTTP API, 메시지 바디에 직접 입력.

### 개요.
- HTTP API를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로, HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다.

### 참고.
- HTML이나 뷰 템플릿을 사용해도 HTTP 응답 메시지 바디에 HTML 데이터가 담겨서 전달된다.
- 여기서 설명하는 내용은 정적 리소스나 뷰 템플릿을 거치지 않고, 직접 HTTP 응답 메시지를 전달하는 경우를 말한다.

### @ResponseStatus(상태값)
- Controller에 메서드 위에 사용해서 상태값을 지정해 줄 수 있다.
- HttpStatus.OK, HttpStatus.BAD_REQUEST 등.

### @RestController.
- @Controller + @ResponseBody이다.
- 컨트롤러를 의미하면서, 컨트롤러에 있는 모든 메서드들을 HTTP Response 메시지 바디에 결과값을 반환하게 해준다
- 이름 그대로 Rest API(HTTP API)를 만들 때 사용하는 컨트롤러이다.

-------------------------------------------------------------------------------------------------------------------------------

> ## HTTP 메시지 컨버터.










