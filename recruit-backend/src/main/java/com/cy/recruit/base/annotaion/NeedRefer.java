package com.cy.recruit.base.annotaion;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NeedRefer {

    boolean isNeed() default true;
}
