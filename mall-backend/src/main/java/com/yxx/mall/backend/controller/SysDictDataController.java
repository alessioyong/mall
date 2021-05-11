package com.yxx.mall.backend.controller;

import com.yxx.mall.backend.service.SysDictTypeService;
import com.yxx.mall.common.entity.backend.SysDictDataEntity;
import com.yxx.mall.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-11
 */
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController {

    @Autowired
    private SysDictTypeService dictTypeService;

    @GetMapping("/type/{dictType}")
    public R dictType(@PathVariable String dictType){
        List<SysDictDataEntity> data = dictTypeService.selectDictDataByType(dictType);
        return R.ok().put("data",data);
    }

}
