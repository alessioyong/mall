package com.yxx.mall.product.fegin;

import com.yxx.mall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mall-ware")
public interface WareFeignService {

    @PostMapping("/ware/sku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);
}
