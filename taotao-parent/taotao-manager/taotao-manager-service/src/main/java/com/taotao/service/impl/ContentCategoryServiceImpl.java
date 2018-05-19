package com.taotao.service.impl;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EUTreeNode> getCategoryList(Long parentId) {

        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        List<TbContentCategory> categories = contentCategoryMapper.selectByExample(example);

        List<EUTreeNode> treeNodeList = new ArrayList<>();

        for (TbContentCategory category : categories) {
            EUTreeNode node = new EUTreeNode();
            node.setId(category.getId());
            node.setText(category.getName());
            node.setState(category.getIsParent() ? "closed" : "open");
            treeNodeList.add(node);
        }

        return treeNodeList;
    }

    @Override
    public TaotaoResult insertCategory(String name, Long parentId) {
        TbContentCategory category = new TbContentCategory();
        category.setName(name);
        category.setParentId(parentId);
        category.setIsParent(false);//
        category.setSortOrder(1);
        category.setCreated(new Date());
        category.setUpdated(new Date());
        category.setStatus(1);
        contentCategoryMapper.insert(category);
        //查看父节点isParent是否为true
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!contentCategory.getIsParent()) {//不是父节点,更新为父节点
            contentCategory.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(contentCategory);
        }
        return TaotaoResult.ok(category);
    }

    @Override
    public TaotaoResult deleteById(Long parentId, Long id) {
        //删除节点
        contentCategoryMapper.deleteByPrimaryKey(id);
        //查询父节点是否还有其他子节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        if (list == null || list.size() == 0) {//没有其他子节点，将isParent设为false
            TbContentCategory contentCategory = new TbContentCategory();
            contentCategory.setId(parentId);
            contentCategory.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        }

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateNameById(Long id, String name) {
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setId(id);
        contentCategory.setName(name);
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        return TaotaoResult.ok();
    }
}
