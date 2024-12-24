<template>
  <div class="app-container">
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
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="printTable"
        >打印
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border ref="myTable">
      <el-table-column label="班次" align="center" prop="shiftId"/>
      <el-table-column label="工号" align="center" prop="operatorId"/>
      <el-table-column label="客一" align="center" prop="cust1"/>
      <el-table-column label="客二" align="center" prop="cust2"/>
      <el-table-column label="客三" align="center" prop="cust3"/>
      <el-table-column label="客四" align="center" prop="cust4"/>
      <el-table-column label="客车小计" align="center" prop="custSubTotal"/>
      <el-table-column label="货一" align="center" prop="truck1"/>
      <el-table-column label="货二" align="center" prop="truck2"/>
      <el-table-column label="货三" align="center" prop="truck3"/>
      <el-table-column label="货四" align="center" prop="truck4"/>
      <el-table-column label="货五" align="center" prop="truck5"/>
      <el-table-column label="货六" align="center" prop="truck6"/>
      <el-table-column label="货车小计" align="center" prop="truckSubTotal"/>
      <el-table-column label="专一" align="center" prop="spec1"/>
      <el-table-column label="专二" align="center" prop="spec2"/>
      <el-table-column label="专三" align="center" prop="spec3"/>
      <el-table-column label="专四" align="center" prop="spec4"/>
      <el-table-column label="专五" align="center" prop="spec5"/>
      <el-table-column label="专六" align="center" prop="spec6"/>
      <el-table-column label="专车小计" align="center" prop="specSubTotal"/>
      <el-table-column label="军车" align="center" prop="militaryNum"/>
      <el-table-column label="公务" align="center" prop="officialNum"/>
      <el-table-column label="车队" align="center" prop="fleetNum"/>
      <el-table-column label="优惠" align="center" prop="preferNum"/>
      <el-table-column label="ETC" align="center" prop="etcNum"/>
      <el-table-column label="纸券" align="center" prop="paperNum"/>
      <el-table-column label="应收卡" align="center" prop="issuedNum"/>
      <el-table-column label="实收卡" align="center" prop="actualNum"/>
      <el-table-column label="无卡" align="center" prop="noneNum"/>
      <el-table-column label="卡损" align="center" prop="badNum"/>
      <el-table-column label="总流量" align="center" prop="totalFlow"/>
    </el-table>

    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {s1StationShift, exportC1StationShift} from "@/api/report/card"


export default {
  name: "C1StationShiftDetail",
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
        time: null,
        tableFlag: '1',
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      stationOptions: [],
      conditionList: [],
      shiftOptions: [
        {label : '早班', value: 1},
        {label : '中班', value: 2},
        {label : '晚班', value: 3},
      ],
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
    this.queryParams = this.$route.query;
    if (this.queryParams) {
      this.getList();
    }
  },
  watch: {},
  methods: {
    getList() {
      this.loading = true;
      s1StationShift(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.conditionList = response.conditionList;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出C1收费站通行卡回收统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportC1StationShift(queryParams);
      }).then(response => {
        this.downloadFile(response.msg);
      })
    },
    printTable() {
      const elTable = this.$refs.myTable.$el;
      const printFrame = document.getElementById('printFrame');
      const printDocument = printFrame.contentDocument || printFrame.contentWindow.document;
      let conditionListHtml = this.conditionList.map(item => `<span>${item}</span>`).join('');
      let htmlContent = `
      <!DOCTYPE html>
        <html>
        <head>
        <title>Print</title>
        <style>
            /* 在这里添加你的样式 */
        .table-container {
          zoom: 0.6
        }
        .print-title {
          text-align: center;
          font-size: 24px;
          font-weight: bold;
          margin-bottom: 20px;
        }
        body {
          margin: 0;
          padding: 20px;
          font-family: Arial, sans-serif;
          box-sizing: border-box;
        }
        .container {
          display: flex;
        }
        .container span {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .el-table {
          width: 100%;
          border-collapse: collapse;
          table-layout: fixed; /* Ensure fixed layout */
        }
        .el-table__header-wrapper {
          border: 1px solid #ebeef5 !important;
        }
        .el-table__body-wrapper {
          border: 1px solid #ebeef5 !important;
        }
        .el-table td {
          border: 1px solid #ebeef5 !important;
          font-size: 16px;
          padding: 0 0;
          text-align: center; /* Center text */
          word-wrap: break-word;
          white-space: normal; /* Prevent text from wrapping */
        }
        .el-table th {
          border: 1px solid #ebeef5 !important;
          font-size: 16px;
          padding: 4px; /* Reduce padding to make cells more compact */
          text-align: center; /* Center text */
          word-wrap: break-word; /* Ensure text wraps within cells */
          white-space: normal; /* Allow text to wrap */
        }
        @media print {
          body {
            padding: 0;
            -webkit-print-color-adjust: exact; /* Chrome, Safari */
            color-adjust: exact; /* Firefox */
          }
        }
        @page {
          size: auto;
          margin: 0mm;
        }
        </style>
        </head>
        <body>
            <div class="print-title">C1收费站通行卡回收班统计表</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">${elTable.outerHTML}</div>
        </body>
        </html>
      `
      printDocument.write(htmlContent);
      printDocument.close();

      // Trigger print
      printFrame.contentWindow.focus();
      printFrame.contentWindow.print();
    },
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
