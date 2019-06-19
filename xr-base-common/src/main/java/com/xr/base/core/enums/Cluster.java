package com.xr.base.core.enums;

/**
 * 集群节点属性
 * Created by forvoyager@outlook.com on 2019-01-31 13:38.
 */
public enum Cluster {

  master("主节点"),
  slave("从节点"),
  ;

  private String label;

  private Cluster(String label){
    this.label = label;
  }
}
