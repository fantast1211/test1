package com.yc.springframework.steretype;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyComponentScan {
    String[] basePackages() default {};
}
