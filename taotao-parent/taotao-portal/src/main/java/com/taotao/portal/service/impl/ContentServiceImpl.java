package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbContent;
import com.taotao.portal.service.ContentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentServiceImpl implements ContentService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_INDEX_AD_URL}")
    private String REST_INDEX_AD_URL;

    @Override
    public String getContentList() {
        //调用服务层服务
        String result = HttpClientUtil.doGet(REST_BASE_URL + REST_INDEX_AD_URL);
        try {
            //吧字符串转换成taotaoresult
            TaotaoResult taotaoResult = TaotaoResult.formatToList(result, TbContent.class);
            List<TbContent> contentList = (List<TbContent>) taotaoResult.getData();
            List<Map> resultList = new ArrayList<>();
            //创建一个jsp页码要求的pojo列表
            for (TbContent content : contentList) {
                Map map = new HashMap();
                map.put("src",content.getPic());
                map.put("height",240);
                map.put("width",670);
                map.put("srcB",content.getPic2());
                map.put("widthB",550);
                map.put("heightB",240);
                map.put("href",content.getUrl());
                map.put("alt",content.getSubTitle());
                resultList.add(map);
            }
            return JsonUtils.objectToJson(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
