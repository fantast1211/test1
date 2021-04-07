package com.yc.springframework.steretype;

import java.lang.annotation.*;

@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyPostConstruct {
}
