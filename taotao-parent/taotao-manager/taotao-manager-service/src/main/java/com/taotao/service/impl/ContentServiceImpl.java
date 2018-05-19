package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_CONTENT_SYNC_URL}")
    private String REST_CONTENT_SYNC_URL;

    @Override
    public EUDataGridResult getContentListByCid(Long categoryId,int page,int rows) {
        EUDataGridResult result = new EUDataGridResult();

        PageHelper.startPage(page,rows);
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> contentList = contentMapper.selectByExampleWithBLOBs(example);

        result.setRows(contentList);
        PageInfo pageInfo = new PageInfo(contentList);
        result.setTotal(pageInfo.getTotal());

        return result;
    }

    @Override
    public TaotaoResult insertContent(TbContent content) {
        //补全Content内容
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);

        //同步缓存
        try {
            HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());
        } catch (Exception e) {
            e.printStackTrace();
            //todo 通知管理员逻辑
        }

        return TaotaoResult.ok();
    }
}
