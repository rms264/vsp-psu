package unitTests;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    public int order();
}

