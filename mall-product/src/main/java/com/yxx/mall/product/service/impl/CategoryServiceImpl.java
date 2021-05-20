package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.AttrGroupEntity;
import com.yxx.mall.common.entity.product.CategoryEntity;
import com.yxx.mall.common.utils.RRException;
import com.yxx.mall.product.mapper.AttrGroupMapper;
import com.yxx.mall.product.mapper.CategoryMapper;
import com.yxx.mall.product.service.CategoryBrandRealtionService;
import com.yxx.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    AttrGroupMapper attrGroupMapper;

    @Autowired
    CategoryBrandRealtionService categoryBrandRealtionService;

    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2.组装成父子结构
        //2.1)找到所有的一级分类
        List<CategoryEntity> leve1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0).map((menu)->{
                    menu.setChildren(getChildrens(menu,entities));
                    return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return leve1Menus;
    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //1.找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2.菜单排序
            return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

    /**
     * 根据ID删除分类
     * @param ids
     */
    @Override
    public void deleteByIds(List<Long> ids) {

        Integer count = attrGroupMapper.selectCount(new QueryWrapper<AttrGroupEntity>().in("catelog_id", ids));
        if(count>0){
            throw new RRException("当前菜单被其他地方引用，无法删除");
        }else {
            baseMapper.deleteBatchIds(ids);
        }
    }

    /**
     * 查询完整的路径
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths=new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有的关联数据
     * @param category
     */
    @Override
    public void updateCasecade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRealtionService.updateCategory(category.getCatId(),category.getName());
    }

    private List<Long> findParentPath(Long catelogId,List<Long> path){
        //1.收集当前节点ID
        path.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),path);
        }
        return path;
    }
}