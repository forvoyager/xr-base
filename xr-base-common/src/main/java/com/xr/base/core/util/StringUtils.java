package com.xr.base.core.util;

/**
 * 字符操作工具类
 *
 * 继承自 com.ms.base.comm.util.StringUtils
 * 可以自行扩展
 *
 * Created by forvoyager@outlook.com on 2019-01-08 16:42.
 */
public final class StringUtils extends org.springframework.util.StringUtils {

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    private StringUtils(){}
}
