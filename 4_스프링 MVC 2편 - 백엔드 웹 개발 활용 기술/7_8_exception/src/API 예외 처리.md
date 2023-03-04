
# API 예외 처리.

------------------------------------------------------------------------------------------------------------------------------------

> ## 시작.

### 목표.
- HTML 페이지의 경우 지금까지 설명했던 것처럼 4xx, 5xx와 같은 오류 페이지만 있으면 대부분의 문제를 해결할 수 있다.
- 그런데 API의 경우에는 생각할 내용이 더 많다.
- API는 각 오류 상황에 맞는 오류 응답 스펙을 정하고, JSON으로 데이터를 내려주어야 한다.


### 예외 발생 호출.
- API를 요청했는데, 오류가 발생하는 경우 JSON을 반환하지 않는다.
- 클라이언트는 정상 요청이든, 오류 요청이든 JSON이 반환되기를 기대한다.


### 팁.
- "HashMap"은 순서를 보장하지 않는다.


### 우선순위.
- Accept 헤더 설정을 "/*"을 하게 되면 Controller에서 produces를 설정해도 설정하지 않은 Controller가 우선순위에 의해 먼저 호출된다.

------------------------------------------------------------------------------------------------------------------------------------

> ## 스프링 부트 기본 오류 처리.

### 개요.
- 스프링 부트가 제공하는 "BasicErrorController"를 활용해서 API 처리를 해보자.


### BasicErrorController
- errorHTml() : "produces = MediaType.TEXT_HTML_VALUE" accept가 text/html인 경우 호출 후 view를 제공한다.
- error() : 그외 경우에 호출되고 ResponseEntity로 HTTP Body에 JSON 데이터를 반환한다.


### 스프링 부트.
- "BasicErrorController"가 제공하는 기본 정보들을 활용해서 오류 API를 생성해준다.


### Html 페이지 vs API 오류.
- "BasicErrorController"를 확장하면 JSON 메시지도 변경할 수 있다.
- 하지만, "@ExceptionHandler"를 활용하는 것이 가장 좋은 방법이다.

------------------------------------------------------------------------------------------------------------------------------------

> ## HandlerExceptionResolver 시작.

### 목표.
- 예외가 발생해서 서블릿을 넘어 "WAS"까지 예외가 전달되면 HTTP 상태코드가 500으로 처리된다.
- 예외에 따라서 400, 404 등등 다른 상태코드로 처리하고 싶다.
- 오류 메시지, 형식 등을 API마다 다르게 처리하고 싶다.


### ExceptionResolver.
- 컨트롤러(예외 발생) -> 인터셉터 -> 서블릿 -> ExceptionResolver(예외 처리) -> view -> afterCompletion -> WAS(sendError()확인)
- "ExceptionResolver"로 예외를 처리해도 "postHandler"는 실행되지 않는다.  


### 반환 값에 따른 동작 방식.
- 빈 ModelAndView : 뷰를 렌더링 하지 않고, 정상 흐름으로 서블릿이 리턴된다.
- ModelAndView 지정 :"ModelAndView"에 "View", "Model" 등의 정보를 지정해서 반환하면 뷰를 렌더링 한다.
- null : "null"을 반환하면, 다음 "ExceptionResolver"를 찾아서 실행한다. 만약 처리할 수 있는 "ExceptionResolve"가 없으면 예외를 던진다.


### ExceptionResolver 활용.
- 예외 상태 코드 변환.
  - 예외를 response.sendError() 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임.
  - 이후 WAS는 서블릿 오류 페이지를 찾아서 내부 호출, 예를 들어서 스프링 부트가 기본으로 설정한 "/error"가 호출됨.
- 뷰 템플릿 처리.
  - "ModelAndView"에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링 해서 고객에게 제공.
- API 응답 처리.
  - "response.getWriter().println("hello")"처럼 HTTP 응답 바디에 직접 데이터를 넣어주는 것도 가능하다.
  - 여기서 JSON으로 데이터를 처리할 수 있다. (ex : println(JSONData))


### ExceptionHandler 등록 방법.
1. extendHandlerExceptionResolver(...) : 이것만 사용하자. 
2. configureHandlerExceptionResolver(...) : 스프링이 기본으로 등록하는 "ExceptionResolver"가 제거된다. (사용 금지) 


### 참고.
- 예외 처리를 할 때, 여러 가지 상황을 생각해서 처리해야 한다.

------------------------------------------------------------------------------------------------------------------------------------

> ## HandlerExceptionResolver 활용.

### 예외를 마무리하기.
- "ExceptionResolver"를 활용하면 예외가 발생했을 때 복잡한 과정 없이 여기에서 문제를 깔끔하게 해결할 수 있다.


### ExceptionResolver 변경.
- response.sendError(...)를 사용하면 "WAS"에서 예외를 확인하고 다시 서블릿 -> 컨트롤 -> ... 이 수행된다.
- response.sendError(...)를 사용하지 않고, "ExceptionResolver" 예외 처리를 끝내면 "WAS"에서 응답을 수행한다.


### 정리.
- 서블릿 컨테이너까지 예외가 올라가면 복잡하고 지저분하게 추가 프로세스가 실행된다.
- "ExceptionResolver"를 사용하면 예외처리가 상당히 깔끔해진다. (프로세스가 줄어든다)

------------------------------------------------------------------------------------------------------------------------------------

> ## 스프링이 제공하는 ExceptionResolver1.

### 스프링 부트가 기본으로 제공하는 ExceptionResolver.
1. ExceptionHandlerExceptionResolver
2. ResponseStatusExceptionResolver
3. DefaultHandlerExceptionResolver


### ExceptionHandlerResolver.
- 조금 뒤에 자세히 설명한다.


### ResponseStatusExceptionResolver.
- HTTP 상태 코드를 지정해준다.
- @ResponseStatus(value = HttpStatus.NOT_FOUND)


### DefaultsHandlerExceptionResolver.
- 스프링 내부 기본 예외를 처리한다.


### ResponseStatusExceptionResolver 처리.
1. "@ResponseStatus"가 달려있는 예외
2. "ResponseStatusException" 예외


### ResponseStatusExceptionResolver 코드.
- 코드를 확인해보면 결국 response.sendError(statusCode, resolverReason)을 호출한다.
- 결국 was에서 다시 오류 페이지로 내부 요청한다.
- "reason"에 언어 properties 파일에 작성한 코드를 사용할 수 있다.


### @ResponseStatus 단점.
- 개발자가 직접 변경할 수 없는 예외에는 적용할 수 없다.
- 추가로 어노테이션을 사용하기 때문에 조건ㄷ에 따라 동적으로 변경하는 것도 어렵다.


### ResponseStatusException 사용
    throw new ResponseStatusException(HttpStatus.NOT_FOUNT, "error.bad", new IlleagalArgumentException());
- "ResponseStatusExceptionResolver"에서 처리된다.

------------------------------------------------------------------------------------------------------------------------------------

> ## 스프링이 제공하는 ExceptionResolver2.

### DefaultHandlerExceptionResolver.
- 스프링 내부에서 발생하는 스프링 예외를 해결한다.
- 대표적으로 파라미터 바인딩 시점에 타입이 맞지 않으면 내부에서 "TypeMismatchException"이 발생한다.
- "DefaultHandlerExceptionResolver"는 이것을 500오류가 아니라 400 오류로 변경한다.


### 지금까지 정리.
- "HandlerException"을 직접 활용하기에 너무 복잡하다.
- API 오류 응답의 경우 "response"에 직접 데이터를 넣어야 해서 매우 불편하고 번거롭다.
- 스프링은 이 문제를 해결하기 위해 "@ExceptionHandler"라는 매우 혁신적인 예외 처리 기능을 제공한다.

------------------------------------------------------------------------------------------------------------------------------------

> ## @ExceptionHandler.

### API 예외처리의 어려운 점.
- "ModelAndView"는 API 응답에는 필요하지 않다.
  - API 응답을 위해서 "HttpServletResponse"에 직접 응답 데이터를 넣어주었다. (매우 불편)
- 하나의 "Exception"이 도메인 마다 처리하는 방식이 다른 경우 이 로직을 구현하기 어렵다.


### @ExceptionHandler.
- API 예외 처리 문제를 해결하기 위해 "@ExceptionHandler"를 사용한다.


### @ExceptionHandler 예외 처리 방법.
- "@ExceptionHandler" 어노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정해주면 된다.
- 해당 컨트롤러에서 예외가 발생하면 이 메서드가 호출된다. 참고로 지정한 예외 또는 그 예외의 자식 클래스는 모두 잡을 수 있다.


### 우선순위.
- 자식 클래스, 부모 클래스가 모두 해당되면 "자식 클래스"가 우선 순위를 갖는다.


### 다양한 예외.
    @ExceptionHandler({AException.class, BException.class})
    public String ex(Exception e) {
      ...
    }
- 다양한 예외를 한번에 처리할 수 있다.
- 매개변수로 여러 예외의 공통 부모를 사용하면 된다.


### 파라미터와 응답.
- "@ExceptionHandler"에는 마치 스프링의 컨트롤러의 파라미터 응답처럼 다양한 파라미터와 응답을 지정할 수 있다.
- 자세한 파라미터와 응답은 공식 메뉴얼을 참고하자.
- https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-exceptionhandler-args


### @ExceptionHandler 다양한 활용.
- "Controller"에서 활용할 수 있는 것들은 대부분 사용가능하지만, API에서 보통 사용한다.


### 실행 흐름.
- 컨트롤러를 호출한 결과 "IllegalArgumentException" 예외가 컨트롤러 밖으로 던져진다.
- 예외가 발생했으므로 "ExceptionResolver"가 작동한다. 가장 우선순위가 높은 "ExceptionHandlerExceptionResolver"가 실행된다.
- "ExceptionHandlerExceptionResolver"는 해당 컨트롤러에 "IllegalArgumentException"을 처리할 수 있는 "@ExceptionHandler"가 있는지 확인한다.
- illegalExHandle()을 실행한다.
- "@RestController"이므로 illegalExHandle()에도 "@ResponseBody"가 적용된다. 따라서 HTTP 컨버터가 사용되고, 응답을 "JSON"으로 반환된다.
- @ResponseStatus(HttpStatus.BAD_REQUEST)를 지정했으므로 HTTP 상태 코드 400으로 응답한다.


### 참고.
- "@ExceptionHandler"를 적용하면 예외가 발생해도 정상적인 응답으로 판단하고 200을 반환한다. 
- @ResponseStatus()로 지정하거나, ResponseEntity(값, HttpStatus.BAD_REQUEST)를 통해 Http 상태값을 변경해줘야 한다.

------------------------------------------------------------------------------------------------------------------------------------

> ## @ControllerAdvice.

### ControllerAdvice.
- 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여해주는 역할을 한다.
- "@ControllerAdvice"에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌)
- "@RestControllerAdvice"는 "@ControllerAdvice"에 "ResponseBody"만 적용된 것이다.


### 대상 컨트롤러 지정 방법.
    // 어노테이션이 적용된 컨트롤러 지정.
    @ControllerAdvice(annotations = RestController.class)
    
    // 패키지 하위에 있는 컨트롤러 지정.
    @ControllerAdvice("org.example.controllers")

    // 컨트롤러 이름을 통해 지정.
    @ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
1. 특정 어노테이션이 있는 컨트롤러.
2. 특정 패키지에 속하는 컨트롤러.
3. 특정 클래스.


### 팁.
- 대상 컨트롤러를 지정하지 않으면 모든 컨트롤러에 적용된다.
- 일반적으로 패키지에 속하는 컨트롤러를 많이 사용한다.

------------------------------------------------------------------------------------------------------------------------------------

> ## 정리.












