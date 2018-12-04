package com.kubernetes.k8s;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class ThreadSafeHttpclientGetter {

    private static final String CHARSET = HTTP.UTF_8;
    private static HttpClient customerHttpClient;

    private ThreadSafeHttpclientGetter() {
    }

    /**
     * 线程安全的HttpClient实例 创建
     * 
     * @param poolTimeout
     *            从连接池中取连接的超时时�?
     * @param connectionTimeout
     *            连接超时
     * @param requestTimeout
     *            请求超时
     * @return
     */
    public static synchronized HttpClient getNewInstance(int poolTimeout,
            int connectionTimeout, int requestTimeout) {
        HttpClient customerHttpClient;
        HttpParams params = new BasicHttpParams();
        // 设置�?些基本参�?
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpProtocolParams
                .setUserAgent(
                        params,
                        "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        // 超时设置
        /* 从连接池中取连接的超时时�? */
        ConnManagerParams.setTimeout(params, poolTimeout);
        /* 连接超时 */
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        /* 请求超时 */
        HttpConnectionParams.setSoTimeout(params, requestTimeout);

        // 设置我们的HttpClient支持HTTP和HTTPS两种模式
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        schReg.register(new Scheme("https",
                SSLSocketFactory.getSocketFactory(), 443));
        // 使用线程安全的连接管理来创建HttpClient
        ClientConnectionManager conMgr = new SingleClientConnManager(params,
                schReg);
        customerHttpClient = new DefaultHttpClient(conMgr, params);
        return customerHttpClient;
    }

    /**
     * 线程安全的HttpClient实例 创建
     * 
     * @param poolTimeout
     *            从连接池中取连接的超时时�?
     * @param connectionTimeout
     *            连接超时
     * @param requestTimeout
     *            请求超时
     * @return
     */
    public static synchronized HttpClient getNewInstanceThreadSafe(
            int poolTimeout, int connectionTimeout, int requestTimeout) {
        HttpClient customerHttpClient;
        HttpParams params = new BasicHttpParams();
        // 设置�?些基本参�?
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpProtocolParams
                .setUserAgent(
                        params,
                        "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        // 超时设置
        /* 从连接池中取连接的超时时�? */
        ConnManagerParams.setTimeout(params, poolTimeout);
        /* 连接超时 */
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        /* 请求超时 */
        HttpConnectionParams.setSoTimeout(params, requestTimeout);

        // 设置我们的HttpClient支持HTTP和HTTPS两种模式
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        schReg.register(new Scheme("https",
                SSLSocketFactory.getSocketFactory(), 443));
        // 使用线程安全的连接管理来创建HttpClient
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                params, schReg);
        customerHttpClient = new DefaultHttpClient(conMgr, params);
        return customerHttpClient;
    }

    /**
     * 线程安全的HttpClient实例 创建
     * 
     * @param poolTimeout
     *            从连接池中取连接的超时时�?
     * @param connectionTimeout
     *            连接超时
     * @param requestTimeout
     *            请求超时
     * @return
     */
    public static synchronized HttpClient getHttpClient(int poolTimeout,
            int connectionTimeout, int requestTimeout) {
        if (null == customerHttpClient) {
            customerHttpClient = getNewInstanceThreadSafe(poolTimeout,
                    connectionTimeout, requestTimeout);
        }
        return customerHttpClient;
    }

    /**
     * 线程安全的HttpClient实例 创建 poolTimeout = 2000ms
     * 
     * @param connectionTimeout
     *            = 10000 ms
     * @param requestTimeout
     *            = 40000 ms
     * @return
     * @return
     */
    public static synchronized HttpClient getHttpClient() {
        return getHttpClient(5000, 10000, 40000);
    }
}
