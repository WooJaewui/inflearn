
# 검증 1_Validation

----------------------------------------------------------------------------------------------------------------------------------

> ## 검증 요구사항.

### 요구사항.
- 타입 검증.
  - 가격, 수량에 문자가 들어가면 검증 오류 처리.
- 필드 검즈.ㅇ
  - 상품명 : 필수, 공백 X.
  - 가격 : 1000원 이상, 1백만원 이하.
  - 수량 : 최대 9999
- 특정 필드의 범위를 넘어서는 검증.
  - 가격 * 수량의 합은 10,000원 이상.


### 클라이언트 검증, 서버 검증.
- 클라이언트 검증은 조작할 수 있으므로 보안에 취약하다.
- 서버 검증은 즉각적인 고객 사용성이 부족해진다.
- 둘을 적절히 섞어서 사용하되, 최종적으로 서버 검증은 필수.
- API 방식을 사용하면 API 스펙을 잘 정의해서 검증 오류를 API 응답 결과에 잘 남겨주어야 함.


### 컨트롤러.
- 컨트롤러의 중요한 역할 중 하나는 HTTP 요청이 정상인지 검증하는 것이다.
- 그리고 정상 로직보다 이런 검증 로직을 잘 개발하는 것이 어쩌면 더 어려울 수 있다.

----------------------------------------------------------------------------------------------------------------------------------

> ## 검증 직접 처리 - 소개.

### 상품 저장 검증 실패.
- 고객이 상품 등록 폼에서 상품명을 입력하지 않거나, 가격, 수량 등이 너무 작거나 커서 검증 범위를 넘어서면, 서버 검증 로직이 실패해야 한다.
- 이렇게 검증에 실패한 경우 고객에게 다시 상품 등록 폼을 보여주고, 어떤 값이 잘못 입력됐는지 알려주어야 한다.

----------------------------------------------------------------------------------------------------------------------------------

> ## 검증 직업 처리 - 개발.

### 리팩토링 팁.
- 부정의 부정은 이해하기 어려워서 읽기 쉽도록 리팩토링 대상해야 한다.


### Safe Navigation Operator.
- errors?. 은 errors가 null일 때 NullPointerException이 발생하는 대신, null을 반환하는 문법이다.


### 필드 오류 처리.
    <input type="text" th:classappend="${errros?.containKey('itemName')} ? 'field-error' : _" class="form-control">


### 정리.
- 만약 검증 오류가 발생하면 입력 폼을 다시 보여준다.
- 검증 오륟르을 고객에게 친절하게 안내해서 다시 입력할 수 있게 한다.
- 검증 오류가 발생해도 고객이 입력한 데이터가 유지된다.


### 남은 문제점.
- 뷰 템플릿에 중복된 처리가 많다.
- 타입 오류 처리가 안된다. (Controller에 들어오기 전에 400 오류 발생)
- Controller에 들어오지 않기 때문에 클라이언트가 입력한 값을 뷰에서 확인할 수 없다.
- 고객이 입력한 값도 어딘가에 별도로 관리가 되어야 한다.

----------------------------------------------------------------------------------------------------------------------------------

> ## 프로젝트 V2.

### 개요.
- 앞서 만든 기능을 유지하기 위해, 컨트롤러와 템플릿 파일을 복사하자. (V1 -> V2)


### 폴더 변경.
- 폴더 선택 후 ctrl + shift + R : 폴더 하위에 모든 검색 결과를 보여준다. (replaceALL 가능) 

----------------------------------------------------------------------------------------------------------------------------------

> ## BindingResult1.

### 인텔리제이 단축키.
- ctrl + p : 메서드의 파라미터 설명이 나온다.


### BindingResult.
- 필드에 오류가 있으면 FieldError 객체를 생성해서 bindingResult에 담아두면 된다.
  - ObjectName : @ModelAttribute 이름
  - field : 오류가 발생한 필드 이름
  - defaultMessage : 오류 기본 메시지.
  - FieldError 클래스는 ObjectError를 상속받은 클래스이다.
- 특정 필드를 넘어서는 오류가 있으면 ObjectError 객체를 생성해서 bindingResult에 담아두면 된다.
  - ObjectName : @ModelAttribute 이름.
  - defaultMessage : 오류 기본 메시지.

  
### 예시 코드.
    bindingResult.addError(new FieldError("item", "price", "값 오류"));
    bindingResult.addError(new ObjectError("item", "필드 이외의 오류"));


### BindingResult의 주의사항.
- Controller에 매개변수에 @ModelAttribute 다음에 BindingResult를 넣어야 한다.
- 순서가 중요하다.


### #fields.
    <div th:if="${#fields.hasGlobalErrors()}">
        <div th:each="err : ${#fields.globalErrros()}" th:text="${err}"></div>
    </div>
- #fields로 BindingResult가 제공하는 검증 오류에 접근할 수 있다.


### th:errors
    <div th:errors="*{price}" ...></dvi>
- fieldError중에 price 필드와 관련 있는 에러가 있으면 에러 메시지가 보이도록 만든다.


### th:errorclass
    <div th:field="*{price}" th:errorclass="new-class" ...></div>
- th:field에서 지정한 필드 오류가 있으면 class 정보를 추가한다.

----------------------------------------------------------------------------------------------------------------------------------

> ## BindingResult2.

### BindingResult.
- 스프링이 제공하는 검증 오류를 보관하는 객체이다.
- 검증 오류가 발생하면 여기에 보관하면 된다.
- BindingResult가 있으면 @ModelAttribute에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다.


### @ModelAttribute에 바인딩 시 타입 오류가 발생하면.
1. BindingResult가 없으면 -> 400오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다.
2. BindingResult가 있으면 -> 오류 정보(FieldError)를 BindingResult에 담아서 컨트롤러를 정상 호출한다.


### BindingResult 검증 오류를 적용하는 3가지 방법.
1. @ModelAttribute의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 FieldError를 생성해서 BindingResult에 넣어준다.
2. 개발자가 직접 넣어준다.
3. Validator를 사용.(뒤에서 설명)


### 주의.
- BindingResult는 검증할 대상 바로 다음에 와야한다. 순서가 중요하다.
- BindingResult는 Model에 자동으로 포함된다.


### BindingResult 오류 구분.
1. 데이터를 바인딩할 때 오류. (스프링이 직접 생성)
2. 비즈니스 로직적인 오류. (직접 작성해줘야 한다)


### BindingResult 인터페이스.
- Errors 인터페이스를 상속받고 있다.
- 실제 넘어오는 구현체는 BeanPropertyBindingResult 라는 것인데, 둘 다 구현하고 있으므로 BindingResult 대신 Errors를 사용해도 된다.


### Errors 인터페이스.
- 단순한 오류 저장과 조회 기능만 제공한다.
- BindingResult는 여기에 더해서 추가적인 기능들을 제공한다.
- 주로 관례상 BindingResult를 많이 사용한다.


### 정리.
- BindingResult, FieldError, ObjectError를 사용해서 오류 메시지를 처리하는 방법을 알아보았따.
- 그런데 오류가 발생하는 경우 고객이 입력한 데이터가 모두 사라졌다.

----------------------------------------------------------------------------------------------------------------------------------

> ## FieldError, ObjectError.

### 목표.
- 사용자 입력 오류 메시지가 화면에 남도록 하자.


### FieldError 생성자 파라미터 목록.
- objectName : 오류가 발생한 객체 이름.
- field : 오류 필드.
- rejectedValue : 사용자가 입력한 값(거절된 값)
- bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값(false = 바인딩 실패 아님)
- codes : 메시지 코드
- arguments : 메시지에 사용하는 인자.
- defaultMessage : 기본 오류 메시지.


### 파라미터 기능 설명.
- 사용자의 입력 데이터가 @ModelAttribute에 바인딩되는 시점에 오류가 발생하면 모델 객체에 사용자 입력 값을 유지하기 어렵다.
- 그래서 오류가 ㅂ라생한 경우 사용자 입력 값을 보관하는 별도의 방법이 필요하다.
- 그리고 이렇게 보관한 사용자 입력 값을 검증 오류 발생시 화면에 다시 출력하면 된다.
- FieldError는 오류 발생시 사용자 입력 값을 저장하는 기능을 제공한다.
- 여기서는 rejectValue는 오류 발생시 사용자 입력 값을 저장하는 필드다.
- bindingFailure는 타입 오류 같은 바인딩이 실패했는지 여부를 적어주면 된다.


### 타임리프의 사용자 입력 값 유지.
    th:field="*{price}"
- 타임리프의 th:field는 매우 똑똑하게 동작한다.
- 정상 상황에는 모델 객체의 값을 사용하지만, 오류가 발생하면 FieldError에서 보관한 값을 사용해서 값을 출력한다.


### 스프링의 바인딩 오류 처리.
- 타입 오류로 바인딩에 실패하면 스프링은 FiledError를 생성하면서 사용자 입력한 값을 넣어둔다.
- 그리고 해당 오류를 BindingResult에 담아서 컨트롤러를 호출한다.
- 따라서 타입 오류같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력할 수 있다.

----------------------------------------------------------------------------------------------------------------------------------

> ## 오류 코드와 메시지 처리 1.

### 개요.
- 오류 메시지를 체계젹으로 다루어보자.
- FieldError 생성자에 codes, arguments를 통해 설정할 수 있다.


### FieldError 파라미터.
- codes.
  - String[]의 배열 값을 받는다.
  - String 배열의 첫 번째 값부터 찾은 후에 없으면 순차적으로 확인한 후 마지막 값까지 없으면 defaultMessage를 반환한다.
- arguments.
  - Object[]의 배열 값을 받는다.
  - error.properties에서 사용될 파라미터의 값을 Object[] 배열로 작성한다.


### errors.properties 작성.
1. application.properties 파일에 "spring.message.name=이름"을 적용한다.
2. errors.properties를 생성하고, 키(codes) 값을 설정하여 작성한다.

----------------------------------------------------------------------------------------------------------------------------------

> ## 오류 코드와 메시지 처리 2.

### 목표.
- FieldError, ObjectError는 다루기 너무 번거롭다.
- 오류 코드도 좀 더 자동적으로 할 수 있지 않을까?


### BindingResult.
- Controller에서 본인이 검증해야 될 타겟 객체 바로 뒤에 와야 한다.
- BindingResult는 이미 타겟 객체가 누군지 알고 있다.


### BindingResult rejectValue(), reject().
- BindingResult가 제공하는 rejectValue(), reject()를 사용하면 FieldError, ObjectError를 직접 생성하지 않고 검증할 수 있다.


### rejectValue().
- FieldError를 대신 생성해준다.
- 매개변수
  - field : 오류 필드명.
  - errorCode : error.properties에 등록한 것과 조금 다르다.
  - errorArgs : 오류 메시지에서 {0}을 치환하기 위한 값.
  - defaultMessage : 오류 메시지를 찾지 못했을 때 사용하는 기본 메시지.


### 축약된 오류 코드.
- MessageCodesResolver를 이해해야 한다.
- 다음 장에서 자세히 설명한다.

----------------------------------------------------------------------------------------------------------------------------------

> ## 오류 코드와 메시지 처리 3.

### 오류 메시지 작성.
- 단순하게 만들면 범용성이 좋아서 여러곳에서 사용할 수 있지만, 메시지를 세밀하게 작성하기 어렵다.
- 반대로 너무 자세하게 만들면 범용성이 떨어진다.
- 가장 좋은 방법은 범용성으로 사용하다가, 세밀하게 작성해야 하는 경우에는 세밀한 내용이 적용되도록 메시지에 단계를 두는 방법이다.


### codes 동작 원리.
- codes 파라미터로 "required"를 설정한 경우.
  - errros.properties에서 "required=값" 으로 작성된 메시지가 있으면 사용한다.
  - "required.item.itemName"와 같이 더 자세한 메시지 키가 존재하면 더 자세한 메시지를 사용한다.
    - new Object[]{"required.item.itemName", "required"}
- 스프링은 MessageCodesResolver라는 것으로 이러한 기능을 제공한다.

----------------------------------------------------------------------------------------------------------------------------------

> ## 오류 코드와 메시지 처리 4.

### 예시 코드.
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    String[] strings = codesResolver.resolveMessageCodes("required", "item");

    String[] strings = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);


### MessageCodesResolver.
- 검증 오류 코드로 메시지 코드들을 생성한다.
- MessageCodesResolver 인터페이스고 DefaultMessageCodesResolver는 기본 구현체이다.
- 주로 다음과 함께 사용 ObjectError, FieldError.


### DefaultMessageCodesResolver의 기본 메시지 생성 규칙.
    // 객체 오류의 경우 다음 순서로 2가지 생성.
    1. code + "." + object name
    2. code

    예) 오류 코드 required, object name : item.
    1. required.item.
    2. required


    // 필드 오류의 경우 다음 순서로 4가지 메시지 코드 생성.
    1. code + "." + object name + "." + field
    2. code + "." + field
    3. code + "." + field type
    4. code

    예) 오류 코드 : typeMisamtch, object name "user", field "age", field type : int
    1. typeMismatch.user.age
    2. typeMismatch.age
    3. typeMismatch.int
    4. typeMismatch


### 동작 방식.
- rejectValue(), reject()는 내부에서 MessageCodesResolver를 사용한다. 여기에서 메시지 코드들을 생성한다.
- FieldError, ObjectError의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다.
- MessageCoesResolver를 통해서 생성된 순서대로 오류 코드를 보관한다.


### FieldError.
- required.item.itemName
- required.itemName
- required.java.lang.String
- required


### ObjectError.
- required.item
- required

----------------------------------------------------------------------------------------------------------------------------------

> ## 오류 코드와 메시지 처리 5.

### 오류 코드 관리 전략.
- 핵심은 구체적인 것에서 덜 구체적인 것으로.
- MessageCodesResolver는 required.item.itemName처럼 구체적인 것을 먼저 만들어주고, required처럼 덜 구체적인 것을 가장 나중에 만든다.


### 왜 이렇게 복잡하게 사용하는가 ?
- 모든 오류 코드에 대해서 메시지를 각각 다 정의함녀 개발자 입장에서 관리하기 너무 힘들다.
- 크게 중요하지 않은 메시지는 범용성 있는 required같은 메시지로 끝내고, 정말 중요한 메시지는 구체적으로 적어서 작성한다.


### ValidationUtils.
- 공백같은 단순 기능에서만 사용할 수 있다.

----------------------------------------------------------------------------------------------------------------------------------

> ## 오류 코드와 메시지 처리 6.

### 검증 오류 코드 2가지.
1. 개발자가 직접 만든 코드.
2. 스프링이 직접 검증 오류에 추가한 경우. (주로 타입 정보가 맞지 않음)


### 스프링이 직접 만든 오류 메시지 처리.
- codes에 "typeMismatch" 등을 통해 작성되어 넘어온다.

----------------------------------------------------------------------------------------------------------------------------------

> ## Validator 분리 1.

### @Autowired.
- 생성자가 하나일 때는 @Autowired 생략 가능하다.


### 검증 로직 분리.
- Validator 클래스를 만들어서 별도의 클래스로 역할을 분리하는 것이 좋다.
- 이렇게 분리한 검증 로직을 재사용 할 수도 있다.


### supports()
    @Override
    public boolean supports(Class<?> clazz) {
        // item == clazz || item == subItem(Item의 자식)
        return Item.class.isAssignableFrom(clazz);
    }
- isAssignableFrom(clazz) : clazz 또는 clazz의 자손 클래스와 같은지 확인한다.
- 뒤에 더 자세히 알아본다.


### Validator 주입.
- 스프링 빈으로 Validator를 등록하고, @Autowired를 통해 주입해준다.

----------------------------------------------------------------------------------------------------------------------------------

> ## Validator 분리 2.

### 개요.
- Validator 인터페이스를 구현하면 스프링의 추가적인 도움을 받을 수 있다.


### WebDataBinder.
- 스프링의 파라미터 바인딩, 검증 기능 등을 내부에 포함한다.


### @InitBinder.
    @InitBinder
    public void init(WebDataBinder dataBinder) {
      dataBinder.addValidators(itemValidator);
    }
- WebDataBinder에 검증기를 추가하면 해당 컨트롤러에서는 검증기를 자동으로 적용할 수 있다.
- 글로벌 설정은 별도로 해야한다.


### @Validated.
- Controller에서 검증을 하려는 타겟 클래스 앞에 사용한다.


### 동작 방식.
- @Validated는 검증기를 실행하라는 어노테이션이다.
- @Validated가 붙으면 WebDataBinder에 등록한 검증기를 찾아서 실행한다.
- 등록한 검증기에서 supports()를 통해 내가 원하는 타겟 클래스가 넘어왔는지 확인한다.
- supports()의 결과가 true인 검증기만 validate()를 호출한다.


### 글로벌 Validator 등록하는 방법.
    @SpringBootApplication
    public class ItemServiceApplication implements WebMvcConfigurer {
    
      public static void main(String[] agrs) {
        SpringApplication.run(ItemServiceApplication.class, args);
      }

      @Override
      public Validator getValidator() {
        return new ItemValidator();
      }
    }
- @InitBinder를 제거해도 Validator가 동작한다.
- 컨트롤러에 @Validated는 생략하면 안된다.


### 주의 사항.
- 글로벌 설정을 하면 다음에 설명할 BeanValidator가 자동 등록되지 않는다.
- 참고로 글로벌 설정을 직접 사용하는 경우는 드물다.


### @Validated, @Valid.
- @Valida를 사용하려면 build.grable 의존관계 추가가 필요하다. (javax.validation.@Valid)
- @Validated는 스프링 전용 검증 어노테이션이고, @Valid는 자바 표준 검증 어노테이션이다.
- 자세한 내용은 Bean Validation에서 설명한다.


### 정리.
- reject => ObjectError를 생성.
  - 오류 코드를 자동으로 생성할 때, 총 2개 레벨이 존재.
- rejectValue() => FieldError를 생성.
  - 오류 코드를 자동으로 생성할 때, 총 4개 레벨이 존재.









