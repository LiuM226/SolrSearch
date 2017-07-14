package com.jxtii.solr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Solr注解
 * Created by guolf on 17/7/8.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SolrField {

    /**
     * 索引类型（FieldType.PRIMARY：主键，FieldType.FILE：文件，FieldType.NORMAL普通）
     *
     * @return
     * @see FieldType
     */
    FieldType type() default FieldType.NORMAL;

}
