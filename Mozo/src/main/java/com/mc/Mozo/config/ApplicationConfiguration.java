package com.mc.Mozo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


public class ApplicationConfiguration {
  public static final String BASE_PACKAGE = "com.mc.Mozo";

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
