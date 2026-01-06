<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">MOB移动支付统计按车型统计</h3>
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
      <el-table-column label="客车" align="center">
        <el-table-column label="客一" align="center">
          <el-table-column label="流量" align="center" prop="k1f" min-width="70"/>
          <el-table-column label="金额" align="center" prop="k1t" min-width="120"/>
        </el-table-column>
        <el-table-column label="客二" align="center">
          <el-table-column label="流量" align="center" prop="k2f" min-width="50"/>
          <el-table-column label="金额" align="center" prop="k2t" min-width="80"/>
        </el-table-column>
        <el-table-column label="客三" align="center">
          <el-table-column label="流量" align="center" prop="k3f" min-width="50"/>
          <el-table-column label="金额" align="center" prop="k3t" min-width="80"/>
        </el-table-column>
        <el-table-column label="客四" align="center">
          <el-table-column label="流量" align="center" prop="k4f" min-width="50"/>
          <el-table-column label="金额" align="center" prop="k4t" min-width="80"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="流量" align="center" prop="ksumf" min-width="70"/>
          <el-table-column label="金额" align="center" prop="ksumt" min-width="120"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="货车" align="center">
        <el-table-column label="货一" align="center">
          <el-table-column label="流量" align="center" prop="h1f" min-width="70"/>
          <el-table-column label="金额" align="center" prop="h1t" min-width="120"/>
        </el-table-column>
        <el-table-column label="货二" align="center">
          <el-table-column label="流量" align="center" prop="h2f" min-width="50"/>
          <el-table-column label="金额" align="center" prop="h2t" min-width="80"/>
        </el-table-column>
        <el-table-column label="货三" align="center">
          <el-table-column label="流量" align="center" prop="h3f" min-width="50"/>
          <el-table-column label="金额" align="center" prop="h3t" min-width="80"/>
        </el-table-column>
        <el-table-column label="货四" align="center">
          <el-table-column label="流量" align="center" prop="h4f" min-width="50"/>
          <el-table-column label="金额" align="center" prop="h4t" min-width="80"/>
        </el-table-column>
        <el-table-column label="货五" align="center">
          <el-table-column label="流量" align="center" prop="h5f" min-width="50"/>
          <el-table-column label="金额" align="center" prop="h5t" min-width="80"/>
        </el-table-column>
        <el-table-column label="货六" align="center">
          <el-table-column label="流量" align="center" prop="h6f" min-width="50"/>
          <el-table-column label="金额" align="center" prop="h6t" min-width="80"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="流量" align="center" prop="hsumf" min-width="70"/>
          <el-table-column label="金额" align="center" prop="hsumt" min-width="120"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="专车" align="center">
        <el-table-column label="专一" align="center">
          <el-table-column label="流量" align="center" prop="z1f"/>
          <el-table-column label="金额" align="center" prop="z1t"/>
        </el-table-column>
        <el-table-column label="专二" align="center">
          <el-table-column label="流量" align="center" prop="z2f"/>
          <el-table-column label="金额" align="center" prop="z2t"/>
        </el-table-column>
        <el-table-column label="专三" align="center">
          <el-table-column label="流量" align="center" prop="z3f"/>
          <el-table-column label="金额" align="center" prop="z3t"/>
        </el-table-column>
        <el-table-column label="专四" align="center">
          <el-table-column label="流量" align="center" prop="z4f"/>
          <el-table-column label="金额" align="center" prop="z4t"/>
        </el-table-column>
        <el-table-column label="专五" align="center">
          <el-table-column label="流量" align="center" prop="z5f"/>
          <el-table-column label="金额" align="center" prop="z5t"/>
        </el-table-column>
        <el-table-column label="专六" align="center">
          <el-table-column label="流量" align="center" prop="z6f"/>
          <el-table-column label="金额" align="center" prop="z6t"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="流量" align="center" prop="zsumf"/>
          <el-table-column label="金额" align="center" prop="zsumt"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="合计" align="center">
        <el-table-column label="流量" align="center" prop="sumf"/>
        <el-table-column label="金额" align="center" prop="sumt" min-width="120"/>
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

import {mobVc, exportMobVc} from "@/api/report/toll"

export default {
  name: "MobVcDetail",
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
      mobVc(this.queryParams).then(response => {
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
      this.$confirm('是否确认导出MOB移动支付统计按车型统计?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportMobVc(queryParams);
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

      let kc = `
        <h3 style="text-align: center; margin: 10px 0; font-size: 18px;">客车数据</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="3" style="min-width: 100px;">统计方式</th>
              <th colspan="10">客车</th>
            </tr>
            <tr>
              <th colspan="2">客一</th>
              <th colspan="2">客二</th>
              <th colspan="2">客三</th>
              <th colspan="2">客四</th>
              <th colspan="2">小计</th>
            </tr>
            <tr>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
            </tr>
          </thead>
          <tbody>
      `;

      let hc = `
        <h3 style="text-align: center; margin: 10px 0; font-size: 18px;">货车数据</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="3" style="min-width: 100px;">统计方式</th>
              <th colspan="14">货车</th>
            </tr>
            <tr>
              <th colspan="2">货一</th>
              <th colspan="2">货二</th>
              <th colspan="2">货三</th>
              <th colspan="2">货四</th>
              <th colspan="2">货五</th>
              <th colspan="2">货六</th>
              <th colspan="2">小计</th>
            </tr>
            <tr>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
            </tr>
          </thead>
          <tbody>
      `;

      let zc = `
        <h3 style="text-align: center; margin: 10px 0; font-size: 18px;">专车数据</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="3" style="min-width: 100px;">统计方式</th>
              <th colspan="14">专车</th>
            </tr>
            <tr>
              <th colspan="2">专一</th>
              <th colspan="2">专二</th>
              <th colspan="2">专三</th>
              <th colspan="2">专四</th>
              <th colspan="2">专五</th>
              <th colspan="2">专六</th>
              <th colspan="2">小计</th>
            </tr>
            <tr>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
              <th>流量</th>
              <th>金额</th>
            </tr>
          </thead>
          <tbody>
      `;

      let hj = `
        <h3 style="text-align: center; margin: 10px 0; font-size: 18px;">合计数据</h3>
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="2" style="min-width: 100px;">统计方式</th>
              <th colspan="2">合计</th>
            </tr>
            <tr>
              <th rowspan="2">流量</th>
              <th rowspan="2">金额</th>
            </tr>
          </thead>
          <tbody>
      `;


      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为0的数值，确保0也能正确显示
        const statType = row.statType !== undefined && row.statType !== null ? row.statType : '';
        const k1f = row.k1f !== undefined && row.k1f !== null ? row.k1f : '';
        const k1t = row.k1t !== undefined && row.k1t !== null ? row.k1t : '';
        const k2f = row.k2f !== undefined && row.k2f !== null ? row.k2f : '';
        const k2t = row.k2t !== undefined && row.k2t !== null ? row.k2t : '';
        const k3f = row.k3f !== undefined && row.k3f !== null ? row.k3f : '';
        const k3t = row.k3t !== undefined && row.k3t !== null ? row.k3t : '';
        const k4f = row.k4f !== undefined && row.k4f !== null ? row.k4f : '';
        const k4t = row.k4t !== undefined && row.k4t !== null ? row.k4t : '';
        const ksumf = row.ksumf !== undefined && row.ksumf !== null ? row.ksumf : '';
        const ksumt = row.ksumt !== undefined && row.ksumt !== null ? row.ksumt : '';
        const h1f = row.h1f !== undefined && row.h1f !== null ? row.h1f : '';
        const h1t = row.h1t !== undefined && row.h1t !== null ? row.h1t : '';
        const h2f = row.h2f !== undefined && row.h2f !== null ? row.h2f : '';
        const h2t = row.h2t !== undefined && row.h2t !== null ? row.h2t : '';
        const h3f = row.h3f !== undefined && row.h3f !== null ? row.h3f : '';
        const h3t = row.h3t !== undefined && row.h3t !== null ? row.h3t : '';
        const h4f = row.h4f !== undefined && row.h4f !== null ? row.h4f : '';
        const h4t = row.h4t !== undefined && row.h4t !== null ? row.h4t : '';
        const h5f = row.h5f !== undefined && row.h5f !== null ? row.h5f : '';
        const h5t = row.h5t !== undefined && row.h5t !== null ? row.h5t : '';
        const h6f = row.h6f !== undefined && row.h6f !== null ? row.h6f : '';
        const h6t = row.h6t !== undefined && row.h6t !== null ? row.h6t : '';
        const hsumf = row.hsumf !== undefined && row.hsumf !== null ? row.hsumf : '';
        const hsumt = row.hsumt !== undefined && row.hsumt !== null ? row.hsumt : '';
        const z1f = row.z1f !== undefined && row.z1f !== null ? row.z1f : '';
        const z1t = row.z1t !== undefined && row.z1t !== null ? row.z1t : '';
        const z2f = row.z2f !== undefined && row.z2f !== null ? row.z2f : '';
        const z2t = row.z2t !== undefined && row.z2t !== null ? row.z2t : '';
        const z3f = row.z3f !== undefined && row.z3f !== null ? row.z3f : '';
        const z3t = row.z3t !== undefined && row.z3t !== null ? row.z3t : '';
        const z4f = row.z4f !== undefined && row.z4f !== null ? row.z4f : '';
        const z4t = row.z4t !== undefined && row.z4t !== null ? row.z4t : '';
        const z5f = row.z5f !== undefined && row.z5f !== null ? row.z5f : '';
        const z5t = row.z5t !== undefined && row.z5t !== null ? row.z5t : '';
        const z6f = row.z6f !== undefined && row.z6f !== null ? row.z6f : '';
        const z6t = row.z6t !== undefined && row.z6t !== null ? row.z6t : '';
        const zsumf = row.zsumf !== undefined && row.zsumf !== null ? row.zsumf : '';
        const zsumt = row.zsumt !== undefined && row.zsumt !== null ? row.zsumt : '';
        const sumf = row.sumf !== undefined && row.sumf !== null ? row.sumf : '';
        const sumt = row.sumt !== undefined && row.sumt !== null ? row.sumt : '';
        kc += `
          <tr>
            <td>${statType}</td>
            <td>${k1f}</td>
            <td>${k1t}</td>
            <td>${k2f}</td>
            <td>${k2t}</td>
            <td>${k3f}</td>
            <td>${k3t}</td>
            <td>${k4f}</td>
            <td>${k4t}</td>
            <td>${ksumf}</td>
            <td>${ksumt}</td>
          </tr>
        `;
        hc += `
        <tr>
            <td>${statType}</td>
            <td>${h1f}</td>
            <td>${h1t}</td>
            <td>${h2f}</td>
            <td>${h2t}</td>
            <td>${h3f}</td>
            <td>${h3t}</td>
            <td>${h4f}</td>
            <td>${h4t}</td>
            <td>${h5f}</td>
            <td>${h5t}</td>
            <td>${h6f}</td>
            <td>${h6t}</td>
            <td>${hsumf}</td>
            <td>${hsumt}</td>
        </tr>
        `
        zc += `
        <tr>
            <td>${statType}</td>
            <td>${z1f}</td>
            <td>${z1t}</td>
            <td>${z2f}</td>
            <td>${z2t}</td>
            <td>${z3f}</td>
            <td>${z3t}</td>
            <td>${z4f}</td>
            <td>${z4t}</td>
            <td>${z5f}</td>
            <td>${z5t}</td>
            <td>${z6f}</td>
            <td>${z6t}</td>
            <td>${zsumf}</td>
            <td>${zsumt}</td>
        </tr>
        `
        hj += `
            <tr>
            <td>${statType}</td>
            <td>${sumf}</td>
            <td>${sumt}</td>
        </tr>
        `
      });
      kc += `</tbody></table>`;
      hc += `</tbody></table>`;
      zc += `</tbody></table>`;
      hj += `</tbody></table>`;

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
            <div class="print-title">MOB移动支付统计按车型统计</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">
              <div class="table-section">
                ${kc}
              </div>
              <div class="table-section">
                ${hc}
              </div>
              <div class="table-section">
                ${zc}
              </div>
              <div class="table-section">
                ${hj}
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
