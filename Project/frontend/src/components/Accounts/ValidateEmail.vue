<template>
  <v-container fluid mt-5 style="font-family: 'Handon3gyeopsal300g' !important;">
    <v-alert v-if="isError" type="error" style="font-family: 'Handon3gyeopsal300g';">
      존재하지 않는 회원입니다. 이메일을 다시 한번 확인해 주세요.
    </v-alert>  
    <div class="font-weight-bold text-sm-h3 text-h6" style="font-family: 'Handon3gyeopsal600g' !important;">비밀번호를 잊으셨나요?<v-icon x-large class="ml-1">mdi-emoticon-cry-outline</v-icon></div>
    <v-form ref="form" v-model="isValid" class="px-3">
      <div class="font-weight-bold mt-5">이메일<v-icon class='ml-1'>mdi-email</v-icon></div>
      <v-text-field
        v-model="authData.email"
        :rules="[emailRules.required, emailRules.email]"
        background-color="white"
        placeholder="이메일을 입력해주세요."
        outlined
        color="green darken-2"
        autocapitalize="off"
        class="my-2"
        autofocus
      ></v-text-field>
    </v-form>
    <div class="text-right px-3">
      <v-btn color="grey darken-1 white--text" class="mr-2" @click="cancle">취소</v-btn>
      <v-btn
        :disabled="!isValid"
        color="green darken-1 white--text"
        @click="sendEmail"
        :loading="loading"
      >인증</v-btn>
    </div>
  </v-container>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'

export default {
  name: 'ValidateEmail',
  data() {
    return {
      isError: false,
      isValid: false,
      loader: null,
      loading: false,
      authData: {
        email: '',
      },
      emailRules: {
        required: value => !! value || '이메일을 입력해주세요.',
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
          return pattern.test(value) || '유효하지 않은 형식입니다.'
        }
      },
    }
  },
  methods: {
    cancle() {
      this.$router.push('/')
    },
    sendEmail() {
      this.loader = 'loading'
      axios.post(`${this.$store.state.api_server}/accounts/changepwd`, '', { params: { email: this.authData.email }})
        .then(() => {
          alert('입력하신 이메일로 비밀번호 변경 인증 메일을 발송했습니다. 메일함을 확인해주세요.')
          this.$router.push('/accounts/setnewpwd')
        })
        .catch(() => this.isError = !this.isError)
    }
  },
  watch: {
    loader() {
      const l = this.loader
      this[l] = !this[l]
      setTimeout(() => (this[l] = false), 3000)
      this.loader = null
    }
  },
  computed: {
    ...mapState([
      'api_server'
    ])
  }
}
</script>

<style>

</style>