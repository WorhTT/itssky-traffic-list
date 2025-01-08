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
          icon="el-icon-document"
          size="mini"
          @click="printTable"
        >打印
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border ref="myTable">
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

    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {getExitFlow, exportExitFlow} from "@/api/report/exitFlow"

export default {
  name: "CSJExitFlowDetail",
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
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
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
    this.queryParams = this.$route.query;
    if (this.queryParams) {
      this.getList();
    }
  },
  methods: {
    getList() {
      this.loading = true;
      getExitFlow(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
      }).then(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出CSJ出口(MTC+ETC)交通流量统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportExitFlow(queryParams);
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
            <div class="print-title">RSJ入口(MTC+ETC)交通流量统计表</div>
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
