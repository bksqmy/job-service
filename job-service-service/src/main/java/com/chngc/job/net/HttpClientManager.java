package com.chngc.job.net;

import com.chngc.job.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * http访问公共类
 *
 * @FileName: HttpClientManager
 * @Version: 1.0
 * @Author: yanpeng
 * @Date: 2015/8/31 14:49
 */
public class HttpClientManager {
    private static PoolingClientConnectionManager cm = null;

    static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        cm = new PoolingClientConnectionManager(schemeRegistry);
        try {
            int maxTotal = 800;
            cm.setMaxTotal(maxTotal);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // 每条通道的并发连接数设置（连接池）
        try {
            int defaultMaxConnection = 100;
            cm.setDefaultMaxPerRoute(defaultMaxConnection);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static HttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        // 连接超时
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000); // 3000ms
        // 等待数据超时
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        return new DefaultHttpClient(cm, params);
    }

    /**
     * 发起get请求,如果成功（200）则返回，反之抛出异常
     *
     * @param url 访问地址
     * @return
     */
    public static String httpGet(String url) {
        HttpClient client = getHttpClient();
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
            throw new RuntimeException(" response status error:"
                    + response.getStatusLine().getStatusCode() + " url:" + url);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("ClientProtocol exception url:" + url, e);
        } catch (IOException e) {
            throw new RuntimeException("IOException exception url:" + url, e);
        } catch (Exception e) {
            throw new RuntimeException("Exception url:" + url, e);
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    /**
     * 发起post请求,如果成功（200）则返回，反之抛出异常
     *
     * @param url      请求url
     * @param paramMap 请求参数，key为空则跳过
     * @return
     */
    public static String httpPost(String url, Map<String, String> paramMap) {
        HttpClient client = getHttpClient();
        HttpPost httpost = null;
        try {
            httpost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            if (paramMap == null || paramMap.keySet() == null || paramMap.keySet().size() == 0) {
                // donothing
            } else {
                Iterator<Map.Entry<String, String>> iter = paramMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (StringUtil.isEmptyOrNull(key)) {
                        continue;
                    }
                    nvps.add(new BasicNameValuePair(key, value));
                }
            }

            httpost.setEntity(new UrlEncodedFormEntity(nvps));

            HttpResponse response = client.execute(httpost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
            throw new RuntimeException(" response status error:"
                    + response.getStatusLine().getStatusCode() + " url:" + url);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("ClientProtocol exception url:" + url, e);
        } catch (IOException e) {
            throw new RuntimeException("IOException exception url:" + url, e);
        } catch (Exception e) {
            throw new RuntimeException("Exception url:" + url, e);
        } finally {
            if (httpost != null) {
                httpost.releaseConnection();
            }
        }
    }



    /**
     * Post请求
     * @param url
     * @param params
     * @return
     * @throws Exception
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String post(String url, Map<String, String> params) {

        //HttpPost连接对象
        HttpPost httpRequest = new HttpPost(url);

        //使用NameValuePair来保存要传递的Post参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        //添加要传递的参数
        for(String key : params.keySet()){
            nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
        }

        //设置字符集
        HttpEntity httpentity = null;
        try {
            httpentity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("post接口编码异常", e);
        }

        //请求httpRequest
        httpRequest.setEntity(httpentity);

        //取得默认的HttpClient
        HttpClient httpclient = getHttpClient();

        //取得HttpResponse
        HttpResponse httpResponse;
        try {
            httpResponse = httpclient.execute(httpRequest);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("post接口Client代理异常", e);
        } catch (IOException e) {
            throw new RuntimeException("post接口网络异常", e);
        }

        //HttpStatus.SC_OK表示连接成功
        int code = httpResponse.getStatusLine().getStatusCode();
        if (code == HttpStatus.SC_OK){

            HttpEntity entity = httpResponse.getEntity();

            try {
                String result = EntityUtils.toString(entity,"UTF-8");
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
                return result;
            } catch (ParseException e) {
                throw new RuntimeException("post接口数据异常", e);
            } catch (IOException e) {
                throw new RuntimeException("post接口数据异常", e);
            }

        } else {
            throw new RuntimeException("post接口调用返回[" + code + "]错误");
        }
    }

    /**
     * Post请求
     * @param url
     * @param xml
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String postXML(String url, String xml) {

        //HttpPost连接对象
        HttpPost httpRequest = new HttpPost(url);

        StringEntity se = null;   //中文乱码在此解决
        try {
            se = new StringEntity(xml, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("编码问题", e);
        }

        httpRequest.setHeader("Content-Type", "text/xml;charset=UTF-8");
        httpRequest.setEntity(se);

        //取得默认的HttpClient
        HttpClient httpclient = getHttpClient();

        //取得HttpResponse
        HttpResponse httpResponse;
        try {
            httpResponse = httpclient.execute(httpRequest);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("post接口Client代理异常", e);
        } catch (IOException e) {
            throw new RuntimeException("post接口网络异常", e);
        }

        //HttpStatus.SC_OK表示连接成功
        int code = httpResponse.getStatusLine().getStatusCode();
        if (code == HttpStatus.SC_OK){

            HttpEntity entity = httpResponse.getEntity();
            try {
                String result = EntityUtils.toString(entity,"UTF-8");
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
                return result;
            } catch (ParseException e) {
                throw new RuntimeException("post接口数据异常", e);
            } catch (IOException e) {
                throw new RuntimeException("post接口数据异常", e);
            }
        } else {
            throw new RuntimeException("post接口调用返回[" + code + "]错误");
        }
    }



    public static String get(String url, Map<String, String> params) {

        //使用NameValuePair来保存要传递的Post参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        //添加要传递的参数
        for(String key : params.keySet()){
            nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
        }

        //对参数编码
        String param = URLEncodedUtils.format(nameValuePairs, "UTF-8");

        //将URL与参数拼接
        HttpGet httpRequest = new HttpGet(url + "?" + param);

        //取得默认的HttpClient
        HttpClient httpclient = getHttpClient();

        //取得HttpResponse
        HttpResponse httpResponse;
        try {
            httpResponse = httpclient.execute(httpRequest);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("get接口Client代理异常", e);
        } catch (IOException e) {
            throw new RuntimeException("get接口网络异常", e);
        }

        //HttpStatus.SC_OK表示连接成功
        int code = httpResponse.getStatusLine().getStatusCode();
        if (code == HttpStatus.SC_OK){

            HttpEntity entity = httpResponse.getEntity();

            try {
                String result = EntityUtils.toString(entity,"UTF-8");
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
                return result;
            } catch (ParseException e) {
                throw new RuntimeException("get接口数据异常", e);
            } catch (IOException e) {
                throw new RuntimeException("get接口数据异常", e);
            }
        } else {
            throw new RuntimeException("get接口调用返回[" + code + "]错误");
        }
    }



    /**
     * 下载文件
     * @param url
     * @param params
     * @param filepath
     */
    public static String download(String url, Map<String, String> params, String filepath, String filename) {

        //使用NameValuePair来保存要传递的Post参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        //添加要传递的参数
        for(String key : params.keySet()){
            nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
        }

        //对参数编码
        String param = URLEncodedUtils.format(nameValuePairs, "UTF-8");

        try{
            HttpClient client = getHttpClient();
            HttpGet httpget = new HttpGet(url + "?" + param);
            HttpResponse httpResponse = client.execute(httpget);

            //HttpStatus.SC_OK表示连接成功
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK){
                Header contentHeader = httpResponse.getFirstHeader("Content-Disposition");
                String downloadFilename = null;
                String downloadFiletype = null;
                if (contentHeader != null) {
                    HeaderElement[] values = contentHeader.getElements();
                    if (values.length == 1) {
                        NameValuePair nvp = values[0].getParameterByName("filename");
                        if (nvp != null) {
                            downloadFilename = nvp.getValue();
                            downloadFiletype = downloadFilename.substring(downloadFilename.lastIndexOf("."));
                        }
                    }
                }

                if (downloadFilename != null && downloadFiletype != null){
                    try {
                        FileUtils.copyInputStreamToFile(httpResponse.getEntity().getContent(), new File(filepath, filename + downloadFiletype));
                        return filename + downloadFiletype;
                    } catch (IllegalStateException e) {
                        throw new RuntimeException("下载文件网络异常", e);
                    } catch (IOException e) {
                        throw new RuntimeException("下载文件网路异常", e);
                    }
                } else {
                    throw new RuntimeException("下载文件数据异常");
                }
            } else {
                throw new RuntimeException("下载接口调用返回[" + code + "]错误");
            }

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("下载文件数据异常");
        }

    }

    /**
     *查询民航物流
     * @param urlStr
     * @param paramMap
     * @return
     */
    public static String httpPostCAE(String urlStr, Map<String, Object> paramMap) {
        String result = "";
        try {
            HttpClient httpclient = getHttpClient();
            HttpPost httpost = new HttpPost(urlStr);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("LoginKey", "JB15071100954CAE006"));
            for (String key : paramMap.keySet()) {
                nvps.add(new BasicNameValuePair("JsonText", paramMap.get(key)
                        .toString()));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps,
                    "UTF-8");
            httpost.setEntity(formEntity);
            HttpResponse response = httpclient.execute(httpost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }
}
