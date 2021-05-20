package com.yxx.mall.product.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.AttrEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.AttrService;
import com.yxx.mall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-20
 */
@RestController
@RequestMapping("/product/attr")
public class AttrController {

    @Autowired
    AttrService attrService;

    @GetMapping("/list")
    public R list(@RequestParam("pageNum") Integer pageNum,
                  @RequestParam("pageSize") Integer pageSize,
                  AttrEntity attr){
        PageHelper.startPage(pageNum,pageSize);
        List<AttrEntity> list =attrService.selectAttrList(attr);
        PageInfo pageInfo=new PageInfo(list);
        return R.ok().put("data",pageInfo);

    }

    @PostMapping("/add")
    public R save(@RequestBody AttrVo attrVo){

        return R.ok();
    }
}
