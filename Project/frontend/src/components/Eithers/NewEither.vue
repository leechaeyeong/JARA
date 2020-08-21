<template>
  <v-container fluid style="font-family: 'Handon3gyeopsal300g';">
    <v-alert v-if="isError" type="error">
      유효하지 않은 입력입니다.
    </v-alert>
    <div class="mt-5 d-flex align-center font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
      <v-icon x-large v-if="psas[userInfo.id]==null" class="mr-2">mdi-account-circle</v-icon>
      <v-avatar v-else class="mr-2"><img :src="psas[userInfo.id]"></v-avatar>
      {{ userInfo.nickname }}
    </div>
    <v-text-field
      class="mt-5"
      label="어떤 고민을 하고 계신가요?"
      v-model="eitherData.question"
      append-outer-icon="mdi-comment-question"
      color="green darken-2"
    ></v-text-field>
    <v-text-field
      class="mt-5"
      label="선택지 1번을 입력해 주세요."
      v-model="eitherData.choiceA"
      append-outer-icon="mdi-alpha-a-box"
      color="green darken-2"
    ></v-text-field>
    <v-text-field
      class="mt-5"
      label="선택지 2번을 입력해 주세요."
      v-model="eitherData.choiceB"
      append-outer-icon="mdi-alpha-b-box"
      color="green darken-2"
    ></v-text-field>
    <v-btn fixed bottom right fab color="green lighten-1" @click="createEither" small dark><v-icon>mdi-pencil</v-icon></v-btn>
  </v-container>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'NewEither',
  computed: {
    ...mapState([
      'userInfo',
      'api_server',
      'psas'
    ])
  },
  data() {
    return {
      eitherData: {
        writer: this.$store.state.userInfo.id,
        question: '',
        choiceA: '',
        choiceB: '',
      },
      isError: false
    }
  },
  methods: {
    createEither() {
      if (this.eitherData.question.trim().length == 0 || this.eitherData.choiceA.trim().length == 0 || this.eitherData.choiceB.trim().length == 0) {
        this.isError = true
        setTimeout(() => {
          this.isError = false
        }, 2000)
      }
      else if (this.eitherData.question.trim().length >= 1 && this.eitherData.choiceA.trim().length >= 1 && this.eitherData.choiceB.trim().length >= 1) {
        axios.post(`${this.$store.state.api_server}/eithers`, this.eitherData)
          .then(() => this.$router.push('/eithers'))
      }
    }
  }
}
</script>

<style>

</style>