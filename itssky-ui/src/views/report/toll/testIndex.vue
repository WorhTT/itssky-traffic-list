<template>
    <div>
      <el-table :data="salesDataWithSubtotal">
        <el-table-column prop="productName" label="产品名称"></el-table-column>
        <el-table-column prop="salesVolume" label="销售量"></el-table-column>
        <el-table-column prop="salesAmount" label="销售额"></el-table-column>
      </el-table>
      <el-table :data="purchaseDataWithSubtotal">
        <el-table-column prop="productName" label="产品名称"></el-table-column>
        <el-table-column prop="purchaseVolume" label="采购量"></el-table-column>
        <el-table-column prop="purchaseAmount" label="采购额"></el-table-column>
      </el-table>
      <el-table :data="overallDataWithTotal">
        <el-table-column prop="productName" label="产品名称"></el-table-column>
        <el-table-column prop="salesVolume" label="销售量"></el-table-column>
        <el-table-column prop="salesAmount" label="销售额"></el-table-column>
        <el-table-column prop="purchaseVolume" label="采购量"></el-table-column>
        <el-table-column prop="purchaseAmount" label="采购额"></el-table-column>
      </el-table>
    </div>
</template>

<script>
export default {
  name: "testIndex",
  data() {
    return {
      salesData: [
        { productName: 'Product A', salesVolume: 10, salesAmount: 100 },
        { productName: 'Product B', salesVolume: 15, salesAmount: 150 },
      ],
      purchaseData: [
        { productName: 'Product C', purchaseVolume: 8, purchaseAmount: 80 },
        { productName: 'Product D', purchaseVolume: 12, purchaseAmount: 120 },
      ],
    };
  },
  computed: {
    salesDataWithSubtotal() {
      const subtotalRow = this.calculateSubtotal(this.salesData);
      return [...this.salesData, subtotalRow];
    },
    purchaseDataWithSubtotal() {
      const subtotalRow = this.calculateSubtotal(this.purchaseData);
      return [...this.purchaseData, subtotalRow];
    },
    overallDataWithTotal() {
      const totalRow = this.calculateTotal(this.salesData, this.purchaseData);
      return [...this.salesDataWithSubtotal,...this.purchaseDataWithSubtotal, totalRow];
    },
  },
  methods: {
    calculateSubtotal(data) {
      const totalVolume = data.reduce((acc, item) => acc + item.salesVolume || item.purchaseVolume, 0)
      const totalAmount = data.reduce((acc, item) => acc + item.salesAmount || item.purchaseAmount, 0)
      return {
        productName: '小计',
        salesVolume: totalVolume,
        salesAmount: totalAmount,
      };
    },
    calculateTotal(salesData, purchaseData) {
      const totalSalesVolume = salesData.reduce((acc, item) => acc + item.salesVolume, 0);
      const totalSalesAmount = salesData.reduce((acc, item) => acc + item.salesAmount, 0);
      const totalPurchaseVolume = purchaseData.reduce((acc, item) => acc + item.purchaseVolume, 0);
      const totalPurchaseAmount = purchaseData.reduce((acc, item) => acc + item.purchaseAmount, 0);
      return {
        productName: '合计',
        salesVolume: totalSalesVolume + totalPurchaseVolume,
        salesAmount: totalSalesAmount + totalPurchaseAmount,
      };
    }
  }
}
</script>

<style scoped>

</style>
