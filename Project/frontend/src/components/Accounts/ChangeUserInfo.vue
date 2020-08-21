<template>
  <v-container mt-5 v-if="isLoad" fluid style="font-family: 'Handon3gyeopsal300g';">
    <v-alert v-if="isError" type="error">
      유효하지 않은 입력입니다.
    </v-alert>
    <v-icon x-large>mdi-account-edit-outline</v-icon>
    <div class="font-weight-bold mt-5 px-3">프로필 사진<v-icon class="ml-1">mdi-camera</v-icon></div>
    <div v-if="file==null" class="d-flex justify-center">
      <v-icon v-if="user.img_src==null" x-large>mdi-account-circle</v-icon>
      <v-avatar v-else><img :src="user.img_src"></v-avatar>
    </div>
    <div v-else class="d-flex justify-center"><v-avatar><img :src="imgURL" alt="프로필 사진"></v-avatar></div>
    <v-file-input
      @change="image"
      v-model="file"
      outlined
      placeholder="사진을 첨부해 주세요."
      color="green darken-2"
      class="mt-3 px-3"
    >
    </v-file-input>
    <v-form
      v-model="isValid"
      ref="form"
      class="px-3"
    >
      <div class="font-weight-bold mt-5">닉네임<v-icon class="ml-1">mdi-account</v-icon></div>
      <v-text-field
        v-model="user.nickname"
        :rules="[nickNameRules.required, nickNameRules.max]"
        background-color="white"
        placeholder="닉네임을 입력해주세요."
        outlined
        color="green darken-2"
        class="my-2"
      ></v-text-field>
    
      <div class="font-weight-bold">자기 소개<v-icon class="ml-1">mdi-card-account-details</v-icon></div>
      <v-textarea
        v-model="user.bio"
        color="green darken-2"
        class="my-2"
        outlined
        auto-grow
        rows="1"
        row-height="20"
        label="회원님을 어떻게 소개하시겠나요?"
      ></v-textarea>

      <div class="font-weight-bold">현재 비밀번호<v-icon class="ml-1">mdi-lock</v-icon></div>
      <v-text-field
        v-model="currentPWD"
        :append-icon="showCurrentPWD ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[currentPWDRules.required, currentPWDRules.same]"
        background-color="white"
        :type="showCurrentPWD ? 'text' : 'password'"
        placeholder="현재 비밀번호를 입력해주세요."
        outlined
        counter
        @click:append="showCurrentPWD = !showCurrentPWD"
        color="green darken-2"
        class="my-2"
      ></v-text-field>

      <div class="mb-2 font-weight-bold d-flex align-center">
        <v-btn icon @click="isChange = !isChange"><v-icon>mdi-lock-question</v-icon></v-btn>
        <div>비밀번호를 변경하시겠습니까?</div>
      </div>
      
      <div v-if="isChange" class="font-weight-bold">변경하실 비밀번호<v-icon class="ml-1">mdi-lock</v-icon></div>
      <v-text-field
        v-if="isChange"
        v-model="changePWD"
        :append-icon="showPWD ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[passwordRules.min]"
        background-color="white"
        :type="showPWD ? 'text' : 'password'"
        placeholder="비밀번호를 입력해주세요."
        outlined
        counter
        @click:append="showPWD = !showPWD"
        color="green darken-2"
        class="my-2"
      ></v-text-field>

      <div v-if="isChange" class="font-weight-bold">비밀번호 확인<v-icon class="ml-1">mdi-lock-check</v-icon></div>
      <v-text-field
        v-if="isChange"
        v-model="confirmPWD"
        :append-icon="showConfirmPWD ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[passwordConfirmRules.same]"
        background-color="white"
        :type="showConfirmPWD ? 'text' : 'password'"
        placeholder="비밀번호를 입력해주세요."
        outlined
        counter
        @click:append="showConfirmPWD = !showConfirmPWD"
        color="green darken-2"
        class="my-2"
      ></v-text-field>

      <div class="font-weight-bold d-flex align-center">주소<v-icon class="ml-2">mdi-map-search</v-icon></div>
      <v-select
        :items="districts"
        :rules="[districtsRules.required]"
        outlined
        background-color="white"
        class="my-2"
        color="green darken-2"
        item-color="green darken-2"
        v-model="user.location"
        style="font-family: 'Handon3gyeopsal300g';"
      ></v-select>
    </v-form>
    <div class="text-right px-3">
      <v-btn color="grey darken-1 white--text" class="mr-2" @click="cancle">취소</v-btn>
      <v-btn
        :disabled="!isValid"
        color="green darken-1 white--text"
        @click="updateInfo"
      >수정</v-btn>
    </div>
  </v-container>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import axios from 'axios'
import firebase from 'firebase'

export default {
  name: 'ChageUserInfo',
  created() {
    this.getUserInfo()
  },
  computed: {
    ...mapState([
      'userInfo',
      'api_server'
    ])
  },
  data() {
    return {
      user: null,
      file: null,
      imgURL: '',
      src: '',
      password: '',
      currentPWD: '',
      changePWD: '',
      confirmPWD: '',
      isLoad: false,
      isValid: false,
      isError: false,
      isSave: false,
      isChange: false,
      showCurrentPWD: false,
      showPWD: false,
      showConfirmPWD: false,
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
      nickNameRules: {
        required: value => !!value || '닉네임을 입력해주세요.',
        max: v => v.length <= 8 || '닉네임은 8자리 이하이어야 합니다.'
      },
      currentPWDRules: {
        required: value => !!value || '현재 비밀번호를 입력해주세요.',
        same: v => v == this.password || '비밀번호가 일치하지 않습니다.'
      },
      passwordRules: {
        required: value => !!value || '변경하실 비밀번호를 입력해주세요.',
        min: v => v.length >= 8 || '비밀번호는 8자리 이상이어야 합니다.',
      },
      passwordConfirmRules: {
        required: value => !!value || '입력하신 비밀번호를 한번 더 입력해주세요.',
        same: v => v == this.changePWD || '비밀번호가 일치하지 않습니다.'
      },
      districtsRules: {
        required: value => !!value || '주소를 선택해주세요.',
      },
    }
  },
  methods: {
    ...mapActions([
      'getUser',
      'getUsers'
    ]),
    getUserInfo() {
      axios.get(`${this.$store.state.api_server}/accounts/${this.$route.params.user_id}`)
        .then(res => {
          this.user = res.data
          if (this.$store.state.userInfo.password == null) {
            this.password = 'jara0708'
          } else {this.password = this.$store.state.userInfo.password}
          this.isLoad = !this.isLoad
        })
    },
    updateInfo() {
      if (this.changePWD.length > 0 && this.confirmPWD == this.changePWD) {this.user.password = this.changePWD}
      else {this.user['password'] = this.$store.state.userInfo.password}
      if (this.file!=null) {this.user.img_src = this.src}
      axios.put(`${this.$store.state.api_server}/accounts/${this.$route.params.user_id}`, this.user)
        .then(() => {
          this.$store.dispatch('getUser')
          this.$store.dispatch('getUsers')
          alert('수정이 성공적으로 완료되었습니다.')
          this.isSave = !this.isSave
          this.$router.push(`/accounts/${this.$route.params.user_id}`)
        })
        .catch(() => {
          this.$vuetify.goTo(0)
          this.isError = !this.isError
        })
    },
    cancle() {
      this.$router.push(`/accounts/${this.$route.params.user_id}`)
    },
    image() {
      this.imgURL = URL.createObjectURL(this.file)
      firebase.storage().ref(`psas/${this.user.id}`).put(this.file).then(() => {
        firebase.storage().ref(`psas/${this.user.id}`).getDownloadURL().then(url => this.src = url)
      })
    }
  },
  beforeRouteLeave(to, from, next) {
    if (!this.isSave) {
      if (confirm('입력 내용이 저장되지 않습니다.\n정말로 이 페이지에서 벗어나시겠습니까?')) {next()}
      else {next(false)}
    } else {next()}
  },
}
</script>

<style>

</style>