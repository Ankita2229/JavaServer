package com.mc.Mozo.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:environment.properties")
public class EnvironmentConfiguration implements InitializingBean {

  @Value("${#systemProperties['dev.testing']:false}")
  public boolean isDevTesting;

  public static boolean IS_DEV_TESTING;

  @Value("${env}")
  private String env;

  @Value("#{systemProperties['run.schedulers']}")
  private Boolean runSchedulers;

  public static Boolean RUN_SCHEDULERS;
  public static String ENV;
  public static String HOST_URL;

  @Value("${mysql.driverClassName}")
  private String mysqlDriverClassName;
  @Value("${mysql.url}")
  private String mysqlUrl;
  @Value("${mysql.username}")
  private String mysqlUserName;
  @Value("${mysql.password}")
  private String mysqlPassword;
  @Value("${mysql.maxPoolSize}")
  private Integer mysqlMaxPoolSize;
  @Value("${mysql.minPoolSize}")
  private Integer mysqlMinPoolSize;


  @Value("${hibernate.dialect}")
  private String hibernateDialect;
  @Value("${hibernate.show_sql}")
  private Boolean hibernateShowSql;
  // @Value("${hibernate.persistenceUnitName}")
  // private String hibernatePersistenceUnitName;

  @Value("${hibernate.hbm2ddl.auto}")
  private String hibernateHbm2ddlAuto;


  @Override
  public void afterPropertiesSet() throws Exception {
    // TODO Auto-generated method stub

  }

}
