<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">ECS2收费站电子支付综合出口流量日统计表</h3>
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

    <el-table v-loading="loading" :data="dataList"  border
              :span-method="arraySpanMethod" :cell-style="cellStyle" ref="myTable" >
      <el-table-column label="时间" align="center" prop="time"/>
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
        <el-table-column label="C卡" align="center" prop="ksumc"/>
        <el-table-column label="D卡" align="center" prop="ksumd"/>
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
      <el-table-column label="客车小计" align="center">
        <el-table-column label="C卡" align="center" prop="hsumc"/>
        <el-table-column label="D卡" align="center" prop="hsumd"/>
      </el-table-column>
      <el-table-column label="总计" align="center">
        <el-table-column label="C卡" align="center" prop="sumc"/>
        <el-table-column label="D卡" align="center" prop="sumd"/>
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

import {ecs2, exportEcs2} from "@/api/report/exitFlow"

export default {
  name: "Ecs2Detail",
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
    cellStyle({row, column, rowIndex, columnIndex}) {
      if (row.subTotalRow === true) {
        return 'background:	#C0C0C0';
      }
    },
    arraySpanMethod({row, column, rowIndex, columnIndex}) {
      if (row.subTotalRow === true) {
        row.shiftId = "小计"
        if (columnIndex === 0) {
          return [1, 2];
        } else if (columnIndex === 1) {
          return [0, 0];
        }
      }
      if (row.totalRow === true) {
        row.shiftId = "合计";
        if (columnIndex === 0) {
          return [1, 2];
        } else if (columnIndex === 1) {
          return [0, 0];
        }
      }
    },
    spanStyle({row, column, rowIndex, columnIndex}) {
      if (row.totalRow === true) {
        if (columnIndex === 0) {
          console.log(rowIndex, columnIndex);
          return [rowIndex, columnIndex];
        } else {
          return [0, 0];
        }
        // console.log('1234');
      }
    },
    tableHeaderColor({row, column, rowIndex, columnIndex}) {
      return 'background:	#C0C0C0; color:#000000; font-size: 16px;';
    },
    tableCellColor({row, column, rowIndex, columnIndex}) {
      return 'background:	#CCFFCC; color:#000000;';
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      ecs2(this.queryParams).then(response => {
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
      this.$confirm('是否确认导出ECS2收费站电子支付综合出口流量日统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEcs2(queryParams);
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

      // 创建一个新的表格结构，模拟原表格的复杂嵌套结构
      let tableHtml = `
        <table class="el-table" border="1" style="border-collapse: collapse; width: 100%;">
          <thead>
            <tr>
              <th rowspan="2" style="min-width: 120px; text-align: center; padding: 8px;">时间</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客一</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客二</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客三</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客四</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客车小计</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货一</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货二</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货三</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货四</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货五</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货六</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货车小计</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">总计</th>
            </tr>
            <tr>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        tableHtml += `
          <tr>
            <td style="padding: 8px; text-align: center;">${row.time !== undefined && row.time !== null ? row.time : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k1c !== undefined && row.k1c !== null ? row.k1c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k1d !== undefined && row.k1d !== null ? row.k1d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k2c !== undefined && row.k2c !== null ? row.k2c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k2d !== undefined && row.k2d !== null ? row.k2d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k3c !== undefined && row.k3c !== null ? row.k3c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k3d !== undefined && row.k3d !== null ? row.k3d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k4c !== undefined && row.k4c !== null ? row.k4c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k4d !== undefined && row.k4d !== null ? row.k4d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.ksumc !== undefined && row.ksumc !== null ? row.ksumc : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.ksumd !== undefined && row.ksumd !== null ? row.ksumd : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h1c !== undefined && row.h1c !== null ? row.h1c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h1d !== undefined && row.h1d !== null ? row.h1d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h2c !== undefined && row.h2c !== null ? row.h2c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h2d !== undefined && row.h2d !== null ? row.h2d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h3c !== undefined && row.h3c !== null ? row.h3c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h3d !== undefined && row.h3d !== null ? row.h3d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h4c !== undefined && row.h4c !== null ? row.h4c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h4d !== undefined && row.h4d !== null ? row.h4d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h5c !== undefined && row.h5c !== null ? row.h5c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h5d !== undefined && row.h5d !== null ? row.h5d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h6c !== undefined && row.h6c !== null ? row.h6c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h6d !== undefined && row.h6d !== null ? row.h6d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.hsumc !== undefined && row.hsumc !== null ? row.hsumc : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.hsumd !== undefined && row.hsumd !== null ? row.hsumd : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.sumc !== undefined && row.sumc !== null ? row.sumc : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.sumd !== undefined && row.sumd !== null ? row.sumd : ''}</td>
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
        /* 2026/1/5打印字体样式增加修改 */
        body {
          margin: 0;
          padding: 15px;
          font-family: "Microsoft YaHei", SimHei, Arial, sans-serif;
          box-sizing: border-box;
          font-size: 18px; /* 增大字体大小 */
        }
        .print-title {
          text-align: center;
          font-size: 24px; /* 增大标题字体 */
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
            font-size: 18px; /* 增大条件信息字体 */
        }
        .table-container {
          margin-top: 10px;
          width: 100%;
          zoom: 0.45;
        }
        .el-table {
          width: 100%;
          border-collapse: collapse;
          table-layout: auto; /* 自动调整列宽 */
          font-size: 16px; /* 增大表格字体 */
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
          font-size: 18px; /* 增大表格内容字体 */
          min-width: 50px;
          word-break: break-word; /* 允许单词内换行 */
          break-inside: avoid; /* 防止单元格跨页 */
        }
        .el-table th {
          font-weight: bold;
          font-size: 20px; /* 增大表头字体 */
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
            font-size: 16px; /* 增大底部信息字体 */
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
            font-size: 18px; /* 2026/1/5打印字体样式增加修改 - 增大字体以提高可读性 */
          }
          .el-table {
            width: 100% !important;
            table-layout: auto !important; /* 自动调整列宽 */
            font-size: 18px;
          }
          .el-table thead tr {
            background-color: #ebeef5;
            break-inside: avoid; /* 防止表头跨页 */
          }
          .el-table th, .el-table td {
            padding: 4px 3px; /* 减小内边距以节省空间 */
            font-size: 20px;
            min-width: 40px; /* 调整最小宽度 */
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid; /* 防止单元格跨页 */
          }
          .el-table th {
            font-size: 22px; /* 2026/1/5打印字体样式增加修改 - 表头字体稍大 */
            font-weight: bold;
            break-inside: avoid; /* 防止表头单元格跨页 */
          }
          .container span {
            font-size: 22px; /* 2026/1/5打印字体样式增加修改 - 增大条件信息字体 */
          }
          .print-title {
            font-size: 24px; /* 2026/1/5打印字体样式增加修改 - 增大标题字体 */
          }
          .footer-info {
            margin-top: 15px;
            font-size: 16px; /* 2026/1/5打印字体样式增加修改 - 增大底部信息字体 */
          }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">ECS2收费站电子支付综合出口流量日统计表</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">${tableHtml}</div>
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
