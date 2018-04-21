package com.hnqc.ironhand.exceptions;


import com.hnqc.ironhand.utils.NestedRuntimeUtils;

/**
 * 包装异常类,规范化异常信息
 *
 * @author zido
 * @date 2018/04/12
 */
public abstract class AbstractNestedRuntimeException extends RuntimeException {
    public AbstractNestedRuntimeException(String msg) {
        super(msg);
    }

    public AbstractNestedRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public String getMessage() {
        return NestedRuntimeUtils.buildMessage(super.getMessage(), getCause());
    }

    public Throwable getRootCause() {
        return NestedRuntimeUtils.getRootCause(getCause());
    }

    public Throwable getOriginalCause() {
        return NestedRuntimeUtils.getOriginalCause(getCause());
    }

    /**
     * 检测是否包含给定异常类
     *
     * @param type 检测类
     * @return true/false
     */
    public boolean contains(Class<?> type) {
        if (type == null) {
            return false;
        }
        if (type.isInstance(this)) {
            return true;
        }
        Throwable cause = getCause();
        if (cause == this) {
            return false;
        }
        if (cause instanceof AbstractNestedRuntimeException) {
            return ((AbstractNestedRuntimeException) cause).contains(type);
        } else {
            while (cause != null) {
                if (type.isInstance(cause)) {
                    return true;
                }
                if (cause.getCause() == cause) {
                    break;
                }
                cause = cause.getCause();
            }
            return false;
        }
    }
}
