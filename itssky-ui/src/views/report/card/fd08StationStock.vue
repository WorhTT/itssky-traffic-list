<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="收费站:" prop="stationId">
        <el-select
          v-model="queryParams.stationId"
          class="custom-input"
          placeholder="请选择收费站"
          clearable
          style="width: 240px"
          filterable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="item in stationOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="统计日期:" prop="statisticsTime">
        <el-date-picker
          v-model="queryParams.beginTime"
          placeholder="请选择日期"
          :type="pickerType"
          value-format="yyyy-MM-dd"
          :picker-options="pickOptions"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期:" prop="statisticsTime">
        <el-date-picker
          v-model="queryParams.endTime"
          placeholder="请选择日期"
          :type="pickerType"
          value-format="yyyy-MM-dd"
          :picker-options="pickOptions"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="统计类型:" prop="type">
        <el-select
          v-model="queryParams.type"
          class="custom-input"
          placeholder="请选择"
          clearable
          style="width: 240px"
          filterable
          @keyup.enter.native="handleQuery"
          @change="changeStatisticsType"
        >
          <el-option value="0" label="人员" key="0"/>
          <el-option value="1" label="日" key="1"/>
          <el-option value="2" label="月" key="2"/>
          <el-option value="3" label="站" key="3"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange" border
              :span-method="arraySpanMethod" :cell-style="cellStyle" style="color:#000000;">
      <el-table-column label="统计方式" align="center" prop="statisticsType"/>
      <el-table-column label="库存增加" align="center">
        <el-table-column label="通行卡调入" align="center" prop="inNum"/>
        <el-table-column label="出口回收" align="center" prop="receiveNum"/>
        <el-table-column label="坏卡回收" align="center" prop="badRecNum"/>
        <el-table-column label="通行卡恢复" align="center" prop="recoverNum"/>
      </el-table-column>
      <el-table-column label="库存减少" align="center">
        <el-table-column label="通行卡调出" align="center" prop="outNum"/>
        <el-table-column label="入口发卡" align="center" prop="sendNum"/>
        <el-table-column label="坏卡上缴" align="center" prop="badOutNum"/>
      </el-table-column>
      <el-table-column label="库存变动" align="center" prop="changeNum"/>
      <el-table-column label="库存坏卡" align="center" prop="badNum"/>
      <el-table-column label="库存正常卡" align="center" prop="normalNum"/>
    </el-table>
  </div>
</template>

<script>

import {getCharge, exportCharge} from "@/api/report/charge"

export default {
  name: "FD08StationStock",
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
        beginTime: null,
        endTime: null,
        type: '0'
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
      },
      stationOptions: [],
      shiftOptions: [],
      pickerType: 'date',
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
    };
  },
  computed: {},
  created() {
    this.getList();
  },
  watch: {},
  methods: {
    changeStatisticsType(val) {
      if (val === '1') {
        this.pickerType = 'date'
        this.queryParams.beginTime = null;
        this.queryParams.endTime = null;
      } else if (val === '2') {
        this.pickerType = 'month'
        this.queryParams.beginTime = null;
        this.queryParams.endTime = null;
      } else if (val === '3') {
        this.pickerType = 'date'
        this.queryParams.beginTime = null;
        this.queryParams.endTime = null;
      } else if (val === '0') {
        this.pickerType = 'date'
        this.queryParams.beginTime = null;
        this.queryParams.endTime = null;
      }
    },
    // getTimeType() {
    //   if (this.queryParams.type === 1) {
    //     return 'date'
    //   } else if (this.queryParams.type === 2) {
    //     return 'month'
    //   }
    // },
    cellStyle({row, column, rowIndex, columnIndex}) {
      if (row.totalRow === true) {
        return 'background:yellow;';
      }
      if (row.subTotalRow === true) {
        return 'background:	#C0C0C0';
      }
    },
    arraySpanMethod({row, column, rowIndex, columnIndex}) {
      if (row.subTotalRow === true) {
        if (columnIndex === 0) {
          return [1, 2];
        } else if (columnIndex === 1) {
          return [0, 0];
        }
      }
      if (row.totalRow === true) {
        if (columnIndex === 0) {
          return [1, 2];
        } else if (columnIndex === 1) {
          return [0, 0];
        }
      }
    },
    spanStyle({row, column, rowIndex, columnIndex}) {
      if (row.totalRow === true) {
        if (columnIndex === 0) {
          return [rowIndex, columnIndex];
        } else {
          return [0, 0];
        }
      }
    },
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
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出高速FT通行费收入统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportCharge(queryParams);
      }).then(response => {
        this.downloadFile(response.msg);
      })
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
