package com.chngc.job.entity;


import com.chngc.job.util.StringUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JOB定义。
 * <p/>
 * <p>Table: <strong>sys_job</strong>
 * <p><table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 * <tr style="background-color:#ddd;Text-align:Left;">
 * <th nowrap>属性名</th><th nowrap>属性类型</th><th nowrap>字段名</th><th nowrap>字段类型</th><th nowrap>说明</th>
 * </tr>
 * <tr><td>jobId</td><td>{@link Integer}</td><td>job_id</td><td>int</td><td>Job ID</td></tr>
 * <tr><td>jobName</td><td>{@link String}</td><td>job_name</td><td>varchar</td><td>Job名称</td></tr>
 * <tr><td>jobType</td><td>{@link String}</td><td>job_type</td><td>varchar</td><td>Job类型。<br />custom：自定义Job，必须继承自JobBase类；<br />dubbo：调用一个dubbo服务方法；<br />url：调用一个url地址；<br />sql：执行一个SQL语句；</td></tr>
 * <tr><td>jobStatus</td><td>{@link Integer}</td><td>job_status</td><td>tinyint</td><td>Job状态（-1删除，0禁用，1启用）。</td></tr>
 * <tr><td>cfgData</td><td>{@link java.util.Map}</td><td>cfg_data</td><td>text</td><td>Job配置数据（JSON格式的字符串）。</td></tr>
 * <tr><td>cron</td><td>{@link String}</td><td>cron</td><td>varchar</td><td>CRON表达式，Job的调度时间设置。</td></tr>
 * <tr><td>updateUser</td><td>{@link String}</td><td>update_user</td><td>varchar</td><td>最后更新人</td></tr>
 * <tr><td>updateTime</td><td>{@link java.util.Date}</td><td>update_time</td><td>timestamp/date</td><td>最后更新时间</td></tr>
 * </table>
 *
 * @version 1.0
 */
public class SysJob implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4943975920835841L;

    public final static int STATUS_DELETED = -1;
    public final static int STATUS_DISABLED = 0;
    public final static int STATUS_ENABLED = 1;
    public final static int STATUS_CHANGEED = 5;

    public final static String TYPE_CUSTOM = "custom";
    public final static String TYPE_DUBBO = "dubbo";
    public final static String TYPE_URL = "url";
    public final static String TYPE_SQL = "sql";

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

    private String jobName = "";

    /**
     * 获取 Job名称。
     */
    public String getJobName() {
        return this.jobName;
    }

    /**
     * 设置 Job名称。
     *
     * @param value 属性值
     */
    public void setJobName(String value) {
        this.jobName = value;
    }

    private String jobType = "";

    /**
     * 获取 Job类型。
     * <p/>
     * <p/>
     * custom：自定义Job，必须继承自JobBase类；<br />
     * dubbo：调用一个dubbo服务方法；<br />
     * url：调用一个url地址；<br />
     * sql：执行一个SQL语句；
     */
    public String getJobType() {
        return this.jobType;
    }

    /**
     * 设置 Job类型。
     * <p/>
     * <p/>
     * custom：自定义Job，必须继承自JobBase类；<br />
     * dubbo：调用一个dubbo服务方法；<br />
     * url：调用一个url地址；<br />
     * sql：执行一个SQL语句；
     *
     * @param value 属性值
     */
    public void setJobType(String value) {
        this.jobType = value;
    }

    private Integer jobStatus = 0;

    /**
     * 获取 Job状态（-1删除，0禁用，1启用）。
     */
    public Integer getJobStatus() {
        return this.jobStatus;
    }

    /**
     * 设置 Job状态（-1删除，0禁用，1启用）。
     *
     * @param value 属性值
     */
    public void setJobStatus(Integer value) {
        this.jobStatus = value;
    }

    private Map<String, Object> cfgData;

    /**
     * 获取 Job配置数据（JSON格式的字符串）。
     */
    public Map<String, Object> getCfgData() {
        return this.cfgData;
    }

    /**
     * 获取Job配置数据。
     *
     * @param key
     * @return
     */
    public Object getCfgData(String key) {
        return this.cfgData == null || StringUtil.isEmpty(key) ? null : this.cfgData.get(key);
    }

    /**
     * 设置 Job配置数据（JSON格式的字符串）。
     *
     * @param value 属性值
     */
    public void setCfgData(Map<String, Object> value) {
        this.cfgData = value;
    }

    /**
     * 设置 Job配置数据。
     *
     * @param key   配置项名称。
     * @param value 配置项的值。
     */
    public void setCfgData(String key, Object value) {
        if (this.cfgData == null)
            this.cfgData = new HashMap<String, Object>();
        this.cfgData.put(key, value);
    }

    private String cron = "";

    /**
     * 获取 CRON表达式，Job的调度时间设置。
     */
    public String getCron() {
        return this.cron;
    }

    /**
     * 设置 CRON表达式，Job的调度时间设置。
     *
     * @param value 属性值
     */
    public void setCron(String value) {
        this.cron = value;
    }

    private String updateUser = "";

    /**
     * 获取 最后更新人。
     */
    public String getUpdateUser() {
        return this.updateUser;
    }

    /**
     * 设置 最后更新人。
     *
     * @param value 属性值
     */
    public void setUpdateUser(String value) {
        this.updateUser = value;
    }

    private Date updateTime;

    /**
     * 获取 最后更新时间。
     */
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置 最后更新时间。
     *
     * @param value 属性值
     */
    public void setUpdateTime(Date value) {
        this.updateTime = value;
    }

}