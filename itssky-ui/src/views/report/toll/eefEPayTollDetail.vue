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
          class="print-button-container"
          @click="printTable"
        >打印
        </el-button>
      </el-col>
    </el-row>

      <el-table v-loading="loading" :data="dataList" border style="width: 100%" fit ref="myTable">
        <el-table-column label="统计方式" align="center" prop="statType" />
        <el-table-column label="客车" align="center">
          <el-table-column label="客一" align="center">
            <el-table-column label="C卡" align="center" prop="cust1C" />
            <el-table-column label="D卡" align="center" prop="cust1D" />
            <el-table-column label="合计" align="center" prop="cust1Sum" />
          </el-table-column>
          <el-table-column label="客二" align="center">
            <el-table-column label="C卡" align="center" prop="cust2C" />
            <el-table-column label="D卡" align="center" prop="cust2D" />
            <el-table-column label="合计" align="center" prop="cust2Sum" />
          </el-table-column>
          <el-table-column label="客三" align="center">
            <el-table-column label="C卡" align="center" prop="cust3C" />
            <el-table-column label="D卡" align="center" prop="cust3D" />
            <el-table-column label="合计" align="center" prop="cust3Sum" />
          </el-table-column>
          <el-table-column label="客四" align="center" prop="k4Count">
            <el-table-column label="C卡" align="center" prop="cust4C" />
            <el-table-column label="D卡" align="center" prop="cust4D" />
            <el-table-column label="合计" align="center" prop="cust4Sum" />
          </el-table-column>
          <el-table-column label="小计" align="center">
            <el-table-column label="C卡" align="center" prop="custCSubTotal" />
            <el-table-column label="D卡" align="center" prop="custDSubTotal" />
            <el-table-column label="合计" align="center" prop="custSubSum" />
          </el-table-column>
        </el-table-column>
        <el-table-column label="货车" align="center">
          <el-table-column label="货一" align="center">
            <el-table-column label="C卡" align="center" prop="trust1C" />
            <el-table-column label="D卡" align="center" prop="trust1D" />
            <el-table-column label="合计" align="center" prop="trust1Sum" />
          </el-table-column>
          <el-table-column label="货二" align="center">
            <el-table-column label="C卡" align="center" prop="trust2C" />
            <el-table-column label="D卡" align="center" prop="trust2D" />
            <el-table-column label="合计" align="center" prop="trust2Sum" />
          </el-table-column>
          <el-table-column label="货三" align="center">
            <el-table-column label="C卡" align="center" prop="trust3C" />
            <el-table-column label="D卡" align="center" prop="trust3D" />
            <el-table-column label="合计" align="center" prop="trust3Sum" />
          </el-table-column>
          <el-table-column label="货四" align="center">
            <el-table-column label="C卡" align="center" prop="trust4C" />
            <el-table-column label="D卡" align="center" prop="trust4D" />
            <el-table-column label="合计" align="center" prop="trust4Sum" />
          </el-table-column>
          <el-table-column label="货五" align="center">
            <el-table-column label="C卡" align="center" prop="trust5C" />
            <el-table-column label="D卡" align="center" prop="trust5D" />
            <el-table-column label="合计" align="center" prop="trust5Sum" />
          </el-table-column>
          <el-table-column label="货六" align="center">
            <el-table-column label="C卡" align="center" prop="trust6C" />
            <el-table-column label="D卡" align="center" prop="trust6D" />
            <el-table-column label="合计" align="center" prop="trust6Sum" />
          </el-table-column>
          <el-table-column label="小计" align="center">
            <el-table-column label="C卡" align="center" prop="trustCSubTotal" />
            <el-table-column label="D卡" align="center" prop="trustDSubTotal" />
            <el-table-column label="合计" align="center" prop="trustSubSum" />
          </el-table-column>
        </el-table-column>
        <el-table-column label="专车" align="center">
          <el-table-column label="专一" align="center">
            <el-table-column label="C卡" align="center" prop="spec1C" />
            <el-table-column label="D卡" align="center" prop="spec1D" />
            <el-table-column label="合计" align="center" prop="spec1Sum" />
          </el-table-column>
          <el-table-column label="专二" align="center">
            <el-table-column label="C卡" align="center" prop="spec2C" />
            <el-table-column label="D卡" align="center" prop="spec2D" />
            <el-table-column label="合计" align="center" prop="spec2Sum" />
          </el-table-column>
          <el-table-column label="专三" align="center">
            <el-table-column label="C卡" align="center" prop="spec3C" />
            <el-table-column label="D卡" align="center" prop="spec3D" />
            <el-table-column label="合计" align="center" prop="spec3Sum" />
          </el-table-column>
          <el-table-column label="专四" align="center">
            <el-table-column label="C卡" align="center" prop="spec4C" />
            <el-table-column label="D卡" align="center" prop="spec4D" />
            <el-table-column label="合计" align="center" prop="spec4Sum" />
          </el-table-column>
          <el-table-column label="专五" align="center">
            <el-table-column label="C卡" align="center" prop="spec5C" />
            <el-table-column label="D卡" align="center" prop="spec5D" />
            <el-table-column label="合计" align="center" prop="spec5Sum" />
          </el-table-column>
          <el-table-column label="专六" align="center">
            <el-table-column label="C卡" align="center" prop="spec6C" />
            <el-table-column label="D卡" align="center" prop="spec6D" />
            <el-table-column label="合计" align="center" prop="spec6Sum" />
          </el-table-column>
          <el-table-column label="小计" align="center">
            <el-table-column label="C卡" align="center" prop="specCSubTotal" />
            <el-table-column label="D卡" align="center" prop="specDSubTotal" />
            <el-table-column label="合计" align="center" prop="specSubSum"/>
          </el-table-column>
        </el-table-column>
        <el-table-column label="总计" align="center">
          <el-table-column label="C卡" align="center" prop="ctotal"/>
          <el-table-column label="D卡" align="center" prop="dtotal"/>
          <el-table-column label="合计" align="center" prop="totalSum"/>
        </el-table-column>
      </el-table>
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {eefepay, exportEefepay} from "@/api/report/toll"

export default {
  name: "EEFEPayTollDetail",
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
      queryParams: {},
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
      conditionList: [],
    };
  },
  computed: {},
  created() {
    this.queryParams = this.$route.query;
    console.log('queryparams2', this.queryParams);
    if (this.queryParams) {
      this.getList();
    }
  },
  watch: {},
  methods: {
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      eefepay(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
      }).finally(() => {
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
        .table-container {
          zoom: 0.3
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
            <div class="print-title">EEF电子支付通行费(MTC+ETC)统计表</div>
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
.print-button-container {
  display: flex;
}

.export-button-container {
  display: flex;
}
</style>
