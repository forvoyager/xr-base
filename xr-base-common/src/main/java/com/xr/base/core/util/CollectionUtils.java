package com.xr.base.core.util;

import java.util.Collection;

/**
 * 集合工具类
 * Created by forvoyager@outlook.com on 2019-01-08 16:47.
 */
public final class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }

        return false;
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    private CollectionUtils(){}
}
