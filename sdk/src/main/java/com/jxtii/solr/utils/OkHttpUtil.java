package com.jxtii.solr.utils;

import com.jxtii.solr.entity.QueryCondition;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * 网络请求工具类
 * @date 17/7/8.
 * @author guolf
 */
public class OkHttpUtil {

    private static Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * post请求
     * @param url 请求地址
     * @param json 请求参数
     * @return 成功返回OK
     * @throws IOException
     */
    public static String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        return response.isSuccessful() ? "OK" : response.body().string();
    }

    /**
     * GET请求
     * @param url 请求地址
     * @param condition 请求条件
     * @return
     * @throws IOException
     */
    public static String get(String url, final QueryCondition condition) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl.Builder builder = originalHttpUrl.newBuilder()
                        .addQueryParameter("show", condition.getShow())
                        .addQueryParameter("from", condition.getFrom())
                        .addQueryParameter("keywords", escapeQueryChars(condition.getKeywords()));

                if (condition.getRows() > 0) {
                    builder.addQueryParameter("rows", condition.getStart() + "");

                }
                if (condition.getStart() > 0) {
                    builder.addQueryParameter("start", condition.getRows() + "");
                }
                HttpUrl url = builder.build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    public static String escapeQueryChars(String s) {
        try {
            if (!StringUtils.isEmpty(s)) {
                return URLEncoder.encode(s, "UTF-8");
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return "";
    }


}
