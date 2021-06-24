<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="仓库">
        <el-select
          style="width: 120px"
          v-model="queryParams.wareId"
          placeholder="请选择仓库"
          clearable
        >
          <el-option
            :label="w.name"
            :value="w.id"
            v-for="w in wareList"
            :key="w.id"
          ></el-option>
        </el-select>
      </el-form-item>
       <el-form-item label="状态">
        <el-select  v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="新建" :value="0"></el-option>
          <el-option label="已分配" :value="1"></el-option>
          <el-option label="正在采购" :value="2"></el-option>
          <el-option label="已完成" :value="3"></el-option>
          <el-option label="采购失败" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="关键字">
        <el-input  v-model="queryParams.key" placeholder="参数名" clearable></el-input>
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
          v-hasPermi="['ware:detail:add']"
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
          v-hasPermi="['ware:detail:edit']"
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
          v-hasPermi="['ware:detail:remove']"
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
          v-hasPermi="['ware:detail:export']"
          > 导出</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-dropdown @command="handleBatchCommand" :disabled="ids.length <= 0">
          <el-button type="danger" size="mini">
            批量操作
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="delete">批量删除</el-dropdown-item>
            <el-dropdown-item command="merge">合并整单</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="detailList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="id"
        align="center"
        prop="id"
      />
      <el-table-column label="采购单id" align="center" prop="purchaseId" />
      <el-table-column label="采购商品id" align="center" prop="skuId" />
      <el-table-column label="采购数量" align="center" prop="skuNum" />
      <el-table-column label="采购金额" align="center" prop="skuPrice" />
      <el-table-column label="仓库id" align="center" prop="wareId" />
     <el-table-column prop="status" header-align="center" align="center" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status==0">新建</el-tag>
          <el-tag type="info" v-if="scope.row.status==1">已分配</el-tag>
          <el-tag type="wanring" v-if="scope.row.status==2">正在采购</el-tag>
          <el-tag type="success" v-if="scope.row.status==3">已完成</el-tag>
          <el-tag type="danger" v-if="scope.row.status==4">采购失败</el-tag>
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
            v-hasPermi="['ware:detail:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ware:detail:remove']"
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

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!-- <el-form-item label="采购单id" prop="purchaseId">
          <el-input v-model="form.purchaseId" placeholder="请输入采购单id" />
        </el-form-item> -->
        <el-form-item label="采购商品id" prop="skuId">
          <el-input v-model="form.skuId" placeholder="请输入采购商品id" />
        </el-form-item>
        <el-form-item label="采购数量" prop="skuNum">
          <el-input v-model="form.skuNum" placeholder="请输入采购数量" />
        </el-form-item>
        <el-form-item label="采购金额" prop="skuPrice">
          <el-input v-model="form.skuPrice" placeholder="请输入采购金额" />
        </el-form-item>
        <el-form-item label="仓库" prop="wareId">
        <el-select v-model="form.wareId" placeholder="请选择仓库" clearable>
          <el-option :label="w.name" :value="w.id" v-for="w in wareList" :key="w.id"></el-option>
        </el-select>
      </el-form-item>
        <!-- <el-form-item
          label="状态[0新建，1已分配，2正在采购，3已完成，4采购失败]"
        >
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <el-dialog title="合并到整单" :visible.sync="mergedialogVisible">
      <!-- id  assignee_id  assignee_name  phone   priority status -->
      <el-select v-model="purchaseId" placeholder="请选择" clearable filterable>
        <el-option
          v-for="item in purchasetableData"
          :key="item.id"
          :label="item.id"
          :value="item.id"
        >
          <span style="float: left">{{ item.id }}</span>
          <span
            style="float: right; color: #8492a6; font-size: 13px"
          >{{ item.assigneeName }}：{{item.phone}}</span>
        </el-option>
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button @click="mergedialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="mergeItem">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  listDetail,
  getDetail,
  delDetail,
  addDetail,
  updateDetail,
} from "@/api/ware/detail";
import { listInfo} from "@/api/ware/info";

export default {
  name: "Detail",
  components: {},
  data() {
    return {
      purchasetableData: [],
      purchaseId: "",
      mergedialogVisible: false,
      dataListSelections: [],
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
      // 【请填写功能名称】表格数据
      detailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        purchaseId: null,
        skuId: null,
        skuNum: null,
        skuPrice: null,
        key:null,
        wareId: null,
        status: null,
      },
      wareList: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
    this.getWareInfo();
  },
  methods: {
    handleBatchCommand(cmd){
      if (cmd == "delete") {
        //this.deleteHandle();
      }
      if (cmd == "merge") {
        if (this.ids.length != 0) {
          //this.getUnreceivedPurchase();
          this.mergedialogVisible = true;
        } else {
          this.$alert("请先选择需要合并的需求", "提示", {
            confirmButtonText: "确定",
            callback: action => {}
          });
        }
      }
    },
    getWareInfo() {
      let params = {
        pageNum: 1,
        pageSize: 10000,
      };
      listInfo(params).then((res) => {
        this.wareList = res.data.list;
      });
    },
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      listDetail(this.queryParams).then((response) => {
        this.detailList = response.data.list;
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
        id: null,
        purchaseId: null,
        skuId: null,
        skuNum: null,
        skuPrice: null,
        wareId: null,
        status: 0,
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
      this.queryParams.status=null;
      this.queryParams.wareId=null;
      this.queryParams.key=null;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加采购需求";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getDetail(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改采购需求";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateDetail(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDetail(this.form).then((response) => {
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
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认删除【请填写功能名称】编号为"' + ids + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delDetail(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "ware/detail/export",
        {
          ...this.queryParams,
        },
        `ware_detail.xlsx`
      );
    },
  },
};
</script>