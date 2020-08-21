<template>
  <v-container fluid mt-5 style="font-family: 'Handon3gyeopsal300g' !important;">
    <v-form ref="form" v-model="isValid" class="px-3">  
      <div class="font-weight-bold">이메일로 받은 인증번호<v-icon class="ml-1">mdi-email</v-icon></div>
      <v-text-field
        v-model="changePWDData.code"
        :rules="[validationNumRules.required, validationNumRules.max]"
        placeholder="인증번호를 입력해주세요."
        outlined
        color="green darken-2"
        class="my-2"
      ></v-text-field>
      <div class="font-weight-bold">변경하실 비밀번호<v-icon class="ml-1">mdi-lock</v-icon></div>
      <v-text-field
        v-model="changePWDData.password"
        :append-icon="showPWD ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[passwordRules.required, passwordRules.min]"
        background-color="white"
        :type="showPWD ? 'text' : 'password'"
        placeholder="비밀번호를 입력해주세요."
        outlined
        counter
        @click:append="showPWD = !showPWD"
        color="green darken-2"
        class="my-2"
      ></v-text-field>

      <div class="font-weight-bold">비밀번호 확인<v-icon class="ml-1">mdi-lock-check</v-icon></div>
      <v-text-field
        v-model="changePWDData.passwordConfirm"
        :append-icon="showConfirmPWD ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[passwordConfirmRules.required, passwordConfirmRules.same]"
        background-color="white"
        :type="showConfirmPWD ? 'text' : 'password'"
        placeholder="비밀번호를 입력해주세요."
        outlined
        counter
        @click:append="showConfirmPWD = !showConfirmPWD"
        color="green darken-2"
        class="my-2"
      ></v-text-field>
    </v-form>
    <div class="text-right px-3">
      <v-btn    
        :disabled="!isValid"
        color="green darken-1 white--text"
        @click="updatePWD">변경</v-btn>
    </div>
  </v-container>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'

export default {
  name: 'ChangePWD',
  data() {
    return {
      isValid: false,
      showPWD: false,
      showConfirmPWD: false,
      changePWDData: {
        code: '',
        password: '',
        passwordConfirm: '',
        email: this.$route.params.email
      },
      passwordRules: {
        required: value => !!value || '변경하실 비밀번호를 입력해주세요.',
        min: v => v.length >= 8 || '비밀번호는 8자리 이상이어야 합니다.',
      },
      passwordConfirmRules: {
        required: value => !!value || '입력하신 비밀번호를 한번 더 입력해주세요.',
        same: v => v == this.changePWDData.password || '비밀번호가 일치하지 않습니다.'
      },
      validationNumRules: {
        required: v => !!v || '10자리 인증번호를 입력해주세요.',
        max: val => (9 < val.length && val.length <= 10) || '인증번호는 10자리로 입력해 주세요.'
      }  
    }  
  },
  computed: {
    ...mapState([
      'api_server'
    ])
  },
  methods: {
    updatePWD() {
      axios.put(`${this.$store.state.api_server}/accounts/setnewpwd`, this.changePWDData)
        .then(() => {
          alert('비밀번호가 성공적으로 변경되었습니다.')
          this.$router.push('/accounts/signin')
        })
    }
  },
}
</script>

<style>

</style>