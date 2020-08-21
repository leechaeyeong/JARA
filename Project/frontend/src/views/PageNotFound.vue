<template>
  <v-container fluid class='mt-5'>
    <v-img src="@/assets/cryingjara.png"></v-img>
    <div class="mt-5 font-weight-bold green--text text-h3 text-center" style="font-family: 'Handon3gyeopsal600g' !important;">Ooops!</div>
    <div class="font-weight-bold text-h6 text-center" style="font-family: 'Handon3gyeopsal300g' !important;">Page Not Found</div>
    <div class="mt-3 font-weight-bold text-h6 text-center" style="font-family: 'Handon3gyeopsal300g' !important;">자라가 페이지를 찾을 수 없습니다.</div>
    <div class="mt-10 text-center" style="font-family: 'Handon3gyeopsal300g' !important;">입력하신 URL을 다시 확인해 주시기 바랍니다.</div>
    <div class="mt-10 text-center">
      <v-btn @click="goBack" color="green darken-1" class="white--text" style="font-family: 'Handon3gyeopsal300g' !important;"><v-icon>mdi-arrow-left</v-icon>홈으로 돌아가기</v-btn>
    </div>
  </v-container>
</template>

<script>
export default {
  name: 'PageNotFound',
  methods: {
    goBack() {
      this.$router.push('/main')
    }
  }
}
</script>

<style>

</style>