<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">ERJ电子支付(ETC)入口流量统计表</h3>
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
    </div>

    <el-table v-loading="loading" :data="dataList" border ref="myTable" >
      <el-table-column label="统计方式" align="center" prop="statType" min-width="120"/>
      <el-table-column label="客一" align="center">
        <el-table-column label="C卡" align="center" prop="k1c"/>
        <el-table-column label="D卡" align="center" prop="k1d"/>
      </el-table-column>
      <el-table-column label="客二" align="center">
        <el-table-column label="C卡" align="center" prop="k2c"/>
        <el-table-column label="D卡" align="center" prop="k2d"/>
      </el-table-column>
      <el-table-column label="客三" align="center">
        <el-table-column label="C卡" align="center" prop="k3c"/>
        <el-table-column label="D卡" align="center" prop="k3d"/>
      </el-table-column>
      <el-table-column label="客四" align="center">
        <el-table-column label="C卡" align="center" prop="k4c"/>
        <el-table-column label="D卡" align="center" prop="k4d"/>
      </el-table-column>
      <el-table-column label="客车小计" align="center">
        <el-table-column label="C卡" align="center" prop="kamountc"/>
        <el-table-column label="D卡" align="center" prop="kamountd"/>
      </el-table-column>
      <el-table-column label="货一" align="center">
        <el-table-column label="C卡" align="center" prop="h1c"/>
        <el-table-column label="D卡" align="center" prop="h1d"/>
      </el-table-column>
      <el-table-column label="货二" align="center">
        <el-table-column label="C卡" align="center" prop="h2c"/>
        <el-table-column label="D卡" align="center" prop="h2d"/>
      </el-table-column>
      <el-table-column label="货三" align="center">
        <el-table-column label="C卡" align="center" prop="h3c"/>
        <el-table-column label="D卡" align="center" prop="h3d"/>
      </el-table-column>
      <el-table-column label="货四" align="center">
        <el-table-column label="C卡" align="center" prop="h4c"/>
        <el-table-column label="D卡" align="center" prop="h4d"/>
      </el-table-column>
      <el-table-column label="货五" align="center">
        <el-table-column label="C卡" align="center" prop="h5c"/>
        <el-table-column label="D卡" align="center" prop="h5d"/>
      </el-table-column>
      <el-table-column label="货六" align="center">
        <el-table-column label="C卡" align="center" prop="h6c"/>
        <el-table-column label="D卡" align="center" prop="h6d"/>
      </el-table-column>
      <el-table-column label="货车小计" align="center">
        <el-table-column label="C卡" align="center" prop="hamountc"/>
        <el-table-column label="D卡" align="center" prop="hamountd"/>
      </el-table-column>
      <el-table-column label="专一" align="center">
        <el-table-column label="C卡" align="center" prop="z1c"/>
        <el-table-column label="D卡" align="center" prop="z1d"/>
      </el-table-column>
      <el-table-column label="专二" align="center">
        <el-table-column label="C卡" align="center" prop="z2c"/>
        <el-table-column label="D卡" align="center" prop="z2d"/>
      </el-table-column>
      <el-table-column label="专三" align="center">
        <el-table-column label="C卡" align="center" prop="z3c"/>
        <el-table-column label="D卡" align="center" prop="z3d"/>
      </el-table-column>
      <el-table-column label="专四" align="center">
        <el-table-column label="C卡" align="center" prop="z4c"/>
        <el-table-column label="D卡" align="center" prop="z4d"/>
      </el-table-column>
      <el-table-column label="专五" align="center">
        <el-table-column label="C卡" align="center" prop="z5c"/>
        <el-table-column label="D卡" align="center" prop="z5d"/>
      </el-table-column>
      <el-table-column label="专六" align="center">
        <el-table-column label="C卡" align="center" prop="z6c"/>
        <el-table-column label="D卡" align="center" prop="z6d"/>
      </el-table-column>
      <el-table-column label="专车小计" align="center">
        <el-table-column label="C卡" align="center" prop="zamountc"/>
        <el-table-column label="D卡" align="center" prop="zamountd"/>
      </el-table-column>
      <el-table-column label="总计" align="center">
        <el-table-column label="C卡" align="center" prop="allAmountc"/>
        <el-table-column label="D卡" align="center" prop="allAmountd"/>
        <el-table-column label="合计" align="center" prop="total"/>
      </el-table-column>
    </el-table>
    <!-- 添加底部信息区域 -->
    <div style="display: flex; justify-content: space-between; margin-top: 20px;">
      <span>操作人：{{ operatorName }}</span>
      <span>打印时间：{{ currentDateTime }}</span>
    </div>
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {erjsFlow, exportErjFlow} from "@/api/report/exitFlow"
import {getLoginUser} from "@/api/login";

export default {
  name: "ErjFlowDetail",
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
      conditionList:[],
      operatorName: '',
      corpName: '',
    };
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
  computed: {
    // corpName() {
    //   return process.env.VUE_APP_CORP_NAME ? process.env.VUE_APP_CORP_NAME : '宁杭高速'
    // },
    currentDateTime() {
      return this.getCurrentDateTime();
    },
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
    getList() {
      this.loading = true;
      this.queryParams.flagStr = '1';
      erjsFlow(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.conditionList = response.conditionList;
        this.corpName = response.title;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      this.queryParams.flagStr = '1';
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出ERJ电子支付(ETC)入口流量统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportErjFlow(queryParams);
      }).then(response => {
        this.downloadFile(response.msg);
      }).finally(() => {
        this.loading = false;
      })
    },
    printTable() {
      const corpName = this.corpName;
      const printFrame = document.getElementById('printFrame');
      const printDocument = printFrame.contentDocument || printFrame.contentWindow.document;
      let conditionListHtml = this.conditionList.map(item => `<span>${item}</span>`).join('');
      // 获取操作人信息
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
        <h3 style="text-align: center; margin: 10px 0;font-size: 26px;">客车数据</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="2" style="min-width: 60px;">统计方式</th>
              <th colspan="2">客一</th>
              <th colspan="2">客二</th>
              <th colspan="2">客三</th>
              <th colspan="2">客四</th>
              <th colspan="2">客车小计</th>
            </tr>
            <tr>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
            </tr>
          </thead>
          <tbody>
      `;

      let truckTableHtml = `
        <h3 style="text-align: center; margin: 10px 0;font-size: 26px;">货车数据</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="2" style="min-width: 60px;">统计方式</th>
              <th colspan="2">货一</th>
              <th colspan="2">货二</th>
              <th colspan="2">货三</th>
              <th colspan="2">货四</th>
              <th colspan="2">货五</th>
              <th colspan="2">货六</th>
              <th colspan="2">货车小计</th>
            </tr>
            <tr>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
            </tr>
          </thead>
          <tbody>
      `;

      let specialTableHtml = `
        <h3 style="text-align: center; margin: 10px 0;font-size: 26px;">专车数据</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="2" style="min-width: 60px;">统计方式</th>
              <th colspan="2">专一</th>
              <th colspan="2">专二</th>
              <th colspan="2">专三</th>
              <th colspan="2">专四</th>
              <th colspan="2">专五</th>
              <th colspan="2">专六</th>
              <th colspan="2">专车小计</th>
            </tr>
            <tr>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>C卡</th>
              <th>D卡</th>
            </tr>
          </thead>
          <tbody>
      `;

      let totalTableHtml = `
        <h3 style="text-align: center; margin: 10px 0; font-size: 26px;">总计数据</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th style="min-width: 60px;">统计方式</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        const statType = row.statType !== undefined && row.statType !== null ? row.statType : '';
        const k1c = row.k1c !== undefined && row.k1c !== null ? row.k1c : '';
        const k1d = row.k1d !== undefined && row.k1d !== null ? row.k1d : '';
        const k2c = row.k2c !== undefined && row.k2c !== null ? row.k2c : '';
        const k2d = row.k2d !== undefined && row.k2d !== null ? row.k2d : '';
        const k3c = row.k3c !== undefined && row.k3c !== null ? row.k3c : '';
        const k3d = row.k3d !== undefined && row.k3d !== null ? row.k3d : '';
        const k4c = row.k4c !== undefined && row.k4c !== null ? row.k4c : '';
        const k4d = row.k4d !== undefined && row.k4d !== null ? row.k4d : '';
        const kamountc = row.kamountc !== undefined && row.kamountc !== null ? row.kamountc : '';
        const kamountd = row.kamountd !== undefined && row.kamountd !== null ? row.kamountd : '';
        const h1c = row.h1c !== undefined && row.h1c !== null ? row.h1c : '';
        const h1d = row.h1d !== undefined && row.h1d !== null ? row.h1d : '';
        const h2c = row.h2c !== undefined && row.h2c !== null ? row.h2c : '';
        const h2d = row.h2d !== undefined && row.h2d !== null ? row.h2d : '';
        const h3c = row.h3c !== undefined && row.h3c !== null ? row.h3c : '';
        const h3d = row.h3d !== undefined && row.h3d !== null ? row.h3d : '';
        const h4c = row.h4c !== undefined && row.h4c !== null ? row.h4c : '';
        const h4d = row.h4d !== undefined && row.h4d !== null ? row.h4d : '';
        const h5c = row.h3c !== undefined && row.h3c !== null ? row.h3c : '';
        const h5d = row.h3d !== undefined && row.h3d !== null ? row.h3d : '';
        const h6c = row.h4c !== undefined && row.h4c !== null ? row.h4c : '';
        const h6d = row.h4d !== undefined && row.h4d !== null ? row.h4d : '';
        const hamountc = row.hamountc !== undefined && row.hamountc !== null ? row.hamountc : '';
        const hamountd = row.hamountd !== undefined && row.hamountd !== null ? row.hamountd : '';
        const z1c = row.z1c !== undefined && row.z1c !== null ? row.z1c : '';
        const z1d = row.z1d !== undefined && row.z1d !== null ? row.z1d : '';
        const z2c = row.z2c !== undefined && row.z2c !== null ? row.z2c : '';
        const z2d = row.z2d !== undefined && row.z2d !== null ? row.z2d : '';
        const z3c = row.z3c !== undefined && row.z3c !== null ? row.z3c : '';
        const z3d = row.z3d !== undefined && row.z3d !== null ? row.z3d : '';
        const z4c = row.z4c !== undefined && row.z4c !== null ? row.z4c : '';
        const z4d = row.z4d !== undefined && row.z4d !== null ? row.z4d : '';
        const z5c = row.z3c !== undefined && row.z3c !== null ? row.z3c : '';
        const z5d = row.z3d !== undefined && row.z3d !== null ? row.z3d : '';
        const z6c = row.z4c !== undefined && row.z4c !== null ? row.z4c : '';
        const z6d = row.z4d !== undefined && row.z4d !== null ? row.z4d : '';
        const zamountc = row.zamountc !== undefined && row.zamountc !== null ? row.zamountc : '';
        const zamountd = row.zamountd !== undefined && row.zamountd !== null ? row.zamountd : '';
        const allamountc = row.allAmountc !== undefined && row.allAmountc !== null ? row.allAmountc : '';
        const allamountd = row.allAmountd !== undefined && row.allAmountd !== null ? row.allAmountd : '';
        const total = row.total !== undefined && row.total !== null ? row.total : '';
        // 客车表格行
        passengerTableHtml += `
          <tr>
            <td>${statType}</td>
            <td>${k1c}</td>
            <td>${k1d}</td>
            <td>${k2c}</td>
            <td>${k2d}</td>
            <td>${k3c}</td>
            <td>${k3d}</td>
            <td>${k4c}</td>
            <td>${k4d}</td>
            <td>${kamountc}</td>
            <td>${kamountd}</td>
          </tr>
        `;
        // 货车表格行
        truckTableHtml += `
          <tr>
            <td>${statType}</td>
            <td>${h1c}</td>
            <td>${h1d}</td>
            <td>${h2c}</td>
            <td>${h2d}</td>
            <td>${h3c}</td>
            <td>${h3d}</td>
            <td>${h4c}</td>
            <td>${h4d}</td>
            <td>${h5c}</td>
            <td>${h5d}</td>
            <td>${h6c}</td>
            <td>${h6d}</td>
            <td>${hamountc}</td>
            <td>${hamountd}</td>
          </tr>
        `;
        // 专车表格行
        specialTableHtml += `
          <tr>
            <td>${statType}</td>
            <td>${z1c}</td>
            <td>${z1d}</td>
            <td>${z2c}</td>
            <td>${z2d}</td>
            <td>${z3c}</td>
            <td>${z3d}</td>
            <td>${z4c}</td>
            <td>${z4d}</td>
            <td>${z5c}</td>
            <td>${z5d}</td>
            <td>${z6c}</td>
            <td>${z6d}</td>
            <td>${zamountc}</td>
            <td>${zamountd}</td>
          </tr>
        `;
        // 总计表格行
        totalTableHtml += `
          <tr>
            <td>${statType}</td>
            <td>${allamountc}</td>
            <td>${allamountd}</td>
            <td>${total}</td>
          </tr>
        `;
      });
      passengerTableHtml += `</tbody></table>`;
      truckTableHtml += `</tbody></table>`;
      specialTableHtml += `</tbody></table>`;
      totalTableHtml += `</tbody></table>`;
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
            <div class="print-title">ERJ电子支付(ETC)入口流量统计表</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">
              <div class="table-section">
                ${passengerTableHtml}
              </div>
              <div class="table-section">
                ${truckTableHtml}
              </div>
              <div class="table-section">
                ${specialTableHtml}
              </div>
              <div class="table-section">
                ${totalTableHtml}
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
::v-deep .el-table .el-table__header-wrapper th {
  height: 20px;
}
::v-deep .el-table__header-wrapper {
  & thead {
    tr {
      background-color: #f5f7fa !important; // 设置表头行背景色
    }
  }
}

::v-deep .el-table th {
  background-color: #f5f7fa !important; // 确保表头单元格背景色
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
