package com.yxx.mall.backend.controller;

import com.yxx.mall.common.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xyong
 * date 2021-05-11
 */
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController {

    @GetMapping("/type/{dictType}")
    public R dictType(@PathVariable String dictType){

        return R.ok();
    }

}
