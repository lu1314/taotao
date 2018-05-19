package com.taotao.rest.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liujia
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT_REDIS_KEY}")
    private String INDEX_CONTENT_REDIS_KEY;

    @Override
    public List<TbContent> getContentListByCid(Long categoryId) {

        //查缓存
        try {
            String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, categoryId + "");
            if (StringUtils.isNotBlank(result)){
                return JsonUtils.jsonToList(result, TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //查数据库
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> tbContents = contentMapper.selectByExampleWithBLOBs(example);

        //添加缓存
        try {
            String cacheStr = JsonUtils.objectToJson(tbContents);
            jedisClient.hset(INDEX_CONTENT_REDIS_KEY, categoryId + "", cacheStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbContents;
    }
}
