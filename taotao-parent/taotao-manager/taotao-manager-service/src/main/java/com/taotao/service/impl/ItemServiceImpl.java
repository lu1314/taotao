package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

/**
 * @author liujia
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Override
    public EUDataGridResult getItemList(int page, int rows) {
        // 查询商品列表
        TbItemExample example = new TbItemExample();
        // 分页处理
        PageHelper.startPage(page, rows);
        List<TbItem> list = itemMapper.selectByExample(example);
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    /**
     * 添加商品
     *
     * @param item
     * @param desc
     * @param itemParams
     * @return
     */
    @Override
    public TaotaoResult createItem(TbItem item, String desc, String itemParams) throws Exception {
        // item补全
        // 生成商品ID
        Long itemId = IDUtils.genItemId();
        item.setId(itemId);
        // '商品状态，1-正常，2-下架，3-删除',
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //插入到数据库
        itemMapper.insert(item);

        //添加商品描述
        TaotaoResult result = insertItemDesc(itemId, desc);
        if (result.getStatus() != 200) {
            throw new Exception();
        }
        //添加商品规格参数
        result = insertItemParamItem(itemId, itemParams);
        if (result.getStatus() != 200) {
            throw new Exception();
        }
        return TaotaoResult.ok();
    }

    /**
     * 添加商品描述
     *
     * @param itemId
     * @param desc
     * @return
     */
    private TaotaoResult insertItemDesc(Long itemId, String desc) {
        TbItemDesc itemDesc = new TbItemDesc();

        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDescMapper.insert(itemDesc);
        return TaotaoResult.ok();
    }

    /**
     * 添加规格参数
     *
     * @param itemId
     * @param itemParam
     * @return
     */
    private TaotaoResult insertItemParamItem(Long itemId, String itemParam) {
        TbItemParamItem itemParamItem = new TbItemParamItem();

        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParam);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(new Date());

        itemParamItemMapper.insert(itemParamItem);

        return TaotaoResult.ok();
    }

}
