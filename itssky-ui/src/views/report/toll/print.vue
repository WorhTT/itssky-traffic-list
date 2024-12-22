<template>
  <div>
    <el-table :data="tableData" ref="myTable">
      <el-table-column prop="date" label="Date" width="180"></el-table-column>
      <el-table-column prop="name" label="Name" width="180"></el-table-column>
      <el-table-column prop="address" label="Address"></el-table-column>
    </el-table>
    <el-button @click="printTable">Print Table</el-button>
    <!-- Hidden iframe for printing -->
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [
        { date: '2024-12-22', name: 'John Smith', address: 'No. 1 Street' },
        { date: '2024-12-23', name: 'Jane Doe', address: 'No. 2 Street' },
        // Add more rows as needed
      ]
    };
  },
  methods: {
    printTable() {
      const elTable = this.$refs.myTable.$el;
      const printFrame = document.getElementById('printFrame');
      const printDocument = printFrame.contentDocument || printFrame.contentWindow.document;

      printDocument.open();
      printDocument.write('<html><head><title>Print Table</title>');
      printDocument.write('<style>');
      printDocument.write(`
        .el-table {
          width: 100%;
          border-collapse: collapse;
        }
        .el-table__header-wrapper {
          border: 1px solid #ebeef5 !important;
        }
        .el-table__body-wrapper {
          border: 1px solid #ebeef5 !important;
        }
        .el-table td, .el-table th {
          border: 1px solid #ebeef5 !important;
          font-size: 12px;
          padding: 0 0;
          text-align: center; /* Center text */
        }
        @media print {
          body {
            margin: 0;
            padding: 0;
          }
          .el-table {
            width: 100%;
          }
          .el-table__header-wrapper, .el-table__body-wrapper {
            overflow: visible !important;
          }
        }
      `);
      printDocument.write('</style>');
      printDocument.write('</head><body>');
      printDocument.write(elTable.outerHTML);
      printDocument.write('</body></html>');
      printDocument.close();

      // Trigger print
      printFrame.contentWindow.focus();
      printFrame.contentWindow.print();
    }
  }
};
</script>
