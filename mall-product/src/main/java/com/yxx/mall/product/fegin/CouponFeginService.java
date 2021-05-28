package com.yxx.mall.product.fegin;

import com.yxx.mall.common.to.SkuReductionTo;
import com.yxx.mall.common.to.SpuBoundTo;
import com.yxx.mall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author xyong
 * date 2021-05-28
 */
@FeignClient("mall-coupon")
public interface CouponFeginService {


    @PostMapping("/coupon/bounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
