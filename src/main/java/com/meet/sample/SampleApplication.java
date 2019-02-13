package com.meet.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

//声明当前类提供Spring Boot application
@SpringBootConfiguration
//通过这个注解把spring应用所需的bean注入容器中
@EnableAutoConfiguration
//自动扫描注解标识的类，最终生成ioc容器里的bean
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public class SampleApplication {

    public static void main(String[] args) {

        SpringApplication.run(SampleApplication.class, args);
    }

}

