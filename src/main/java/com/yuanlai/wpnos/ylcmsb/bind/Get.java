package com.yuanlai.wpnos.ylcmsb.bind;

import java.lang.annotation.*;


@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Get {
    String value();
}
