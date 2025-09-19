<template>
  <div class="app-container" ref="box">
    <div
      style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">CF1收费中心通行费收入班统计表</h3>
    </div>
    <div style="display: flex">
      <span v-for="item in conditionList" style="flex: 1;
        display: flex;
        justify-content: center;
        align-items: center;">
        {{ item }}
      </span>
      <el-row :gutter="10" class="mb8" style="display: flex; justify-content: flex-end;">
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
            icon="el-icon-document"
            size="mini"
            @click="printTable"
            class="print-button-container"
          >打印
          </el-button>
        </el-col>
      </el-row>
    </div>


    <el-table v-loading="loading" :data="dataList" ref="myTable" style="width: 100%;table-layout: auto;" >
      <el-table-column label="收费站名称" align="center" prop="stationName"/>
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
    <!-- 添加底部信息区域 -->
    <div style="display: flex; justify-content: space-between; margin-top: 20px;">
      <span>操作人：{{ operatorName }}</span>
      <span>打印时间：{{ currentDateTime }}</span>
    </div>
    <!-- Hidden iframe for printing -->
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {cf1Toll, exportCf1Toll} from "@/api/report/toll"
import {getLoginUser} from "@/api/login";

export default {
  name: "F1StationShiftDetail",
  components: {},
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
      shiftOptions: [
        {label: '早班', value: 1},
        {label: '中班', value: 2},
        {label: '晚班', value: 3},
      ],
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      conditionList: [],
      operatorName: '',
      corpName: '',
    };
  },
  computed: {
    // corpName() {
    //   return process.env.VUE_APP_CORP_NAME ? process.env.VUE_APP_CORP_NAME : '宁杭高速'
    // },
    currentDateTime() {
      return this.getCurrentDateTime();
    },
  },
  created() {
    this.queryParams = this.$route.query;
    if (this.queryParams) {
      this.getList();
    }
    getLoginUser().then(res => {
      if (res.data) {
        this.operatorName = res.data.username;
      }
    })
  },
  mounted() {
    console.log('当前公司:', process.env.VUE_APP_CORP_NAME)
  },
  methods: {
    getCurrentDateTime() {
      const now = new Date();
      const year = now.getFullYear();
      const month = (now.getMonth() + 1).toString().padStart(2, '0');
      const day = now.getDate().toString().padStart(2, '0');
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      cf1Toll(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.conditionList = response.conditionList;
        this.corpName = response.title;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出CF1收费中心通行费收入班统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportCf1Toll(queryParams);
      }).then(response => {
        this.downloadFile(response.msg);
      }).finally(() => {
        this.loading = false;
      })
    },

    printTable() {
      const corpName = this.corpName;
      const elTable = this.$refs.myTable.$el;
      const printFrame = document.getElementById('printFrame');
      const printDocument = printFrame.contentDocument || printFrame.contentWindow.document;
      let conditionListHtml = this.conditionList.map(item => `<span>${item}</span>`).join('');
      // 获取操作人信息（这里假设您有存储操作人的方式）
      const operator = this.operatorName;
      const printTime = this.getCurrentDateTime();
      // 添加底部信息行
      const footerHtml = `
    <div class="footer-info">
      <span class="operator">操作人：${operator}</span>
      <span class="print-time">打印时间：${printTime}</span>
    </div>
  `;
      // 创建一个新的表格结构，避免样式冲突
      let tableHtml = `
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="2">收费站名称</th>
              <th colspan="6">通行费收入总额</th>
              <th rowspan="2">移动支付</th>
              <th rowspan="2">电子支付</th>
              <th rowspan="2">公务IC卡</th>
              <th rowspan="2">军车IC卡</th>
              <th rowspan="2">免费IC卡</th>
              <th rowspan="2">应缴IC卡</th>
            </tr>
            <tr>
              <th>统计金额</th>
              <th>应缴金额</th>
              <th>实缴金额</th>
              <th>金额差异</th>
              <th>欠款</th>
              <th>加收款</th>
            </tr>
          </thead>
          <tbody>
      `;
      
      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为0的数值，确保0也能正确显示
        const stationName = row.stationName !== undefined && row.stationName !== null ? row.stationName : '';
        const statAmount = row.statAmount !== undefined && row.statAmount !== null ? row.statAmount : '';
        const dueAmount = row.dueAmount !== undefined && row.dueAmount !== null ? row.dueAmount : '';
        const paidAmount = row.paidAmount !== undefined && row.paidAmount !== null ? row.paidAmount : '';
        const amountDiff = row.amountDiff !== undefined && row.amountDiff !== null ? row.amountDiff : '';
        const arrearsAmount = row.arrearsAmount !== undefined && row.arrearsAmount !== null ? row.arrearsAmount : '';
        const extraTotal = row.extraTotal !== undefined && row.extraTotal !== null ? row.extraTotal : '';
        const mobilePaymentAmount = row.mobilePaymentAmount !== undefined && row.mobilePaymentAmount !== null ? row.mobilePaymentAmount : '';
        const epaymentAmount = row.epaymentAmount !== undefined && row.epaymentAmount !== null ? row.epaymentAmount : '';
        const officialIcCardCount = row.officialIcCardCount !== undefined && row.officialIcCardCount !== null ? row.officialIcCardCount : '';
        const militaryIcCardCount = row.militaryIcCardCount !== undefined && row.militaryIcCardCount !== null ? row.militaryIcCardCount : '';
        const freeIcCardCount = row.freeIcCardCount !== undefined && row.freeIcCardCount !== null ? row.freeIcCardCount : '';
        const dueIcCardCount = row.dueIcCardCount !== undefined && row.dueIcCardCount !== null ? row.dueIcCardCount : '';
        
        tableHtml += `
          <tr>
            <td>${stationName}</td>
            <td>${statAmount}</td>
            <td>${dueAmount}</td>
            <td>${paidAmount}</td>
            <td>${amountDiff}</td>
            <td>${arrearsAmount}</td>
            <td>${extraTotal}</td>
            <td>${mobilePaymentAmount}</td>
            <td>${epaymentAmount}</td>
            <td>${officialIcCardCount}</td>
            <td>${militaryIcCardCount}</td>
            <td>${freeIcCardCount}</td>
            <td>${dueIcCardCount}</td>
          </tr>
        `;
      });
      
      tableHtml += `
          </tbody>
        </table>
      `;
      
      let htmlContent = `
      <!DOCTYPE html>
        <html>
        <head>
        <title>Print</title>
        <style>
        body {
          margin: 0;
          padding: 15px;
          font-family: "Microsoft YaHei", SimHei, Arial, sans-serif;
          box-sizing: border-box;
          font-size: 14px;
        }
        .print-title {
          text-align: center;
          font-size: 20px;
          font-weight: bold;
          margin-bottom: 10px;
        }
        .container {
          display: flex;
          margin-bottom: 15px;
        }
        .container span {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 15px;
        }
        .table-container {
          margin-top: 10px;
          width: 100%;
        }
        .el-table {
          width: 100%;
          border-collapse: collapse;
          table-layout: auto; /* 自动调整列宽 */
          font-size: 13px;
        }
        .el-table thead tr {
          background-color: #ebeef5;
          break-inside: avoid; /* 防止表头跨页 */
        }
        .el-table th, .el-table td {
          border: 1px solid #000;
          padding: 8px 5px;
          text-align: center;
          word-wrap: break-word;
          white-space: normal; /* 允许内容换行 */
          font-size: 13px;
          min-width: 60px;
          word-break: break-word; /* 允许单词内换行 */
          break-inside: avoid; /* 防止单元格内容跨页 */
        }
        .el-table th {
          font-weight: bold;
          font-size: 14px;
          background-color: #f5f7fa;
          break-inside: avoid; /* 防止表头单元格跨页 */
        }
        /* 防止表格跨页截断 */
        thead {
          display: table-header-group;
        }
        tfoot {
          display: table-footer-group;
        }
        tbody {
          display: table-row-group;
        }
        tr {
          page-break-inside: avoid;
          page-break-after: auto;
        }
        td, th {
          page-break-inside: avoid;
        }
        .footer-info {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
            font-size: 13px;
        }
        .operator {
            text-align: left;
        }
        .print-time {
            text-align: right;
        }
        @media print {
          @page {
            size: A4 landscape; /* 横向打印 */
            margin: 8mm;
          }
          body {
            -webkit-print-color-adjust: exact;
            color-adjust: exact;
            padding: 0;
            margin: 0;
            width: 100%;
            font-size: 11px; /* 调小字体 */
          }
          .el-table {
            width: 100% !important;
            table-layout: auto !important; /* 自动调整列宽 */
            font-size: 11px;
          }
          .el-table th, .el-table td {
            padding: 4px 3px; /* 减小内边距 */
            font-size: 11px;
            min-width: 40px;
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid;
          }
          .el-table th {
            font-size: 12px; /* 表头字体稍大 */
            font-weight: bold;
            break-inside: avoid;
          }
          .container span {
            font-size: 12px;
          }
          .print-title {
            font-size: 16px;
          }
          .footer-info {
            margin-top: 10px;
            font-size: 11px;
          }
          /* 防止表格跨页截断 */
          thead {
            display: table-header-group;
          }
          tfoot {
            display: table-footer-group;
          }
          tbody {
            display: table-row-group;
          }
          tr {
            page-break-inside: avoid;
            page-break-after: auto;
            break-inside: avoid;
          }
          td, th {
            page-break-inside: avoid;
          }
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">CF1收费中心通行费收入班统计表</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">${tableHtml}</div>
            ${footerHtml}
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

::v-deep .el-table .el-table__header-wrapper th {
  height: 20px;
}

::v-deep .el-table--medium .el-table__cell {
  padding: 4px 0;
}
</style>
