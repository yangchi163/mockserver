package com.server.util;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlUtil {
    public static Map read(String filePath){
        Map map = null;
        Yaml yaml = new Yaml();
        File file = new File(filePath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            map = yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(filePath);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
