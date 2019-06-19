package com.xr.base.trace.feign;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <b>description</b>：链路追踪Filter，一次请求只会走一次 <br>
 * <b>time</b>：2019-02-13 15:45 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
public class TraceFilter extends OncePerRequestFilter implements Ordered {

  private TraceProperties traceProperties;

  public TraceFilter(TraceProperties traceProperties){
    this.traceProperties = traceProperties;
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    boolean flag = insertMDC(request);
    try {
      filterChain.doFilter(request, response);
    } finally {
      if (flag) {
        MDC.remove(TraceProperties.TRACE_KEY);
        TraceUtil.remove();
      }
    }
  }

  /**
   * MDC（Mapped Diagnostic Context，映射调试上下文）是 log4j 和 logback 提供的一种方便在多线程条件下记录日志的功能。
   * 收到feign请求后，从request中取出trace id，然后放入log4j MDC中
   *
   * @param request
   * @return
   */
  private boolean insertMDC(HttpServletRequest request) {
    String trace_id = request.getHeader(TraceProperties.TRACE_KEY);
    if (trace_id == null) {
      trace_id = TraceUtil.get();
    }
    TraceUtil.set(trace_id);
    MDC.put(TraceProperties.TRACE_KEY, trace_id);
    return true;
  }
}
