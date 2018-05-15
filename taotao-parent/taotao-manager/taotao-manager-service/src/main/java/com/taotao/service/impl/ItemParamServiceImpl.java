package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper itemParamMapper;

    @Override
    public EUDataGridResult getItemParamList(int page, int rows) {
        EUDataGridResult result = new EUDataGridResult();

        TbItemParamExample example = new TbItemParamExample();

        PageHelper.startPage(page, rows);

        List<TbItemParam> tbItemParams = itemParamMapper.selectByExampleWithBLOBs(example);

        result.setRows(tbItemParams);

        PageInfo pageInfo = new PageInfo(tbItemParams);

        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public TaotaoResult getItemParamByItemcatId(Long id) {
        TbItemParamExample example = new TbItemParamExample();

        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(id);
        List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            return TaotaoResult.ok(list.get(0));
        }

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult insertItemParam(TbItemParam itemParam) {
        itemParam.setCreated(new Date());
        itemParam.setUpdated(new Date());
        itemParamMapper.insert(itemParam);
        return TaotaoResult.ok();
    }
}
