package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
