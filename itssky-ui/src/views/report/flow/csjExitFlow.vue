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
      <el-form-item label="结束日期:" prop="statisticsTime">
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
        >
          <el-option value="0" label="日" key="0"/>
<!--          <el-option value="3" label="时" key="3"/>-->
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
      <!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange" border>
      <el-table-column label="统计方式" align="center" prop="statType" min-width="120"/>
      <el-table-column label="客一" align="center" prop="k1"/>
      <el-table-column label="客二" align="center" prop="k2"/>
      <el-table-column label="客三" align="center" prop="k3"/>
      <el-table-column label="客四" align="center" prop="k4"/>
      <el-table-column label="客车小计" align="center" prop="kamount"/>
      <el-table-column label="货一" align="center" prop="h1"/>
      <el-table-column label="货二" align="center" prop="h2"/>
      <el-table-column label="货三" align="center" prop="h3"/>
      <el-table-column label="货四" align="center" prop="h4"/>
      <el-table-column label="货五" align="center" prop="h5"/>
      <el-table-column label="货六" align="center" prop="h6"/>
      <el-table-column label="货车小计" align="center" prop="hamount"/>
      <el-table-column label="专一" align="center" prop="z1"/>
      <el-table-column label="专二" align="center" prop="z2"/>
      <el-table-column label="专三" align="center" prop="z3"/>
      <el-table-column label="专四" align="center" prop="z4"/>
      <el-table-column label="专五" align="center" prop="z5"/>
      <el-table-column label="专六" align="center" prop="z6"/>
      <el-table-column label="专车小计" align="center" prop="zamount"/>
      <el-table-column label="公务" align="center" prop="official"/>
      <el-table-column label="军车" align="center" prop="military"/>
      <el-table-column label="优惠" align="center" prop="discount"/>
      <el-table-column label="免费" align="center" prop="free"/>
      <el-table-column label="车队" align="center" prop="fleet"/>
      <el-table-column label="总计" align="center" prop="allAmount"/>
    </el-table>
  </div>
</template>

<script>

import {getExitFlow, exportExitFlow} from "@/api/report/exitFlow"
import {stationSelectList} from "@/api/system/station";

export default {
  name: "CSJExitFlow",
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
        beginTime: null,
        endTime: null,
        statisticsType: '0'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
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
  created() {
    //获取收费站下拉框
    stationSelectList().then(res => {
      this.stationOptions = res.data
      this.queryParams.stationIdArray = res.selectData
    })
  },
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
    tableHeaderColor ({row, column, rowIndex, columnIndex}) {
      return 'background:	#C0C0C0; color:#000000;';
    },
    tableCellColor ({row, column, rowIndex, columnIndex}) {
      return 'background:	#CCFFCC; color:#000000;';
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      getExitFlow(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出高速CSJ出口交通流量统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportExitFlow(queryParams);
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
      this.single = selection.length!=1
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
    submitForm: function() {
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
      this.$modal.confirm('是否确认删除公告编号为"' + noticeIds + '"的数据项？').then(function() {
        return delNotice(noticeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
