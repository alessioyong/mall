package com.yxx.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xyong
 * date 2021-07-01
 * 2级分类vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {

    private String catalog1Id; //1.一级分类id
    private List<Catelog3Vo> catalog3List; // 三级子分类
    private String id;
    private String name;

    /**
     * 三级分类vo
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo{
        private String catalog2Id; //父分类 ，二级分类id
        private String id;
        private String name;
    }
}
