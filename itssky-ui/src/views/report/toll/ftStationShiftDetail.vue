<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          class="export-button-container"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="printTable"
          class="print-button-container"
        >打印
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border ref="myTable">
      <el-table-column label="统计方式" align="center" prop="statType"/>
      <el-table-column label="通行费收入总额" align="center">
        <el-table-column label="统计金额" align="center" prop="statAmount"/>
        <el-table-column label="应缴金额" align="center" prop="dueAmount"/>
        <el-table-column label="实缴金额" align="center" prop="paidAmount"/>
        <el-table-column label="金额差异" align="center" prop="amountDiff"/>
        <el-table-column label="欠款" align="center" prop="arrearsAmount"/>
        <el-table-column label="加收款" align="center" prop="extraTotal"/>
      </el-table-column>
      <el-table-column label="移动支付" align="center" prop="mobilePaymentAmount"/>
      <el-table-column label="电子支付" align="center" prop="epaymentAmount"/>
      <el-table-column label="公务IC卡" align="center" prop="officialIcCardCount"/>
      <el-table-column label="军车IC卡" align="center" prop="militaryIcCardCount"/>
      <el-table-column label="免费IC卡" align="center" prop="freeIcCardCount"/>
      <el-table-column label="应缴IC卡" align="center" prop="dueIcCardCount"/>
    </el-table>

    <!-- Hidden iframe for printing -->
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {ftToll, exportFtToll} from "@/api/report/toll"

export default {
  name: "FTStationShiftDetail",
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
        beginTime: null,
        endTime: null,
        statisticsType: '0',
        stationIdArray: [],
        // noticeTitle: undefined,
        // createBy: undefined,
        // status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      stationOptions: [
      ],
      shiftOptions: [
        {label: '早班', value: 1},
        {label: '中班', value: 2},
        {label: '晚班', value: 3},
      ],
      pickerType: 'date',
      disabledDatePicker: false,
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      showProp: null,
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
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      ftToll(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
      }).finally(() => {
        this.loading = false;
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出FT通行费收入统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportFtToll(queryParams);
      }).then(response => {
        this.downloadFile(response.msg);
      })
    },
    printTable() {
      const elTable = this.$refs.myTable.$el;
      const printFrame = document.getElementById('printFrame');
      const printDocument = printFrame.contentDocument || printFrame.contentWindow.document;
      printDocument.open();
      printDocument.write('<html><head><title>Print Table</title>');
      printDocument.write('<style>');
      printDocument.write(`
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
      `);
      printDocument.write('</style>');
      printDocument.write('</head><body>');
      printDocument.write('<div class="print-title">FT通行费收入统计表</div>'); // Add the title here
      printDocument.write('<div class="container"><span class="info-left">收费站：中心</span><span class="info-center">统计时间：2024-12-04</span></div>'); // Add the info line here // Add the info line here
      printDocument.write(elTable.outerHTML);
      printDocument.write('</body></html>');
      printDocument.close();

      // Trigger print
      printFrame.contentWindow.focus();
      printFrame.contentWindow.print();
    },
  }
};
</script>

<style lang="scss" scoped>
.print-button-container {
  display: flex;
}

.export-button-container {
  display: flex;
}
</style>
