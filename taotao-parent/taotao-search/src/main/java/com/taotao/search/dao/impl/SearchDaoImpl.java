package com.taotao.search.dao.impl;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.Item;
import com.taotao.search.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDaoImpl implements SearchDao {

    @Autowired
    private SolrClient solrClient;

    @Override
    public SearchResult search(SolrQuery solrQuery) throws Exception {
        SearchResult result = new SearchResult();
        //根据查询条件查询索引库
        QueryResponse queryResponse = solrClient.query(solrQuery);
        //取查询结果
        SolrDocumentList documentList = queryResponse.getResults();
        //取查询结果总数
        result.setRecordCount(documentList.getNumFound());

        List<Item> itemList = new ArrayList<>();

        //取高亮显示
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        //取商品列表
        for (SolrDocument document : documentList) {
            Item item = new Item();
            item.setId((String) document.get("id"));
            String title = "";
            //取高亮显示结果
            List<String> list = highlighting.get(document.get("id")).get("item_title");
            if (list != null && list.size() >0){
                title = list.get(0);
            }else {
                title = (String) document.get("item_title");
            }
            item.setTitle(title);
            item.setImage((String) document.get("item_image"));
            item.setPrice((Long) document.get("item_price"));
            item.setSell_point((String) document.get("item_sell_poimt"));
            item.setCategory_name((String) document.get("item_category_name"));

            itemList.add(item);
        }
        result.setItemList(itemList);
        return result;
    }
}
