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
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span>
              <el-button v-if="node.level<=2" type="text" size="mini" @click="() => append(data)">Append</el-button>
              <el-button v-if="node.childNodes.length==0" type="text" size="mini" @click="() => remove(node, data)">Delete</el-button>
            </span>
          </span>
        </el-tree>
      </el-col>
    </el-row>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { listWithTree } from "@/api/product/category";
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
        console.log("数据", res.data);
        this.menus = res.data;
      });
    },
    append(data){
        console.log("append",data)
    },

    remove(node,data){
        console.log("remove",node,data)
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
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