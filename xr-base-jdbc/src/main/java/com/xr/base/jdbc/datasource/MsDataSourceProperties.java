package com.xr.base.jdbc.datasource;

/**
 * 数据源自动配置信息
 * Created by forvoyager@outlook.com on 2019-02-13 10:12.
 */
public class MsDataSourceProperties {
  public static final String PREFIX = "ms.base.data.source";

  private boolean enabled = true;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
