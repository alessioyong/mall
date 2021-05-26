package com.yxx.mall.product.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.AttrEntity;
import com.yxx.mall.common.entity.product.AttrGroupEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.AttrAttrgroupRelationService;
import com.yxx.mall.product.service.AttrGroupService;
import com.yxx.mall.product.service.AttrService;
import com.yxx.mall.product.service.CategoryService;
import com.yxx.mall.product.vo.AttrGroupRelationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 属性分组Controller
 * 
 * @author xyong
 * @date 2021-05-13
 */
@RestController
@RequestMapping("/product/attrgroup")
@Slf4j
public class AttrGroupController {

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrService attrService;

    @Autowired
    AttrAttrgroupRelationService relationService;
    /**
     * 查询分组列表
     * @param pageNum
     * @param pageSize
     * @param attrGroupEntity
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam("pageNum") Integer pageNum,
                  @RequestParam("pageSize") Integer pageSize,
                  AttrGroupEntity attrGroupEntity){
        PageHelper.startPage(pageNum,pageSize);
        List<AttrGroupEntity> list= attrGroupService.getAttrGroup(attrGroupEntity);
        PageInfo page=new PageInfo(list);
        return R.ok().put("data",page);
    }


    /**
     * 根据分组Id查询关联的基本属性
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> entities=attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@RequestParam Map<String,Object> params,
                            @PathVariable("attrgroupId") Long attrgroupId){
        log.info("params:{}",params);
        PageInfo pageInfo=attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("data",pageInfo);
    }
    /**
     * 删除关联的基本属性
     * @param vos
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteAttrRelation(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 新增分组
     * @param attrGroupEntity
     * @return
     */
    @PostMapping("/add")
    public R add(@RequestBody AttrGroupEntity attrGroupEntity){
        attrGroupService.save(attrGroupEntity);
        return R.ok();
    }

    /**
     * 新建分组关联关系
     * @param vos
     * @return
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.batchSave(vos);
        return R.ok();
    }
    /**
     * 根据ID查询分组信息
     * @param attrGroupId
     * @return
     */
    @GetMapping("/{attrGroupId}")
    public R get(@PathVariable("attrGroupId") Long attrGroupId){
        AttrGroupEntity groupEntity = attrGroupService.getById(attrGroupId);
        Long[] path=categoryService.findCatelogPath(groupEntity.getCatelogId());
        groupEntity.setCatelogPath(path);
        return R.ok().put("data",groupEntity);
    }

    /**
     * 根据ID修改分组信息
     * @param attrGroupEntity
     * @return
     */
    @PutMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroupEntity){
        attrGroupService.updateById(attrGroupEntity);
        return R.ok();
    }

    /**
     * 批量删出
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable("ids") Long[] ids){

        attrGroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
