package com.chngc.job.commons;

import java.io.Serializable;

/**
 * 服务接口返回对象。
 * <p>
 * <strong>服务接口基本规范</strong><br />
 * 1. 任何服务方法，都必须使用<code>ServiceResult</code>返回结果；<br />
 * 2. 客户端调用服务方法，得到<code>ServiceResult</code>对象；<br />
 * 3. {@link ServiceResult#getSuccess()}为true表示服务方法执行成功，通过{@link #getResult()}得到执行结果；<br />
 * 4. {@link ServiceResult#getSuccess()}为false表示服务方法执行失败，通过{@link #getMessage()}获取消息描述信息（错误信息）。<br />
 *
 * <p>
 * <strong>分页查询规范</strong>：<br />
 * 1. 服务方法必须有一个分页信息参数{@link PagerInfo}，客户端设置好分页信息并通过参数传递给服务端；<br />
 * 2. 服务端按照分页信息进行查询，将符合条件的总记录数设置到<code>PagerInfo</code>上，通过<code>ServiceResult</code>返回<code>PagerInfo</code>；<br />
 * 3. 客户端通过{@link #getPager()}获取<code>PagerInfo</code>，通过{@link PagerInfo#getRowsCount()}得到总记录数；
 *
 * <p>
 * <strong>异常处理规范</strong><br />
 * 1. 任何服务方法，不允许抛出异常。服务方法需要返回错误信息时，参考<strong>消息代码{@link #getCode()}、消息描述{@link #getMessage()}使用规范</strong><br />
 *    &nbsp;&nbsp;&nbsp;1.1). 通过网络传递java异常本身是不可靠的事情，依赖于底层服务框架对异常堆栈进行序列化、反序列化以及传输处理，
 *    有些远程通讯框架会对异常进行二次封装处理；<br />
 *    &nbsp;&nbsp;&nbsp;1.2). 某些异常堆栈信息可能存在循环引用等情况，会导致堆栈序列化失败；<br />
 *    &nbsp;&nbsp;&nbsp;1.3). 服务端封装业务逻辑的实现，可能使用到各种第三方组件、库，处理过程中可能抛出各种自定义异常。而客户端
 *    则无需依赖这些第三方组件和库，这样对于某些异常，服务端序列化传递到客户端之后，客户端无法反序列化回来；<br />
 * 2. 客户端调用服务方法，必须使用try .. catch捕获异常；<br />
 *    &nbsp;&nbsp;&nbsp;SOA中服务方法调用涉及到网络通讯，虽然服务方法承诺不会抛出任何异常，但网络通讯传输过程可能会出现各种异常信息，甚至服务不可用等情况，
 *    客户端必须捕获异常并进行相应处理；
 *
 * <p>
 * <strong>消息代码{@link #getCode()}、消息描述{@link #getMessage()}使用规范</strong><br />
 * 1. 绝大部分服务方法不需要使用消息代码。<br />
 *    &nbsp;&nbsp;&nbsp;如<strong>服务接口基本规范</strong>中所示，客户端通过<code>getSuccess()</code>确定服务方法是否执行成功，
 *    如果执行失败，通过<code>getMessage()</code>得到错误描述即可；<br />
 *    &nbsp;&nbsp;&nbsp;服务器端发生异常执行失败时，详细的错误描述、堆栈信息应当记录到数据库或者服务端的文件日志中，用于问题排查处理；
 *    然后通过<code>getMessage()</code>返回对用户友好的、业务型的描述信息，客户端开发者通过这个信息可以大致了解、定位问题所在，详细的排查处理则通过服务端日志完成。
 *    客户端可以选择直接或者根据场景稍加补充，在用户界面展示这个错误消息，向用户解释执行状况。<br />
 * 2. 某些服务方法可能逻辑比较复杂，需要进行一系列比较重要的处理步骤，可能在不同步骤发生各种异常时，客户端需要有针对性的采取不同的处理方式，这种情况下可以使用消息代码。<br />
 *    &nbsp;&nbsp;&nbsp;使用到消息代码的服务方法，必须在服务方法的JavaDoc中详细列出所有消息代码及解释。<br />
 *    &nbsp;&nbsp;&nbsp;对消息代码的编码不做进一步规范要求，各服务方法根据实际场景自行定义，但应该尽量采用英文字符，能比较准确的描述错误，
 *    看到消息代码时能够比较直观的理解所代表的错误类型。
 *
 * @Version: 2.0
 * @Author: 刘志斌
 * @Email: yudi@sina.com
 */
public class ServiceResult<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3765720967319047788L;

    private T                 result;
    private PagerInfo         pagerInfo;
    private boolean           success          = true;
    private String            message          = "";
    private String            code             = "";

    /**
     * 服务是否执行成功。
     * @return
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * 服务是否执行成功。
     * @param isSuccess
     */
    public void setSuccess(boolean isSuccess) {
        this.success = isSuccess;
    }

    /**
     * 设置服务执行结果。
     * @param result
     */
    public void setResult(T result) {
        this.result = result;
    }

    /**
     * 服务执行过程发生异常时设置错误信息。
     * 该方法会将【success-服务是否执行成功】设置为false。
     * @param code 返回给客户端的消息代码。
     * @param message 返回给客户端的消息描述。
     */
    public void setError(String code, String message) {
        this.code = code;
        this.message = message;
        this.success = false;
    }

    /**
     * 返回给客户端的消息代码。
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取服务执行结果。
     * @return
     */
    public T getResult() {
        return result;
    }

    /**
     * 获取服务返回的描述消息。
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置服务返回的描述消息。
     * @param value
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * 获取分页信息。
     * <p>只有某些使用分页查询列表的方法才会返回分页信息，除此之外均返回null。具体参考各服务方法说明。
     * @return
     */
    public PagerInfo getPager() {
        return this.pagerInfo;
    }

    /**
     * 设置分页信息。
     * @param pager
     */
    public void setPager(PagerInfo pager) {
        this.pagerInfo = pager;
    }

    @Override
    public String toString() {
        return "[result=" + result + ", message=" + message + ", success=" + success + ", code="
                + code + "]";
    }

}
