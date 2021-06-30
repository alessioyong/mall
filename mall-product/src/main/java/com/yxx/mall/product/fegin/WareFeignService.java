package com.yxx.mall.product.fegin;

import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mall-ware")
public interface WareFeignService {

    @PostMapping("/ware/sku/hasstock")
    R<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds);
}
