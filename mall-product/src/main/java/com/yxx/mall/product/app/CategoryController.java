package com.yxx.mall.product.app;

import com.yxx.mall.common.entity.product.CategoryEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 商品三级分类
 *
 * @author xyong
 * @email ${email}
 * @date 2021-05-12 10:31:44
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list/tree")
    public R list(){
        List<CategoryEntity> list= categoryService.listWithTree();
        return R.ok().put("data",list);
    }

    /**
     * 根据ID删除分类
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        categoryService.deleteByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 添加新的分类菜单
     * @param category
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category){
        categoryService.save(category);
        return R.ok();
    }

    /**
     * 根据ID更新分类菜单
     * @param category
     * @return
     */
    @PutMapping("/edit")
    public R edit(@RequestBody CategoryEntity category){
        categoryService.updateCasecade(category);
        return R.ok();
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CategoryEntity category = categoryService.getById(id);
        return R.ok().put("data",category);
    }

}
