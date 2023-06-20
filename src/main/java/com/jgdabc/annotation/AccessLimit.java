package com.jgdabc.annotation;


import java.lang.annotation.*;
/**
 * 注解@Target和@Retention可以用来修饰注解，是注解的注解，称为元注解
 * @Documented是元注解，可以修饰其他注解。许多注解头部都有@Documented注解
 * @author 兰舟千帆
 * @version 1.0
 * @date 2022/12/11 15:00
 */

/**
 * 如果一个注解@B，被@Documented标注，那么被@B修饰的类，生成文档时，会显示@B。
 * 如果@B没有被@Documented标准，最终生成的文档中就不会显示@B。
 */
//* 这个注解的意思是让AccessLimit注解只在java源文件中存在，编译成.class文件后注解就不存在了
//        * @Retention(RetentionPolicy.CLASS)
//    ElementType.TYPE：该注解只能声明在一个类前。
//    ElementType.METHOD 该注解只能声明在一个方法前
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {
    int seconds()  default 15;
    int maxCount() default 15;
    boolean needLogin()default false;
}
