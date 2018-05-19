package com.taotao.controller;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/query/list")
    @ResponseBody
    public EUDataGridResult getContentList(Long categoryId,int page,int rows){
        return contentService.getContentListByCid(categoryId,page,rows);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createContent(TbContent content){
        return contentService.insertContent(content);
    }
}
