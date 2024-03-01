<template>
  <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside :width="aside_width" style="background-color: rgb(238, 241, 246);height: 100%;margin-left: -1px">
      <Aside :isCollapse="isCollapse">
      </Aside>
    </el-aside>

    <el-container style="height: 100%">
      <el-header style="text-align: right ;height: 100%; font-size: 12px;border-bottom: rgba(169,169,169,0.3) 1px solid;">
        <Header @doCollapse="doCollapse" :icon = "icon">
        </Header>
      </el-header>

      <el-main style="height: 100%">
<!--       <Main></Main>-->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>
<style>
.el-header {
  #background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
.el-main{
  padding: 5px;
}
</style>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";
import Main from "@/components/Main";
export default {
  components: {Main, Header, Aside},
  methods:{
    doCollapse(){
      this.isCollapse=!this.isCollapse
      if(this.isCollapse){
        this.aside_width = '64px'
        this.icon = "el-icon-s-unfold";
      }
      else{
        this.aside_width = '200px'
        this.icon = "el-icon-s-fold";
      }
    }
  },
  data(){
      return{
        aside_width:'200px',
        isCollapse: false,
        icon:"el-icon-s-fold",
      }
  }
};
</script>
