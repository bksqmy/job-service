package com.chngc.job.buildin;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;

import com.chngc.job.AbstractJobLoader;
import com.chngc.job.InvalidateCfgDataException;
import com.chngc.job.JobManager;
import com.chngc.job.entity.SysJob;
import com.chngc.job.util.ConvertUtil;
import com.chngc.job.util.StringUtil;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.newJob;

/**
 * Dubbo Job的装载器。
 * <p/>
 * 主要完成Dubbo Job配置数据的解析、基本校验，以及构造Job启动时的{@link JobDetail}、{@link Trigger}。
 * <p/>
 * <p/>
 * <strong>{@link SysJob}的配置</strong><br />
 * 1. {@link SysJob#getJobType()}必须为{@link SysJob#TYPE_DUBBO}类型；<br />
 * 2. {@link SysJob#getCfgData()}配置格式如下：<pre style="margin:0;padding:0;">
 * {
 * interface:"com.chngc.job.service.JobService",
 * method:"echo",
 * url:"dubbo://127.0.0.1:20880"
 * }</pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;interface: 必填，被调用服务接口类名；<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;method: 必填，被调用的服务方法名；<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;url: 可选，如果指定了url则使用直连方式调用Dubbo服务方法，将不使用注册中心；
 * 如果未指定url参数，则通过注册中心调用Dubbo服务，注册中心配置在job-service-config.xml中（jobManager的registryProtocol、registryAddress）
 *
 * @version 1.0
 * @see DubboJob
 */
public class DubboJobLoader extends AbstractJobLoader {
    public final static String REFERENCE_CONFIG_KEY = "$REFERENCE_CONFIG_KEY$";

    private String interfaceName;
    private String methodName;
    private String url;
    private Integer timeout;
    private String group;
    private ReferenceConfig<GenericService> reference;

    public DubboJobLoader(SysJob job, JobManager manager) {
        super(job, manager);
    }

    @Override
    public JobDetail internalBuildJobDetail() {
        JobDetail jd = newJob(DubboJob.class).withIdentity(
                "[job-" + super.job.getJobType() + "-" + job.getJobId() + "]", DEFAULT_GROUP).build();

        ApplicationConfig appConfig = new ApplicationConfig();
        appConfig.setName("job-scheduler");

        //每个dubbo类型的job可以配置一个url属性，直连dubbo服务，绕过注册中心，这在某些情况下比较有用：
        //为job所需使用到的服务独立的部署一台机器，不加入注册中心，job直连这台机器调用服务；
        if (StringUtil.isEmpty(url, true)) {
            if (log.isInfoEnabled())
                log.info("[job-" + job.getJobId() + "][" + job.getJobName()
                        + "] Dubbo job, using registry: " + super.jobManager.getRegistryProtocol()
                        + "://" + super.jobManager.getRegistryAddress());
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setProtocol(super.jobManager.getRegistryProtocol());
            registryConfig.setAddress(super.jobManager.getRegistryAddress());
            appConfig.setRegistry(registryConfig);
        }

        // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
        this.reference = new ReferenceConfig<GenericService>();
        this.reference.setApplication(appConfig);
        if (!StringUtil.isEmpty(url, true)) {
            if (log.isInfoEnabled())
                log.info("[job-" + job.getJobId() + "][" + job.getJobName()
                        + "] Dubbo job, using direct connect: " + url);
            this.reference.setUrl(url);
        }
        this.reference.setInterface(interfaceName); // 弱类型接口名 
        this.reference.setGeneric(true); // 声明为泛化接口 
        if (this.timeout > 0)
            this.reference.setTimeout(timeout);
        if (!StringUtil.isEmptyOrNull(group)) {
            this.reference.setGroup(group);
        }

        jd.getJobDataMap().put(REFERENCE_CONFIG_KEY, reference);

        return jd;
    }

    @Override
    protected void parseCfgData() {
        this.interfaceName = (String) super.job.getCfgData("interface");
        this.methodName = (String) super.job.getCfgData("method");
        this.url = (String) super.job.getCfgData("url");
        this.timeout = ConvertUtil.toInt(super.job.getCfgData("timeout"), -1);
        this.group = (String) super.job.getCfgData("group");

        if (StringUtil.isEmpty(this.interfaceName, true))
            throw new InvalidateCfgDataException("Null or empty dubbo interface name");
        if (StringUtil.isEmpty(this.methodName, true))
            throw new InvalidateCfgDataException("Null or empty dubbo method name");
    }

    public String getInterface() {
        return this.interfaceName;
    }

    public String getMethod() {
        return this.methodName;
    }

    public ReferenceConfig<GenericService> getReferenceConfig() {
        return this.reference;
    }

}