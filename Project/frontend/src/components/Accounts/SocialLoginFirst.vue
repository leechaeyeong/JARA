<template>
  <v-container fluid style="font-family: 'Handon3gyeopsal300g';">
    <div class="mt-5 font-weight-bold text-h5" style="font-family: 'Handon3gyeopsal600g' !important;"><v-icon x-large>mdi-turtle</v-icon> 자라에 첫 방문이시군요!</div>
    <v-form
      v-model="isValid"
      ref="form"
      class="mt-5 px-3"
    >
      <div class="d-flex align-center my-5">
        <v-btn icon large @click="isAgree = !isAgree">
          <v-icon v-if="!isAgree">mdi-check-circle-outline</v-icon>
          <v-icon v-else color="green darken-1">mdi-check-circle</v-icon>
        </v-btn>
        <div class="font-weight-bold">위치정보 이용약관 동의</div>
        <div class="green--text text--darken-1">(필수)</div>
      </div>
      <div>
        <v-textarea
          :value="locationTerms"
          solo
          readonly
          style="font-family: 'Handon3gyeopsal300g';"
        ></v-textarea>
      </div>
      <div class="font-weight-bold d-flex align-center">주소<v-icon class="ml-2">mdi-map-search</v-icon></div>
      <v-select
        :items="districts"
        :rules="[districtsRules.required]"
        outlined
        background-color="white"
        class="my-2"
        color="green darken-2"
        item-color="green darken-2"
        v-model="location"
      ></v-select>
    </v-form>
    <div class="text-right px-3">
      <v-btn
        :disabled="!isValid||!isAgree"
        color="green darken-1 white--text"
        @click="updateLocation"
      >확인</v-btn>
    </div>
  </v-container>
</template>

<script>
import { mapState, mapMutations, mapActions } from 'vuex'
import axios from 'axios'

export default {
  name: 'SocialLoginFirst',
  computed: {
    ...mapState([
      'userInfo',
      'api_server'
    ]),
    ...mapMutations([
      'SET_TOKEN',
    ])
  },
  data() {
    return {
      location: '',
      districts: [
        '강남구',
        '강동구',
        '강북구',
        '강서구',
        '관악구',
        '광진구',
        '구로구',
        '금천구',
        '노원구',
        '도봉구',
        '동대문구',
        '동작구',
        '마포구',
        '서대문구',
        '서초구',
        '성동구',
        '송파구',
        '양천구',
        '영등포구',
        '용산구',
        '은평구',
        '종로구',
        '중구',
        '중랑구'
      ],
      locationTerms: '위치정보 이용약관에 동의하시면, 위치를 활용한 날씨 정보를 제공하는 JARA의 위치기반 서비스를 이용하실 수 있습니다.\n\nJARA가 제공하는 서비스의 종류, 세부 내용, 이용 요금은 아래와 같습니다.\n1.종류 : SNS\n2.서비스명 : JARA\n3.설명 : 자취 인구(1인 가구)를 위한 SNS\n4.위치정보 이용 목적: 날씨 정보 제공\n5.이용 요금 : 무료',
      districtsRules: {
        required: value => !!value || '주소를 선택해주세요.',
      },
      isAgree: false,
      isSave: false,
      isValid: false,
    }
  },
  methods: {
    ...mapActions([
      'getUser',
      'getUsers',
      'checkDB'
    ]),
    check() {
      if (this.$store.state.userInfo.location != null) { 
        this.isSave = true  
        this.$router.push('/main')
      }
      this.$store.dispatch('checkDB', this.$store.state.userInfo.id)
    },
    updateLocation() {
      this.isSave = !this.isSave
      const user = {
        id: this.$store.state.userInfo.id,
        location: this.location
      }
      axios.put(`${this.$store.state.api_server}/accounts/signin/naver/access`, user)
        .then(res => {
          this.$store.commit('SET_TOKEN', res.headers['jwt-auth-token'])
          this.$store.dispatch('getUser')
          alert('주소가 성공적으로 입력되었습니다.\n초기 비밀번호는 jara0708 입니다.\n회원정보수정페이지에서 비밀번호를 변경해주세요.')
        }).then(() => this.$router.push('/accounts/user'))
    }
  },
  created() {
    this.check()
  },
  beforeRouteLeave(to, from, next) {
    if (!this.isSave) {
      if (confirm('주소를 입력하지 않으면 자라의 부분적인 이용만 가능합니다.\n정말로 이 페이지에서 벗어나시겠습니까?')) {next()}
      else {next(false)}
    } else {next()}
  },
}
</script>

<style>

</style>