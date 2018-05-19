package com.taotao.portal.pojo;

import com.taotao.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;

public class ItemInfo extends TbItem {


    public String[] getImages(){
        if (StringUtils.isNotBlank(getImage())){
            return getImage().split(",");
        }
        return null;
    }
}
