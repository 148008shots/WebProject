package com.hh.anno;

import com.hh.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented //元注解
@Target({ElementType.FIELD})//元注解，用在那一些文件上
@Retention(RetentionPolicy.RUNTIME)//保留时间段 编译，运行
@Constraint(validatedBy = {StateValidation.class})//提供校验规则的类
public @interface State {
    String message() default "{state的值只能是已发布或者草稿}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
