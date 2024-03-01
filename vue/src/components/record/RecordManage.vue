<template>
  <div>
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadGet"></el-input>
      <el-select v-model="storage" placeholder="请选择仓库" style="margin-left: 5px;">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>

      <el-select v-model="goodstype" placeholder="请选择分类" style="margin-left: 5px;">
        <el-option
            v-for="item in goodstypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 5px;" @click="loadGet">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="add">新增</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background: '#f2f5fc' , color: '#555555'}"
              border>
      <el-table-column prop="id" label="Id" width="60">
      </el-table-column>

      <el-table-column prop="goodsname" label="物品名" width="180">
      </el-table-column>
      <el-table-column prop="storagename" label="仓库" width="180">
      </el-table-column>
      <el-table-column prop="goodstypename" label="分类" width="180">
      </el-table-column>
      <el-table-column prop="adminname" label="操作人" width="180">
      </el-table-column>
      <el-table-column prop="username" label="申请人" width="180">
      </el-table-column>
      <el-table-column prop="createtime" label="操作时间" width="180">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>
      <el-table-column prop="operate" label="操作" width="180">

      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :page-sizes="[2, 5, 10]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: "RecordsManage",
  data() {

    return {
      storageData:[],
      goodstypeData:[],
      storage:'',
      goodstype:'',
      tableData: [],
      pageNum: 1,
      pageSize: 2,
      total: 0,
      name: "",

      centerDialogVisible: false,
      form: {
        id: "",
        name: "",
        remark: "",
        count: 0,
        goodstype: '',
        storage: '',
      },
    }
  },
  methods: {
    formatStorage(row){
      let temp = this.storageData.find(item=>{
        return item.id == row.storage
      })
      return temp && temp.name
    },
    formatGoodstype(row){
      let temp = this.goodstypeData.find(item=>{
        return item.id == row.goodstype
      })
      return temp && temp.name
    },
    resetForm() {
      this.$refs.form.resetFields()
    },
    add() {
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm();
        this.form.id = '';
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.pageSize = val
      this.pageNum = 1
      this.loadGet()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val
      this.loadGet()
    },
    loadGet() {
      this.$axios.post('http://localhost:8088/record/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          name : this.name,
          storage: this.storage+'',
          goodstype: this.goodstype+'',
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.tableData = res.data
          this.total = res.total
        } else {
          alert("获取失败")
        }
      })
    },
    loadStorage() {
      this.$axios.get('http://localhost:8088/storage/list').then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.storageData = res.data
        } else {
          alert("获取失败")
        }
      })
    },
    loadGoodstype() {
      this.$axios.get('http://localhost:8088/goodstype/list').then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.goodstypeData = res.data
        } else {
          alert("获取失败")
        }
      })
    },
    resetParam() {
      this.name = ''
      this.goodstype = ''
      this.storage = ''
      this.loadGet()
    },
  },
  created() {
    this.loadGet()
    this.loadStorage()
    this.loadGoodstype()
  }
}
</script>

<style scoped>

</style>
