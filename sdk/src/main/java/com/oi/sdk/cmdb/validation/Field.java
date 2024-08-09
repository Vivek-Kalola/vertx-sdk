package com.oi.sdk.cmdb.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for entity fields.
 * datatype of the field on which data validation will be applicable
 * is required field?
 * is unique field? it inherits the required
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {

    DataType dataType() default DataType.STRING;

    boolean required() default false;

    boolean unique() default false;
}
