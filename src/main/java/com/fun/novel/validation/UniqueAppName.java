package com.fun.novel.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueAppNameValidator.class)
@Documented
public @interface UniqueAppName {
    String message() default "应用名称已存在";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 