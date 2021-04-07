package com.yc.springframework.steretype;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyResource {
    String name() default "";
}
