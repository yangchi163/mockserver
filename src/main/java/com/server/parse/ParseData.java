package com.server.parse;

import com.server.model.HttpEntity;
import com.server.util.YamlUtil;

import java.util.HashMap;
import java.util.Map;
import static com.server.KeyWords.*;

public class ParseData {
    private Map<String,HttpEntity> resultMap;

    public Map<String, HttpEntity> getResultMap(String filePath) throws Exception {
        parseData(filePath);
        return resultMap;
    }

    private void parseData(String filePath) throws Exception {
        resultMap = new HashMap<String, HttpEntity>();
        //从文件data.yaml读取数据
        Map dataFromFile = YamlUtil.read(filePath);
        for (Object keyIndataFromFile : dataFromFile.keySet()){
            Map base = (Map) getValueFromMap(dataFromFile,null,BASE);
            Map apis = (Map) getValueFromMap(dataFromFile,null,API);
            //模块层
            for (Object moduleKey : apis.keySet()){
                Map moduleInApis = (Map) apis.get(moduleKey);
                //api层
                for (Object apiKey : moduleInApis.keySet()){
                    String apiName = moduleKey + "." + apiKey;
                    Map httpDetailMap = (Map) moduleInApis.get(apiKey);
                    HttpEntity entity = getHttpEntity(apiName,base,httpDetailMap);
                    resultMap.put(apiName,entity);
                }
            }
        }
    }

    /**
     * 生成httpEntity
     * @param name httpentity的唯一标识:module.apiName
     * @param base data.yaml文件中的base节点的值
     * @param httpDetailMap data.yaml文件中的api.module.apiName节点的值
     * @return
     * @throws Exception
     */
    private HttpEntity getHttpEntity(String name,Map base,Map httpDetailMap) throws Exception {
        //获取base中request的map
        Map requsetInBase = (Map) base.get(REQUEST);
        //获取base中response的map
        Map responseInBase = null;
        if (base.containsKey(RESPONSE)){
            responseInBase = (Map) base.get(RESPONSE);
        }

        //获取httpDetailMap中request的map
        Map requestInHttpDetailMap = (Map) httpDetailMap.get(REQUEST);

        //组装httpEntity
        HttpEntity httpEntity = new HttpEntity();
        //设置name
        httpEntity.setName(name);
        //设置host
        httpEntity.setHost((String) getValueFromMap(requsetInBase,requestInHttpDetailMap,HOST));
        //设置path
        httpEntity.setPath((String) getValueFromMap(requsetInBase,requestInHttpDetailMap,PATH));
        //设置method,httpDetailMap.request中的method会覆盖base.request中的
        httpEntity.setMethod((String) getValueFromMap(requsetInBase,requestInHttpDetailMap,METHOD));
        //设置ISSUCCESS,httpDetailMap.request中的isSuccess会覆盖base.request中的
        Boolean isSuccess = (Boolean) getValueFromMap(requsetInBase,requestInHttpDetailMap,ISSUCCESS);
        httpEntity.setSuccess(isSuccess);
        //设置resqponse
        Map responseInHttpDetailMap ;
        if (isSuccess){
            responseInHttpDetailMap = (Map) getValueFromMap(null,httpDetailMap,SUCCESS);
        }else if (!isSuccess){
            responseInHttpDetailMap = (Map) getValueFromMap(null,httpDetailMap,FAIL);
        }else {
            throw new Exception("isSuccess没有值");
        }
        //设置statuscode
        httpEntity.setStatusCode((Integer) getValueFromMap(responseInBase,responseInHttpDetailMap,STATUSCODE));
        //设置responseheaders
        httpEntity.setResponseHeaders((Map) getValueFromMap(responseInBase,responseInHttpDetailMap,HEADERS));
        //设置responsebody
        httpEntity.setResponseBody(getValueFromMap(responseInBase,responseInHttpDetailMap,RESPONSEBODY));
        return httpEntity;
    }

    /**
     * 如果key在target2中存在，就用target2中的
     * 不存在就用target1中的数据
     * @param target1
     * @param target2
     * @param key
     * @return
     * @throws Exception
     */
    private Object getValueFromMap(Map target1,Map target2,Object key) {
        Object res = null;
        if (target1 != null){
            if (target1.containsKey(key)){
                res = target1.get(key);
            }
        }
        if (target2 != null){
            if (target2.containsKey(key)){
                res = target2.get(key);
            }
        }
        return res;
    }
}
