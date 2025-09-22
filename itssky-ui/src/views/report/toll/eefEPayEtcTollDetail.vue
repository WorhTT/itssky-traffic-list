<template>
  <div class="app-container">
    <div
      style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">EEF_ETC电子支付通行费统计表</h3>
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

    <el-table v-loading="loading" :data="dataList" border style="width: 100%" fit ref="myTable" >
      <el-table-column label="统计方式" align="center" prop="statType" min-width="100"/>
      <el-table-column label="客车" align="center">
        <el-table-column label="客一" align="center">
          <el-table-column label="C卡" align="center" prop="cust1C" min-width="120"/>
          <el-table-column label="D卡" align="center" prop="cust1D" min-width="120"/>
          <el-table-column label="合计" align="center" prop="cust1Sum" min-width="120"/>
        </el-table-column>
        <el-table-column label="客二" align="center">
          <el-table-column label="C卡" align="center" prop="cust2C" min-width="120"/>
          <el-table-column label="D卡" align="center" prop="cust2D" min-width="120"/>
          <el-table-column label="合计" align="center" prop="cust2Sum" min-width="120"/>
        </el-table-column>
        <el-table-column label="客三" align="center">
          <el-table-column label="C卡" align="center" prop="cust3C" min-width="120"/>
          <el-table-column label="D卡" align="center" prop="cust3D" min-width="120"/>
          <el-table-column label="合计" align="center" prop="cust3Sum" min-width="120"/>
        </el-table-column>
        <el-table-column label="客四" align="center" prop="k4Count">
          <el-table-column label="C卡" align="center" prop="cust4C" min-width="120"/>
          <el-table-column label="D卡" align="center" prop="cust4D" min-width="120"/>
          <el-table-column label="合计" align="center" prop="cust4Sum" min-width="120"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="C卡" align="center" prop="custCSubTotal" min-width="120"/>
          <el-table-column label="D卡" align="center" prop="custDSubTotal" min-width="120"/>
          <el-table-column label="合计" align="center" prop="custSubSum" min-width="120"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="货车" align="center">
        <el-table-column label="货一" align="center">
          <el-table-column label="C卡" align="center" prop="trust1C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust1D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust1Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货二" align="center">
          <el-table-column label="C卡" align="center" prop="trust2C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust2D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust2Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货三" align="center">
          <el-table-column label="C卡" align="center" prop="trust3C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust3D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust3Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货四" align="center">
          <el-table-column label="C卡" align="center" prop="trust4C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust4D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust4Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货五" align="center">
          <el-table-column label="C卡" align="center" prop="trust5C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust5D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust5Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货六" align="center">
          <el-table-column label="C卡" align="center" prop="trust6C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust6D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust6Sum" width="100"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="C卡" align="center" prop="trustCSubTotal" width="100"/>
          <el-table-column label="D卡" align="center" prop="trustDSubTotal" width="100"/>
          <el-table-column label="合计" align="center" prop="trustSubSum" width="100"/>
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
        <el-table-column label="C卡" align="center" prop="ctotal" width="120"/>
        <el-table-column label="D卡" align="center" prop="dtotal" width="120"/>
        <el-table-column label="合计" align="center" prop="totalSum" width="120"/>
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

import {eefepay, exportEefepay} from "@/api/report/toll"
import {getLoginUser} from "@/api/login";

export default {
  name: "EEFEPayEtcTollDetail",
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
    getLoginUser().then(res => {
      if (res.data) {
        this.operatorName = res.data.username;
      }
    })
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
      //ETC标志
      this.queryParams.flag = '2';
      eefepay(this.queryParams).then(response => {
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
      //ETC标志
      this.queryParams.flag = '2';
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出EEF_ETC电子支付通行费统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEefepay(queryParams);
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
              <th rowspan="3">统计方式</th>
              <th colspan="15">客车</th>
              <th colspan="21">货车</th>
              <th colspan="21">专车</th>
              <th colspan="3">总计</th>
            </tr>
            <tr>
              <th colspan="3">客一</th>
              <th colspan="3">客二</th>
              <th colspan="3">客三</th>
              <th colspan="3">客四</th>
              <th colspan="3">小计</th>
              <th colspan="3">货一</th>
              <th colspan="3">货二</th>
              <th colspan="3">货三</th>
              <th colspan="3">货四</th>
              <th colspan="3">货五</th>
              <th colspan="3">货六</th>
              <th colspan="3">小计</th>
              <th colspan="3">专一</th>
              <th colspan="3">专二</th>
              <th colspan="3">专三</th>
              <th colspan="3">专四</th>
              <th colspan="3">专五</th>
              <th colspan="3">专六</th>
              <th colspan="3">小计</th>
              <th rowspan="2">C卡</th>
              <th rowspan="2">D卡</th>
              <th rowspan="2">合计</th>
            </tr>
            <tr>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
              <th>C卡</th>
              <th>D卡</th>
              <th>合计</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为0的数值，确保0也能正确显示
        const statType = row.statType !== undefined && row.statType !== null ? row.statType : '';
        const cust1C = row.cust1C !== undefined && row.cust1C !== null ? row.cust1C : '';
        const cust1D = row.cust1D !== undefined && row.cust1D !== null ? row.cust1D : '';
        const cust1Sum = row.cust1Sum !== undefined && row.cust1Sum !== null ? row.cust1Sum : '';
        const cust2C = row.cust2C !== undefined && row.cust2C !== null ? row.cust2C : '';
        const cust2D = row.cust2D !== undefined && row.cust2D !== null ? row.cust2D : '';
        const cust2Sum = row.cust2Sum !== undefined && row.cust2Sum !== null ? row.cust2Sum : '';
        const cust3C = row.cust3C !== undefined && row.cust3C !== null ? row.cust3C : '';
        const cust3D = row.cust3D !== undefined && row.cust3D !== null ? row.cust3D : '';
        const cust3Sum = row.cust3Sum !== undefined && row.cust3Sum !== null ? row.cust3Sum : '';
        const cust4C = row.cust4C !== undefined && row.cust4C !== null ? row.cust4C : '';
        const cust4D = row.cust4D !== undefined && row.cust4D !== null ? row.cust4D : '';
        const cust4Sum = row.cust4Sum !== undefined && row.cust4Sum !== null ? row.cust4Sum : '';
        const custCSubTotal = row.custCSubTotal !== undefined && row.custCSubTotal !== null ? row.custCSubTotal : '';
        const custDSubTotal = row.custDSubTotal !== undefined && row.custDSubTotal !== null ? row.custDSubTotal : '';
        const custSubSum = row.custSubSum !== undefined && row.custSubSum !== null ? row.custSubSum : '';
        const trust1C = row.trust1C !== undefined && row.trust1C !== null ? row.trust1C : '';
        const trust1D = row.trust1D !== undefined && row.trust1D !== null ? row.trust1D : '';
        const trust1Sum = row.trust1Sum !== undefined && row.trust1Sum !== null ? row.trust1Sum : '';
        const trust2C = row.trust2C !== undefined && row.trust2C !== null ? row.trust2C : '';
        const trust2D = row.trust2D !== undefined && row.trust2D !== null ? row.trust2D : '';
        const trust2Sum = row.trust2Sum !== undefined && row.trust2Sum !== null ? row.trust2Sum : '';
        const trust3C = row.trust3C !== undefined && row.trust3C !== null ? row.trust3C : '';
        const trust3D = row.trust3D !== undefined && row.trust3D !== null ? row.trust3D : '';
        const trust3Sum = row.trust3Sum !== undefined && row.trust3Sum !== null ? row.trust3Sum : '';
        const trust4C = row.trust4C !== undefined && row.trust4C !== null ? row.trust4C : '';
        const trust4D = row.trust4D !== undefined && row.trust4D !== null ? row.trust4D : '';
        const trust4Sum = row.trust4Sum !== undefined && row.trust4Sum !== null ? row.trust4Sum : '';
        const trust5C = row.trust5C !== undefined && row.trust5C !== null ? row.trust5C : '';
        const trust5D = row.trust5D !== undefined && row.trust5D !== null ? row.trust5D : '';
        const trust5Sum = row.trust5Sum !== undefined && row.trust5Sum !== null ? row.trust5Sum : '';
        const trust6C = row.trust6C !== undefined && row.trust6C !== null ? row.trust6C : '';
        const trust6D = row.trust6D !== undefined && row.trust6D !== null ? row.trust6D : '';
        const trust6Sum = row.trust6Sum !== undefined && row.trust6Sum !== null ? row.trust6Sum : '';
        const trustCSubTotal = row.trustCSubTotal !== undefined && row.trustCSubTotal !== null ? row.trustCSubTotal : '';
        const trustDSubTotal = row.trustDSubTotal !== undefined && row.trustDSubTotal !== null ? row.trustDSubTotal : '';
        const trustSubSum = row.trustSubSum !== undefined && row.trustSubSum !== null ? row.trustSubSum : '';
        const spec1C = row.spec1C !== undefined && row.spec1C !== null ? row.spec1C : '';
        const spec1D = row.spec1D !== undefined && row.spec1D !== null ? row.spec1D : '';
        const spec1Sum = row.spec1Sum !== undefined && row.spec1Sum !== null ? row.spec1Sum : '';
        const spec2C = row.spec2C !== undefined && row.spec2C !== null ? row.spec2C : '';
        const spec2D = row.spec2D !== undefined && row.spec2D !== null ? row.spec2D : '';
        const spec2Sum = row.spec2Sum !== undefined && row.spec2Sum !== null ? row.spec2Sum : '';
        const spec3C = row.spec3C !== undefined && row.spec3C !== null ? row.spec3C : '';
        const spec3D = row.spec3D !== undefined && row.spec3D !== null ? row.spec3D : '';
        const spec3Sum = row.spec3Sum !== undefined && row.spec3Sum !== null ? row.spec3Sum : '';
        const spec4C = row.spec4C !== undefined && row.spec4C !== null ? row.spec4C : '';
        const spec4D = row.spec4D !== undefined && row.spec4D !== null ? row.spec4D : '';
        const spec4Sum = row.spec4Sum !== undefined && row.spec4Sum !== null ? row.spec4Sum : '';
        const spec5C = row.spec5C !== undefined && row.spec5C !== null ? row.spec5C : '';
        const spec5D = row.spec5D !== undefined && row.spec5D !== null ? row.spec5D : '';
        const spec5Sum = row.spec5Sum !== undefined && row.spec5Sum !== null ? row.spec5Sum : '';
        const spec6C = row.spec6C !== undefined && row.spec6C !== null ? row.spec6C : '';
        const spec6D = row.spec6D !== undefined && row.spec6D !== null ? row.spec6D : '';
        const spec6Sum = row.spec6Sum !== undefined && row.spec6Sum !== null ? row.spec6Sum : '';
        const specCSubTotal = row.specCSubTotal !== undefined && row.specCSubTotal !== null ? row.specCSubTotal : '';
        const specDSubTotal = row.specDSubTotal !== undefined && row.specDSubTotal !== null ? row.specDSubTotal : '';
        const specSubSum = row.specSubSum !== undefined && row.specSubSum !== null ? row.specSubSum : '';
        const ctotal = row.ctotal !== undefined && row.ctotal !== null ? row.ctotal : '';
        const dtotal = row.dtotal !== undefined && row.dtotal !== null ? row.dtotal : '';
        const totalSum = row.totalSum !== undefined && row.totalSum !== null ? row.totalSum : '';

        tableHtml += `
          <tr>
            <td>${statType}</td>
            <td>${cust1C}</td>
            <td>${cust1D}</td>
            <td>${cust1Sum}</td>
            <td>${cust2C}</td>
            <td>${cust2D}</td>
            <td>${cust2Sum}</td>
            <td>${cust3C}</td>
            <td>${cust3D}</td>
            <td>${cust3Sum}</td>
            <td>${cust4C}</td>
            <td>${cust4D}</td>
            <td>${cust4Sum}</td>
            <td>${custCSubTotal}</td>
            <td>${custDSubTotal}</td>
            <td>${custSubSum}</td>
            <td>${trust1C}</td>
            <td>${trust1D}</td>
            <td>${trust1Sum}</td>
            <td>${trust2C}</td>
            <td>${trust2D}</td>
            <td>${trust2Sum}</td>
            <td>${trust3C}</td>
            <td>${trust3D}</td>
            <td>${trust3Sum}</td>
            <td>${trust4C}</td>
            <td>${trust4D}</td>
            <td>${trust4Sum}</td>
            <td>${trust5C}</td>
            <td>${trust5D}</td>
            <td>${trust5Sum}</td>
            <td>${trust6C}</td>
            <td>${trust6D}</td>
            <td>${trust6Sum}</td>
            <td>${trustCSubTotal}</td>
            <td>${trustDSubTotal}</td>
            <td>${trustSubSum}</td>
            <td>${spec1C}</td>
            <td>${spec1D}</td>
            <td>${spec1Sum}</td>
            <td>${spec2C}</td>
            <td>${spec2D}</td>
            <td>${spec2Sum}</td>
            <td>${spec3C}</td>
            <td>${spec3D}</td>
            <td>${spec3Sum}</td>
            <td>${spec4C}</td>
            <td>${spec4D}</td>
            <td>${spec4Sum}</td>
            <td>${spec5C}</td>
            <td>${spec5D}</td>
            <td>${spec5Sum}</td>
            <td>${spec6C}</td>
            <td>${spec6D}</td>
            <td>${spec6Sum}</td>
            <td>${specCSubTotal}</td>
            <td>${specDSubTotal}</td>
            <td>${specSubSum}</td>
            <td>${ctotal}</td>
            <td>${dtotal}</td>
            <td>${totalSum}</td>
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
          zoom: 0.44; /* 调整缩放以适应横向打印 */
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
            font-size: 17px;
            min-width: 40px;
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid;
          }
          .el-table th {
            font-size: 18px; /* 表头字体稍大 */
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
            <div class="print-title">EEF_ETC电子支付通行费统计表</div>
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
</style>
