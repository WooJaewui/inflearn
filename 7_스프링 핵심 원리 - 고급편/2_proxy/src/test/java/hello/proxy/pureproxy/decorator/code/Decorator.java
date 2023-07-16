package hello.proxy.pureproxy.decorator.code;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Decorator {
    private final Component component;

    public Decorator(Component component) {
        this.component = component;
    }
}
