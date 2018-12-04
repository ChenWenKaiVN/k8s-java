package com.kubernetes.k8s;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

    private static final ThreadLocal<HttpClient> clients = new ThreadLocal<HttpClient>();

    public static String get(String url, Map<String, String> param,
            Map<String, String> headers) {
        HttpClient httpClient = getClient();
        // HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = null;
        get = new HttpGet(url);
        appendHeaders(get, headers);
        appendParams(get, param);     
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                return EntityUtils.toString(entity);
            } else {
                httpResponse.getEntity().getContent().close();
                //System.out.println("return status code:" + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long e = System.currentTimeMillis();
        return null;
    }

    private static HttpClient getClient() {
//        HttpClient hc = clients.get();
//        if (hc == null) {
//            hc = ThreadSafeHttpclientGetter.getNewInstance(1000, 5000, 40000);
//            clients.set(hc);
//        }
//        return hc;
    	return createSSLClientDefault();
    }
    
    private static CloseableHttpClient createSSLClientDefault() {
        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                @Override
                public boolean isTrusted(X509Certificate[] xcs, String string){
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);            
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyStoreException ex) {
            Logger.getLogger(HttpUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HttpUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(HttpUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return HttpClients.createDefault();
    }

    private static void appendHeaders(HttpRequestBase get,
            Map<String, String> headers) {
        if (headers != null) {
            for (Entry<String, String> e : headers.entrySet()) {
                get.addHeader(e.getKey(), e.getValue());
            }
        }
    }

    private static void appendParams(HttpRequestBase get,
            Map<String, String> params) {
        if (params != null) {
            HttpParams hp = new BasicHttpParams();
            for (Entry<String, String> e : params.entrySet()) {
            	//System.out.println();
                hp.setParameter(e.getKey(), e.getValue());
            }
            get.setParams(hp);
        }
    }

    public static String doPost(String url, Map<String, String> param,
            Map<String, String> headers, String body) {
        long s = System.currentTimeMillis();
        HttpClient httpClient = getClient();// ThreadSafeHttpclientGetter.getHttpClient();
        // HttpClient httpClient = new DefaultHttpClient();
        BufferedReader in = null;
        HttpPost post = null;
        post = new HttpPost(url);
        appendHeaders(post, headers);
        appendParams(post, param);
        post.setHeader("Content-Type", "application/json;charset=UTF-8");     
        if (body != null) {
            post.setEntity(new StringEntity(body, HTTP.UTF_8)); // 设置字符集的状�?�，不设置会出现乱码
        }
        // 添加要传递的参数 NameValuePair
        // 使用HttpPost对象来设置UrlEncodedFormEntity的Entity
        try {
            HttpResponse response = null;
            //System.out.println("post==="+post+post.getParams().getParameter("name"));
            //post.getParams().getParameter("name");
            response = httpClient.execute(post);
            //response.setCharacterEncoding("UTF-8");
			//response.setContentType("application/json;charset=UTF-8");
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer string = new StringBuffer("");
            String lineStr = "";
            while ((lineStr = in.readLine()) != null) {
                string.append(lineStr + "\n");
            }
            String resultStr = string.toString();
            System.out.println(resultStr);
            return resultStr;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
