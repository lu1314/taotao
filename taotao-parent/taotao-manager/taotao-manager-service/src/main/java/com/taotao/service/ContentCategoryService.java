package com.taotao.service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContentCategory;

import java.util.List;

public interface ContentCategoryService {
    List<EUTreeNode> getCategoryList(Long parentId);

    TaotaoResult insertCategory(String name,Long parentId);

    TaotaoResult deleteById(Long parentId,Long id);

    TaotaoResult updateNameById(Long id,String name);
}
