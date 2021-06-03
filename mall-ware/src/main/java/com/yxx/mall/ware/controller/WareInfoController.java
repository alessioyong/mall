package com.yxx.mall.ware.controller;

import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.ware.WareInfoEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.ware.service.WareInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 仓库信息Controller
 * 
 * @author xyong
 * @date 2021-05-25
 */
@RestController
@RequestMapping("/ware/wareinfo")
public class WareInfoController {

    @Autowired
    WareInfoService wareInfoService;

    /**
     * 条件查询出仓库信息
     * @param params
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> params){
        PageInfo page=wareInfoService.listWareInfo(params);
        return R.ok().put("data",page);
    }

    /**
     * 新增仓库信息
     * @param wareInfoEntity
     * @return
     */
    @PostMapping("/add")
    public R add(@RequestBody WareInfoEntity wareInfoEntity){

        wareInfoService.save(wareInfoEntity);
        return R.ok();
    }

    /**
     * 根据ID查询仓库信息
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public R getInfo(@PathVariable("id") Long id){

        WareInfoEntity wareInfoEntity = wareInfoService.getById(id);
        return R.ok().put("data",wareInfoEntity);
    }

    /**
     * 根据id更新仓库信息
     * @param wareInfoEntity
     * @return
     */
    @PutMapping("/update")
    public R update(@RequestBody WareInfoEntity wareInfoEntity){
        wareInfoService.updateById(wareInfoEntity);
        return R.ok();
    }

    /**
     * 批量删除仓库信息
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable("ids") Long []ids){
        wareInfoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
