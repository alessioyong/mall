package com.yxx.mall.product.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.AttrGroupEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.AttrGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 属性分组Controller
 * 
 * @author xyong
 * @date 2021-05-13
 */
@RestController
@RequestMapping("/product/attrgroup")
public class AttrGroupController {

    @Autowired
    AttrGroupService attrGroupService;

    @GetMapping("/list")
    public R list(@RequestParam("pageNum") Integer pageNum,
                  @RequestParam("pageSize") Integer pageSize,
                  AttrGroupEntity attrGroupEntity){
        PageHelper.startPage(pageNum,pageSize);
        List<AttrGroupEntity> list= attrGroupService.getAttrGroup(attrGroupEntity);
        PageInfo page=new PageInfo(list);
        return R.ok().put("data",page);
    }

}
