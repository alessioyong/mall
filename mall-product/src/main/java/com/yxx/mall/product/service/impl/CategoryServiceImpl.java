package com.yxx.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    AttrGroupMapper attrGroupMapper;

    @Autowired
    CategoryBrandRealtionService categoryBrandRealtionService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redisson;

    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2.组装成父子结构
        //2.1)找到所有的一级分类
        List<CategoryEntity> leve1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0).map((menu) -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return leve1Menus;
    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //1.找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2.菜单排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

    /**
     * 根据ID删除分类
     *
     * @param ids
     */
    @Override
    public void deleteByIds(List<Long> ids) {

        Integer count = attrGroupMapper.selectCount(new QueryWrapper<AttrGroupEntity>().in("catelog_id", ids));
        if (count > 0) {
            throw new RRException("当前菜单被其他地方引用，无法删除");
        } else {
            baseMapper.deleteBatchIds(ids);
        }
    }

    /**
     * 查询完整的路径
     *
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有的关联数据
     *
     * @param category
     */
    @Override
    public void updateCasecade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRealtionService.updateCategory(category.getCatId(), category.getName());
    }

    /**
     * 查询所有的一级分类
     *
     * @return
     */
    //每一个需要缓存的数据我们都来指定放到那个名字的缓存【缓存的分区（按照业务类型分）】
    @Cacheable(value = {"category"},key = "'levelOneCategorys'") //代表当前方法的结果需要缓存，如果缓存中有，方法不调用，如果没有会调用方法，最后将结果放入缓存
    @Override
    public List<CategoryEntity> getLevelOneCategorys() {
        long l = System.currentTimeMillis();
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        System.out.println("消耗时间：" + (System.currentTimeMillis() - l));
        return categoryEntities;
    }

    //TODO 产生对外内存溢出：OutOfDirectMemoryError
    //1）、Springboot2.0以后默认使用 Lettuce作为操作redis的客户端、它使用netty进行网络通信
    //2）、Lettuce的bug导致对外内存溢出
    //解决方案 1.升级lettuce客户端 ，2.切换使用jedis
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        /**
         * 1.空结果缓存：解决缓存击穿
         * 2.设置过期时间（加随机值）：解决缓存雪崩
         * 3.加锁：解决缓存击穿
         */
        //1.加入缓存逻辑
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)) {
            //2.缓存中没有 ，查询数据库
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDbWithRedissonLock();
            return catalogJsonFromDb;
        }
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return result;
    }

    //从数据库查询并封装
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {
        // TODO 本地锁；synchronized，JUC（lock）分布式情况下，想要锁住所有，必须使用分布式锁
        synchronized (this) {
            //得到锁以后，应该再去缓存中确定一次，如果没有才需要继续查询
            return getDataFromDb();
        }
        /**
         * 1.将数据库的多次查询变为1次
         */

    }

    /**
     * 缓存里的数据如何和数据库中保持一致
     * 1）双写模式
     * 2）失效模式
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedissonLock() {
        //1.锁的名字，锁的粒度，越细越快
        RLock lock = redisson.getLock("catalogJson-lock");
        lock.lock();
        Map<String, List<Catelog2Vo>> dataFromDb;
        try {
            //加锁成功
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }

        return dataFromDb;


    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock() {
        //1.占分布式锁。去redis占坑//2.设置过期时间,必须是原子的
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            System.out.println("获得分布式锁成功。");
            Map<String, List<Catelog2Vo>> dataFromDb;
            try {
                //加锁成功
                dataFromDb = getDataFromDb();
            } finally {
                //获取锁+删除所=原子操作 luo脚本解锁
                String scrip = "if redis.call(\"get\",KEYS[1]) == ARGV[1] \n" +
                        "then\n" +
                        "\treturn redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end;";
                //删除锁
                redisTemplate.execute(new DefaultRedisScript<Integer>(scrip, Integer.class), Arrays.asList("lock"), uuid);
            }


           /* String lockValue = redisTemplate.opsForValue().get("lock");
            if(uuid.equals(lockValue)){
                //删除自己的锁
                redisTemplate.delete("lock"); //删除锁
            }*/
            return dataFromDb;
        } else {
            //加锁失败。。。重试
            System.out.println("获得分布式锁失败...等待重试");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDbWithRedisLock();
        }

    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        //得到锁以后，应该再去缓存中确定一次，如果没有才需要继续查询
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            //缓存不为空
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        //1。查出所有一级分类
        //List<CategoryEntity> levelOneCategorys = getLevelOneCategorys();
        List<CategoryEntity> levelOneCategorys = getParent_cid(selectList, 0L);

        //2.封装数据
        Map<String, List<Catelog2Vo>> parent_cid = levelOneCategorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1.每一个的一级分类，查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            //2.封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1.找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());

                    if (level3Catelog != null) {
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

        //3.查到的数据再放入缓存中，将对象转为json放在缓存中
        String s = JSON.toJSONString(parent_cid);
        redisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
        return parent_cid;
    }

    //
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock() {
        // TODO 本地锁；synchronized，JUC（lock）分布式情况下，想要锁住所有，必须使用分布式锁
        synchronized (this) {
            //得到锁以后，应该再去缓存中确定一次，如果没有才需要继续查询
            return getDataFromDb();
        }
        /**
         * 1.将数据库的多次查询变为1次
         */

    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
        return collect;
    }

    private List<Long> findParentPath(Long catelogId, List<Long> path) {
        //1.收集当前节点ID
        path.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), path);
        }
        return path;
    }
}