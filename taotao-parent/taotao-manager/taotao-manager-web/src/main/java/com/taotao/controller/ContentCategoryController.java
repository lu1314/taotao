package com.taotao.controller;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EUTreeNode> getCategoryList(@RequestParam(value = "id", defaultValue = "0") Long id) {
        return contentCategoryService.getCategoryList(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult cerateContentCategory(Long parentId, String name) {
        return contentCategoryService.insertCategory(name, parentId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContentCategory(Long parentId, Long id) {
        return contentCategoryService.deleteById(parentId,id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContentCategoryName(Long id, String name) {
        return contentCategoryService.updateNameById(id,name);
    }
}
