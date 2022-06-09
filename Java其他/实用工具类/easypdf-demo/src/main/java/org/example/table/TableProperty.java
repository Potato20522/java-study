package org.example.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableProperty {

    /**
     * 表头名称
     */
    String value();

    /**
     * 忽略此字段
     */
    boolean ignore() default true;

    /**
     * 该单元格的宽度
     */
    float width() default 0;
}