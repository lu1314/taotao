package com.taotao.controller;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item/param")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("/list")
    @ResponseBody
    public EUDataGridResult getItemParamList(int page, int rows) {
        return itemParamService.getItemParamList(page, rows);
    }

    @RequestMapping("/query/itemcatid/{id}")
    @ResponseBody
    public TaotaoResult queryItemcatId(@PathVariable Long id){
        return itemParamService.getItemParamByItemcatId(id);
    }

    @RequestMapping("/save/{cid}")
    @ResponseBody
    public TaotaoResult insertItemParam(@PathVariable Long cid,String paramData){
        TbItemParam itemParam = new TbItemParam();
        itemParam.setItemCatId(cid);
        itemParam.setParamData(paramData);
        return itemParamService.insertItemParam(itemParam);
    }
}
