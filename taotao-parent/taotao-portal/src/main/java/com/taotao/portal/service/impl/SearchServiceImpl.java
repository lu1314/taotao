package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Value("${SEARCH_BASE_URL}")
    private String SEARCH_BASE_URL;

    @Override
    public SearchResult search(String queryString, int page) {
        //查询参数
        Map<String, String> param = new HashMap<>();
        param.put("q", queryString);
        param.put("page", page + "");
        try {
            //调用服务
            String s = HttpClientUtil.doGet(SEARCH_BASE_URL, param);
            //把字符串转换为java对象
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(s, SearchResult.class);
            if (taotaoResult.getStatus() == 200){
                SearchResult searchResult = (SearchResult) taotaoResult.getData();
                return searchResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
