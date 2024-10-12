<template>

</template>

<script>

export default {
  name: 'PageJump',
  data() {
    return {};
  },
  created() {
    console.log("进入跳转页面")
    let authToken = this.$route.query.accessToken
    let redirectPath = this.$route.query.redirectPath
    console.log(authToken)
    this.$store.dispatch('SetAuthToken', authToken).then(res => {
      console.log(redirectPath)
      this.$router.push({'path': redirectPath})
    })
  },
  methods: {},
};

</script>
<style scoped lang="scss">

</style>
