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
            >新增</el-button
          >
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
            >修改</el-button
          >
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
            >删除</el-button
          >
        </el-col>
        <!-- <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['product:attr:export']"
            >导出</el-button
          >
        </el-col> -->
        <right-toolbar
          :showSearch.sync="showSearch"
          @queryTable="getList"
        ></right-toolbar>
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
          label="可检索"
          align="center"
          prop="searchType"
          v-if="attrtype == 1"
        >
          <template slot-scope="scope">
            <i class="el-icon-success" v-if="scope.row.searchType == 1"></i>
            <i class="el-icon-error" v-else></i>
          </template>
        </el-table-column>
         <el-table-column prop="valueType" header-align="center" align="center" label="值类型">
            <template slot-scope="scope">
              <el-tag type="success" v-if="scope.row.valueType==0">单选</el-tag>
              <el-tag v-else>多选</el-tag>
            </template>
          </el-table-column>
        <el-table-column label="属性图标" align="center" prop="icon" />
        <!-- <el-table-column label="可选值列表" align="center" prop="valueSelect" /> -->
         <el-table-column prop="valueSelect" header-align="center" align="center" label="可选值">
            <template slot-scope="scope">
              <el-tooltip placement="top">
                <div slot="content">
                  <span v-for="(i,index) in scope.row.valueSelect.split(';')" :key="index">{{i}}<br/></span>
                </div>
                <el-tag>{{scope.row.valueSelect.split(";")[0]+" ..."}}</el-tag>
              </el-tooltip>
            </template>
          </el-table-column>
        <el-table-column label="属性类型" align="center" prop="attrType" />
        <!-- <el-table-column label="启用状态" align="center" prop="enable" /> -->
        <el-table-column prop="enable" header-align="center" align="center" label="启用">
            <template slot-scope="scope">
              <i class="el-icon-success" v-if="scope.row.enable==1"></i>
              <i class="el-icon-error" v-else></i>
            </template>
          </el-table-column>
        <el-table-column label="所属分类" align="center" prop="catelogName" />
        <el-table-column
            v-if="attrtype == 1"
            prop="groupName"
            header-align="center"
            align="center"
            label="所属分组"
          ></el-table-column>
        <!-- <el-table-column label="快速展示" align="center" prop="showDesc" /> -->
         <el-table-column v-if="attrtype == 1" prop="showDesc" header-align="center" align="center" label="快速展示">
            <template slot-scope="scope">
              <i class="el-icon-success" v-if="scope.row.showDesc==1"></i>
              <i class="el-icon-error" v-else></i>
            </template>
          </el-table-column>
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
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body @closed="dialogClose">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="属性名" prop="attrName">
          <el-input v-model="form.attrName" placeholder="请输入属性名" />
        </el-form-item>
        <el-form-item label="属性类型" prop="attrType">
          <el-select v-model="form.attrType" placeholder="请选择属性类型">
            <el-option
              v-for="dict in attrOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="值类型" prop="valueType">
          <el-switch
            v-model="form.valueType"
            active-text="允许多个值"
            inactive-text="只能单个值"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :inactive-value="0"
            :active-value="1"
          ></el-switch>
        </el-form-item>
        <el-form-item label="可选值" prop="valueSelect">
          <el-select
            v-model="form.valueSelect"
            multiple
            filterable
            allow-create
            placeholder="请输入内容"
          ></el-select>
        </el-form-item>
        <el-form-item label="属性图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入属性图标" />
        </el-form-item>
        <el-form-item label="所属分类" prop="catelogId">
          <category-cascader
            :catelogPath.sync="catelogPath"
          ></category-cascader>
        </el-form-item>
        <el-form-item label="所属分组" prop="attrGroupId" v-if="type == 1">
          <el-select
            ref="groupSelect"
            v-model="form.attrGroupId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in attrGroups"
              :key="item.attrGroupId"
              :label="item.attrGroupName"
              :value="item.attrGroupId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="可检索" prop="searchType" v-if="type == 1">
          <el-switch
            v-model="form.searchType"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0"
          ></el-switch>
        </el-form-item>
        <el-form-item label="快速展示" prop="showDesc" v-if="type == 1">
          <el-switch
            v-model="form.showDesc"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0"
          ></el-switch>
        </el-form-item>
        <el-form-item label="启用状态" prop="enable">
          <el-switch
            v-model="form.enable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0"
          ></el-switch>
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
import CategoryCascader from "../../modules/commons/category-cascader.vue";
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import {
  listAttr,
  getAttr,
  delAttr,
  addAttr,
  updateAttr,
} from "@/api/product/attr";
import { listGroup } from "@/api/product/group";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: { Category, CategoryCascader },
  props: {
    type: {
      type: Number,
      default: 1,
    },
    attrtype: {
      type: Number,
      default: 1
    }
  },
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
      form: {
        attrId: 0,
        attrName: "",
        searchType: 0,
        valueType: 1,
        icon: "",
        valueSelect: "",
        attrType: 1,
        enable: 1,
        catelogId: "",
        attrGroupId: "",
        showDesc: 0,
      },
      catelogPath: [],
      attrGroups: [],
      // 表单校验
      rules: {
        attrName: [
          { required: true, message: "属性名不能为空", trigger: "blur" },
        ],
        searchType: [
          {
            required: true,
            message: "是否需要检索不能为空",
            trigger: "blur",
          },
        ],
        valueType: [
          {
            required: true,
            message: "值类型不能为空",
            trigger: "blur",
          },
        ],
        icon: [
          { required: true, message: "属性图标不能为空", trigger: "blur" },
        ],
        attrType: [
          {
            required: true,
            message: "属性类型不能为空",
            trigger: "blur",
          },
        ],
        enable: [
          {
            required: true,
            message: "启用状态不能为空",
            trigger: "blur",
          },
        ],
        catelogId: [
          {
            required: true,
            message: "需要选择正确的三级分类数据",
            trigger: "blur",
          },
        ],
        showDesc: [
          {
            required: true,
            message: "快速展示不能为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  //计算属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {
    catelogPath(path) {
      //监听到路径变化需要查出这个三级分类的分组信息
      console.log("路径变了", path);
      this.attrGroups = [];
      this.form.attrGroupId = "";
      this.form.catelogId = path[path.length - 1];
      let query = {
        pageNum: 1,
        pageSize: 10000000,
        catelogId: path[path.length - 1],
      };
      if (path && path.length == 3) {
        listGroup(query).then((res) => {
          console.log(res);
          this.attrGroups = res.data.list;
          console.log(this.attrGroups);
        });
      } else if (path.length == 0) {
        this.dataForm.catelogId = "";
      } else {
        this.$message.error("请选择正确的分类");
        this.form.catelogId = "";
      }
    },
  },
  //方法集合
  methods: {
    //感知树节点被点击
    treeNodeClick(data, node, component) {
      if (node.level == 3) {
        this.catId = data.catId;
        this.queryParams.catelogId = this.catId;
        this.getList();
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
        valueType:null,
        icon: null,
        valueSelect: null,
        attrType: null,
        enable: null,
        catelogId: null,
        attrGroupId:null,
        showDesc: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.catelogId = null;
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

      //去查询属性分组

      this.open = true;
      this.title = "添加商品属性";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const attrId = row.attrId || this.ids;
      getAttr(attrId).then((response) => {
        this.form = response.data;
        this.catelogPath=response.data.catelogPath
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
            this.form.valueSelect = this.form.valueSelect.join(";");
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
    dialogClose(){
      this.catelogPath = [];
    }
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