package com.yxx.mall.product.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.AttrEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.AttrService;
import com.yxx.mall.product.vo.AttrRespVo;
import com.yxx.mall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-20
 */
@RestController
@Slf4j
@RequestMapping("/product/attr")
public class AttrController {

    @Autowired
    AttrService attrService;

    @GetMapping("/list")
    public R list(@RequestParam("pageNum") Integer pageNum,
                  @RequestParam("pageSize") Integer pageSize,
                  AttrEntity attr){
        PageHelper.startPage(pageNum,pageSize);
        List<AttrRespVo> list =attrService.selectAttrList(attr);
        PageInfo pageInfo=new PageInfo(list);
        return R.ok().put("data",pageInfo);

    }

    @PostMapping("/add")
    public R save(@RequestBody AttrVo attrVo){
        log.info("前端传入数据：{}",attrVo);
        attrService.saveAttr(attrVo);
        return R.ok();
    }

    /**
     * 根据attrId查询规格参数信息
     * @param attrId
     * @return
     */
    @GetMapping("/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo attrRespVo=attrService.getAttrInfo(attrId);
        return R.ok().put("data",attrRespVo);
    }

    @PutMapping("/update")
    public R update(@RequestBody AttrVo attr){
        attrService.updateAttr(attr);
        return R.ok();
    }
}
