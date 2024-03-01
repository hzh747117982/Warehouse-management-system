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
      <el-button type="primary" style="margin-left: 5px;" @click="inGoods">入库</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="outGoods">出库</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background: '#f2f5fc' , color: '#555555'}"
              highlight-current-row
              @current-change="selectCurrentChange"
              border>
      <el-table-column prop="id" label="Id" width="60">
      </el-table-column>

      <el-table-column prop="name" label="物品名" width="180">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" width="180" :formatter="formatStorage">
      </el-table-column>
      <el-table-column prop="goodstype" label="分类" width="180" :formatter="formatGoodstype">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>
      <el-table-column prop="operate" label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm title="这是一段内容确定删除吗？" @confirm="del(scope.row.id)" style="margin-left: 5px">
            <el-button slot="reference" size="small" type="danger">删除</el-button>
          </el-popconfirm>
        </template>

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
    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">

        <el-form-item label="物品名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="仓库" prop="storage">
          <el-select v-model="form.storage" placeholder="请选择仓库" style="margin-left: 5px;">
            <el-option
                v-for="item in storageData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>

        </el-form-item>
        <el-form-item label="分类" prop="goodstype">
          <el-select v-model="form.goodstype" placeholder="请选择分类" style="margin-left: 5px;">
            <el-option
                v-for="item in goodstypeData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
        title="提示"
        :visible.sync="inDialogVisible"
        width="30%"
        center>

      <el-dialog
              width="75%"
              title="用户选择"
              :visible.sync="innerVisible"
              append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
    <el-button @click="innerVisible = false">取 消</el-button>
    <el-button type="primary" @click="confirmUser">确 定</el-button>
  </span>
      </el-dialog>
      <el-form ref="form1" :rules="rules1" :model="form1" label-width="80px">

        <el-form-item label="物品名">
          <el-input v-model="form1.goodsname" readonly></el-input>
        </el-form-item>
        <el-form-item label="申请人">
          <el-input v-model="form1.username" readonly @click.native="selectUser"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form1.count"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form1.remark"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="inDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doInGoods">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
  import SelectUser from "../user/SelectUser";

export default {
  name: "GoodsManage",
  components: {SelectUser},
  data() {
    let checkCount = (rule,value,callback) =>{
    if(value>9999){
      callback(new Error("数量输入过大"));
    }else{
        callback();
    }
  };
    return {
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      storageData:[],
      goodstypeData:[],
      storage:'',
      goodstype:'',
      tableData: [],
      pageNum: 1,
      pageSize: 2,
      total: 0,
      name: "",
      currentRow:{},
      centerDialogVisible: false,
      inDialogVisible: false,
      innerVisible:false,
      tempUsr:'',
      form: {
        id: "",
        name: "",
        remark: "",
        count: 0,
        goodstype: '',
        storage: '',
      },
      form1:{
        goods:'',
        goodsname:'',
        count:'',
        username:'',
        userid:'',
        adminId:'',
        remark:'',
        action:''
      },
      rules1:{

      },
      rules: {
        name: [
          {required: true, message: '请输入物品名', trigger: 'blur'},
        ],
        goodstype:[
          {required: true, message: '请选择分类', trigger: 'blur'},
        ],
        storage:[
          {required: true, message: '请选择仓库', trigger: 'blur'},
        ],
        count: [
          {required: true, message: '请输入数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数', trigger: "blur"},
          {validator: checkCount, trigger: 'blur'},
        ]
      }
    }
  },
  methods: {
    doSelectUser(val){
      console.log(val)
      this.tempUser = val;
    },
    confirmUser(){
      this.form1.username = this.tempUser.name
      this.form1.userid = this.tempUser.id
      this.innerVisible = false
    },
    selectUser(){
      this.innerVisible = true
    },
    selectCurrentChange(val) {
      this.currentRow = val;
    },
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
    resetInForm() {
      this.$refs.form1.resetFields()
    },
    add() {
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm();
        this.form.id = '';
      })
    },
    inGoods(){
       if(!this.currentRow.id){
         alert("请选择记录")
         return
       }
      this.inDialogVisible = true
      this.$nextTick(() => {
        this.resetInForm();
        this.form.id = '';
      })
      this.form1.goodsname = this.currentRow.name
      this.form1.goods = this.currentRow.id
      this.form1.adminId = this.user.id
      this.form1.action = '1'
    },
    outGoods(){
      if(!this.currentRow.id){
        alert("请选择记录")
        return
      }
      this.inDialogVisible = true
      this.$nextTick(() => {
        this.resetInForm();
        this.form.id = '';
      })
      this.form1.goodsname = this.currentRow.name
      this.form1.goods = this.currentRow.id
      this.form1.adminId = this.user.id
      this.form1.action = '2'
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
      this.$axios.post('http://localhost:8088/goods/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          name: this.name,
          goodstype: this.goodstype+'',
          storage: this.storage+'',
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
    doInGoods(){
      this.$axios.post('http://localhost:8088/record/save',this.form1).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){

          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.inDialogVisible = false
          this.loadGet()
          this. resetInForm()
        }else{
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }

      })
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$axios.post('http://localhost:8088/goods/saveOrMod', this.form).then(res => res.data).then(res => {
            console.log(res.data)
            if (res.code === 200) {
              this.tableData = res.data
              this.total = res.total
              this.$message({
                message: '操作成功',
                type: "success",
              })
              this.centerDialogVisible = false
              this.loadGet()
            } else {
              this.$message({
                message: '操作失败',
                type: "error",
              })
            }
          })
        } else {
          return false;
        }
      });
    },
    mod(row){
      this.centerDialogVisible=true
      this.$nextTick(()=>{
        this.form.id=row.id
        this.form.name=row.name
        this.form.remark=row.remark
        this.form.goodstype=row.goodstype
        this.form.storage=row.storage
        this.form.count=row.count
      })
    },
    del(id){
      this.$axios.get('http://localhost:8088/goods/delete?id='+id).then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.tableData = res.data
          this.total = res.total
          this.$message({
            message: '删除成功',
            type: "success",
          })
          this.loadGet()
        } else {
          this.$message({
            message: '删除失败',
            type: "error",
          })
        }
      })
    },
  },
  created() {
    this.loadStorage()
    this.loadGoodstype()
    this.loadGet()
  }
}
</script>

<style scoped>

</style>
