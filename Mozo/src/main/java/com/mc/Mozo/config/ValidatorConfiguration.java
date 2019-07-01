package com.mc.Mozo.config;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ValidatorConfiguration {

  @Bean
  public Validator validator() {
    return new LocalValidatorFactoryBean();
  }
}
