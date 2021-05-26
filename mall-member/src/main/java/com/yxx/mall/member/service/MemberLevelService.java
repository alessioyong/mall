package com.yxx.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.member.MemberLevelEntity;

import java.util.List;
import java.util.Map;

/**
 * @author xyong
 * date 2021-05-26
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {
    PageInfo listLeves(Map<String, Object> params);

    void batchDeleteByIds(List<Long> asList);
}
