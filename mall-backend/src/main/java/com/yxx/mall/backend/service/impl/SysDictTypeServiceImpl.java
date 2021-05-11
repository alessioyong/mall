package com.yxx.mall.backend.service.impl;

import com.yxx.mall.backend.mapper.SysDictTypeMapper;
import com.yxx.mall.backend.service.SysDictTypeService;
import com.yxx.mall.common.entity.backend.SysDictDataEntity;
import com.yxx.mall.common.utils.StringUtils;
import com.yxx.mall.common.utils.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyong
 * date 2021-05-11
 */
@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {
    @Resource
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private RedisService redisService;
    /**
     * 根据字典类型查询字典数据
     * @param dictType 字典类型
     * @return
     */
    @Override
    public List<SysDictDataEntity> selectDictDataByType(String dictType) {
        //List<SysDictDataEntity> dictDatas = DictUtils.getDictCache(dictType);
        String dictKey= Constants.SYS_DICT_KEY + dictType;
        List<SysDictDataEntity> dictDatas = redisService.getCacheObject(dictKey);
        if(StringUtils.isNotEmpty(dictDatas)){
            return dictDatas;
        }
        dictDatas=dictTypeMapper.selectDictDataByType(dictType);
        if(StringUtils.isNotEmpty(dictDatas)){
           // DictUtils.setDictCache(dictType,dictDatas);
            redisService.setCacheObject(dictKey,dictDatas);
            return dictDatas;
        }
        return null;
    }
}
