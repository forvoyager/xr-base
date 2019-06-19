package com.xr.base.jdbc.datasource;

import com.xr.base.core.enums.Cluster;

/**
 * 保存上下文Data Source类型（线程安全）
 * Created by forvoyager@outlook.com on 2019-02-13 9:59.
 */
public class DataSourceContextHolder {

  private static ThreadLocal<Cluster> contextHolder = new ThreadLocal<Cluster>();

  public static void setMaster(){
    contextHolder.set(Cluster.master);
  }
  public static void setSlave(){
    contextHolder.set(Cluster.slave);
  }

  public static Cluster getDataSourceType() {
    return contextHolder.get();
  }

  public static void clear() {
    contextHolder.remove();
  }
}
