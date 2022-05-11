package com.jeecg.modules.jmreport.adapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.jmreport.desreport.render.handler.convert.ApiDataConvertAdapter;
import org.springframework.stereotype.Component;

/**
 * @program: jimureport-demo
 * @description: 编写适配器，实现ApiDataConvertAdapter
 * @date 2021-06-03
 */
@Component("companyParser")
public class CompanyDataConvertAdapter implements ApiDataConvertAdapter {

  /**
   * 返回list数据集，转换成积木报表需要格式{}，没有嵌套
   * 注意：需要json格式，不用data包裹起来了
   * @param jsonObject 接口数据原始对象
   * @return
   */
  @Override
  public String getData(JSONObject jsonObject) {
    if(jsonObject.containsKey("result")){
      JSONArray data = jsonObject.getJSONObject("result").getJSONArray("data");
      return data.toJSONString();
    }else{
      return jsonObject.toJSONString();
    }
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