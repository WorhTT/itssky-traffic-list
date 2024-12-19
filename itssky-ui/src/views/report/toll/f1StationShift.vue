<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="收费站:" prop="stationIdArray">
        <el-cascader v-model="queryParams.stationIdArray"
                     :options="stationOptions"
                     :props="props"
                     clearable
                     collapse-tags
                     placeholder="请选择收费站" style="width: 12vw;">

        </el-cascader>
      </el-form-item>
      <el-form-item label="统计日期:" prop="statisticsTime">
        <el-date-picker
          v-model="queryParams.time"
          placeholder="请选择日期"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickOptions"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="班次:" prop="shiftId">
        <el-select
          v-model="queryParams.shiftId"
          class="custom-input"
          placeholder="请选择班次"
          clearable
          style="width: 240px"
          filterable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="item in shiftOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"></el-option>
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
              :span-method="arraySpanMethod" :cell-style="cellStyle">
      <el-table-column label="班次" align="center" prop="shiftId"/>
      <el-table-column label="班组" align="center" prop="teamId"/>
      <el-table-column label="工号" align="center" prop="operatorId"/>
      <el-table-column label="通行费收入总额" align="center">
        <el-table-column label="统计金额" align="center" prop="statAmount" width="100"/>
        <el-table-column label="应缴金额" align="center" prop="dueAmount" width="100"/>
        <el-table-column label="实缴金额" align="center" prop="paidAmount" width="100"/>
        <el-table-column label="金额差异" align="center" prop="amountDiff" width="100"/>
        <el-table-column label="欠款" align="center">
          <el-table-column label="车次" align="center" prop="arrearsTrips"/>
          <el-table-column label="金额" align="center" prop="arrearsAmount"/>
        </el-table-column>
        <el-table-column label="加收款" align="center" prop="extraTotal"/>
      </el-table-column>
      <el-table-column label="移动支付" align="center" prop="mobilePaymentAmount" width="100"/>
      <el-table-column label="电子支付" align="center" prop="epaymentAmount"/>
      <el-table-column label="公务IC卡" align="center" prop="officialIcCardCount" width="100"/>
      <el-table-column label="军车IC卡" align="center" prop="militaryIcCardCount" width="100"/>
      <el-table-column label="免费IC卡" align="center" prop="freeIcCardCount" width="100"/>
      <el-table-column label="应缴IC卡" align="center" prop="dueIcCardCount" width="100"/>
    </el-table>
  </div>
</template>

<script>

import {f1StationShift, exportF1Station} from "@/api/report/toll"
import {stationSelectList} from "@/api/system/station";

export default {
  name: "F1StationShift",
  data() {
    return {
      props: {multiple: true},
      // 遮罩层
      loading: false,
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
        stationIdArray: null,
        time: null,
        shiftId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      stationOptions: [
      ],
      shiftOptions: [
        {label : '早班', value: 1},
        {label : '中班', value: 2},
        {label : '晚班', value: 3},
      ],
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
    };
  },
  computed: {},
  created() {
    //获取收费站下拉框
    stationSelectList().then(res => {
      this.stationOptions = res.data
      this.queryParams.stationIdArray = res.selectData
    })
  },
  methods: {
    cellStyle({row, column, rowIndex, columnIndex}) {
      if (row.totalRow === true) {
        return 'background:yellow;';
      }
    },
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      if (row.totalRow  === true) {
        if (columnIndex === 0) {
          return [1, 3];
        } else if (columnIndex === 1) {
          return [0, 0];
        } else if (columnIndex === 2) {
          return [0, 0];
        }
      }
    },
    spanStyle({row, column, rowIndex, columnIndex}) {
      if (row.totalRow  === true) {
        if (columnIndex === 0) {
          console.log(rowIndex, columnIndex);
          return [rowIndex, columnIndex];
        } else {
          return [0, 0];
        }
        // console.log('1234');
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
      f1StationShift(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出F1收费站通行费收入班统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportF1Station(queryParams);
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

<style lang="scss" scoped>
.el-table {
  ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
    width: 15px; /*滚动条宽度*/
    height: 15px; /*滚动条高度*/
  }
}
</style>
