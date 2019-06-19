package com.xr.base.trace.feign;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <b>description</b>：链路追踪配置信息 <br>
 * <b>time</b>：2019-02-13 15:43 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
@ConfigurationProperties(prefix = TraceProperties.PREFIX)
public class TraceProperties {
  public final static String PREFIX = "ms.base.trace.feign";

  public final static String TRACE_KEY = "ms_trace_id";

  private boolean enabled = true;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
