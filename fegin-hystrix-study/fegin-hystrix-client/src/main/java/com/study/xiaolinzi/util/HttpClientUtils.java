//package com.study.xiaolinzi.util;
//
//import com.epay.common.vip.exception.BizException;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ConnectionKeepAliveStrategy;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//public class HttpClientUtils {
//    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
//
//    private static final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();
//
//    private static CloseableHttpClient httpClient = null;
//
//	public static synchronized CloseableHttpClient getHttpClient() {
//		if (httpClient == null) {
//            // http请求
//            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//            cm.setDefaultMaxPerRoute(800);// 设置每个路由基础的连接
//            cm.setMaxTotal(1000);//设置最大连接数//cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);// 设置目标主机的最大连接数
//
//            RequestConfig defaultRequestConfig = RequestConfig.custom()
//				    .setSocketTimeout(30000)
//				    .setConnectTimeout(30000)
//				    .setConnectionRequestTimeout(5000)
//				    .setStaleConnectionCheckEnabled(true)
//				    .build();
//            ConnectionKeepAliveStrategy connectionKeepAliveStrategy = (httpResponse, httpContext) -> {
//                return 8 * 1000; // tomcat默认keepAliveTimeout为20s
//            };
//            httpClient = HttpClients.custom().setConnectionManager(cm)
//				    .setDefaultRequestConfig(defaultRequestConfig)
//                    .setKeepAliveStrategy(connectionKeepAliveStrategy) //连接超时时间
//                    .evictIdleConnections(5, TimeUnit.SECONDS) //每五秒清理一下过期连接
//				    .build();
////            httpClient = HttpClients.createDefault();
//		}
//		return httpClient;
//	}
//
//    /**
//     * 发送content-Type为 multipart/form 的post请求
//     */
//    public static String sendMultipartPost(String url, Map<String, String> paramStrMap, Map<String, File> paramFileMap) {
////        CloseableHttpClient httpClient = null;
//        CloseableHttpResponse httpResponse = null;
//        HttpPost httpPost = null;
//        try {
//            httpPost = new HttpPost(url);
//            httpPost.setConfig(requestConfig);
//
//            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//            for (String key : paramStrMap.keySet()) {
//                multipartEntityBuilder.addPart(key, new StringBody(paramStrMap.get(key), ContentType.TEXT_PLAIN));
//            }
//            for (String key : paramFileMap.keySet()) {
//                multipartEntityBuilder.addPart(key, new FileBody(paramFileMap.get(key)));
//            }
//            HttpEntity httpEntity = multipartEntityBuilder.build();
//            httpPost.setEntity(httpEntity);
//
//            // 执行请求
////            httpClient = HttpClients.createDefault();
////            httpResponse = httpClient.execute(httpPost);
//            httpResponse = getHttpClient().execute(httpPost);
//            HttpEntity entity = httpResponse.getEntity();
//            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
//                String line = reader.readLine();
//                return line;
//            } else {
//                String result = EntityUtils.toString(entity, "UTF-8");
//                logger.error("http请求异常[{}]", result);
//                throw new BizException("系统异常");
//            }
//        } catch (BizException e) {
//            e.printStackTrace();
//            logger.error("http请求异常[{}]", e.getMessage());
//            throw new BizException("系统异常");
//        } catch (Exception e) {
//        	logger.error("http请求异常[{}]", e.getMessage());
//            e.printStackTrace();
//            throw new BizException("系统异常");
//        } finally {
//        	// 关闭连接,释放资源
//        	if (httpPost != null) {
//	            try {
//	            	httpPost.abort();
//	            } catch (Exception e) {
//                    logger.warn("关闭HTTP响应流失败：" + e.getMessage(), e);
//	                e.printStackTrace();
//	            }
//        	}
//
//        	// 关闭连接,释放资源
//        	if (httpResponse != null) {
//	            try {
//                	httpResponse.getEntity().getContent().close();
//                    httpResponse.close();
//	//                if (httpClient != null) {
//	//                    httpClient.close();
//	//                }
//	            } catch (IOException e) {
//                    logger.warn("关闭HTTP响应流失败：" + e.getMessage(), e);
//	                e.printStackTrace();
//	            }
//        	}
//        }
//    }
//
//    /**
//     * @param url
//     * @param map 注意这里发送的数据不是json格式，而是key1=value&&key2=value2
//     * @return
//     * @throws Exception
//     */
//    public static String URLPost(String url, Map<String, String> map) throws Exception {
//        URL sendurl = new URL(url);
//        HttpURLConnection httpURLConnection = (HttpURLConnection) sendurl.openConnection();
//        httpURLConnection.setRequestMethod("POST");
//        httpURLConnection.setDoInput(true);
//        httpURLConnection.setDoOutput(true);
//        httpURLConnection.setUseCaches(false);
//        httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//        httpURLConnection.setConnectTimeout(30000);
//        httpURLConnection.setReadTimeout(30000);
//        StringBuilder sb = new StringBuilder();
//        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, String> pairs = it.next();
//            if (pairs.getValue() == null || pairs.getValue().equals("")) {
//                continue;
//            }
//            sb.append(pairs.getKey()).append("=").append(URLEncoder.encode(pairs.getValue().toString(), "UTF-8")).append("&");
//        }
//        if (sb.length() > 0) {
//            sb.setLength(sb.length() - 1);
//        }
//        OutputStream out = null;
//        try {
//            out = httpURLConnection.getOutputStream();
//            out.write(sb.toString().getBytes("UTF-8"));
//            out.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            out.close();
//        }
//
//        StringBuffer result = new StringBuffer();
//        InputStream in = null;
//        BufferedReader reader = null;
//        try {
//            in = httpURLConnection.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//            while (true) {
//                String line = reader.readLine();
//                if (line == null) {
//                    break;
//                } else {
//                    result.append(line + "\n");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            reader.close();
//            in.close();
//        }
//        return result.toString();
//    }
//
//    /**
//     * @param url   请求的地址
//     * @param param 这里是json格式的字符串
//     * @return
//     */
//    public static String restPost(String url, String param) {
//        PrintWriter out = null;
//        InputStream is = null;
//        BufferedReader br = null;
//        String result = "";
//        HttpURLConnection conn = null;
//        StringBuffer strBuffer = new StringBuffer();
//        try {
//            URL realUrl = new URL(url);
//            conn = (HttpURLConnection) realUrl.openConnection();
//            // 设置通用的请求属性
//            conn.setRequestMethod("POST");
//            conn.setConnectTimeout(20000);
//            conn.setReadTimeout(300000);
//            conn.setRequestProperty("Charset", "UTF-8");
//            // 传输数据为json，如果为其他格式可以进行修改
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Content-Encoding", "utf-8");
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // 发送请求参数
//            out.print(param);
//            // flush输出流的缓冲
//            out.flush();
//            is = conn.getInputStream();
//            br = new BufferedReader(new InputStreamReader(is));
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                strBuffer.append(line);
//            }
//            result = strBuffer.toString();
//        } catch (Exception e) {
//            logger.error("发送 POST 请求出现异常！", e);
//
//        }
//        // 使用finally块来关闭输出流、输入流
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (br != null) {
//                    br.close();
//                }
//                if (conn != null) {
//                    conn.disconnect();
//                }
//            } catch (IOException ex) {
//                logger.error("http异常", ex);
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 从网络Url中下载文件
//     * @param urlStr
//     * @param fileName
//     * @param savePath
//     * @throws IOException
//     */
//    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
//        URL url = new URL(urlStr);
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        //设置超时间为3秒
//        conn.setConnectTimeout(3*1000);
//        //防止屏蔽程序抓取而返回403错误
//        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//
//        //得到输入流
//        InputStream inputStream = conn.getInputStream();
//        //获取自己数组
//        byte[] getData = readInputStream(inputStream);
//
//        //文件保存位置
//        File saveDir = new File(savePath);
//        if(!saveDir.exists()){
//            saveDir.mkdir();
//        }
//        File file = new File(saveDir+File.separator+fileName);
//        FileOutputStream fos = new FileOutputStream(file);
//        fos.write(getData);
//        if(fos!=null){
//            fos.close();
//        }
//        if(inputStream!=null){
//            inputStream.close();
//        }
//    }
//
//    /**
//     * 从输入流中获取字节数组
//     * @param inputStream
//     * @return
//     * @throws IOException
//     */
//    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
//        byte[] buffer = new byte[1024];
//        int len = 0;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        while((len = inputStream.read(buffer)) != -1) {
//            bos.write(buffer, 0, len);
//        }
//        bos.close();
//        return bos.toByteArray();
//    }
//}
