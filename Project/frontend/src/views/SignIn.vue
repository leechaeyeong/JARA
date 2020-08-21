<template>
  <v-container mt-5 fluid>
    <v-alert v-if="isError" type="error">
      이메일 또는 비밀번호를 확인해주세요.
    </v-alert>
    <div class="mt-5 font-weight-bold text-center text-sm-h1 text-h2 green--text text--darken-2" style="font-family: 'GmarketSansBold' !important;">
      J A R A
    </div>
    <div class="text-center" style="font-family: 'Handon3gyeopsal300g';">#사회적 거리두기</div>
    <v-container mt-3 fluid style="font-family: 'Handon3gyeopsal300g';">
      <v-form 
        ref="form"
        v-model="isValid"
      >
        <v-text-field
          v-model="signInData.email"
          :rules="[emailRules.required, emailRules.email]"
          name="input-10-1"
          label="이메일"
          hint="이메일을 입력해주세요."
          autocapitalize="off"
          color="green darken-2"
          autofocus
        ></v-text-field>
        <v-text-field
          v-model="signInData.password"
          :append-icon="showPWD ? 'mdi-eye' : 'mdi-eye-off'"
          :rules="[passwordRules.required, passwordRules.min]"
          :type="showPWD ? 'text' : 'password'"
          name="input-10-1"
          label="비밀번호"
          hint="비밀번호를 입력해주세요."
          counter
          @click:append="showPWD = !showPWD"
          color="green darken-2"
        ></v-text-field>
        <v-btn 
          :disabled="!isValid"
          block
          color="green darken-2"
          class="font-weight-bold white--text mt-5"
          @click="signIn(signInData)"
          large
        >로그인</v-btn>
      </v-form>
    </v-container>
    <v-container fluid style="font-family: 'Handon3gyeopsal300g';">
      <div class="my-5 d-flex">
        <div class="text grey--text">소셜 로그인</div>
        <v-divider class="align-self-center ml-2"></v-divider>
      </div>
      <div class="text-right">
        <v-btn icon :href="naverLoginURL" style="background-color: #1EC800;" class="mr-3"><v-icon x-large color="white">mdi-alpha-n</v-icon></v-btn>
        <v-btn icon :href="kakaoLoginURL" style="background-color: #FDD835;"><v-icon color="brown darken-1">mdi-chat</v-icon></v-btn>  
      </div>
      <v-divider class="my-5"></v-divider>
      <div class="mt-5 d-flex align-center">
        <div class="text grey--text">회원이 아니신가요?</div>
        <v-divider class="mx-2"></v-divider>
        <v-btn text class="pa-0 font-weight-bold" @click="goToSignUp">회원가입</v-btn>
      </div>
      <div class="d-flex align-center">
        <div class="text grey--text">비밀번호를 잊으셨나요?</div>
        <v-divider class="align-self-center mx-2"></v-divider>
        <v-btn text class="pa-0 font-weight-bold" @click="goToChangePWD">비밀번호 변경</v-btn>
      </div>
    </v-container>
  </v-container>
</template>

<script>
import { mapMutations, mapGetters, mapActions } from 'vuex'

export default {
  name: 'SignIn',
  created() {
    this.$store.commit('SET_ENTRANCE', false)
  },
  computed: {
    ...mapGetters([
      'isError',
    ])
  },
  data() {
    return {
      signInData: {
        password: '',
        email: '',
      },
      emailRules: {
        required: value => !! value || '이메일을 입력해주세요.',
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
          return pattern.test(value) || '유효하지 않은 형식입니다.'
        }
      },
      passwordRules: {
        required: value => !!value || '비밀번호를 입력해주세요.',
        min: v => v.length >= 8 || '비밀번호는 8자리 이상이어야 합니다.',
      },
      isValid: false,
      showPWD: false,
      naverLoginURL: 'https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=y_9J6LuNu9tyN5tgnmEN&redirect_uri=https://i3a308.p.ssafy.io/jara/accounts/signin/naver/access&state=20200708',
      kakaoLoginURL: 'https://kauth.kakao.com/oauth/authorize?client_id=2e50ed388c52dc3ef17eb1c332285923&redirect_uri=https://i3a308.p.ssafy.io/jara/accounts/signin/kakao/access&response_type=code'
    }
  },
  methods: {
    ...mapMutations([
      'SET_ENTRANCE',
      'SET_ERROR',
    ]),
    ...mapActions([
      'signIn'
    ]),
    goToSignUp() {
      this.$router.push('/accounts/signup')
    },
    goToChangePWD() {
      this.$router.push('/accounts/email')
    },
    validate() {
      this.$refs.form.validate()
    },
  },
}
</script>

<style scoped>
</style>