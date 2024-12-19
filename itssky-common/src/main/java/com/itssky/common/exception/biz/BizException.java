package com.itssky.common.exception.biz;

/**
 * 自定义业务异常类
 *
 * @author myc
 * @date 2024/11/21 17:26
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
