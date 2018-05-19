package com.taotao.rest.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemCartController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(value = "/itemcat/all",produces = "application/json;charset=utf8")
    @ResponseBody
    public String getItemCatList(String callback){
        CatResult itemCatList = itemCatService.getItemCatList();
        String json = JsonUtils.objectToJson(itemCatList);
        return callback + "(" + json + ");";
    }
}
