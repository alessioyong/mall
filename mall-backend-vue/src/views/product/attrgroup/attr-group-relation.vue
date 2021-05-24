<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="visible"
      @closed="dialogClose"
    >
      <el-dialog
        width="40%"
        title="选择属性"
        :visible.sync="innerVisible"
        append-to-body
      >
        <div>
          <el-form
            :inline="true"
            :model="queryParams"
            @keyup.enter.native="getDataList()"
          >
            <el-form-item>
              <el-input
                v-model="queryParams.key"
                placeholder="参数名"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="getDataList()">查询</el-button>
            </el-form-item>
          </el-form>
          <el-table
            :data="dataList"
            border
            v-loading="dataListLoading"
            @selection-change="innerSelectionChangeHandle"
            style="width: 100%"
          >
            <el-table-column
              type="selection"
              header-align="center"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="attrId"
              header-align="center"
              align="center"
              label="属性id"
            ></el-table-column>
            <el-table-column
              prop="attrName"
              header-align="center"
              align="center"
              label="属性名"
            ></el-table-column>
            <el-table-column
              prop="icon"
              header-align="center"
              align="center"
              label="属性图标"
            ></el-table-column>
            <el-table-column
              prop="valueSelect"
              header-align="center"
              align="center"
              label="可选值列表"
            ></el-table-column>
          </el-table>
          <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getDataList"
          />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitAddRealtion"
            >确认新增</el-button
          >
        </div>
      </el-dialog>
      <el-row>
        <el-col :span="24">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="addRelation"
            >新建关联</el-button
          >
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            @click="batchDeleteRelation"
            :disabled="dataListSelections.length <= 0"
            >批量删除</el-button
          >
          <!--  -->
          <el-table
            :data="relationAttrs"
            style="width: 100%"
            @selection-change="selectionChangeHandle"
            border
          >
            <el-table-column
              type="selection"
              header-align="center"
              align="center"
              width="50"
            ></el-table-column>
            <el-table-column prop="attrId" label="#"></el-table-column>
            <el-table-column prop="attrName" label="属性名"></el-table-column>
            <el-table-column prop="valueSelect" label="可选值">
              <template slot-scope="scope">
                <el-tooltip placement="top">
                  <div slot="content">
                    <span
                      v-for="(i, index) in scope.row.valueSelect.split(';')"
                      :key="index"
                    >
                      {{ i }}
                      <br />
                    </span>
                  </div>
                  <el-tag>{{
                    scope.row.valueSelect.split(";")[0] + " ..."
                  }}</el-tag>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column
              fixed="right"
              header-align="center"
              align="center"
              label="操作"
            >
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="small"
                  @click="relationRemove(scope.row.attrId)"
                  >移除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import {
  getAttrByGroupId,
  delGroupRelation,
  listNoAttrRelation,
  addGroupRelation,
} from "@/api/product/group";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  data() {
    //这里存放数据
    return {
      attrGroupId: 0,
      visible: false,
      innerVisible: false,
      relationAttrs: [],
      dataListSelections: [],
      dataForm: {
        key: "",
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      total: 0,
      dataListLoading: false,
      innerdataListSelections: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        key: "",
      },
    };
  },
  //计算属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    innerSelectionChangeHandle(val) {
      this.innerdataListSelections = val;
    },
    addRelation() {
      this.queryParams.pageNum = 1;
      (this.queryParams.pageSize = 10), (this.queryParams.key = null);
      this.getDataList();
      this.innerVisible = true;
    },
    batchDeleteRelation(val) {
      let postData = [];
      this.dataListSelections.forEach((item) => {
        postData.push({ attrId: item.attrId, attrGroupId: this.attrGroupId });
      });
      delGroupRelation(postData).then((res) => {
        this.msgSuccess("删除成功！");
        this.init(this.attrGroupId);
      });
      // this.$http({
      //   url: this.$http.adornUrl("/product/attrgroup/attr/relation/delete"),
      //   method: "post",
      //   data: this.$http.adornData(postData, false)
      // }).then(({ data }) => {
      //   if (data.code == 0) {
      //     this.$message({ type: "success", message: "删除成功" });
      //     this.init(this.attrGroupId);
      //   } else {
      //     this.$message({ type: "error", message: data.msg });
      //   }
      // });
    },
    //移除关联
    relationRemove(attrId) {
      let data = [];
      data.push({ attrId, attrGroupId: this.attrGroupId });
      console.log(data);
      delGroupRelation(data).then((res) => {
        this.msgSuccess("删除成功！");
        this.init(this.attrGroupId);
      });
      // this.$http({
      //   url: this.$http.adornUrl("/product/attrgroup/attr/relation/delete"),
      //   method: "post",
      //   data: this.$http.adornData(data, false)
      // }).then(({ data }) => {
      //   if (data.code == 0) {
      //     this.$message({ type: "success", message: "删除成功" });
      //     this.init(this.attrGroupId);
      //   } else {
      //     this.$message({ type: "error", message: data.msg });
      //   }
      // });
    },
    submitAddRealtion() {
      this.innerVisible = false;
      //准备数据
      console.log("准备新增的数据", this.innerdataListSelections);
      if (this.innerdataListSelections.length > 0) {
        console.log(111)
        let postData = [];
        this.innerdataListSelections.forEach((item) => {
          postData.push({ attrId: item.attrId, attrGroupId: this.attrGroupId });
        });
        console.log(postData)
        addGroupRelation(postData).then((res) => {
          this.msgSuccess("添加成功!");
          this.init(this.attrGroupId);
        });
        // this.$http({
        //   url: this.$http.adornUrl("/product/attrgroup/attr/relation"),
        //   method: "post",
        //   data: this.$http.adornData(postData, false)
        // }).then(({ data }) => {
        //   if (data.code == 0) {
        //     this.$message({ type: "success", message: "新增关联成功" });
        //   }
        //   this.$emit("refreshData");
        //   this.init(this.attrGroupId);
        // });
      } else {
      }
    },
    init(id) {
      this.attrGroupId = id || 0;
      this.visible = true;
      getAttrByGroupId(this.attrGroupId).then((res) => {
        console.log(res);
        this.relationAttrs = res.data;
      });
      // this.$http({
      //   url: this.$http.adornUrl(
      //     "/product/attrgroup/" + this.attrGroupId + "/attr/relation"
      //   ),
      //   method: "get",
      //   params: this.$http.adornParams({})
      // }).then(({ data }) => {
      //   this.relationAttrs = data.data;
      // });
    },
    dialogClose() {},

    //========
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      listNoAttrRelation(this.queryParams, this.attrGroupId).then((res) => {
        console.log(res);
        this.dataList = res.data.list;
        this.total = res.data.total;
        this.dataListLoading = false;
      });
      // this.$http({
      //   url: this.$http.adornUrl(
      //     "/product/attrgroup/" + this.attrGroupId + "/noattr/relation"
      //   ),
      //   method: "get",
      //   params: this.$http.adornParams({
      //     page: this.pageIndex,
      //     limit: this.pageSize,
      //     key: this.dataForm.key
      //   })
      // }).then(({ data }) => {
      //   if (data && data.code === 0) {
      //     this.dataList = data.page.list;
      //     this.totalPage = data.page.totalCount;
      //   } else {
      //     this.dataList = [];
      //     this.totalPage = 0;
      //   }
      //   this.dataListLoading = false;
      // });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
  },
};
</script>
<style scoped>
</style>