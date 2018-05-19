package com.taotao.rest.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public CatResult getItemCatList() {
        CatResult catResult = new CatResult();

        catResult.setData(getCatList(0));
        return catResult;
    }

    private List getCatList(long parentId){
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> itemCats = itemCatMapper.selectByExample(example);
        List resultList = new ArrayList();
        int count = 0;
        for (TbItemCat itemCat : itemCats) {

            if (itemCat.getIsParent()){//是父节点
                CatNode node = new CatNode();
                if (parentId ==0){//是根节点
                    node.setName("<a href='/products/"+itemCat.getId()+".html'>"+itemCat.getName()+"</a>");
                }else{
                    node.setName(itemCat.getName());
                }
                node.setUrl("/products/"+itemCat.getId()+".html");
                node.setItem(getCatList(itemCat.getId()));

                resultList.add(node);
                count ++;
                if (parentId == 0 && count >= 14){
                    break;
                }
            }else{//是叶子节点
                resultList.add("/products/"+itemCat.getId()+".html|" + itemCat.getName());
            }
        }
        return resultList;

    }

}
