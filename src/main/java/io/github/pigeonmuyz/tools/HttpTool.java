package io.github.pigeonmuyz.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pigeonmuyz.Main;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 网络请求工具类
 */
public class HttpTool {
    private static final Logger log = LogManager.getLogger(HttpTool.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();
    private static String computerId = "";
    public static Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("computerId",computerId)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("computerId",computerId)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    /**
     * 获取computerId需要初始化执行
     */
    public static void getComputerId() throws IOException{
        Request request = new Request.Builder()
                .url("https://searchplugin.csdn.net/api/v1/ip/get?ip=")
                .build();
        Response response = client.newCall(request).execute();
        JsonNode jn = new ObjectMapper().readTree(response.body().string());
        if (jn.get("data") != null){
            log.debug("IP获取接口地址请求成功"+jn);
            if (jn.get("data").get("ip") != null){
                log.debug("IP地址请求成功："+jn.get("data").get("ip").asText());
                String appName = Main.configProperties.getProperty("config.AppName");
                log.debug("获取到配置文件中参数："+appName);
                computerId = Base64.getEncoder().encodeToString((jn.get("data").get("ip").asText()+"-"+appName).getBytes());
                log.info("当前computerId如下："+computerId);
            }else{
                log.error("无法正常解析ip："+jn);
            }
        }else {
            log.error("IP获取接口地址请求失败"+jn);
        }
    }

    public static Response getDefault(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static List<Object> json(){
        return null;
    }

    public static String getData(String url) throws IOException {
        log.debug("GET请求地址："+url);
        return get(url).body().string();
    }

    public static String postData(String url,String json) throws IOException {
        log.debug("POST请求地址："+url);
        log.debug("POST请求数据："+json);
        String returnResult = post(url, json).body().string();
//        System.out.println(returnResult);
        return returnResult;
    }
}
