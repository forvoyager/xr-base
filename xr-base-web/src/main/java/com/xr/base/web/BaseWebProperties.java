package com.xr.base.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <b>description</b>：web全局处理 配置 <br>
 * <b>time</b>：2019-02-11 13:29 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
@ConfigurationProperties(prefix = BaseWebProperties.PREFIX)
public class BaseWebProperties {

  public static final String PREFIX = "ms.base.web";

  private boolean enabled = true;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

}
