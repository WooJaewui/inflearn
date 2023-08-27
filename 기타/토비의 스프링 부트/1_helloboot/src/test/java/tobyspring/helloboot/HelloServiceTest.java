package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Test
@interface UnitTest {

}

public class HelloServiceTest {

    @Test
    void simpleHelloService(){
        SimpleHelloService helloService = new SimpleHelloService();

        String test = helloService.sayHello("test");

        assertThat(test).isEqualTo("hello test");
    }

    @Test
    void helloDecorator() {
        HelloDecorator decorator = new HelloDecorator(name -> name);

        String test = decorator.sayHello("test");

        assertThat(test).isEqualTo("*test*");
    }

}
