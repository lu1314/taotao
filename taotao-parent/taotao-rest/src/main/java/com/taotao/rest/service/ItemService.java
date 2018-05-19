package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {
    TaotaoResult getItemBaseInfo(long id);

    TaotaoResult getItemDescByItemId(long itemId);

    TaotaoResult getItemParams(long itemId);
}
