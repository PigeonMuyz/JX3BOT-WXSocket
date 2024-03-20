package io.github.pigeonmuyz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pigeonmuyz.entity.MessObject;
import io.github.pigeonmuyz.helper.MessFilter;
import io.github.pigeonmuyz.tools.HttpTool;
import io.github.pigeonmuyz.tools.ManualTimer;
import io.github.pigeonmuyz.websocket.SocketServer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);
    public static Properties configProperties = new Properties();
    public static List<MessObject> personal;
    private static final String JSON_FILE_PATH = "./personal.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try{
            InputStream in = Main.class.getClassLoader().getResourceAsStream("config.properties");
            configProperties.load(in);
            HttpTool.getComputerId();
            personal = readFromJsonFile(JSON_FILE_PATH);

            Runnable task = () -> writeToJsonFile(JSON_FILE_PATH, personal);
            ManualTimer timer = new ManualTimer(task, JSON_FILE_PATH);
            timer.start(20, 60);
            SocketServer s = new SocketServer(Integer.parseInt(configProperties.getProperty("config.wssport")));
            s.start();
            MessFilter.initLanguageFilter();
            log.info("程序加载成功");
        }catch (Exception e){
            log.error("主程序运行错误，错误信息：\n");
            e.printStackTrace();
        }
    }
    private static List<MessObject> readFromJsonFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                log.info("配置文件读取成功");
                return mapper.readValue(file, new TypeReference<List<MessObject>>() {});
            } catch (IOException e) {
                log.error("配置文件读取失败");
                e.printStackTrace();
            }
        }
        return new ArrayList<MessObject>();
    }

    private static void writeToJsonFile(String filePath, List<MessObject> data) {
        try {
            mapper.writeValue(new File(filePath), data);
            log.debug("配置文件写入成功");
        } catch (IOException e) {
            log.error("配置文件写入失败");
            e.printStackTrace();
        }
    }
}