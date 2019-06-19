package com.xr.base.core.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Description: spring应用上下文操作工具类
 * @Time: 2019-02-20 10:33
 * @Author: forvoyager@outlook.com
 */
public final class SpringContextHolder implements ApplicationContextAware, DisposableBean {

  private static ApplicationContext applicationContext;

  public static <T> T getBean(String name) {
    checkApplicationContext();
    return (T) applicationContext.getBean(name);
  }

  public static <T> T getBean(Class<T> type) {
    checkApplicationContext();
    return (T) applicationContext.getBean(type);
  }

  private static void checkApplicationContext() {
    if (applicationContext == null) {
      throw new IllegalStateException("applicaitonContext未注入...");
    }
  }

  @Override
  public void destroy() throws Exception {
    applicationContext = null;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextHolder.applicationContext = applicationContext;
  }
}
