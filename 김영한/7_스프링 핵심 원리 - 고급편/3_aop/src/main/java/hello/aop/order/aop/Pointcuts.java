package hello.aop.order.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    // hello.aop.order 하위 패키지에 있는 모든 클래스 메서드
    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder() {} // pointcut signature


    //클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {}

    @Pointcut("allOrder() && allService()")
    public void orderAndService() {}


}
