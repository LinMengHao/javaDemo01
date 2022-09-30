package com.example.demo01.annotation;

import com.example.demo01.common.OperatorType;
import org.springframework.expression.spel.ast.Operator;

import java.lang.annotation.*;
/**
 * 日志切面
 */
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    //操作者
    public String operatorName() default "";
    //操作类型
    public String operatorType() default OperatorType.SEND;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;
}
