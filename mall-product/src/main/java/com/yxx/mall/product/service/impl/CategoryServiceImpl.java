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
import com.yxx.mall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    /**
     * 查询所有的一级分类
     * @return
     */
    @Override
    public List<CategoryEntity> getLevelOneCategorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        //1。查出所有一级分类
        List<CategoryEntity> levelOneCategorys = getLevelOneCategorys();

        //2.封装数据
        Map<String, List<Catelog2Vo>> parent_cid = levelOneCategorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1.每一个的一级分类，查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
            //2.封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1.找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));

                    if(level3Catelog!=null){
                        List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            //2.封装成指定的给是
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));
        return parent_cid;
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