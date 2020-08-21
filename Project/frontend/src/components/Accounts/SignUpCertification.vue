<template>
  <v-container fluid class="mt-5" style="font-family: 'Handon3gyeopsal300g';">
    <div class="text-sm-h4 text-h6 font-weight-bold mx-5" style="font-family: 'Handon3gyeopsal600g' !important;"><v-icon x-large class="mr-2">mdi-email</v-icon>이메일로 받은 인증번호를 입력해주세요.</div>
    <v-form
      v-model="isValid"
      ref="form"
    >
      <v-text-field
        v-model="code"
        :rules="[validationNumRules.required, validationNumRules.max]"
        placeholder="인증번호를 입력해주세요."
        outlined
        color="green darken-2"
        class="mt-10 px-5"
      ></v-text-field>
    </v-form>
    <div class="text-right mr-5">
      <v-btn    
        :disabled="!isValid"
        color="green darken-1 white--text"
        @click="validate"
      >인증</v-btn>
    </div>
  </v-container>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'Certification',
  data() {
    return {
      isValid: false,
      code: '',
      validationNumRules: {
        required: v => !!v || '10자리 인증번호를 입력해주세요.',
        max: val => (9 < val.length && val.length <= 10) || '인증번호는 10자리로 입력해 주세요.'
      }
    }
  },
  methods: {
    validate() {
      axios.post(`${this.$store.state.api_server}/accounts/certification/${this.code}`, '')
        .then(() => {
          alert('인증이 완료되었습니다.')
          this.$router.push('/accounts/signin')
        })
        .catch(() => {
          alert('인증번호가 일치하지 않습니다.')
        })
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