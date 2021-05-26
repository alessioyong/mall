package com.yxx.mall.member.controller;


import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.member.service.MemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xyong
 * date 2021-05-26
 */
@RestController
@RequestMapping("/member/level")
public class MemberLevelController {

    @Autowired
    MemberLevelService memberLevelService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> params){
        PageInfo page=memberLevelService.listLeves(params);
        return R.ok().put("data",page);
    }

}
