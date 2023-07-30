package top.tangtian.datapump.datamodel.annotation;

import top.tangtian.datapump.datamodel.enums.MatchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableName {
    String value() default "common";

    String action() default  "default";

    MatchType matchType() default MatchType.Exact;
}
