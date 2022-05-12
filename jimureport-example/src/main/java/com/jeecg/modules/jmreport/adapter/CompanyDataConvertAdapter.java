package com.jeecg.modules.jmreport.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.jeecg.modules.jmreport.desreport.render.handler.convert.ApiDataConvertAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @program: jimureport-demo
 * @description: 编写适配器，实现ApiDataConvertAdapter
 * @date 2021-06-03
 */
@Component("companyParser")
public class CompanyDataConvertAdapter implements ApiDataConvertAdapter {
  
   @Autowired
   private RestTemplate restTemplate;

  /**
   * 返回list数据集，转换成积木报表需要格式{}，没有嵌套
   * 注意：需要json格式，不用data包裹起来了
   * @param jsonObject 接口数据原始对象
   * @return
   */
  @Override
  public String getData(JSONObject jsonObject) {
    JSONObject result = new JSONObject();
    String post = "post";
    String get = "get";
    HttpHeaders headers = new HttpHeaders();
    MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
    String companyId = jsonObject.getString("companyid");
    String method = jsonObject.getString("method");
    String url = jsonObject.getString("url");
    String data = jsonObject.getString("data");
    String[] dataList = data.split(",");
    headers.setContentType(type);
    headers.add("companyId",companyId);
    JSONObject postData = new JSONObject();
    for (String s : dataList) {
      String[] keyValue = s.split("-");
      String key = keyValue[0];
      String value = "null".equals(keyValue[1]) ? null : keyValue[1];
      postData.put(key,value);
    }
    HttpEntity<String> formEntity = new HttpEntity<String>(postData.toString(), headers);
    if (post.equals(method)){
      result = JSON.parseObject(restTemplate.postForEntity(url,formEntity,String.class).getBody());
    }else if (get.equals(method)){
      result = JSON.parseObject(restTemplate.exchange(url, HttpMethod.GET,formEntity,String.class).getBody());
    }
    return result.getJSONArray("data").toJSONString();
  }


  /**
   * 返回links（没有图表属性可以删掉）
   * @param jsonObject 接口数据原始对象
   * @return
   */
  @Override
  public String getLinks(JSONObject jsonObject) {
    return jsonObject.containsKey("links") ? jsonObject.get("links").toString() : "";
  }

  /**
   * 返回总页数（没有分页可以删掉）
   * @param jsonObject 接口数据原始对象
   * @return
   */
  @Override
  public String getTotal(JSONObject jsonObject) {
    return jsonObject.containsKey("pageNumber") ? jsonObject.get("pageNumber").toString() : "0";
  }

  /**
   * 返回总条数（没有分页可以删掉）
   * @param jsonObject 接口数据原始对象
   * @return
   */
  @Override
  public String getCount(JSONObject jsonObject) {
    return jsonObject.containsKey("count") ? jsonObject.get("count").toString() : "0";
  }
}