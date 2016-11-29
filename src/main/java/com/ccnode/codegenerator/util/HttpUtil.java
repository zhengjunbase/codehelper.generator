package com.ccnode.codegenerator.util;
import com.ccnode.codegenerator.pojo.RequestData;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public class HttpUtil {
    private static final Logger logger = LoggerWrapper.getLogger(HttpUtil.class);

    private static final int CON_TIMEOUT = 1000;
    private static final int SO_TIMEOUT = 1000;
    private static final int POOL_TIMECOUT = 1000;
    private static final int HTTP_RETRY_COUNT = 1;
    private static final String NOTICELINE = "--------------------------------------------";
    private static CloseableHttpClient httpclient;
    public static ContentType TEXT_XML_UTF8 = ContentType.create("text/xml", Consts.UTF_8);
    public static ContentType APPLICATION_JSON_UTF8 = ContentType.create("application/json", Consts.UTF_8);

    static {
        init();
    }

    public static void init() throws RuntimeException {
        try {
            logger.warn(NOTICELINE + " httpUtil init begin " + NOTICELINE);
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
//            sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslContextBuilder.loadTrustMaterial(null,new TrustAnyTrustManager());
            SSLConnectionSocketFactory sslConnectionSocketFactory =
                    new SSLConnectionSocketFactory(
                            sslContextBuilder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().
                    register("http", new PlainConnectionSocketFactory()).
                    register("https", sslConnectionSocketFactory).
                    build();


            logger.warn(NOTICELINE + " SSL context init done " + NOTICELINE);

            //init connectionManager , ThreadSafe pooled conMgr
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
            poolingHttpClientConnectionManager.setMaxTotal(30);
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(3);
            //init request config. pooltimeout,sotime,contimeout
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(POOL_TIMECOUT).setConnectTimeout(CON_TIMEOUT).setSocketTimeout(SO_TIMEOUT).build();
            // begin construct httpclient
            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
            httpClientBuilder.setDefaultRequestConfig(requestConfig);
            httpClientBuilder.setRetryHandler(new HttpRequestRetryHandler() {
                @Override
                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    if (executionCount >= HTTP_RETRY_COUNT) {
                        return false;
                    }
                    if (exception instanceof InterruptedIOException) {
                        // Timeout
                        logger.warn("httpUtil retry for InterruptIOException");
                        return true;
                    }
                    if (exception instanceof UnknownHostException) {
                        // Unknown host
                        return false;
                    }
                    if (exception instanceof SSLException) {
                        // SSL handshake exception
                        return false;
                    }
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                    if (idempotent) {
                        // Retry if the request is considered idempotent
                        logger.warn("httpUtil retry for idempotent");
                        return true;
                    }
                    return false;
                }
            });
            logger.warn(NOTICELINE + " poolManager , requestconfig init done " + NOTICELINE);

            httpclient = httpClientBuilder.build();
            logger.warn(NOTICELINE + " httpUtil init done " + NOTICELINE);
        } catch (Exception e) {
            logger.error(NOTICELINE + "httpclient init fail" + NOTICELINE, e);
            throw new RuntimeException(e);
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager, TrustStrategy {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{};}

        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {return true;}
    }

    public static CloseableHttpClient getHttpclient() {
        if (null == httpclient) {
            init();
        }
        return httpclient;
    }

    public static String postJson(String url, String body) {
        return postJsonWithResult(url, body).result;
    }

    public static String postJson(String url, Object body) {
        RequestData data = new RequestData();
        data.setData(JSONUtil.toJSONString(body));
        return postJsonWithResult(url, JSONUtil.toJSONString(data)).result;
    }

    public static String postJsonEncrypt(String url, Object body) {
        RequestData data = new RequestData();
        data.setData(SecurityHelper.encrypt(JSONUtil.toJSONString(body)));
        return postJsonWithResult(url, JSONUtil.toJSONString(data)).result;
    }



    public static HttpResult postJsonWithResult(String url, String body) {
        HttpPost httpPost = null;
        long start = System.currentTimeMillis();
        int code=0;
        try {
            httpPost = new HttpPost(completeUrl(url));
            logger.info("request is {}  url is {}", body, url);
            HttpEntity httpEntity = new StringEntity(body, APPLICATION_JSON_UTF8);
            httpPost.setEntity(httpEntity);
            httpPost.setHeader("Accept", "application/json");
            CloseableHttpResponse response = httpclient.execute(httpPost);
            code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity, Consts.UTF_8);
                logger.info("url is {} response is {}", url, result);
                return new HttpResult(code, result);
            }
            logger.error("http postJsonWithWrapperReuslt response status not 200 {}, url {}, body {}", code, url, body);
        } catch (IOException e) {
            logger.error("httpUitl4 push fail {} {} ", url, body, e);
        } finally {
            long cost = System.currentTimeMillis() - start;
            logger.info("httpUtil4_postJson {}", cost);
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
        }
        return new HttpResult(code, "");
    }

    private static String completeUrl(String oldUrl){
        if(StringUtils.isBlank(oldUrl)){
            return StringUtils.EMPTY;
        }
        if(!oldUrl.startsWith("http")){
            oldUrl = "http://" + oldUrl;
        }
        return oldUrl;

    }

    /**
     * 发送xml的post请求
     *
     * @param url
     * @param xml
     * @param headers
     * @return
     */
    @Nullable
    public static String postXML(String url, String xml, Header... headers) {
        CloseableHttpClient client = getHttpclient();
        HttpPost httpPost = new HttpPost(url);
        long start = System.currentTimeMillis();
        try {
            HttpEntity httpEntity = new StringEntity(xml, HttpUtil.TEXT_XML_UTF8);
            if (headers != null && headers.length > 0) {
                for (Header header : headers) {
                    httpPost.addHeader(header);
                }
            }
            httpPost.setEntity(httpEntity);
            CloseableHttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity, Consts.UTF_8);
                return result;
            }
        } catch (Exception e) {
            logger.error("push xml fail,url:{}", url, e);
        } finally {
            long cost = System.currentTimeMillis() - start;
            logger.info("httpUtil_postXml {}", cost);
        }
        return null;
    }

    /**
     * 发送xml的post请求 指定contentType 和Charset
     *
     * @param url
     * @param xml
     * @param headers
     * @return
     */
    @Nullable
    public static String postXML(String url, String xml, ContentType contentType,final Charset charset,  Header... headers) {
        CloseableHttpClient client = getHttpclient();
        HttpPost httpPost = new HttpPost(url);
        long start = System.currentTimeMillis();
        try {
            HttpEntity httpEntity = new StringEntity(xml,contentType);
            if (headers != null && headers.length > 0) {
                for (Header header : headers) {
                    httpPost.addHeader(header);
                }
            }
            httpPost.setEntity(httpEntity);
            CloseableHttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                return EntityUtils.toString(responseEntity, charset);
            }
        } catch (Exception e) {
            logger.error("push xml fail,url:{}", url, e);
        } finally {
            long cost = System.currentTimeMillis() - start;
            logger.info("httpUtil_postXml {}", cost);
            httpPost.releaseConnection();
        }
        return null;
    }
    public static class HttpResult {
        public int code;
        public String result;

        public HttpResult(int code, String result) {
            this.code = code;
            this.result = result;
        }
    }
}
