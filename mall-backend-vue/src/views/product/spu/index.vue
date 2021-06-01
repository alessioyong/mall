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
          <el-tag v-if="scope.row.publishStatus == 1" type="success"
            >已上架</el-tag
          >
          <el-tag v-if="scope.row.publishStatus == 2" type="danger"
            >已下架</el-tag
          >
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

    <el-dialog title="修改规格" :visible.sync="dialogFormVisible">
      <el-card class="box-card">
        <el-tabs tab-position="left" style="width: 98%">
          <el-tab-pane
            :label="group.attrGroupName"
            v-for="(group, gidx) in dataResp.attrGroups"
            :key="group.attrGroupId"
          >
            <!-- 遍历属性,每个tab-pane对应一个表单，每个属性是一个表单项  spu.baseAttrs[0] = [{attrId:xx,val:}]-->
            <el-form ref="form" :model="dataResp">
              <el-form-item
                :label="attr.attrName"
                v-for="(attr, aidx) in group.attrs"
                :key="attr.attrId"
              >
                <el-input
                  v-model="dataResp.baseAttrs[gidx][aidx].attrId"
                  type="hidden"
                  v-show="false"
                ></el-input>
                <el-select
                  v-model="dataResp.baseAttrs[gidx][aidx].attrValues"
                  :multiple="attr.valueType == 1"
                  filterable
                  allow-create
                  default-first-option
                  placeholder="请选择或输入值"
                >
                  <el-option
                    v-for="(val, vidx) in attr.valueSelect.split(';')"
                    :key="vidx"
                    :label="val"
                    :value="val"
                  ></el-option>
                </el-select>
                <el-checkbox
                  v-model="dataResp.baseAttrs[gidx][aidx].showDesc"
                  :true-label="1"
                  :false-label="0"
                  >快速展示</el-checkbox
                >
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        <div style="margin: auto">
          <el-button type="success" style="float: right" @click="submitSpuAttrs">确认修改</el-button>
        </div>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { getSpuInfo, UpSpuInfo } from "@/api/product/spuinfo";
import { getAttrGroupWithAttrs } from "@/api/product/group";
import { attrlistforspu,updateAttrBySpuId } from "@/api/product/attr";
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
      dialogFormVisible: false,
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
      spuId: "",
      catalogId: "",
      dataResp: {
        //后台返回的所有数据
        attrGroups: [],
        baseAttrs: [],
      },
      spuAttrsMap: {},
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

    productUp(id) {
      UpSpuInfo(id).then((res) => {
        this.getDataList();
        this.msgSuccess("操作成功！");
      });
    },
    clearData() {
      this.dataResp.attrGroups = [];
      this.dataResp.baseAttrs = [];
      this.spuAttrsMap = {};
    },
    getSpuBaseAttrs() {
      attrlistforspu(this.spuId).then((res) => {
        res.data.forEach((item) => {
          this.spuAttrsMap["" + item.attrId] = item;
        });
        console.log("~~~~", this.spuAttrsMap);
      });
    },
    showBaseAttrs() {
      let _this = this;
      getAttrGroupWithAttrs(this.catalogId).then((data) => {
        //先对表单的baseAttrs进行初始化
        data.data.forEach((item) => {
          let attrArray = [];
          item.attrs.forEach((attr) => {
            let v = "";
            if (_this.spuAttrsMap["" + attr.attrId]) {
              v = _this.spuAttrsMap["" + attr.attrId].attrValue.split(";");
              if (v.length == 1) {
                v = v[0] + "";
              }
            }
            attrArray.push({
              attrId: attr.attrId,
              attrName: attr.attrName,
              attrValues: v,
              showDesc: _this.spuAttrsMap["" + attr.attrId]
                ? _this.spuAttrsMap["" + attr.attrId].quickShow
                : attr.showDesc,
            });
          });
          this.dataResp.baseAttrs.push(attrArray);
        });
        this.dataResp.attrGroups = data.data;
      });
    },
    attrUpdateShow(row) {
      this.dialogFormVisible = true;
      this.clearData();
      this.spuId = row.id;
      this.catalogId = row.catalogId;
      if (this.spuId && this.catalogId) {
        this.showBaseAttrs();
        this.getSpuBaseAttrs();
      }
    },
    submitSpuAttrs() {
      console.log("·····", this.dataResp.baseAttrs);
      //spu_id  attr_id  attr_name             attr_value             attr_sort  quick_show
      let submitData = [];
      this.dataResp.baseAttrs.forEach((item) => {
        item.forEach((attr) => {
          let val = "";
          if (attr.attrValues instanceof Array) {
            val = attr.attrValues.join(";");
          } else {
            val = attr.attrValues;
          }

          if (val != "") {
            submitData.push({
              attrId: attr.attrId,
              attrName: attr.attrName,
              attrValue: val,
              quickShow: attr.showDesc,
            });
          }
        });
      });

      this.$confirm("修改商品规格信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          updateAttrBySpuId(submitData,this.spuId).then((res)=>{
            this.msgSuccess("属性修改成功!");
            this.dialogFormVisible=false;
            this.getDataList()
          })
        })
        .catch((e) => {
          this.$message({
            type: "info",
            message: "已取消修改" + e,
          });
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