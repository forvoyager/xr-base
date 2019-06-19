package com.xr.base.trace.feign;

import java.util.UUID;

/**
 * <b>description</b>：追踪工具类 <br>
 * <b>time</b>：2019-02-13 15:41 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
public final class TraceUtil {

  // 保存追踪ID
  private static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

  /**
   * 获取追踪ID
   *
   * @return
   */
  public static String get() {
    String traceId = inheritableThreadLocal.get();
    if (traceId == null) {
      traceId = UUID.randomUUID().toString().replaceAll("-", "");
      inheritableThreadLocal.set(traceId);
    }
    return traceId;
  }

  /**
   * 设置追踪ID
   * @param trace_id
   */
  public static void set(String trace_id) {
    inheritableThreadLocal.set(trace_id);
  }

  /**
   * 移除追踪ID
   */
  public static void remove() {
    inheritableThreadLocal.remove();
  }

  private TraceUtil(){}
}
