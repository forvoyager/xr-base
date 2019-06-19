package com.xr.base.trace.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * <b>description</b>：链路追踪拦截器 <br>
 * <b>time</b>：2019-02-13 15:49 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
public class FeignTraceRequestInterceptor implements RequestInterceptor {

  /**
   * 发起feign请求前，将追踪ID注入 RequestTemplate
   * @param template
   */
  @Override
  public void apply(RequestTemplate template) {
    template.header(TraceProperties.TRACE_KEY, TraceUtil.get());
  }
}