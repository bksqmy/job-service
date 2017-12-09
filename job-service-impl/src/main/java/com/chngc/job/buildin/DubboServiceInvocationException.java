package com.chngc.job.buildin;

/**
 * 调用Dubbo服务时报错。
 * @version 1.0
 */
public class DubboServiceInvocationException extends RuntimeException {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 5826830069946919434L;

    public DubboServiceInvocationException(String message, Throwable e) {
        super(message, e);
    }
}