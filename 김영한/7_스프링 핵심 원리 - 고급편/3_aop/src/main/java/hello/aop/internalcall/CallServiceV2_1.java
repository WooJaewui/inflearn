package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2_1 {

    private final ObjectProvider<CallServiceV2_1> callServiceProvider;


    public CallServiceV2_1(ObjectProvider<CallServiceV2_1> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }

    public void external() {
        log.info("call external");
        CallServiceV2_1 callServiceV2 = callServiceProvider.getObject();
        callServiceV2.internal(); //내부 메서드 호출 (this.internal())
    }

    public void internal() {
        log.info("call internal");
    }
}
