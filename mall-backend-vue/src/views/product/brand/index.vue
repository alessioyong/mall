<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="品牌名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入品牌名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示状态" prop="showStatus">
        <el-select
          v-model="queryParams.showStatus"
          placeholder="请选择显示状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="检索首字母" prop="firstLetter">
        <el-input
          v-model="queryParams.firstLetter"
          placeholder="请输入检索首字母"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="排序" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="请输入排序"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
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
          v-hasPermi="['product:brand:add']"
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
          v-hasPermi="['product:brand:edit']"
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
          v-hasPermi="['product:brand:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['product:brand:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="brandList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="品牌id" align="center" prop="brandId" />
      <el-table-column label="品牌名" align="center" prop="name" />
      <el-table-column label="品牌logo" align="center" prop="logo">
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.logo"
            fit="fit"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column label="介绍" align="center" prop="descript" />
      <el-table-column label="显示状态" align="center" prop="showStatus">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.showStatus"
            active-color="#12ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0"
            @change="updateBrandStatus(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="检索首字母" align="center" prop="firstLetter" />
      <el-table-column label="排序" align="center" prop="sort" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-plus-outline"
            @click="updateCatelogHandle(scope.row.brandId)"
            >关联分类</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['product:brand:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['product:brand:remove']"
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

    <!-- 添加或修改品牌对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="品牌名" prop="name">
          <el-input v-model="form.name" placeholder="请输入品牌名" />
        </el-form-item>
        <el-form-item label="品牌logo地址" prop="logo">
          <!-- <el-input
            v-model="form.logo"
            type="textarea"
            placeholder="请输入内容"
          /> -->
          <single-upload v-model="form.logo"></single-upload>
        </el-form-item>
        <el-form-item label="介绍" prop="descript">
          <el-input
            v-model="form.descript"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
        <el-form-item label="显示状态">
          <!-- <el-radio-group v-model="form.showStatus">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
              >{{ dict.dictLabel }}</el-radio
            >
          </el-radio-group> -->
          <el-switch
            v-model="form.showStatus"
            active-color="#12ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0"
          ></el-switch>
        </el-form-item>
        <el-form-item label="检索首字母" prop="firstLetter">
          <el-input v-model="form.firstLetter" placeholder="请输入检索首字母" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <!-- <el-input v-model="form.sort" placeholder="请输入排序" /> -->
          <el-input-number
            v-model="form.sort"
            @change="handleChange"
            :min="0"
            label="描述文字"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改属性&属性分组关联对话框 -->
    <el-dialog
      title="关联分类"
      :visible.sync="cateRelationDialogVisible"
      width="500px"
    >
      <el-popover placement="right-end" v-model="popCatelogSelectVisible">
        <category-cascader :catelogPath.sync="catelogPath"></category-cascader>
        <div style="text-align: right; margin: 0">
          <el-button
            size="mini"
            type="text"
            @click="popCatelogSelectVisible = false"
            >取消</el-button
          >
          <el-button type="primary" size="mini" @click="addCatelogSelect"
            >确定</el-button
          >
        </div>
        <el-button slot="reference">新增关联</el-button>
      </el-popover>
      <el-table :data="cateRelationTableData" style="width: 100%">
        <el-table-column prop="id" label="#"></el-table-column>
        <el-table-column prop="brandName" label="品牌名"></el-table-column>
        <el-table-column prop="catelogName" label="分类名"></el-table-column>
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
              @click="deleteCateRelationHandle(scope.row.id, scope.row.brandId)"
              >移除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cateRelationDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="cateRelationDialogVisible = false"
          >确 定</el-button
        >
      </span>
      <pagination
        v-show="relationTotal > 0"
        :total="relationTotal"
        :page.sync="query.pageNum"
        :limit.sync="query.pageSize"
        @pagination="getCateRelation"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  listBrand,
  getBrand,
  delBrand,
  addBrand,
  updateBrand,
  updateBrandStatus,
} from "@/api/product/brand";
import { listRelation, addRelation, delRelation } from "@/api/product/relation";
import SingleUpload from "@/components/upload/singleUpload";
import CategoryCascader from "../../modules/commons/category-cascader.vue";
//import SingleUpload from '../../../components/upload/singleUpload.vue';
export default {
  name: "Brand",
  components: { SingleUpload, CategoryCascader },
  data() {
    return {
      cateRelationTableData: [],
      catelogPath: [],
      brandId: 0,
      relationTotal: 0,
      popCatelogSelectVisible: false,
      statusOptions: [],
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
      // 品牌表格数据
      brandList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      cateRelationDialogVisible: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        logo: null,
        descript: null,
        showStatus: null,
        firstLetter: null,
        sort: null,
      },
      query: {
        pageNum: 1,
        pageSize: 10,
        brandId: 0,
      },
      // 表单参数
      form: {
        sort: 0,
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "请输入品牌名称", trigger: "blur" },
          { max: 10, message: "长度不超过 10 个字符", trigger: "blur" },
        ],
        descript: [
          { required: true, message: "请输入品牌介绍", trigger: "blur" },
        ],
        firstLetter: [
          {
            validator: (rule, value, callback) => {
              if (!value) {
                return callback(new Error("检索首字母不能为空"));
              } else if (!/^[a-z|A-Z]$/.test(value)) {
                return callback(
                  new Error("检索首字母只能是一个a-z或A-Z的字母")
                );
              } else {
                return callback();
              }
            },
            trigger: "blur",
          },
        ],
        sort: [
          {
            validator: (rule, value, callback) => {
              if (!value) {
                return callback(new Error("排序字段不能为空"));
              } else if (!Number.isInteger(value) && value >= 0) {
                return callback(new Error("排序字段必须是一个大于等于0的整数"));
              } else {
                return callback();
              }
            },
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_brand_status").then((response) => {
      this.statusOptions = response.data;
      //console.log(this.showStatus);
    });
  },
  methods: {
    addCatelogSelect() {
      this.popCatelogSelectVisible = false;
      addRelation({
        brandId: this.brandId,
        catelogId: this.catelogPath[this.catelogPath.length - 1],
      }).then((res) => {
        this.getCateRelation();
        this.msgSuccess("添加成功！");
      });
    },

    deleteCateRelationHandle(id, brandId) {
      delRelation(id).then((res)=>{
        this.getCateRelation();
        this.msgSuccess("删除成功！");
      })
    },

    updateCatelogHandle(brandId) {
      this.catelogPath=[]
      this.cateRelationDialogVisible = true;
      this.brandId = brandId;
      this.query.brandId = brandId;
      this.getCateRelation();
    },
    getCateRelation() {
      listRelation(this.query).then((res) => {
        this.cateRelationTableData = res.data.list;
        this.relationTotal = res.data.total;
      });
    },
    handleChange() {},
    /** 查询品牌列表 */
    getList() {
      this.loading = true;
      listBrand(this.queryParams).then((response) => {
        this.brandList = response.data.list;
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
        brandId: null,
        name: null,
        logo: null,
        descript: null,
        showStatus: 0,
        firstLetter: null,
        sort: null,
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
      this.ids = selection.map((item) => item.brandId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加品牌";
      



    },
    updateBrandStatus(data) {
      console.log(data);
      let { brandId, showStatus } = data;
      updateBrandStatus({ brandId, showStatus: showStatus ? 1 : 0 }).then((res) => {
        this.msgSuccess("修改状态成功！");
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const brandId = row.brandId || this.ids;
      getBrand(brandId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改品牌";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.brandId != null) {
            updateBrand(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBrand(this.form).then((response) => {
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
      const brandIds = row.brandId || this.ids;
      this.$confirm(
        '是否确认删除品牌编号为"' + brandIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delBrand(brandIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "product/brand/export",
        {
          ...this.queryParams,
        },
        `product_brand.xlsx`
      );
    },
  },
};
</script>