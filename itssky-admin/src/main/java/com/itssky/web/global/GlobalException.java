package com.itssky.web.global;

import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.exception.biz.BizException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author myc
 * @version 1.0.0
 * @date 2024/11/21 10:54
 */
@ControllerAdvice
public class GlobalException {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalException.class);

    /**
     * 参数校验异常处理器
     *
     * @param e e
     * @return {@link AjaxResult }
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public AjaxResult MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(x -> {
            errorMsg.append(x.getDefaultMessage()).append(",");
        });
        String message = errorMsg.toString();

        return AjaxResult.error(StringUtils.isNotBlank(message) ? message.substring(0, message.length() - 1) : "");
    }

    /**
     * 参数被 @RequestParam 修饰，且 required = true，则被修饰的参数为必传参数，不能为空；否则就会抛出异常 MissingServletRequestParameterException。
     * <p>
     * 若存在多个参数被@RequestParam注解修饰，并且设置required = true，则Spring MVC框架会按照参数在方法签名中声明的顺序逐一进行校验。
     * 一次异常只会包含一个参数的错误信息，而不是一次性列出所有缺失的必填参数。
     * <p>
     * 参数不传时，报错示例： DefaultHandlerExceptionResolver : Resolved
     * [org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'name' for
     * method parameter type String is not present]
     *
     * @param e e
     * @return {@link AjaxResult }
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public AjaxResult missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 业务异常处理程序
     *
     * @param bizException 业务异常
     * @return {@link AjaxResult }
     */
    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public AjaxResult BizExceptionHandler(BizException bizException) {
        LOGGER.error("业务异常", bizException);
        return AjaxResult.error(bizException.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }
}
