package com.taotao.portal.service;

import com.taotao.portal.pojo.ItemInfo;

public interface ItemService {
    ItemInfo getItemById(long id);

    String getItemDesc(long itemId);

    String getItemParam(long itemId);
}
