package com.xr.base.core.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * @Time: 2019-04-13 11:32
 * @Author: forvoyager@outlook.com
 * @Description: 日志切面
 */
@Aspect
public class LogAspect {
  private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

  private static final String BEFORE_PATTERN = "start call {0} method, and input arguments is : {1}";
  private static final String AFTER_RETURN_PATTERN = "after call {0} method, and cost: {1}ms";

  /**
   * 拦截所有添加了@Service注解的类
   * @within(org.springframework.stereotype.Service)
   *
   * 拦截接口/抽象类及其子类的所有方法
   * target(com.xr.base.jdbc.service.IBaseService)
   * within(com.xr.base.jdbc.service.impl.BaseServiceImpl+)
   *
   * 拦截有MsParam注解的方法
   * @annotation(com.xr.api.gateway.config.MsParam)
   *
   */
  @Pointcut("target(com.xr.base.core.service.IService)")
  public void pointcut() {
  }

//  @Before("pointcut()")
//  public void beforMethod(JoinPoint jp) {
//    logger.info(MessageFormat.format(BEFORE_PATTERN, jp.getSignature().getName(), Arrays.toString(jp.getArgs())));
//  }

  @Around("pointcut()")
  public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
    long start = System.currentTimeMillis();
    long end = 0;
    logger.info(MessageFormat.format(BEFORE_PATTERN, pjp.getSignature().getName(), Arrays.toString(pjp.getArgs())));
    try {
      Object result = pjp.proceed();
      end = System.currentTimeMillis();
      return result;
    } catch (Throwable throwable) {
      end = System.currentTimeMillis();
      throw throwable;
    } finally {
      logger.info(MessageFormat.format(AFTER_RETURN_PATTERN, pjp.getSignature().getName(), (end - start)));
    }
  }

//  @AfterReturning(value = "pointcut()", argNames = "rtv", returning = "rtv")
//  public void aferMethod(JoinPoint jp, Object rtv) {
//    logger.info(MessageFormat.format(AFTER_RETURN_PATTERN, jp.getSignature().getName(), rtv));
//  }

}
