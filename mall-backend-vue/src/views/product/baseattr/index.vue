<template>
  <el-row :gutter="20">
    <br />
    <el-col :span="6" :offset="1">
      <category @tree-node-click="treeNodeClick"></category>
    </el-col>
    <el-col :span="17">
      <el-form
        :model="queryParams"
        ref="queryForm"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
      >
        <el-form-item label="属性名" prop="attrName">
          <el-input
            v-model="queryParams.attrName"
            placeholder="请输入属性名"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="属性类型" prop="attrType">
          <el-select
            v-model="queryParams.attrType"
            placeholder="请选择属性类型"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in attrOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery"
            >搜索</el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
<el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['product:attr:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['product:attr:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['product:attr:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['product:attr:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
      <el-table
        v-loading="loading"
        :data="attrList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="属性id" align="center" prop="attrId" />
        <el-table-column label="属性名" align="center" prop="attrName" />
        <el-table-column
          label="是否需要检索"
          align="center"
          prop="searchType"
        />
        <el-table-column label="属性图标" align="center" prop="icon" />
        <el-table-column
          label="可选值列表"
          align="center"
          prop="valueSelect"
        />
        <el-table-column
          label="属性类型"
          align="center"
          prop="attrType"
        />
        <el-table-column
          label="启用状态"
          align="center"
          prop="enable"
        />
        <el-table-column label="所属分类" align="center" prop="catelogId" />
        <el-table-column
          label="快速展示"
          align="center"
          prop="showDesc"
        />
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['product:attr:edit']"
              >修改</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['product:attr:remove']"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-col>
     <!-- 添加或修改商品属性对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="属性名" prop="attrName">
          <el-input v-model="form.attrName" placeholder="请输入属性名" />
        </el-form-item>
        <el-form-item label="是否需要检索[0-不需要，1-需要]" prop="searchType">
          <el-select v-model="form.searchType" placeholder="请选择是否需要检索[0-不需要，1-需要]">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="属性图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入属性图标" />
        </el-form-item>
        <el-form-item label="可选值列表[用逗号分隔]" prop="valueSelect">
          <el-input v-model="form.valueSelect" placeholder="请输入可选值列表[用逗号分隔]" />
        </el-form-item>
        <el-form-item label="属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]" prop="attrType">
          <el-select v-model="form.attrType" placeholder="请选择属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态[0 - 禁用，1 - 启用]" prop="enable">
          <el-input v-model="form.enable" placeholder="请输入启用状态[0 - 禁用，1 - 启用]" />
        </el-form-item>
        <el-form-item label="所属分类" prop="catelogId">
          <el-input v-model="form.catelogId" placeholder="请输入所属分类" />
        </el-form-item>
        <el-form-item label="快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整" prop="showDesc">
          <el-input v-model="form.showDesc" placeholder="请输入快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </el-row>

  
</template>

<script>
import Category from "../../modules/commons/category.vue";
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import {
  listAttr,
  getAttr,
  delAttr,
  addAttr,
  updateAttr,
} from "@/api/product/attr";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: { Category },
  props: {},
  data() {
    //这里存放数据
    return {
      catId: 0,
      // 状态数据字典
      attrOptions: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 商品属性表格数据
      attrList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        attrName: null,
        searchType: null,
        icon: null,
        valueSelect: null,
        attrType: null,
        enable: null,
        catelogId: null,
        showDesc: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  //计算属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    //感知树节点被点击
    treeNodeClick(data, node, component) {
      if (node.level == 3) {
        this.catId = data.catId;
        console.log(this.catId);
      }
    },
    /** 查询商品属性列表 */
    getList() {
      this.loading = true;
      listAttr(this.queryParams).then((response) => {
        this.attrList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        attrId: null,
        attrName: null,
        searchType: null,
        icon: null,
        valueSelect: null,
        attrType: null,
        enable: null,
        catelogId: null,
        showDesc: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.attrId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加商品属性";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const attrId = row.attrId || this.ids;
      getAttr(attrId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改商品属性";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.attrId != null) {
            updateAttr(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAttr(this.form).then((response) => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const attrIds = row.attrId || this.ids;
      this.$confirm(
        '是否确认删除商品属性编号为"' + attrIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delAttr(attrIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getList();
    this.getDicts("sys_attr_type").then((response) => {
      this.attrOptions = response.data;
    });
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {}, //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style scoped>
</style>