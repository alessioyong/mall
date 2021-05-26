package com.yxx.mall.member.controller;


import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.member.MemberLevelEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.member.service.MemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    /**
     * 条件查询出会员等级列表
     * @param params
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> params){
        PageInfo page=memberLevelService.listLeves(params);
        return R.ok().put("data",page);
    }

    /**
     * 新增会员等级
     * @param member
     * @return
     */
    @PostMapping("/add")
    public R add (@RequestBody MemberLevelEntity memberLevel){
        memberLevelService.save(memberLevel);
        return R.ok();
    }

    /**
     * 根据ID查询会员等级
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        MemberLevelEntity entity = memberLevelService.getById(id);
        return R.ok().put("data",entity);
    }

    /**
     * 根据ID修改会员等级信息
     * @param memberLevel
     * @return
     */
    @PutMapping("/update")
    public R update(@RequestBody MemberLevelEntity memberLevel){
        memberLevelService.updateById(memberLevel);
        return R.ok();
    }

    /**
     * 批量删除会员等级信息
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable("ids") Long [] ids){
        memberLevelService.batchDeleteByIds(Arrays.asList(ids));
        return R.ok();
    }

}
