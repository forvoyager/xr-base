package com.xr.base.trace.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * <b>description</b>：链路追踪自动启动配置 <br>
 * <b>time</b>：2019-02-13 15:47 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
@Configuration
@EnableConfigurationProperties({TraceProperties.class})
@ConditionalOnProperty(name = TraceProperties.PREFIX + ".enabled", matchIfMissing = true)
public class TraceAutoConfiguration {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private TraceProperties traceProperties;

  public TraceAutoConfiguration(TraceProperties traceProperties) {
    this.traceProperties = traceProperties;
    logger.info("init feign client call trace...");
  }

  @Bean
  public FilterRegistrationBean<TraceFilter> traceFilter(TraceProperties traceProperties) {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(new TraceFilter(traceProperties));
    ArrayList<String> objects = new ArrayList<>();
    objects.add("/*");
    filterRegistrationBean.setUrlPatterns(objects);
    return filterRegistrationBean;
  }

  @Bean
  public FeignTraceRequestInterceptor feignClientTraceRequestInterceptor() {
    return new FeignTraceRequestInterceptor();
  }
}
