package com.chngc.job;

/**
 * Job执行结果。
 * @version 1.0
 */
public class JobResult {
    private int    result;
    private String message;

    public int getResult() {
        return this.result;
    }

    public void setResult(int value) {
        this.result = value;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String value) {
        this.message = value;
    }
}