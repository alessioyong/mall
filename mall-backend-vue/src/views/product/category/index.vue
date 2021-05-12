<template>
  <div class="app-container">
    <el-row>
      <el-col :offset="1">
        <el-tree
          :data="menus"
          :props="defaultProps"
          :expand-on-click-node="false"
          show-checkbox
          node-key="catId"
          :default-expanded-keys="expandedKey"
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span>
              <el-button
                v-if="node.level <= 2"
                type="text"
                size="mini"
                @click="() => append(data)"
                >Append</el-button
              >
              <el-button
                v-if="node.childNodes.length == 0"
                type="text"
                size="mini"
                @click="() => remove(node, data)"
                >Delete</el-button
              >
              <el-button
                type="text"
                size="mini"
                @click="() => edit(data)"
                >edit</el-button
              >
            </span>
          </span>
        </el-tree>
      </el-col>
    </el-row>

    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%">
      <el-form :model="category">
        <el-form-item label="分类名称">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input
            v-model="category.productUnit"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitData">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import {
  listWithTree,
  removeTree,
  addCategory,
  getCategory,
  editCategory,
} from "@/api/product/category";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  data() {
    //这里存放数据
    return {
      menus: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      expandedKey: [],
      dialogVisible: false,
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        productUnit: "",
        icon: "",
        catId: null,
      },
      title:"",
      titleType:"",

    };
  },
  //计算属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    getMenus() {
      listWithTree().then((res) => {
        this.menus = res.data;
      });
    },

    submitData(){
        if(this.titleType=="add"){
            this.add();
        }
        if(this.titleType=="edit"){
            var {catId,name,icon,productUnit}=this.category;
            editCategory({catId,name,icon,productUnit}).then((res)=>{
                this.msgSuccess("修改成功！");
                this.dialogVisible=false;
                this.getMenus();
                this.expandedKey=[this.category.parentCid]
            })

        }
    },
    edit(data){
      this.title="修改分类";
      this.titleType="edit";
      this.dialogVisible=true;
      getCategory(data.catId).then((res)=>{
          //console.log("回显数据：",res.data);
          this.category.name=res.data.name;
          this.category.sort=res.data.sort;
          this.category.catId=res.data.catId;
          this.category.icon=res.data.icon;
          this.category.productUnit=res.data.productUnit;
          this.category.parentCid=data.parentCid;
          //console.log("===>",this.category)
      })

    },


    append(data) {
      this.title="添加分类";
      this.titleType="add";
      this.dialogVisible = true;
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1;
    },
    add() {
      addCategory(this.category).then((res) => {
        this.msgSuccess("添加成功！");
        this.dialogVisible = false;
        this.getMenus();
        this.expandedKey = [this.category.parentCid];
      });
    },

    remove(node, data) {
      var ids = [data.catId];
      this.$confirm("是否确认删除【" + data.name + "】?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return removeTree(ids);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          //重新刷新菜单
          this.getMenus();
          //设置默认展开的菜单
          this.expandedKey = [node.parent.data.catId];
        })
        .catch(() => {});
    },
  }, //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getMenus();
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