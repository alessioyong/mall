<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams">
      <el-form-item label="分类">
        <category-cascader :catelogPath.sync="catelogPath"></category-cascader>
      </el-form-item>
      <el-form-item label="品牌">
        <brand-select style="width: 160px"></brand-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select
          style="width: 160px"
          size="small"
          v-model="queryParams.status"
          clearable
        >
          <el-option label="新建" :value="0"></el-option>
          <el-option label="上架" :value="1"></el-option>
          <el-option label="下架" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="检索">
        <el-input
          style="width: 160px"
          v-model="queryParams.key"
          size="small"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="small"
          @click="searchSpuInfo"
          >查询</el-button
        >
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%"
    >
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50"
      ></el-table-column>
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="id"
      ></el-table-column>
      <el-table-column
        prop="spuName"
        header-align="center"
        align="center"
        label="名称"
      ></el-table-column>
      <el-table-column
        prop="spuDescription"
        header-align="center"
        align="center"
        label="描述"
      ></el-table-column>
      <el-table-column
        prop="catalogId"
        header-align="center"
        align="center"
        label="分类"
      ></el-table-column>
      <el-table-column
        prop="brandId"
        header-align="center"
        align="center"
        label="品牌"
      ></el-table-column>
      <el-table-column
        prop="weight"
        header-align="center"
        align="center"
        label="重量"
      ></el-table-column>
      <el-table-column
        prop="publishStatus"
        header-align="center"
        align="center"
        label="上架状态"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.publishStatus == 0">新建</el-tag>
          <el-tag v-if="scope.row.publishStatus == 1">已上架</el-tag>
          <el-tag v-if="scope.row.publishStatus == 2">已下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间"
      ></el-table-column>
      <el-table-column
        prop="updateTime"
        header-align="center"
        align="center"
        label="修改时间"
      ></el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.publishStatus == 0"
            type="text"
            size="small"
            @click="productUp(scope.row.id)"
            >上架</el-button
          >
          <el-button type="text" size="small" @click="attrUpdateShow(scope.row)"
            >规格</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getDataList"
    />
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { getSpuInfo } from "@/api/product/spuinfo";
import CategoryCascader from "../../modules/commons/category-cascader";
import BrandSelect from "../../modules/commons/brand-select";
// import Spuinfo from "./spuinfo";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: { CategoryCascader, BrandSelect },
  props: {},
  data() {
    //这里存放数据
    return {
      catId: 0,
      catelogPath: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: undefined,
        key: undefined,
        brandId: undefined,
        catelogId: undefined,
      },
      catPathSub: null,
      brandIdSub: null,

      dataSub: null,
      form: {},
      dataList: [],
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
    };
  },
  //计算属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    resetQueryForm() {
      this.queryParams.status = undefined;
      this.queryParams.key = undefined;
      this.queryParams.brandId = undefined;
      this.queryParams.catelogId = undefined;
    },

    resetQuery() {
      //window.location.reload();
      this.resetQueryForm();
      this.getDataList();
    },
    getDataList() {
      console.log(this.queryParams);
      this.dataListLoading = true;
      getSpuInfo(this.queryParams).then((res) => {
        this.dataList = res.data.list;
        this.total = res.data.total;
        this.dataListLoading = false;
      });
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },

    searchSpuInfo() {
      console.log("搜索条件", this.queryParams);

      this.getDataList();
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getDataList();
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.catPathSub = PubSub.subscribe("catPath", (msg, val) => {
      this.queryParams.catelogId = val[val.length - 1];
    });
    this.brandIdSub = PubSub.subscribe("brandId", (msg, val) => {
      this.queryParams.brandId = val;
    });
  },
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {
    PubSub.unsubscribe(this.catPathSub);
    PubSub.unsubscribe(this.brandIdSub);
  }, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {}, //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style scoped>
</style>