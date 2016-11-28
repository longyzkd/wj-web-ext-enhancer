package com.kingen.aop;  
   
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
 /**
  * controller日志注解，入口日志
  * @author wj
  *
  */
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD})  
public @interface ControllerLogAnnotation {  
    //模块名  
    String moduleName();  
    //操作内容  
    String option();  
}  