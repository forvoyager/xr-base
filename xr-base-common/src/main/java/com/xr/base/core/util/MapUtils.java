package com.xr.base.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by forvoyager@outlook.com on 2019-01-31 15:27.
 */
public final class MapUtils {

  public static boolean isEmpty(Map collection) {
    if (collection == null || collection.size() == 0) {
      return true;
    }

    return false;
  }

  public static <K, V> Map<K, V> newHashMap(Object... args) {
    HashMap map = new HashMap();
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        map.put(args[i], args[++i]);
      }
    }
    return map;
  }

  private MapUtils(){}
}
