package com.chngc.job.commons;


/**
 * 业务层级的异常，这类异常在message中不能包含任何调试信息，必须是用户友好的描述信息，
 * 能够显示在界面给用户看
 *
 * <p>异常处理规范：
 *
 * <p>要解决的问题：
 * <ul>
 *      <li>1.
 * </ul>
 * 代码在抛出这类型异常之前，必须先将详细描述信息、内部异常堆栈等记录到日志中，
 *
 * @Filename: SystemException.java
 * @Version: 1.0
 *
 */
public class BusinessException extends RuntimeException {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6832695224568830049L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}