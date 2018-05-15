package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;

import java.util.List;

public interface ItemParamService {
    EUDataGridResult getItemParamList(int page, int rows);

    TaotaoResult getItemParamByItemcatId(Long id);

    TaotaoResult insertItemParam(TbItemParam itemParam);
}
