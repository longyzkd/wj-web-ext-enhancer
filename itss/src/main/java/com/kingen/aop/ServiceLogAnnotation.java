package com.kingen.aop;  
   
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
 /**
  * service日志注解，异常日志
  * @author wj
  *
  */
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD})  
public @interface ServiceLogAnnotation {  
    String action();  
}  