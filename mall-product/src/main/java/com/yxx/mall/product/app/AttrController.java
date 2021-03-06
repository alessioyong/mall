package com.yxx.mall.product.app;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.AttrEntity;
import com.yxx.mall.common.entity.product.ProductAttrValueEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.AttrService;
import com.yxx.mall.product.service.ProductAttrValueService;
import com.yxx.mall.product.vo.AttrRespVo;
import com.yxx.mall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @Autowired
    ProductAttrValueService productAttrValueService;

    @GetMapping("/{type}/list")
    public R list(@RequestParam("pageNum") Integer pageNum,
                  @RequestParam("pageSize") Integer pageSize,
                  AttrEntity attr,
                  @PathVariable("type") String type){
        PageHelper.startPage(pageNum,pageSize);
        List<AttrRespVo> list =attrService.selectAttrList(attr,type);
        PageInfo pageInfo=new PageInfo(list);
        return R.ok().put("data",pageInfo);

    }

    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrlistforspu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> entities = productAttrValueService.baseAttrlistforspu(spuId);
        return R.ok().put("data",entities);
    }

    /**
     * 根据catelogId查询销售属性
     * @param catelogId
     * @return
     */
    @GetMapping("/sale/list/{catelogId}")
    public R getSaleAttrs(@PathVariable("catelogId")Long catelogId){
        List<AttrEntity> attrs=attrService.getSaleAttrByCatelogId(catelogId);
        return R.ok().put("data",attrs);
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

    /**
     * 更新参数规格
     * @param attr
     * @return
     */
    @PutMapping("/update")
    public R update(@RequestBody AttrVo attr){
        attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 修改规格参数
     * @param spuId
     * @param entities
     * @return
     */
    @PutMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entities){

        productAttrValueService.updateSpuAttr(spuId,entities);

        return R.ok();
    }
    /**
     * 批量删除规格参数
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable("ids") Long[] ids){

        attrService.deleteAttr(Arrays.asList(ids));
        return R.ok();
    }
}
