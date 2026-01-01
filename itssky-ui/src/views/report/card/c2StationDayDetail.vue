<template>
  <div class="app-container">
    <div
      style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">C2收费站通行卡回收日统计表</h3>
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

    <el-table v-loading="loading" :data="dataList" border ref="myTable"
              :span-method="arraySpanMethod" :cell-style="cellStyle">
      <el-table-column label="班次" align="center" prop="shiftId"/>
      <el-table-column label="班组" align="center" prop="teamId"/>
      <el-table-column label="工号" align="center" prop="operatorId" min-width="120"/>
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
    <!-- 添加底部信息区域 -->
    <div style="display: flex; justify-content: space-between; margin-top: 20px;">
      <span>操作人：{{ operatorName }}</span>
      <span>打印时间：{{ currentDateTime }}</span>
    </div>
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {s1StationShift, exportC2StationShift} from "@/api/report/card"
import {getLoginUser} from "@/api/login";

export default {
  name: "C2StationDayDetail",
  data() {
    return {
      // 遮罩层
      props: {multiple: true},
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
  watch: {},
  methods: {
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      if (row.totalRow  === true) {
        row.shiftId = "合计";
        if (columnIndex === 0) {
          return [1, 3];
        } else if (columnIndex === 1 || columnIndex === 2) {
          return [0, 0];
        }
      }
    },
    cellStyle({row, column, rowIndex, columnIndex}) {
      // if (row.totalRow === true) {
      //   return 'background:	#FFD040';
      // }
    },
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
      s1StationShift(this.queryParams).then(response => {
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
      this.$confirm('是否确认导出C2收费站通行卡回收日统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportC2StationShift(queryParams);
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
              <th style="min-width: 50px;">班次</th>
              <th style="min-width: 50px;">班组</th>
              <th style="min-width: 80px;">工号</th>
              <th style="min-width: 50px;">客一</th>
              <th style="min-width: 50px;">客二</th>
              <th style="min-width: 50px;">客三</th>
              <th style="min-width: 50px;">客四</th>
              <th style="min-width: 80px;">客车小计</th>
              <th style="min-width: 50px;">货一</th>
              <th style="min-width: 50px;">货二</th>
              <th style="min-width: 50px;">货三</th>
              <th style="min-width: 50px;">货四</th>
              <th style="min-width: 50px;">货五</th>
              <th style="min-width: 50px;">货六</th>
              <th style="min-width: 80px;">货车小计</th>
              <th style="min-width: 50px;">专一</th>
              <th style="min-width: 50px;">专二</th>
              <th style="min-width: 50px;">专三</th>
              <th style="min-width: 50px;">专四</th>
              <th style="min-width: 50px;">专五</th>
              <th style="min-width: 50px;">专六</th>
              <th style="min-width: 80px;">专车小计</th>
              <th style="min-width: 50px;">军车</th>
              <th style="min-width: 50px;">公务</th>
              <th style="min-width: 50px;">车队</th>
              <th style="min-width: 50px;">优惠</th>
              <th style="min-width: 50px;">ETC</th>
              <th style="min-width: 50px;">纸券</th>
              <th style="min-width: 60px;">应收卡</th>
              <th style="min-width: 60px;">实收卡</th>
              <th style="min-width: 50px;">无卡</th>
              <th style="min-width: 50px;">卡损</th>
              <th style="min-width: 60px;">总流量</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为空的数值，确保空值也能正确显示
        const shiftId = row.shiftId !== undefined && row.shiftId !== null ? row.shiftId : '';
        const teamId = row.teamId !== undefined && row.teamId !== null ? row.teamId : '';
        const operatorId = row.operatorId !== undefined && row.operatorId !== null ? row.operatorId : '';
        const cust1 = row.cust1 !== undefined && row.cust1 !== null ? row.cust1 : '';
        const cust2 = row.cust2 !== undefined && row.cust2 !== null ? row.cust2 : '';
        const cust3 = row.cust3 !== undefined && row.cust3 !== null ? row.cust3 : '';
        const cust4 = row.cust4 !== undefined && row.cust4 !== null ? row.cust4 : '';
        const custSubTotal = row.custSubTotal !== undefined && row.custSubTotal !== null ? row.custSubTotal : '';
        const truck1 = row.truck1 !== undefined && row.truck1 !== null ? row.truck1 : '';
        const truck2 = row.truck2 !== undefined && row.truck2 !== null ? row.truck2 : '';
        const truck3 = row.truck3 !== undefined && row.truck3 !== null ? row.truck3 : '';
        const truck4 = row.truck4 !== undefined && row.truck4 !== null ? row.truck4 : '';
        const truck5 = row.truck5 !== undefined && row.truck5 !== null ? row.truck5 : '';
        const truck6 = row.truck6 !== undefined && row.truck6 !== null ? row.truck6 : '';
        const truckSubTotal = row.truckSubTotal !== undefined && row.truckSubTotal !== null ? row.truckSubTotal : '';
        const spec1 = row.spec1 !== undefined && row.spec1 !== null ? row.spec1 : '';
        const spec2 = row.spec2 !== undefined && row.spec2 !== null ? row.spec2 : '';
        const spec3 = row.spec3 !== undefined && row.spec3 !== null ? row.spec3 : '';
        const spec4 = row.spec4 !== undefined && row.spec4 !== null ? row.spec4 : '';
        const spec5 = row.spec5 !== undefined && row.spec5 !== null ? row.spec5 : '';
        const spec6 = row.spec6 !== undefined && row.spec6 !== null ? row.spec6 : '';
        const specSubTotal = row.specSubTotal !== undefined && row.specSubTotal !== null ? row.specSubTotal : '';
        const militaryNum = row.militaryNum !== undefined && row.militaryNum !== null ? row.militaryNum : '';
        const officialNum = row.officialNum !== undefined && row.officialNum !== null ? row.officialNum : '';
        const fleetNum = row.fleetNum !== undefined && row.fleetNum !== null ? row.fleetNum : '';
        const preferNum = row.preferNum !== undefined && row.preferNum !== null ? row.preferNum : '';
        const etcNum = row.etcNum !== undefined && row.etcNum !== null ? row.etcNum : '';
        const paperNum = row.paperNum !== undefined && row.paperNum !== null ? row.paperNum : '';
        const issuedNum = row.issuedNum !== undefined && row.issuedNum !== null ? row.issuedNum : '';
        const actualNum = row.actualNum !== undefined && row.actualNum !== null ? row.actualNum : '';
        const noneNum = row.noneNum !== undefined && row.noneNum !== null ? row.noneNum : '';
        const badNum = row.badNum !== undefined && row.badNum !== null ? row.badNum : '';
        const totalFlow = row.totalFlow !== undefined && row.totalFlow !== null ? row.totalFlow : '';

        if (row.totalRow === true) {
          tableHtml += `
          <tr>
            <td colspan="3">合计</td>
            <td>${cust1}</td>
            <td>${cust2}</td>
            <td>${cust3}</td>
            <td>${cust4}</td>
            <td>${custSubTotal}</td>
            <td>${truck1}</td>
            <td>${truck2}</td>
            <td>${truck3}</td>
            <td>${truck4}</td>
            <td>${truck5}</td>
            <td>${truck6}</td>
            <td>${truckSubTotal}</td>
            <td>${spec1}</td>
            <td>${spec2}</td>
            <td>${spec3}</td>
            <td>${spec4}</td>
            <td>${spec5}</td>
            <td>${spec6}</td>
            <td>${specSubTotal}</td>
            <td>${militaryNum}</td>
            <td>${officialNum}</td>
            <td>${fleetNum}</td>
            <td>${preferNum}</td>
            <td>${etcNum}</td>
            <td>${paperNum}</td>
            <td>${issuedNum}</td>
            <td>${actualNum}</td>
            <td>${noneNum}</td>
            <td>${badNum}</td>
            <td>${totalFlow}</td>
          </tr>
        `;
        } else {
          tableHtml += `
          <tr>
            <td>${shiftId}</td>
            <td>${teamId}</td>
            <td>${operatorId}</td>
            <td>${cust1}</td>
            <td>${cust2}</td>
            <td>${cust3}</td>
            <td>${cust4}</td>
            <td>${custSubTotal}</td>
            <td>${truck1}</td>
            <td>${truck2}</td>
            <td>${truck3}</td>
            <td>${truck4}</td>
            <td>${truck5}</td>
            <td>${truck6}</td>
            <td>${truckSubTotal}</td>
            <td>${spec1}</td>
            <td>${spec2}</td>
            <td>${spec3}</td>
            <td>${spec4}</td>
            <td>${spec5}</td>
            <td>${spec6}</td>
            <td>${specSubTotal}</td>
            <td>${militaryNum}</td>
            <td>${officialNum}</td>
            <td>${fleetNum}</td>
            <td>${preferNum}</td>
            <td>${etcNum}</td>
            <td>${paperNum}</td>
            <td>${issuedNum}</td>
            <td>${actualNum}</td>
            <td>${noneNum}</td>
            <td>${badNum}</td>
            <td>${totalFlow}</td>
          </tr>
        `;
        }
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
          zoom: 0.7;
        }
        .el-table {
          width: 100%;
          border-collapse: collapse;
          table-layout: auto; /* 自动调整列宽 */
          font-size: 13px;
        }
        .el-table thead tr {
          background-color: #ebeef5;
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
        }
        .el-table th {
          font-weight: bold;
          font-size: 14px;
          background-color: #f5f7fa;
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
            size: A4 landscape;
            margin: 10mm;
          }
          body {
            -webkit-print-color-adjust: exact;
            color-adjust: exact;
            padding: 0;
            margin: 0;
            width: 100%;
            font-size: 12px;
          }
          .el-table {
            width: 100% !important;
            table-layout: auto !important; /* 自动调整列宽 */
            font-size: 12px;
          }
          .el-table th, .el-table td {
            padding: 6px 4px;
            font-size: 15px;
            min-width: 50px;
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word; /* 允许单词内换行 */
          }
          .el-table th {
            font-size: 17px;
            font-weight: bold;
          }
          .container span {
            font-size: 13px;
          }
          .print-title {
            font-size: 18px;
          }
          .footer-info {
            margin-top: 15px;
            font-size: 12px;
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
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">C2收费站通行卡回收日统计表</div>
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
