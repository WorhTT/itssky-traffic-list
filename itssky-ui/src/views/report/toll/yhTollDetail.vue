<template>
  <div class="app-container">
    <div
      style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">YH优惠金额综合报表</h3>
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
            class="print-button-container"
            @click="printTable"
          >打印
          </el-button>
        </el-col>
      </el-row>
    </div>

    <el-table v-loading="loading" :data="dataList" border style="width: 100%" fit ref="myTable">
      <el-table-column label="统计方式" align="center" prop="statType" min-width="120"/>
      <el-table-column label="集装箱优惠" align="center">
        <el-table-column label="优惠前" align="center" prop="jzxq" min-width="100"/>
        <el-table-column label="优惠后" align="center" prop="jzxh" min-width="100"/>
        <el-table-column label="优惠掉" align="center" prop="jzxd" min-width="80"/>
      </el-table-column>
      <el-table-column label="绿色通道优惠" align="center">
        <el-table-column label="优惠前" align="center" prop="lstdq" min-width="120"/>
        <el-table-column label="优惠后" align="center" prop="lstdh" min-width="120"/>
        <el-table-column label="优惠掉" align="center" prop="lstdd" min-width="120"/>
      </el-table-column>
      <el-table-column label="抗震救灾" align="center">
        <el-table-column label="优惠前" align="center" prop="kzjzq" min-width="100"/>
        <el-table-column label="优惠后" align="center" prop="kzjzh" min-width="100"/>
        <el-table-column label="优惠掉" align="center" prop="kzjzd" min-width="80"/>
      </el-table-column>
      <el-table-column label="运管苏通卡货车优惠" align="center">
        <el-table-column label="优惠前" align="center" prop="ygstkq" min-width="100"/>
        <el-table-column label="优惠后" align="center" prop="ygstkh" min-width="100"/>
        <el-table-column label="优惠掉" align="center" prop="ygstkd" min-width="80"/>
      </el-table-column>
      <el-table-column label="军车优惠" align="center">
        <el-table-column label="优惠前" align="center" prop="jcq" min-width="100"/>
        <el-table-column label="优惠后" align="center" prop="jch" min-width="100"/>
        <el-table-column label="优惠掉" align="center" prop="jcd" min-width="80"/>
      </el-table-column>
      <el-table-column label="专用工作卡优惠" align="center">
        <el-table-column label="优惠前" align="center" prop="zygzkq" min-width="100"/>
        <el-table-column label="优惠后" align="center" prop="zygzkh" min-width="100"/>
        <el-table-column label="优惠掉" align="center" prop="zygzkd" min-width="80"/>
      </el-table-column>
      <el-table-column label="收割机优惠" align="center">
        <el-table-column label="优惠前" align="center" prop="sgjq" min-width="100"/>
        <el-table-column label="优惠后" align="center" prop="sgjh" min-width="100"/>
        <el-table-column label="优惠掉" align="center" prop="sgjd" min-width="80"/>
      </el-table-column>
      <el-table-column label="应急优惠" align="center">
        <el-table-column label="优惠前" align="center" prop="yjq" min-width="100"/>
        <el-table-column label="优惠后" align="center" prop="yjh" min-width="100"/>
        <el-table-column label="优惠掉" align="center" prop="yjd" min-width="80"/>
      </el-table-column>
      <el-table-column label="大件运输" align="center">
        <el-table-column label="优惠前" align="center" prop="djysq" min-width="100"/>
        <el-table-column label="优惠后" align="center" prop="djysh" min-width="100"/>
        <el-table-column label="优惠掉" align="center" prop="djysd" min-width="80"/>
      </el-table-column>
      <el-table-column label="合计优惠" align="center">
        <el-table-column label="优惠前" align="center" prop="sumq" min-width="120"/>
        <el-table-column label="优惠后" align="center" prop="sumh" min-width="120"/>
        <el-table-column label="优惠掉" align="center" prop="sumd" min-width="120"/>
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

import {yh, exportYh} from "@/api/report/toll"

export default {
  name: "YhTollDetail",
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
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      yh(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.conditionList = response.conditionList;
        this.corpName = response.title;
        this.operatorName = response.operatorName;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出YH优惠金额综合报表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportYh(queryParams);
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

      // 根据el-table的结构生成打印表格
      let tableHtml = `
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="2" style="min-width: 100px;">统计方式</th>
              <th colspan="3">集装箱优惠</th>
              <th colspan="3">绿色通道优惠</th>
              <th colspan="3">抗震救灾</th>
              <th colspan="3">运管苏通卡货车优惠</th>
              <th colspan="3">军车优惠</th>
              <th colspan="3">专用工作卡优惠</th>
              <th colspan="3">收割机优惠</th>
              <th colspan="3">应急优惠</th>
              <th colspan="3">大件运输</th>
              <th colspan="3">合计优惠</th>
            </tr>
            <tr>
              <th>优惠前</th>
              <th>优惠后</th>
              <th>优惠掉</th>
              <th style="min-width: 80px;">优惠前</th>
              <th>优惠后</th>
              <th style="min-width: 80px;">优惠掉</th>
              <th>优惠前</th>
              <th>优惠后</th>
              <th>优惠掉</th>
              <th style="min-width: 80px;">优惠前</th>
              <th style="min-width: 80px;">优惠后</th>
              <th style="min-width: 70px;">优惠掉</th>
              <th style="min-width: 50px;">优惠前</th>
              <th>优惠后</th>
              <th style="min-width: 50px;">优惠掉</th>
              <th style="min-width: 80px;">优惠前</th>
              <th style="min-width: 80px;">优惠后</th>
              <th style="min-width: 70px;">优惠掉</th>
              <th>优惠前</th>
              <th>优惠后</th>
              <th>优惠掉</th>
              <th>优惠前</th>
              <th>优惠后</th>
              <th>优惠掉</th>
              <th>优惠前</th>
              <th>优惠后</th>
              <th>优惠掉</th>
              <th style="min-width: 80px;">优惠前</th>
              <th style="min-width: 80px;">优惠后</th>
              <th style="min-width: 80px;">优惠掉</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为0的数值，确保0也能正确显示
        const statType = row.statType !== undefined && row.statType !== null ? row.statType : '';
        const jzxq = row.jzxq !== undefined && row.jzxq !== null ? row.jzxq : '';
        const jzxh = row.jzxh !== undefined && row.jzxh !== null ? row.jzxh : '';
        const jzxd = row.jzxd !== undefined && row.jzxd !== null ? row.jzxd : '';
        const lstdq = row.lstdq !== undefined && row.lstdq !== null ? row.lstdq : '';
        const lstdh = row.lstdh !== undefined && row.lstdh !== null ? row.lstdh : '';
        const lstdd = row.lstdd !== undefined && row.lstdd !== null ? row.lstdd : '';
        const kzjzq = row.kzjzq !== undefined && row.kzjzq !== null ? row.kzjzq : '';
        const kzjzh = row.kzjzh !== undefined && row.kzjzh !== null ? row.kzjzh : '';
        const kzjzd = row.kzjzd !== undefined && row.kzjzd !== null ? row.kzjzd : '';
        const ygstkq = row.ygstkq !== undefined && row.ygstkq !== null ? row.ygstkq : '';
        const ygstkh = row.ygstkh !== undefined && row.ygstkh !== null ? row.ygstkh : '';
        const ygstkd = row.ygstkd !== undefined && row.ygstkd !== null ? row.ygstkd : '';
        const jcq = row.jcq !== undefined && row.jcq !== null ? row.jcq : '';
        const jch = row.jch !== undefined && row.jch !== null ? row.jch : '';
        const jcd = row.jcd !== undefined && row.jcd !== null ? row.jcd : '';
        const zygzkq = row.zygzkq !== undefined && row.zygzkq !== null ? row.zygzkq : '';
        const zygzkh = row.zygzkh !== undefined && row.zygzkh !== null ? row.zygzkh : '';
        const zygzkd = row.zygzkd !== undefined && row.zygzkd !== null ? row.zygzkd : '';
        const sgjq = row.sgjq !== undefined && row.sgjq !== null ? row.sgjq : '';
        const sgjh = row.sgjh !== undefined && row.sgjh !== null ? row.sgjh : '';
        const sgjd = row.sgjd !== undefined && row.sgjd !== null ? row.sgjd : '';
        const yjq = row.yjq !== undefined && row.yjq !== null ? row.yjq : '';
        const yjh = row.yjh !== undefined && row.yjh !== null ? row.yjh : '';
        const yjd = row.yjd !== undefined && row.yjd !== null ? row.yjd : '';
        const djysq = row.djysq !== undefined && row.djysq !== null ? row.djysq : '';
        const djysh = row.djysh !== undefined && row.djysh !== null ? row.djysh : '';
        const djysd = row.djysd !== undefined && row.djysd !== null ? row.djysd : '';
        const sumq = row.sumq !== undefined && row.sumq !== null ? row.sumq : '';
        const sumh = row.sumh !== undefined && row.sumh !== null ? row.sumh : '';
        const sumd = row.sumd !== undefined && row.sumd !== null ? row.sumd : '';

        tableHtml += `
          <tr>
            <td>${statType}</td>
            <td>${jzxq}</td>
            <td>${jzxh}</td>
            <td>${jzxd}</td>
            <td>${lstdq}</td>
            <td>${lstdh}</td>
            <td>${lstdd}</td>
            <td>${kzjzq}</td>
            <td>${kzjzh}</td>
            <td>${kzjzd}</td>
            <td>${ygstkq}</td>
            <td>${ygstkh}</td>
            <td>${ygstkd}</td>
            <td>${jcq}</td>
            <td>${jch}</td>
            <td>${jcd}</td>
            <td>${zygzkq}</td>
            <td>${zygzkh}</td>
            <td>${zygzkd}</td>
            <td>${sgjq}</td>
            <td>${sgjh}</td>
            <td>${sgjd}</td>
            <td>${yjq}</td>
            <td>${yjh}</td>
            <td>${yjd}</td>
            <td>${djysq}</td>
            <td>${djysh}</td>
            <td>${djysd}</td>
            <td>${sumq}</td>
            <td>${sumh}</td>
            <td>${sumd}</td>
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
          zoom: 0.8; /* 屏幕显示时也进行适当缩放 */
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
          padding: 12px 5px; /* 增大内边距以增加行高 */
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
            zoom: 0.7; /* 调整缩放比例以适应增加的行高 */
          }
          .el-table th, .el-table td {
            padding: 8px 3px; /* 增大内边距以增加行高 */
            font-size: 17px;
            min-width: 40px;
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid;
          }
          .el-table th {
            font-size: 19px; /* 表头字体稍大 */
            font-weight: bold;
            break-inside: avoid;
          }
          .container span {
            font-size: 19px;
          }
          .print-title {
            font-size: 22px;
          }
          .footer-info {
            margin-top: 10px;
            font-size: 19px;
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
            <div class="print-title">YH优惠金额综合报表</div>
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
