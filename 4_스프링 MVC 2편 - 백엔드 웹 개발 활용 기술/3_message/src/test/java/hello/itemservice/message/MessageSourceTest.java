package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage(){

        //given
        String result = ms.getMessage("hello", null, null);
        //when
        //then
        Assertions.assertThat(result).isEqualTo("안녕");

    }

    @Test
    void notFoundMessageCode() {
        Assertions.assertThatThrownBy(() -> ms.getMessage("no_code", null, null)).isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void defaultMessage() {

        String message = ms.getMessage("no_code", null, "기본 메시지", null);
        Assertions.assertThat(message).isEqualTo("기본 메시지");

    }

    @Test
    void argumentMessage(){
        String message = ms.getMessage("hello.name", new Object[]{"String"}, null);
        Assertions.assertThat(message).isEqualTo("안녕 String");
    }


    @Test
    void enLang() {
        Assertions.assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }


}
