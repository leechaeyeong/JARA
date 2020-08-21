<template>
  <v-container fluid entrance>
    <div id="entDiv" class="text-center">
      <v-img
        alt="JARA Logo"
        src="@/assets/jara.png"
        class="mx-auto"
        transition="scale-transition"
        width="50%"
      />
      <v-spacer></v-spacer>
      <div 
        id="jara"
        class="mt-6 text-h2 text-sm-h1 font-weight-bold white--text"
        style="font-family: 'GmarketSansBold' !important;"
      >JARA</div>
      <div 
        class="text-subtitle-1 white--text"
        style="font-family: 'Handon3gyeopsal300g' !important;"
      >자취인의 라이프 플랫폼</div>
      <v-btn class="ma-6 font-weight-bold" outlined color="white" @click="goToSignIn" style="font-family: 'Handon3gyeopsal300g';">로그인</v-btn>
    </div>
  </v-container>
</template>

<script>

export default {
  name: 'Entrance',
  methods: {
    goToSignIn() {
      this.$router.push('/accounts/signin')
    }
  }
}
</script>

<style scoped>
  .entrance {
    height: 100%;
    background-color: #388E3C;
  }
  #entDiv {
    padding-top: 20vh;
  }
</style>

