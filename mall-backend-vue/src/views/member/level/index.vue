<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="等级名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入等级名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="默认等级" prop="defaultStatus">
        <el-select
          v-model="queryParams.defaultStatus"
          placeholder="请选择是否为默认等级"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in isDefaultLevel"
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
          v-hasPermi="['member:level:add']"
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
          v-hasPermi="['member:level:edit']"
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
          v-hasPermi="['member:level:remove']"
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
          v-hasPermi="['member:level:export']"
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
      :data="levelList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="等级名称" align="center" prop="name" />
      <el-table-column label="所需成长值" align="center" prop="growthPoint" />
      <el-table-column label="默认等级" align="center" prop="defaultStatus">
        <template slot-scope="scope">
          <i class="el-icon-success" v-if="scope.row.defaultStatus == 1"></i>
          <i class="el-icon-error" v-else></i>
        </template>
      </el-table-column>
      <el-table-column
        label="免运费标准"
        align="center"
        prop="freeFreightPoint"
      />
      <el-table-column
        label="每次评价获取的成长值"
        align="center"
        prop="commentGrowthPoint"
      />
      <el-table-column label="特权" align="center">
        <el-table-column
          label="免邮特权"
          align="center"
          prop="priviledgeFreeFreight"
        >
          <template slot-scope="scope">
            <i
              class="el-icon-success"
              v-if="scope.row.priviledgeFreeFreight == 1"
            ></i>
            <i class="el-icon-error" v-else></i>
          </template>
        </el-table-column>
        <el-table-column
          label="会员价格特权"
          align="center"
          prop="priviledgeMemberPrice"
        >
          <template slot-scope="scope">
            <i
              class="el-icon-success"
              v-if="scope.row.priviledgeMemberPrice == 1"
            ></i>
            <i class="el-icon-error" v-else></i>
          </template>
        </el-table-column>
        <el-table-column
          label="生日特权"
          align="center"
          prop="priviledgeBirthday"
        >
          <template slot-scope="scope">
            <i
              class="el-icon-success"
              v-if="scope.row.priviledgeBirthday == 1"
            ></i>
            <i class="el-icon-error" v-else></i>
          </template>
        </el-table-column>
      </el-table-column>

      <el-table-column label="备注" align="center" prop="note" />
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
            v-hasPermi="['member:level:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['member:level:remove']"
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

    <!-- 添加或修改会员等级对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="550px" append-to-body>
        <el-row>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-col :span="24">
            <el-form-item label="等级名称" prop="name">
              <el-input v-model="form.name" placeholder="等级名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="所需成长值" prop="growthPoint">
              <el-input-number
                v-model="form.growthPoint"
                :min="0"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="默认等级" prop="defaultStatus">
              <el-checkbox
                v-model="form.defaultStatus"
                :true-label="1"
                :false-label="0"
              ></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="免运费标准" prop="freeFreightPoint">
              <el-input-number
                :min="0"
                v-model="form.freeFreightPoint"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="每次评价获取的成长值"
              prop="commentGrowthPoint"
            >
              <el-input-number
                :min="0"
                v-model="form.commentGrowthPoint"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="免邮特权" prop="priviledgeFreeFreight">
              <el-checkbox
                v-model="form.priviledgeFreeFreight"
                :true-label="1"
                :false-label="0"
              ></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="会员价格特权" prop="priviledgeMemberPrice">
              <el-checkbox
                v-model="form.priviledgeMemberPrice"
                :true-label="1"
                :false-label="0"
              ></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="生日特权" prop="priviledgeBirthday">
              <el-checkbox
                v-model="form.priviledgeBirthday"
                :true-label="1"
                :false-label="0"
              ></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="note">
              <el-input v-model="form.note" placeholder="备注"></el-input>
            </el-form-item>
          </el-col>
       
      </el-form>
       </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listLevel,
  getLevel,
  delLevel,
  addLevel,
  updateLevel,
} from "@/api/member/level";

export default {
  name: "Level",
  components: {},
  data() {
    return {
      //默认等级状态
      isDefaultLevel: [],
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
      // 会员等级表格数据
      levelList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        growthPoint: null,
        defaultStatus: null,
        freeFreightPoint: null,
        commentGrowthPoint: null,
        priviledgeFreeFreight: null,
        priviledgeMemberPrice: null,
        priviledgeBirthday: null,
        note: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {name: [
            { required: true, message: '等级名称不能为空', trigger: 'blur' }
          ],
          growthPoint: [
            { required: true, message: '等级需要的成长值不能为空', trigger: 'blur' }
          ],
          defaultStatus: [
            { required: true, message: '是否为默认等级[0->不是；1->是]不能为空', trigger: 'blur' }
          ],
          freeFreightPoint: [
            { required: true, message: '免运费标准不能为空', trigger: 'blur' }
          ],
          commentGrowthPoint: [
            { required: true, message: '每次评价获取的成长值不能为空', trigger: 'blur' }
          ],
          priviledgeFreeFreight: [
            { required: true, message: '是否有免邮特权不能为空', trigger: 'blur' }
          ],
          priviledgeMemberPrice: [
            { required: true, message: '是否有会员价格特权不能为空', trigger: 'blur' }
          ],
          priviledgeBirthday: [
            { required: true, message: '是否有生日特权不能为空', trigger: 'blur' }
          ],
          note: [
            { required: true, message: '备注不能为空', trigger: 'blur' }
          ]},
    };
  },
  created() {
    this.getList();

    this.getDicts("member_level_type").then((response) => {
      this.isDefaultLevel = response.data;
    });
  },
  methods: {
    /** 查询会员等级列表 */
    getList() {
      this.loading = true;
      listLevel(this.queryParams).then((response) => {
        this.levelList = response.data.list;
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
        name: null,
        growthPoint: null,
        defaultStatus: 0,
        freeFreightPoint: null,
        commentGrowthPoint: null,
        priviledgeFreeFreight: null,
        priviledgeMemberPrice: null,
        priviledgeBirthday: null,
        note: null,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加会员等级";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getLevel(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改会员等级";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateLevel(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLevel(this.form).then((response) => {
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
        '是否确认删除会员等级编号为"' + ids + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delLevel(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "member/level/export",
        {
          ...this.queryParams,
        },
        `member_level.xlsx`
      );
    },
  },
};
</script>