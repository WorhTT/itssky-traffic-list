<template>
  <div class="app-container">
    <div style="width: 100%;
    height: 50px;
    line-height: 30px;
    display:flex;
    font-size: 30px;
    justify-content: center;
    align-content: center;
    align-items: center;">高速通行费收入统计表
    </div>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!--      <el-form-item label="公告标题" prop="noticeTitle">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.noticeTitle"-->
      <!--          placeholder="请输入公告标题"-->
      <!--          clearable-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="操作人员" prop="createBy">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.createBy"-->
      <!--          placeholder="请输入操作人员"-->
      <!--          clearable-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="类型" prop="noticeType">-->
      <!--        <el-select v-model="queryParams.noticeType" placeholder="公告类型" clearable>-->
      <!--          <el-option-->
      <!--            v-for="dict in dict.type.sys_notice_type"-->
      <!--            :key="dict.value"-->
      <!--            :label="dict.label"-->
      <!--            :value="dict.value"-->
      <!--          />-->
      <!--        </el-select>-->
      <!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!--    <el-row :gutter="10" class="mb8">-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          plain-->
    <!--          icon="el-icon-plus"-->
    <!--          size="mini"-->
    <!--          @click="handleAdd"-->
    <!--          v-hasPermi="['system:notice:add']"-->
    <!--        >新增</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="success"-->
    <!--          plain-->
    <!--          icon="el-icon-edit"-->
    <!--          size="mini"-->
    <!--          :disabled="single"-->
    <!--          @click="handleUpdate"-->
    <!--          v-hasPermi="['system:notice:edit']"-->
    <!--        >修改</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="danger"-->
    <!--          plain-->
    <!--          icon="el-icon-delete"-->
    <!--          size="mini"-->
    <!--          :disabled="multiple"-->
    <!--          @click="handleDelete"-->
    <!--          v-hasPermi="['system:notice:remove']"-->
    <!--        >删除</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="warning"-->
    <!--          icon="el-icon-download"-->
    <!--          size="mini"-->
    <!--          @click="handleExport"-->
    <!--        >导出-->
    <!--        </el-button>-->
    <!--      </el-col>-->
    <!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
    <!--    </el-row>-->

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange" border
              :header-cell-style="tableHeaderColor" :cell-style="tableCellColor">
      <el-table-column label="统计方式" align="center" prop="stationName" width="200"/>
      <el-table-column label="通行费收入总额" align="center">
        <el-table-column label="统计金额" align="center" prop="tjje" width="100"/>
        <el-table-column label="应缴金额" align="center" prop="yjje" width="100"/>
        <el-table-column label="实缴金额" align="center" prop="sjje" width="100"/>
        <el-table-column label="金额差异" align="center" prop="jecy" width="100"/>
        <el-table-column label="欠款" align="center">
          <el-table-column label="车次" align="center" prop="qkcc"/>
          <el-table-column label="金额" align="center" prop="qkje"/>
        </el-table-column>
        <el-table-column label="加收款" align="center">
          <el-table-column label="现金" align="center" prop="jsxj"/>
          <el-table-column label="金额" align="center" prop="jsje"/>
          <el-table-column label="合计" align="center" prop="jshj"/>
          <el-table-column label="次数" align="center" prop="jscs"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="移动支付" align="center" prop="ydzf" width="100"/>
      <el-table-column label="电子支付消费额" align="center">
        <el-table-column label="储值卡" align="center" prop="dzczk"/>
        <el-table-column label="记账卡" align="center" prop="dzjzk"/>
        <el-table-column label="合计" align="center" prop="dzhj"/>
      </el-table-column>
      <el-table-column label="打印票据" align="center">
        <el-table-column label="现金" align="center">
          <el-table-column label="张数" align="center" prop="xjpjzs"/>
          <el-table-column label="打票金额" align="center" prop="xjdpje" width="100"/>
        </el-table-column>
        <el-table-column label="移动支付" align="center">
          <el-table-column label="张数" align="center" prop="ydzfzs"/>
          <el-table-column label="打票金额" align="center" prop="ydzfdpje" width="100"/>
        </el-table-column>
        <el-table-column label="打印票合计" align="center" prop="dyphj" width="125"/>
      </el-table-column>
      <el-table-column label="定额票据" align="center">
        <el-table-column label="张数" align="center" prop="depjzs"/>
        <el-table-column label="金额" align="center" prop="depjje"/>
      </el-table-column>
      <el-table-column label="废票" align="center">
        <el-table-column label="张数" align="center" prop="fpzs"/>
        <el-table-column label="金额" align="center" prop="fpje"/>
      </el-table-column>
      <el-table-column label="公务IC卡" align="center" prop="gwic" width="100"/>
      <el-table-column label="军车IC卡" align="center" prop="jcic" width="100"/>
      <el-table-column label="免费IC卡" align="center" prop="mfic" width="100"/>
      <el-table-column label="应缴IC卡" align="center" prop="yjic" width="100"/>
    </el-table>

    <!--    <pagination-->
    <!--      v-show="total>0"-->
    <!--      :total="total"-->
    <!--      :page.sync="queryParams.pageNum"-->
    <!--      :limit.sync="queryParams.pageSize"-->
    <!--      @pagination="getList"-->
    <!--    />-->

    <!-- 添加或修改公告对话框 -->
    <!--    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>-->
    <!--      <el-form ref="form" :model="form" :rules="rules" label-width="80px">-->
    <!--        <el-row>-->
    <!--          <el-col :span="12">-->
    <!--            <el-form-item label="公告标题" prop="noticeTitle">-->
    <!--              <el-input v-model="form.noticeTitle" placeholder="请输入公告标题" />-->
    <!--            </el-form-item>-->
    <!--          </el-col>-->
    <!--          <el-col :span="12">-->
    <!--            <el-form-item label="公告类型" prop="noticeType">-->
    <!--              <el-select v-model="form.noticeType" placeholder="请选择公告类型">-->
    <!--                <el-option-->
    <!--                  v-for="dict in dict.type.sys_notice_type"-->
    <!--                  :key="dict.value"-->
    <!--                  :label="dict.label"-->
    <!--                  :value="dict.value"-->
    <!--                ></el-option>-->
    <!--              </el-select>-->
    <!--            </el-form-item>-->
    <!--          </el-col>-->
    <!--          <el-col :span="24">-->
    <!--            <el-form-item label="状态">-->
    <!--              <el-radio-group v-model="form.status">-->
    <!--                <el-radio-->
    <!--                  v-for="dict in dict.type.sys_notice_status"-->
    <!--                  :key="dict.value"-->
    <!--                  :label="dict.value"-->
    <!--                >{{dict.label}}</el-radio>-->
    <!--              </el-radio-group>-->
    <!--            </el-form-item>-->
    <!--          </el-col>-->
    <!--          <el-col :span="24">-->
    <!--            <el-form-item label="内容">-->
    <!--              <editor v-model="form.noticeContent" :min-height="192"/>-->
    <!--            </el-form-item>-->
    <!--          </el-col>-->
    <!--        </el-row>-->
    <!--      </el-form>-->
    <!--      <div slot="footer" class="dialog-footer">-->
    <!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
    <!--        <el-button @click="cancel">取 消</el-button>-->
    <!--      </div>-->
    <!--    </el-dialog>-->
  </div>
</template>

<script>

import {getCharge} from "@/api/report/charge"

export default {
  name: "EntryFlow",
  data() {
    return {
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
      // 公告表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        timeType: 0
        // noticeTitle: undefined,
        // createBy: undefined,
        // status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // noticeTitle: [
        //   { required: true, message: "公告标题不能为空", trigger: "blur" }
        // ],
        // noticeType: [
        //   { required: true, message: "公告类型不能为空", trigger: "change" }
        // ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    tableHeaderColor({row, column, rowIndex, columnIndex}) {
      return 'background:	#C0C0C0; color:#000000; font-size: 16px;';
    },
    tableCellColor({row, column, rowIndex, columnIndex}) {
      return 'background:	#CCFFCC; color:#000000;';
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      getCharge().then(response => {
        this.dataList = response.rows;
        this.total = response.total;
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
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: undefined,
        status: "0"
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
      this.ids = selection.map(item => item.noticeId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加公告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const noticeId = row.noticeId || this.ids
      getNotice(noticeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改公告";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.noticeId != undefined) {
            updateNotice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNotice(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const noticeIds = row.noticeId || this.ids
      this.$modal.confirm('是否确认删除公告编号为"' + noticeIds + '"的数据项？').then(function () {
        return delNotice(noticeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    }
  }
};
</script>
