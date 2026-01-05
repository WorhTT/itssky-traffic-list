<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">EU电子支付通行费(MTC+ETC)统计按车型</h3>
    </div>
    <div style="display: flex">
      <span v-for="item in conditionList" style="flex: 1;
        display: flex;
        justify-content: center;
        align-items: center;">
        {{item}}
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
            icon="el-icon-download"
            size="mini"
            @click="printTable"
            class="print-button-container"
          >打印
          </el-button>
        </el-col>
      </el-row>
    </div>

    <el-table v-loading="loading" :data="dataList" border ref="myTable"
              :cell-style="cellStyle" >
      <el-table-column label="统计方式" align="center" prop="statType" min-width="120"/>
      <el-table-column label="一车型" align="center">
        <el-table-column label="C卡" align="center" prop="vc1" min-width="100"/>
        <el-table-column label="D卡" align="center" prop="vd1" min-width="100"/>
        <el-table-column label="小计" align="center" prop="v1" min-width="100"/>
      </el-table-column>
      <el-table-column label="二车型" align="center">
        <el-table-column label="C卡" align="center" prop="vc2"/>
        <el-table-column label="D卡" align="center" prop="vd2" min-width="100"/>
        <el-table-column label="小计" align="center" prop="v2" min-width="100"/>
      </el-table-column>
      <el-table-column label="三车型" align="center">
        <el-table-column label="C卡" align="center" prop="vc3"/>
        <el-table-column label="D卡" align="center" prop="vd3" min-width="100"/>
        <el-table-column label="小计" align="center" prop="v3" min-width="100"/>
      </el-table-column>
      <el-table-column label="四车型" align="center">
        <el-table-column label="C卡" align="center" prop="vc4"/>
        <el-table-column label="D卡" align="center" prop="vd4"/>
        <el-table-column label="小计" align="center" prop="v4"/>
      </el-table-column>
      <el-table-column label="五车型" align="center">
        <el-table-column label="C卡" align="center" prop="vc5"/>
        <el-table-column label="D卡" align="center" prop="vd5"/>
        <el-table-column label="小计" align="center" prop="v5"/>
      </el-table-column>
      <el-table-column label="六车型" align="center">
        <el-table-column label="C卡" align="center" prop="vc6"/>
        <el-table-column label="D卡" align="center" prop="vd6" min-width="100"/>
        <el-table-column label="小计" align="center" prop="v6" min-width="100"/>
      </el-table-column>
      <el-table-column label="专项车" align="center">
        <el-table-column label="C卡" align="center" prop="vcz"/>
        <el-table-column label="D卡" align="center" prop="vdz"/>
        <el-table-column label="小计" align="center" prop="vz"/>
      </el-table-column>
      <el-table-column label="合计" align="center">
        <el-table-column label="C卡" align="center" prop="sumc" min-width="100"/>
        <el-table-column label="D卡" align="center" prop="sumd" min-width="120"/>
        <el-table-column label="小计" align="center" prop="sum" min-width="120"/>
      </el-table-column>
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

import {eu, exportEu} from "@/api/report/toll"

export default {
  name: "EuDetail",
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
      conditionList: [],
      showProp: null,
      operatorName: '',
      corpName: '',
    };
  },
  computed: {
    currentDateTime() {
      return this.getCurrentDateTime();
    },
  },
  created() {
    this.queryParams = this.$route.query;
    if (this.queryParams) {
      this.getList();
    }
  },
  watch: {},
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
    cellStyle({row, column, rowIndex, columnIndex}) {
      // if (row.totalRow === true) {
      //   row.statType = "合计"
      //   return 'background:	#FFD040';
      // }
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      eu(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.conditionList = response.conditionList;
        this.corpName = response.title;
        this.operatorName = response.operatorName;
      }).finally(() => {
        this.loading = false;
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出EU电子支付通行费(MTC+ETC)统计按车型?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEu(queryParams);
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

      let passengerTableHtml = `
        <h3 style="text-align: center; margin: 10px 0; font-size: 18px;">一车型~四车型</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="2" style="min-width: 100px;">统计方式</th>
              <th colspan="3">一车型</th>
              <th colspan="3">二车型</th>
              <th colspan="3">三车型</th>
              <th colspan="3">四车型</th>
            </tr>
            <tr>
              <th>C卡</th>
              <th>D卡</th>
              <th>小计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>小计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>小计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>小计</th>
            </tr>
          </thead>
          <tbody>
      `;

      let truckTableHtml = `
        <h3 style="text-align: center; margin: 10px 0; font-size: 18px;">五车型、六车型、专项车、总计</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="2" style="min-width: 100px;">统计方式</th>
              <th colspan="3">五车型</th>
              <th colspan="3">六车型</th>
              <th colspan="3">专项车</th>
              <th colspan="3">总计</th>
            </tr>
            <tr>
              <th>C卡</th>
              <th>D卡</th>
              <th>小计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>小计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>小计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>小计</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为0的数值，确保0也能正确显示
        const statType = row.statType !== undefined && row.statType !== null ? row.statType : '';
        const vc1 = row.vc1 !== undefined && row.vc1 !== null ? row.vc1 : '';
        const vd1 = row.vd1 !== undefined && row.vd1 !== null ? row.vd1 : '';
        const v1 = row.v1 !== undefined && row.v1 !== null ? row.v1 : '';
        const vc2 = row.vc2 !== undefined && row.vc2 !== null ? row.vc2 : '';
        const vd2 = row.vd2 !== undefined && row.vd2 !== null ? row.vd2 : '';
        const v2 = row.v2 !== undefined && row.v2 !== null ? row.v2 : '';
        const vc3 = row.vc3 !== undefined && row.vc3 !== null ? row.vc3 : '';
        const vd3 = row.vd3 !== undefined && row.vd3 !== null ? row.vd3 : '';
        const v3 = row.v3 !== undefined && row.v3 !== null ? row.v3 : '';
        const vc4 = row.vc4 !== undefined && row.vc4 !== null ? row.vc4 : '';
        const vd4 = row.vd4 !== undefined && row.vd4 !== null ? row.vd4 : '';
        const v4 = row.v4 !== undefined && row.v4 !== null ? row.v4 : '';
        const vc5 = row.vc5 !== undefined && row.vc5 !== null ? row.vc5 : '';
        const vd5 = row.vd5 !== undefined && row.vd5 !== null ? row.vd5 : '';
        const v5 = row.v5 !== undefined && row.v5 !== null ? row.v5 : '';
        const vc6 = row.vc6 !== undefined && row.vc6 !== null ? row.vc6 : '';
        const vd6 = row.vd6 !== undefined && row.vd6 !== null ? row.vd6 : '';
        const v6 = row.v6 !== undefined && row.v6 !== null ? row.v6 : '';
        const vcz = row.vcz !== undefined && row.vcz !== null ? row.vcz : '';
        const vdz = row.vdz !== undefined && row.vdz !== null ? row.vdz : '';
        const vz = row.vz !== undefined && row.vz !== null ? row.vz : '';
        const sumc = row.sumc !== undefined && row.sumc !== null ? row.sumc : '';
        const sumd = row.sumd !== undefined && row.sumd !== null ? row.sumd : '';
        const sum = row.sum !== undefined && row.sum !== null ? row.sum : '';
        passengerTableHtml += `
          <tr>
            <td>${statType}</td>
            <td>${vc1}</td>
            <td>${vd1}</td>
            <td>${v1}</td>
            <td>${vc2}</td>
            <td>${vd2}</td>
            <td>${v2}</td>
            <td>${vc3}</td>
            <td>${vd3}</td>
            <td>${v3}</td>
            <td>${vc4}</td>
            <td>${vd4}</td>
            <td>${v4}</td>
          </tr>
        `;
        truckTableHtml +=`
        <tr>
            <td>${statType}</td>
            <td>${vc5}</td>
            <td>${vd5}</td>
            <td>${v5}</td>
            <td>${vc6}</td>
            <td>${vd6}</td>
            <td>${v6}</td>
            <td>${vcz}</td>
            <td>${vdz}</td>
            <td>${vz}</td>
            <td>${sumc}</td>
            <td>${sumd}</td>
            <td>${sum}</td>
        </tr>
        `
      });

      passengerTableHtml += `</tbody></table>`;
      truckTableHtml += `</tbody></table>`;

      let htmlContent = `
      <!DOCTYPE html>
        <html>
        <head>
        <title>Print</title>
        <style>
        @page {
          margin: 0.2in;
          size: A4 landscape;
        }
        body {
          margin: 0.2in;
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
          overflow-x: auto;
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
          font-size: 18px;
          min-width: 50px;
          word-break: break-word; /* 允许单词内换行 */
          break-inside: avoid; /* 防止单元格跨页 */
        }
        .el-table th {
          font-weight: bold;
          font-size: 20px;
          background-color: #f5f7fa;
          height: auto;
          line-height: 1.2;
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
          break-inside: avoid; /* 防止行跨页 */
        }
        td, th {
          page-break-inside: avoid;
        }
        .footer-info {
            display: flex;
            justify-content: space-between;
            margin-top: 15px;
            font-size: 12px;
        }
        .operator {
            text-align: left;
        }
        .print-time {
            text-align: right;
        }
        body {
            -webkit-print-color-adjust: exact;
            color-adjust: exact;
            padding: 0;
            margin: 0;
            width: 100%;
            font-size: 11px; /* 调小字体以适应更多内容 */
          }
          .el-table {
            width: 100% !important;
            table-layout: auto !important; /* 自动调整列宽 */
            font-size: 11px; /* 调小字体以适应更多内容 */
          }
          .el-table thead tr {
            background-color: #ebeef5;
            break-inside: avoid; /* 防止表头跨页 */
          }
          .el-table th, .el-table td {
            padding: 4px 3px; /* 减小内边距以节省空间 */
            font-size: 16px;
            min-width: 40px; /* 调整最小宽度 */
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid; /* 防止单元格跨页 */
          }
          .el-table th {
            font-size: 18px; /* 表头字体稍大 */
            font-weight: bold;
            break-inside: avoid; /* 防止表头单元格跨页 */
          }
          .container span {
            font-size: 18px;
          }
          .print-title {
            font-size: 20px;
          }
          .footer-info {
            margin-top: 15px;
            font-size: 18px;
          }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">EU电子支付通行费(MTC+ETC)统计按车型</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">
              <div class="table-section">
                ${passengerTableHtml}
              </div>
              <div class="table-section">
                ${truckTableHtml}
              </div>
            </div>
            ${footerHtml}
        </body>
        </html>
      `;

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

// 自定义滚动条样式
::v-deep .el-table {
  &::-webkit-scrollbar {
    width: 14px;
    height: 14px;
  }

  &::-webkit-scrollbar-track {
    background: #f0f0f0;
    border-radius: 7px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
  }

  &::-webkit-scrollbar-thumb {
    background: linear-gradient(45deg, #409eff, #4a9eff);
    border-radius: 7px;
    border: 3px solid #f0f0f0;
    min-height: 30px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(45deg, #3a8ee6, #409eff);
  }

  &::-webkit-scrollbar-corner {
    background: #f0f0f0;
  }
}

// 如果表格内容区域也有滚动条，也应用样式
::v-deep .el-table__body-wrapper {
  &::-webkit-scrollbar {
    width: 14px;
    height: 14px;
  }

  &::-webkit-scrollbar-track {
    background: #f0f0f0;
    border-radius: 7px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
  }

  &::-webkit-scrollbar-thumb {
    background: linear-gradient(45deg, #409eff, #4a9eff);
    border-radius: 7px;
    border: 3px solid #f0f0f0;
    min-height: 30px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(45deg, #3a8ee6, #409eff);
  }

  &::-webkit-scrollbar-corner {
    background: #f0f0f0;
  }
}
</style>
