package com.xr.base.web.error;

import com.google.common.base.Throwables;
import com.xr.base.core.dto.ResultDto;
import com.xr.base.core.exception.BaseException;
import com.xr.base.core.util.ReflectUtils;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * <b>description</b>：web全局错误处理 <br>
 * <b>time</b>：2019-02-11 13:29 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
public class GlobalErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
    Throwable error = getError(webRequest);
    ResultDto<Object> result = null;
    if (error != null) {
      while (error instanceof Exception && error.getCause() != null) {
        error = error.getCause();
      }

      if (error instanceof BaseException) {
        BaseException e = (BaseException) error;
        result = ResultDto.failure(e.getMessage(), e);
      } else {
        result = ResultDto.failure(error.getMessage());
      }

      //异常堆栈信息
      if (includeStackTrace) {
        result.putExtData("trace", Throwables.getStackTraceAsString(error));
      }
    } else {
      result = ResultDto.failure("您访问的资源不存在");
    }

    try {
      return ReflectUtils.javaBeanToMap(result);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
