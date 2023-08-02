
# 스프링 MVC - 구조 이해.

-------------------------------------------------------------------------------------------------------------------------------

> ## 스프링 MVC 전체 구조.

### 직접 만든 프레임워크 -> 스플이 MVC 비교.
- FrontController -> DispatcherServlet.
- handlerMappingMap -> HandlerMapping.
- MyHandlerAdapter -> HandlerAdapter.
- ModelView -> ModelAndView
- viewResolver -> viewResolver
- MyView -> View.

### DispatcherServlet 구조 살펴보기.
- org.springframework.web.servlet.DispatcherServlet.
- 스프링 MVC도 프론트 컨트롤러 패턴으로 구현되어 있다.
- 스프링 MVC의 프론트 컨트롤러가 바로 디스패처 서블릿이다.
- 디스패처 서블릿도 서블릿이다.

### DispatcherServlet 서블릿 등록.
- DispatcherServlet도 부모 클래스에서 HttpServlet을 상속받아서 사용하고, 서블릿으로 동작한다.
- 스프링 부트는 DispatcherServlet을 서블릿으로 자동으로 등록하면서 모든 경로에 대해서 매핑한다.
- 더 자세한 경로가 우선순위가 높다. 그래서 기존에 등록한 서블릿도 함께 동작한다.

### 요청 흐름.
- 서블릿이 호출되면 HttpServlet이 제공하는 service()가 호출된다.
- DispatcherServlet은 service() 메서드를 @Override 해두었다.
- FrameworkServlet.service()가 호출되면서 결과적으로 DispatcherServlet doDispatch()메서드가 수행된다.

### doDispatch(request, response)
1. 핸들러 조회
   - 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
2. 핸들러 어댑터 조회 - 핸들러를 처리할 수 있는 어댑터.
   - 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다. 
3. 핸들러 어댑터 실행.
   - 핸들러 어댑터를 실행한다.
4. 핸들러 어댑터를 통해 핸들러 실행.
   - 핸들러 어댑터가 실제 핸들러를 실행한다.
5. ModelAndView 반환.
   - 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해서 반환한다. 
6. 뷰 리졸버를 통해 뷰 찾기.
   - JSP의 경우 InternalResourceViewResolver가 자동 등록되고 사용된다.
7. View 반환.
   - JSP의 경우 InternalResourceView(view)를 반환하는데, 내부에 forward() 로직이 있다.
8. 뷰 렌더링.
   - 뷰를 통해서 뷰를 렌더링한다.

### 인터페이스 살펴보기.
- 스프링 MVC의 큰 강엊ㅁ은 DispatcherServlet 코드의 변경 없이, 원하는 기능을 변경하거나 확장할 수 있다는 점이다.
- 지금까지 설명한 대부분을 확장 가능할 수 있게 인터페이스로 제공한다.
- 이 인터페이스들만 구현해서 DispatcherServlet에 등록하면 여러분만의 컨트롤러를 만들 수도 있다.

### 주요 인터페이스 목록.
- 핸들러 매핑 : org.springframework.web.servlet.HandlerMapping.
- 핸들러 어댑터 : org.springframework.web.servlet.HandlerAdapter.
- 뷰 리졸버 : org.springframework.web.servlet.ViewResolver.
- 뷰 : org.springframework.web.servlet.View.

### 정리.
- 스프링 MVC는 코드 분량도 매우 많고, 복잡해서 내부 구조를 다 파악하는 것은 쉽지 않다.
- 사실 해당 기능을 직접 확장하거나 나만의 컨트롤러를 만드는 일은 없으므로 걱정하지 않아도 된다.
- 웹 애플리케이션을 만들 때 필요로 하는 대부분의 기능이 이미 다 구현되어 있다.
- 핵심 동작방식을 알아두어야 향후 문제가 발생했을 때 어떤 부분에서 문제가 발생했는지 쉽게 파악하고, 문제를 해결할 수 있다.

-------------------------------------------------------------------------------------------------------------------------------

> ## 핸들러 매핑과 핸들러 어댑터.

### 개요.
- 지금은 전혀 사용하지 않지만, 과거에 주로 사용했던 스프링이 제공하는 간단한 컨트롤러로 핸들러 매핑과 어댑터를 이해해보자.

### Controller 인터페이스.
- 과거 컨트롤러에서 사용했던 인터페이스다.
- org.springframework.web.servlet.mvc.Controller

### HandlerMapping.
- 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 한다.
- 예)스프링 빈의 이름으로 핸들러를 찾을 수 있는 핸들러 매핑이 필요하다.
- 스프링 부트가 자동 등록하는 HandlerMapping.
  - RequestMappingHandlerMapping. (0순위)
    - 어노테이션 기반의 컨트롤러인 @RequestMapping에서 사용.
  - BeanNameUrlHandlerMapping. (1순위)
    - 스프링 빈의 이름으로 핸들러를 찾는다.

### HandlerAdapter.
- 핸들러 매핑을 통해서 찾은 핸들러를 실행할 수 있는 핸들러 어댑터가 필요하다.
- 예)Controller 인터페이스를 실행할 수 있는 핸들러 어댑터를 찾고 실행해야 한다.
- 스프링 부트가 자동 등록하는 HandlerAdapter. 
  - RequestMappingHandlerAdapter. (0순위)
    - 어노테이션 기반의 컨트롤러인 @RequestMapping에서 사용.
  - HttpRequestHandlerAdapter. (1순위)
    - HttpRequestHandler 인터테이스 처리.
  - SimpleControllerHandlerAdapter. (2순위)
    - Controller 인터페이스(어노테이션 X, 과거에 사용) 처리.

### Map 람다식.
    Map<String, String> map = HashMap<>();
    map.forEach((key,value) -> System.out.printlng( "key = " + key + "value = " + value );

### Controller 실행 과정.
1. 핸들러 매핑으로 핸들러 조회.
   - HandlerMapping을 순서대로 실행해서, 핸들러를 찾는다.
   - 이 경우 빈 읾으로 핸들러를 찾아야 하기 때문에 이름 그대로 빈 이름으로 핸들러를 찾아주는 BeanNameUrlHandlerMapping가 실행에 성공.
2. 핸들러 어댑터 조회.
   - HandlerAdapter의 supports()를 순서대로 호출한다.
   - SimpleControllerHandlerAdapter가 Controller 인터페이스를 지원하므로 대상이 된다.
3. 핸들러 어댑터 실행.
   - 디스패처 서블릿이 조회한 SimpleControllerHandlerAdapter를 실행하면서 핸들러 정보도 함께 넘겨준다.
   - SimpleControllerHandlerAdapter는 핸들러인 OldController를 내부에서 실행하고, 그 결과를 반환한다.

### HttpRequestHandler 인터페이스.
- 서블릿과 가장 유사한 형태의 핸들러이다.
- BeanNameUrlHandlerMapping을 통해 handler 등록.
- HttpRequestHandlerAdapter 어댑터를 통해 handler를 실행해준다.

### @RequestMapping.
- 가장 우선순위가 높은 핸들러 매핑과 핸들러 어댑터는 RequestMappingHandlerMapping, RequestMappingHandlerAdapter이다.
- @RequestMapping의 앞글자를 따서 만든 이름인데, 어노테이션 기반의 컨트롤러를 지원하는 매핑과 어댑터이다. (실무 99.9% 사용)

-------------------------------------------------------------------------------------------------------------------------------

> ## 뷰 리졸버.

### 스프링 부트 설정.
- InternalResourceViewResolver를 자동으로 등록한다.
- application.properties에서 spring.mvc.view.prefix/suffix로 view 경로를 설정해줄 수 있다.

### 스프링 부트가 자동 등록하는 뷰 리졸버.
1. BeanNameViewResolver. (1순위)
   - 빈 이름으로 뷰를 찾아서 반환한다.
   - 예) 엑셀 파일 생성 기능에 사용.
2. InternalResourceViewResolver.
   - JSP를 처리할 수 있는 뷰를 반환한다.

### 뷰 리졸버 실행 순서.
1. 핸들러 어댑터 호출.
   - 핸들러 어댑터를 통해 'new-form'이라는 논리 뷰 이름을 획득한다.
2. ViewResolver 호출.
   - 'new-form'이라는 뷰 이름으로 viewResolver를 순서대로 호출한다.
   - 'BeanNameViewResolver'는 'new-form'이라는 이름의 스프링 빈으로 등록된 뷰를 찾아야 하는데 없다.
   - 'InternalResourceViewResolver'가 호출된다.
3. InternalResourceViewResolver.
   - 'InternalResourceView'를 반환한다.
4. 뷰 - InternalResourceView.
   - 'InternalResourceView'는 JSP처럼 포워드 'forward()'를 호출해서 처리할 수 있는 경우에 사용한다.
5. view.render().
   - 'view.render()'가 호출되고 'InternalResourceView'는 'forward()'를 사용해서 JSP를 실행한다.

### 참고1.
- InternalResourceViewResolver는 만약 JSTL 라이브러리가 있으면 InternalResourceView를 상속받은 JstlView를 반환한다.
- JstlView는 JSTL 태그 사용시 약간의 부가 기능이 추가된다.

### 참고2.
- 다른 뷰는 실제 뷰를 렌더링하지만, JSP의 경우 forward()를 통해서 JSP로 이동해야 렌더링이 된다.
- JSP를 제외한 나머지 뷰 템플릿들은 forward()과정 없이 바로 렌더링 된다.

### 참고3.
- Thymeleaf 뷰 템플릿을 사용하면 ThymeleafViewResolver를 등록해야 한다.
- 최근에는 라이브러리만 추가하면 스프링 부트가 이런 작업도 모두 자동화해준다.

-------------------------------------------------------------------------------------------------------------------------------

> ## 스프링 MVC - 시작하기.

### 개요.
- 스프링이 제공하는 컨트롤러는 어노테이션 기반으로 실행된다.
- 스프링도 처음부터 이런 유연한 컨트롤러를 제공한 것은 아니다.

### @RequestMapping.
- 스프링은 어노테이션을 활용한 매우 유연하고, 실용적인 컨트롤러를 만들었는데 이것이 바로 '@RequestMapping' 어노테이션을 사용하는 컨트롤러다.
- 과거에는 스프링에 MVC 기능이 약해서 MVC 웹 기술은 스트럿츠 같은 다른 프레임워크를 사용했었다.
- @RequestMapping이 등장하면서 대중적으로 스프링 MVC를 사용하게 되었다.
- RequestMappingHandlerMapping, RequestMappingHandlerAdapter를 통해 핸들러 매핑해준다. (우선순위 1)

### @Controller.
- 스프링이 자동으로 스프링 빈으로 등록한다. (@Component를 가지고 있다)
- 스프링 MVC에서 어노테이션 기반 컨트롤러로 인식한다.
- RequestMappingHandlerMapping에서 컨트롤러로 인식해서 매핑해준다.

### @RequestMapping.
- 요청 정보를 매핑한다.
- 해당 URL이 호출되면 이 메서드가 호출된다.
- 어노테이션 기반으로 동작하기 때문에, 메서드의 이름은 임의로 지으면 된다.

### ModelAndView.
- 모델과 뷰 정보를 담아서 반환하면 된다.

### RequestMappingHandlerMapping.
    @Override
	protected boolean isHandler(Class<?> beanType) {
		return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) ||
				AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class));
	}
- 스프링 빈 중에서 @RequestMapping 또는 @Controller가 클래스 레벨에 붙어 있는 경우에 매핑 정보로 인식한다.

-------------------------------------------------------------------------------------------------------------------------------

> ## 스프링 MVC - 컨트롤러 통합.

### 개요.
- @RequestMapping을 잘 보면 메서드 단위로 분리되어 있기 때문에 하나의 클래스에서 만들 수 있다.
- 연관성이 있는 단위로 Controller를 만들어서 분리해준다.

### @RequestMapping.
- 클래스에 @RequestMapping 붙이고 url을 작성하면 메서드의 @ReuqestMapping의 url과 합쳐져서 url을 찾는다.

-------------------------------------------------------------------------------------------------------------------------------

> ## 스프링 MVC - 실용적인 방식.

### 개요.
- 스프링 MVC는 개발자가 편리하게 개발할 수 있도록 수 많은 편의 기능을 제공한다.

### @RequestParam.
    @ReuqetsMapping("/url")
    public String url(@Param("파람이름") String value, ...) { ... }
- 파라미터를 Controller 메서드에서 직접 받을 수 있다.

### 원하는 Method 요청만 받기.
    @RequestMapping(value = "url", method = RequestMethod.GET)
    @GetMapping("url");
    @PostMapping("url");
- @RequestMapping method 값을 통해 원하는 method 요청만 처리할 수 있다.
- @GetMapping, @PostMapping 어노테이션으로 더 간소화할 수 있다.

-------------------------------------------------------------------------------------------------------------------------------

> ## 정리.

### 핸들러 매핑.
1. RequestMappingHandlerMapping 1순위.
2. BeanNameUrlHandlerMapping 2순위.

### 핸들러 어댑터.
1. RequestMappingHandlerAdapter 1순위.
2. HttpRequestHandlerAdapter 2순위.
3. SimpleControllerHandlerAdapter 3순위.














