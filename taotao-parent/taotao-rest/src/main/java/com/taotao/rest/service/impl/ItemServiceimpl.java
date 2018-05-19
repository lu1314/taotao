package com.taotao.rest.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceimpl implements ItemService {

    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;

    @Value("${REDIS_ITEM_EXPIRE}")
    private Integer REDIS_ITEM_EXPIRE;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Override
    public TaotaoResult getItemBaseInfo(long id) {
        try {
            //cache从缓存中取商品信息
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + id + ":base");
            if (!StringUtils.isBlank(json)) {
                TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
                return TaotaoResult.ok(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //从数据库取商品信息
        TbItem tbItem = itemMapper.selectByPrimaryKey(id);


        try {
            //吧商品信息写入缓存
            jedisClient.set(REDIS_ITEM_KEY + ":" + id + ":base", JsonUtils.objectToJson(tbItem));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_KEY + ":" + id + ":base", REDIS_ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return TaotaoResult.ok(tbItem);
    }

    @Override
    public TaotaoResult getItemDescByItemId(long itemId) {

        try {
            //cache从缓存中取商品描述
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":desc");
            if (!StringUtils.isBlank(json)) {
                TbItemDesc desc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return TaotaoResult.ok(desc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);


        try {
            //吧商品描述写入缓存
            jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":desc", JsonUtils.objectToJson(itemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":desc", REDIS_ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return TaotaoResult.ok(itemDesc);
    }

    @Override
    public TaotaoResult getItemParams(long itemId) {

        try {
            //cache从缓存中取商品
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
            if (!StringUtils.isBlank(json)) {
                TbItemParamItem param = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
                return TaotaoResult.ok(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> tbItemParamItems = itemParamItemMapper.selectByExampleWithBLOBs(example);
        if (tbItemParamItems != null && tbItemParamItems.size() > 0) {
            TbItemParamItem paramItem = tbItemParamItems.get(0);
            try {
                //吧商品描述写入缓存
                jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":param", JsonUtils.objectToJson(paramItem));
                //设置过期时间
                jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":param", REDIS_ITEM_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
                return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
            }
            return TaotaoResult.ok(paramItem);
        }
        return TaotaoResult.build(400, "无此商品规格");
    }
}
