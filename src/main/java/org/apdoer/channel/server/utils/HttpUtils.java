package org.apdoer.channel.server.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apdoer.channel.server.model.vo.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    protected static final CloseableHttpClient httpClient;
    protected static final int maxTotal = 200;
    protected static final int defaultMaxPerRoute = 50;
    protected static final int connectTimeout = 60;

    protected static final int socketTimeout = 60;

    protected static final int connectionRequestTimeout = 500;

    protected static final int timeToLive = 540;
    protected String encoding = "utf-8";

    static {
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial((chain, authType) -> true).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry, null, null, null, timeToLive * 1000,
                    TimeUnit.MILLISECONDS);
            cm.setMaxTotal(maxTotal);
            cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout * 1000).setConnectTimeout(connectTimeout * 1000)
                    .setConnectionRequestTimeout(connectionRequestTimeout).build();
            httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(cm).build();
        } catch (Exception e) {
            logger.error("fail to initial httptransfer: ", e);
            throw new RuntimeException(e);
        }
    }

    public static String doGet(final String url, final Map<String, String> params) throws Exception {
        final StringBuilder query = new StringBuilder();
        if (params != null && params.size() > 0) {
            query.append("?");
            for (final Map.Entry<String, String> entry : params.entrySet()) {
                if (!StringUtils.isBlank(entry.getValue())) {
                    query.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                }
            }
            query.deleteCharAt(query.length() - 1);
        }
        final HttpGet httpGet = new HttpGet(url + query.toString());
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpResponse response = httpClient.execute(httpGet);
            final HttpEntity entity = response.getEntity();
            return IOUtils.toString(entity.getContent(), "UTF-8");
        } catch (final Exception e) {
            logger.error("HttpUtils.doGet() error: url=" + url + ", params=" + params, e);
            throw e;
        } finally {
            httpClient.close();
        }
    }

    public static String doPost(final String url, final Map<String, String> params) throws Exception {
        final List<NameValuePair> list = new ArrayList<>();
        if (params != null) {
            for (final Map.Entry<String, String> entry : params.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpResponse response = httpClient.execute(httpPost);
            final HttpEntity entity = response.getEntity();

            return IOUtils.toString(entity.getContent(), "UTF-8");
        } catch (final Exception e) {
            logger.error("HttpUtils.doPost() error: url=" + url + ", params=" + params, e);
            throw e;
        } finally {
            httpClient.close();
        }
    }

    public static HttpResult doPost(String url, String data, ContentType contentTtype) throws IOException {
        HttpPost post = new HttpPost(url);
        if (StringUtils.isNotEmpty(data)) {
            post.setEntity(new StringEntity(data, contentTtype));
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);
            return new HttpResult(String.valueOf(response.getStatusLine().getStatusCode()), EntityUtils.toString(response.getEntity(), contentTtype.getCharset()));
        } finally {
            if (response != null) {
                response.close();
            }
            if (post != null) {
                post.releaseConnection();
            }
        }
    }

    /**
     * http post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String post(String url, String params, String CHARSET) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity sEntity = new StringEntity(params, CHARSET);
        httpPost.setEntity(sEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, CHARSET);
            }
        } finally {
            response.close();
            httpClient.close();
        }
        return null;
    }
}
