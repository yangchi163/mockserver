package com.server;

import com.google.gson.Gson;
import com.server.model.HttpEntity;
import com.server.parse.ParseData;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

public class DoMain {
    public static Map<String,HttpEntity> resultMap;
    public static int port;

    public static void main(String[] arg) throws Exception {
        //生成resultMap
        String filePath = System.getProperty("user.dir") + File.separator +"src" + File.separator + "main"
                + File.separator + "resources" + File.separator + "data.yaml";
        ParseData parseData = new ParseData();
        resultMap = parseData.getResultMap(filePath);
        port = parseData.getPort();
        //启动服务
        domain(port);
    }

    /**
     * 启动服务
     * @throws Exception
     */
    public static void domain(int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new TestHandler());
        server.start();
        System.out.println("the service is started at " + port);
    }


    static  class TestHandler implements HttpHandler {
        public void handle(final HttpExchange exchange){
            new Thread(new Runnable() {
                public void run() {
                    //获取请求方法
                    String method = exchange.getRequestMethod();
                    //获取请求path
                    URI uri = exchange.getRequestURI();
                    String path = uri.getPath();
                    System.out.println(method+ "    " + path);
                    //声明要返回的数据
                    String response = "the path does not exsit";
                    int statusCode = 404;
                    Headers headers = exchange.getResponseHeaders();
                    OutputStream os = null;
                    //根据method和path定位请求
                    for (HttpEntity entity : resultMap.values()){
                        if (method.equalsIgnoreCase(entity.getMethod()) && path.equalsIgnoreCase(entity.getPath())){
                            //获取response,并转成string
                            Object responseFromEntity = entity.getResponseBody();
                            Gson gson = new Gson();
                            response = gson.toJson(responseFromEntity);
                            //获取statuscode
                            statusCode = entity.getStatusCode();
                            //获取heades
                            Map headersFromEntity = entity.getResponseHeaders();
                            if (headersFromEntity != null){
                                for (Object key : headersFromEntity.keySet()){
                                    headers.add((String) key, (String) headersFromEntity.get(key));
                                }
                            }
                        }
                    }
                    try {
                        os = exchange.getResponseBody();
                        exchange.sendResponseHeaders(statusCode, 0);
                        os.write(response.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}
