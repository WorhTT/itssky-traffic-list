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
      <el-form-item label="统计日期:" prop="beginTime">
        <el-date-picker
          v-model="queryParams.beginTime"
          placeholder="请选择日期"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickOptions"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期:" prop="endTime">
        <el-date-picker
          v-model="queryParams.endTime"
          placeholder="请选择日期"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickOptions"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="统计类型:" prop="statisticsType">
        <el-select
          v-model="queryParams.statisticsType"
          class="custom-input"
          placeholder="请选择"
          clearable
          style="width: 240px"
          filterable
          @keyup.enter.native="handleQuery"
          @change="changeStatisticsType"
        >
          <el-option value="3" label="人员" key="3"/>
          <el-option value="0" label="日" key="0"/>
          <el-option value="1" label="月" key="1"/>
          <el-option value="2" label="站" key="2"/>
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
      <el-table-column label="统计方式" align="center" prop="statType"/>
      <el-table-column label="客车" align="center">
        <el-table-column label="客一" align="center">
          <el-table-column label="C卡" align="center" prop="cust1C"/>
          <el-table-column label="D卡" align="center" prop="cust1D"/>
          <el-table-column label="合计" align="center" prop="cust1Sum"/>
        </el-table-column>
        <el-table-column label="客二" align="center">
          <el-table-column label="C卡" align="center" prop="cust2C"/>
          <el-table-column label="D卡" align="center" prop="cust2D"/>
          <el-table-column label="合计" align="center" prop="cust2Sum"/>
        </el-table-column>
        <el-table-column label="客三" align="center">
          <el-table-column label="C卡" align="center" prop="cust3C"/>
          <el-table-column label="D卡" align="center" prop="cust3D"/>
          <el-table-column label="合计" align="center" prop="cust3Sum"/>
        </el-table-column>
        <el-table-column label="客四" align="center" prop="k4Count">
          <el-table-column label="C卡" align="center" prop="cust4C"/>
          <el-table-column label="D卡" align="center" prop="cust4D"/>
          <el-table-column label="合计" align="center" prop="cust4Sum"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="C卡" align="center" prop="custCSubTotal"/>
          <el-table-column label="D卡" align="center" prop="custDSubTotal"/>
          <el-table-column label="合计" align="center" prop="custSubSum"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="货车" align="center">
        <el-table-column label="货一" align="center">
          <el-table-column label="C卡" align="center" prop="trust1C"/>
          <el-table-column label="D卡" align="center" prop="trust1D"/>
          <el-table-column label="合计" align="center" prop="trust1Sum"/>
        </el-table-column>
        <el-table-column label="货二" align="center">
          <el-table-column label="C卡" align="center" prop="trust2C"/>
          <el-table-column label="D卡" align="center" prop="trust2D"/>
          <el-table-column label="合计" align="center" prop="trust2Sum"/>
        </el-table-column>
        <el-table-column label="货三" align="center">
          <el-table-column label="C卡" align="center" prop="trust3C"/>
          <el-table-column label="D卡" align="center" prop="trust3D"/>
          <el-table-column label="合计" align="center" prop="trust3Sum"/>
        </el-table-column>
        <el-table-column label="货四" align="center">
          <el-table-column label="C卡" align="center" prop="trust4C"/>
          <el-table-column label="D卡" align="center" prop="trust4D"/>
          <el-table-column label="合计" align="center" prop="trust4Sum"/>
        </el-table-column>
        <el-table-column label="货五" align="center">
          <el-table-column label="C卡" align="center" prop="trust5C"/>
          <el-table-column label="D卡" align="center" prop="trust5D"/>
          <el-table-column label="合计" align="center" prop="trust5Sum"/>
        </el-table-column>
        <el-table-column label="货六" align="center">
          <el-table-column label="C卡" align="center" prop="trust6C"/>
          <el-table-column label="D卡" align="center" prop="trust6D"/>
          <el-table-column label="合计" align="center" prop="trust6Sum"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="C卡" align="center" prop="trustCSubTotal"/>
          <el-table-column label="D卡" align="center" prop="trustDSubTotal"/>
          <el-table-column label="合计" align="center" prop="trustSubSum"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="专车" align="center">
        <el-table-column label="专一" align="center">
          <el-table-column label="C卡" align="center" prop="spec1C"/>
          <el-table-column label="D卡" align="center" prop="spec1D"/>
          <el-table-column label="合计" align="center" prop="spec1Sum"/>
        </el-table-column>
        <el-table-column label="专二" align="center">
          <el-table-column label="C卡" align="center" prop="spec2C"/>
          <el-table-column label="D卡" align="center" prop="spec2D"/>
          <el-table-column label="合计" align="center" prop="spec2Sum"/>
        </el-table-column>
        <el-table-column label="专三" align="center">
          <el-table-column label="C卡" align="center" prop="spec3C"/>
          <el-table-column label="D卡" align="center" prop="spec3D"/>
          <el-table-column label="合计" align="center" prop="spec3Sum"/>
        </el-table-column>
        <el-table-column label="专四" align="center">
          <el-table-column label="C卡" align="center" prop="spec4C"/>
          <el-table-column label="D卡" align="center" prop="spec4D"/>
          <el-table-column label="合计" align="center" prop="spec4Sum"/>
        </el-table-column>
        <el-table-column label="专五" align="center">
          <el-table-column label="C卡" align="center" prop="spec5C"/>
          <el-table-column label="D卡" align="center" prop="spec5D"/>
          <el-table-column label="合计" align="center" prop="spec5Sum"/>
        </el-table-column>
        <el-table-column label="专六" align="center">
          <el-table-column label="C卡" align="center" prop="spec6C"/>
          <el-table-column label="D卡" align="center" prop="spec6D"/>
          <el-table-column label="合计" align="center" prop="spec6Sum"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="C卡" align="center" prop="specCSubTotal"/>
          <el-table-column label="D卡" align="center" prop="specDSubTotal"/>
          <el-table-column label="合计" align="center" prop="specSubSum"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="总计" align="center">
        <el-table-column label="C卡" align="center" prop="ctotal"/>
        <el-table-column label="D卡" align="center" prop="dtotal"/>
        <el-table-column label="合计" align="center" prop="totalSum"/>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import {eefepay, exportEefepay} from "@/api/report/toll"
import {stationSelectList} from "@/api/system/station";

export default {
  name: "EEFEPayToll",
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
        stationIdArray: [],
        pageNum: 1,
        pageSize: 10,
        beginTime: null,
        endTime: null,
        statisticsType: '3'
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
    //获取收费站下拉框
    stationSelectList().then(res => {
      this.stationOptions = res.data
      this.queryParams.stationIdArray = res.selectData
    })
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
      eefepay(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出EEF电子支付通行费(MTC+ETC)统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEefepay(queryParams);
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
