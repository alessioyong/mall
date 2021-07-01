package com.yxx.mall.product.web;

import com.yxx.mall.common.entity.product.CategoryEntity;
import com.yxx.mall.product.service.CategoryService;
import com.yxx.mall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author xyong
 * date 2021-07-01
 */
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        //TODO 1.查找所有的一级分类

        List<CategoryEntity> categoryEntities=categoryService.getLevelOneCategorys();
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }
}
