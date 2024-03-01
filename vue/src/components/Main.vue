<template>
  <div>
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="请输入名字" suffix-icon="el-icon-search" style="width: 200px;"
      @keyup.enter.native="loadGet"></el-input>
      <el-select v-model="sex" filterable placeholder="请选择性别" style="margin-left: 5px;">
        <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value">
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
    <el-table-column prop="no" label="账号" width="180">
    </el-table-column>
    <el-table-column prop="name" label="姓名" width="180">
    </el-table-column>
    <el-table-column prop="age" label="年龄" width="80">
    </el-table-column>
    <el-table-column prop="sex" label="性别" width="80">
      <template slot-scope="scope">
        <el-tag :type="scope.row.sex === 1 ? 'primary':'success'"
                disable-transitions>{{scope.row.sex === 1 ? '男':'女'}}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="roleId" label="角色" width="120">
      <template slot-scope="scope">
        <el-tag :type="scope.row.roleId === 1 ? 'danger':(scope.row.roleId === 0 ? 'primary':'success')"
                disable-transitions>{{scope.row.roleId === 1 ? '管理员':(scope.row.roleId === 0 ? '超级管理员':'用户')}}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="phone" label="电话" width="120">
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
        <el-form-item label="账号" prop="no">
          <el-input v-model="form.no"></el-input>
        </el-form-item>
        <el-form-item label="名字" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Main",
  data() {
    let checkAge = (rule, value, callback) => {
      if (value > 150) {
        callback(new Error('年龄输入过大'));
      } else {
        callback();
      }
    };
    let checkDuplicate = (rule, value, callback) => {
      if (this.form.id) {
         return callback();
      }
      this.$axios.get('http://localhost:8088/user/findByNo?no='+this.form.no).then(res=>res.data).then(res => {
        if (res.code != 200) {
          callback();
        } else {
          callback(new Error('账号已经存在'));
        }
      })
    };
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 2,
      total: 0,
      name: "",
      sex: "",
      sexs: [
        {
          value: '1',
          label: "男",
        },
        {
          value: '0',
          label: "女",
        }
      ],
      centerDialogVisible: false,
      form: {
        id:"",
        name: "",
        no: "",
        password: "",
        age: "",
        phone: "",
        sex: '0',
        roleId:'2',
      },
      rules: {
        name: [
          {required: true, message: '请输入名字', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
        ],
        no: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
          {validator: checkDuplicate, trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 3, message: '长度在 1 到 3 个字符', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/, message: '年龄必须为正数', trigger: 'blur'},
          {validator: checkAge, trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '手机号不能为空', trigger: 'blur'},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确手机号', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    resetForm() {
      this.$refs.form.resetFields()
    },
    add() {
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm();
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
      this.$axios.post('http://localhost:8088/user/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          name: this.name,
          sex: this.sex,
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
    resetParam() {
      this.sex = ''
      this.name = ''
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$axios.post('http://localhost:8088/user/saveOrMod', this.form).then(res => res.data).then(res => {
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
        this.form.no=row.no
        this.form.name=row.name
        this.form.sex=row.sex+''
        this.form.password=''
        this.form.age=row.age+''
        this.form.roleId=row.roleId
        this.form.id=row.id
      })
    },
    del(id){
      this.$axios.get('http://localhost:8088/user/delete?id='+id).then(res => res.data).then(res => {
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
    this.loadGet();
  }
}
</script>

<style scoped>

</style>
