package com.chngc.job.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Job日志。
 * <p/>
 * <p>Table: <strong>sys_job_log</strong>
 * <p><table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 * <tr style="background-color:#ddd;Text-align:Left;">
 * <th nowrap>属性名</th><th nowrap>属性类型</th><th nowrap>字段名</th><th nowrap>字段类型</th><th nowrap>说明</th>
 * </tr>
 * <tr><td>logId</td><td>{@link Integer}</td><td>log_id</td><td>int</td><td>Job日志ID</td></tr>
 * <tr><td>jobId</td><td>{@link Integer}</td><td>job_id</td><td>int</td><td>Job ID</td></tr>
 * <tr><td>startTime</td><td>{@link java.util.Date}</td><td>start_time</td><td>datetime</td><td>开始时间</td></tr>
 * <tr><td>endTime</td><td>{@link java.util.Date}</td><td>end_time</td><td>datetime</td><td>结束时间</td></tr>
 * <tr><td>nextTime</td><td>{@link java.util.Date}</td><td>next_time</td><td>datetime</td><td>下一次运行时间。</td></tr>
 * <tr><td>sysStatus</td><td>{@link Integer}</td><td>sys_status</td><td>tinyint</td><td>Job执行结果：系统状态。<br />0：初始状态；-1：发生异常；1：执行成功；<br />JobBase执行excecute方法过程中未发生异常则认为执行成功。</td></tr>
 * <tr><td>bizStatus</td><td>{@link Integer}</td><td>biz_status</td><td>int</td><td>Job执行结果：业务状态。<br />状态值完全由Job自行定义。</td></tr>
 * <tr><td>message</td><td>{@link String}</td><td>message</td><td>text</td><td>&nbsp;</td></tr>
 * </table>
 *
 * @version 1.0
 */
public class SysJobLog implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3553317999729022683L;

    public final static int SYS_STATUS_INIT = 0;
    public final static int SYS_STATUS_SUCCESS = 1;
    public final static int SYS_STATUS_ERROR = -1;

    private Integer logId;

    /**
     * 获取 Job日志ID。
     */
    public Integer getLogId() {
        return this.logId;
    }

    /**
     * 设置 Job日志ID。
     *
     * @param value 属性值
     */
    public void setLogId(Integer value) {
        this.logId = value;
    }

    private Integer jobId;

    /**
     * 获取 Job ID。
     */
    public Integer getJobId() {
        return this.jobId;
    }

    /**
     * 设置 Job ID。
     *
     * @param value 属性值
     */
    public void setJobId(Integer value) {
        this.jobId = value;
    }

    private Date startTime = null;

    /**
     * 获取 开始时间。
     */
    public Date getStartTime() {
        return this.startTime;
    }

    /**
     * 设置 开始时间。
     *
     * @param value 属性值
     */
    public void setStartTime(Date value) {
        this.startTime = value;
    }

    private Date endTime = null;

    /**
     * 获取 结束时间。
     */
    public Date getEndTime() {
        return this.endTime;
    }

    /**
     * 设置 结束时间。
     *
     * @param value 属性值
     */
    public void setEndTime(Date value) {
        this.endTime = value;
    }

    private Date nextTime = null;

    /**
     * 获取 下一次运行时间。
     */
    public Date getNextTime() {
        return this.nextTime;
    }

    /**
     * 设置 下一次运行时间。
     *
     * @param value 属性值
     */
    public void setNextTime(Date value) {
        this.nextTime = value;
    }

    private Integer sysStatus = 0;

    /**
     * 获取 Job执行结果：系统状态。
     * <p/>
     * <p/>
     * 0：初始状态；-1：发生异常；1：执行成功；<br />
     * JobBase执行excecute方法过程中未发生异常则认为执行成功。
     */
    public Integer getSysStatus() {
        return this.sysStatus;
    }

    /**
     * 设置 Job执行结果：系统状态。
     * <p/>
     * <p/>
     * 0：初始状态；-1：发生异常；1：执行成功；<br />
     * JobBase执行excecute方法过程中未发生异常则认为执行成功。
     *
     * @param value 属性值
     */
    public void setSysStatus(Integer value) {
        this.sysStatus = value;
    }

    private Integer bizStatus = 0;

    /**
     * 获取 Job执行结果：业务状态。
     * <p/>
     * <p/>
     * 状态值完全由Job自行定义。
     */
    public Integer getBizStatus() {
        return this.bizStatus;
    }

    /**
     * 设置 Job执行结果：业务状态。
     * <p/>
     * <p/>
     * 状态值完全由Job自行定义。
     *
     * @param value 属性值
     */
    public void setBizStatus(Integer value) {
        this.bizStatus = value;
    }

    private String message = "";

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

}