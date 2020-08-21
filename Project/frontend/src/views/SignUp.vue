<template>
  <v-container mt-5 fluid style="font-family: 'Handon3gyeopsal300g' !important;">
    <v-alert v-if="isError" type="error" style="font-family: 'Handon3gyeopsal600g' !important;">
      이미 존재하는 회원이거나 유효하지 않은 입력입니다.
    </v-alert>
    <div class="mt-5 font-weight-bold text-center text-sm-h1 text-h2 green--text text--darken-2" style="font-family: 'GmarketSansBold' !important;">
      JARA
    </div>
    <v-stepper v-model="present" class="mt-5">
      <v-stepper-header>
        <v-stepper-step :complete="present > 1" step="1" color="green darken-1">약관 동의</v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step :complete="present > 2" step="2" color="green darken-1">정보 입력</v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step step="3" color="green darken-1">이메일 인증</v-stepper-step>
      </v-stepper-header>

      <v-stepper-items>
        <v-stepper-content step="1">
          <v-card
            class="mb-12"
            color="grey lighten-4"
            height="870px"
          >
            <v-container fluid>
              <div class="d-flex align-center my-5">
                <v-btn icon large @click="isAgree = !isAgree">
                  <v-icon v-if="!isAgree">mdi-check-circle-outline</v-icon>
                  <v-icon v-else color="green darken-1">mdi-check-circle</v-icon>
                </v-btn>
                <div class="font-weight-bold text-subheading-1">이용약관, 개인정보 수집 및 이용, 위치정보 이용약관에 모두 동의합니다.</div>
              </div>
              <div class="d-flex align-center my-5 px-3">
                <div class="font-weight-bold">JARA 이용 약관 동의</div>
                <div class="green--text text--darken-1">(필수)</div>
              </div>
              <div class="px-3">
                <v-textarea
                  :value="values.jara"
                  solo
                  readonly
                ></v-textarea>
              </div>
              <div class="d-flex align-center my-5 px-3">
                <div class="font-weight-bold">개인정보 수집 및 이용에 대한 안내</div>
                <div class="green--text text--darken-1">(필수)</div>
              </div>
              <div class="px-3">
                <v-textarea
                  :value="values.privacy"
                  solo
                  readonly
                ></v-textarea>
              </div>
              <div class="d-flex align-center my-5 px-3">
                <div class="font-weight-bold">위치정보 이용약관 동의</div>
                <div class="green--text text--darken-1">(필수)</div>
              </div>
              <div class="px-3">
                <v-textarea
                  :value="values.location"
                  solo
                  readonly
                ></v-textarea>
              </div>
            </v-container>
          </v-card>

          <div class="text-right">
            <v-btn color="grey darken-1 white--text" class="mr-2">취소</v-btn>
            <v-btn
              :disabled="!isAgree"
              color="green darken-1 white--text"
              @click="next"
            >
              확인
            </v-btn>
          </div>

        </v-stepper-content>

        <v-stepper-content step="2">
          <v-card
            class="mb-12"
            color="grey lighten-4"
            height="930px"
          >
            <v-container fluid>
              <v-form 
                ref="form"
                v-model="isValid"
                class="px-3"
              >
                <div class="font-weight-bold mt-5">이메일<v-icon class='ml-1'>mdi-email</v-icon></div>
                <v-text-field
                  v-model="signUpData.email"
                  :rules="[emailRules.required, emailRules.email]"
                  background-color="white"
                  placeholder="이메일을 입력해주세요."
                  outlined
                  color="green darken-2"
                  autocapitalize="off"
                  class="my-2"
                  autofocus
                ></v-text-field>
                <div class="font-weight-bold">비밀번호<v-icon class='ml-1'>mdi-lock</v-icon></div>
                <v-text-field
                  v-model="signUpData.password"
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
                <div class="font-weight-bold">비밀번호 재확인<v-icon class='ml-1'>mdi-lock-check</v-icon></div>
                <v-text-field
                  v-model="signUpData.passwordConfirm"
                  :append-icon="showConfirmPWD ? 'mdi-eye' : 'mdi-eye-off'"
                  :rules="[passwordConfirmRules.required, passwordConfirmRules.same]"
                  background-color="white"
                  :type="showConfirmPWD ? 'text' : 'password'"
                  placeholder="비밀번호를 한번 더 입력해주세요."
                  outlined
                  counter
                  @click:append="showConfirmPWD = !showConfirmPWD"
                  color="green darken-2"
                  class="my-2"
                ></v-text-field>
                <div class="font-weight-bold">닉네임</div>
                <v-text-field
                  v-model="signUpData.nickname"
                  :rules="[nickNameRules.required, nickNameRules.max]"
                  background-color="white"
                  placeholder="닉네임을 입력해주세요."
                  outlined
                  color="green darken-2"
                  class="my-2"
                ></v-text-field>
                <div class="font-weight-bold">성별</div>
                <v-select
                  :items="items"
                  :rules="[sexRules.required]"
                  item-text="text"
                  item-value="value"
                  outlined
                  background-color="white"
                  class="my-2"
                  color="green darken-2"
                  item-color="green darken-2"
                  v-model="signUpData.sex"
                ></v-select>
                <div class="d-flex align-center font-weight-bold">생년월일
                  <div>
                     <v-menu
                        v-model="showCalendar"
                        :close-on-content-click="false"
                        :nudge-width="200"
                      >
                        <template v-slot:activator="{ on, attrs }">
                          <v-btn
                            icon
                            v-bind="attrs"
                            v-on="on"
                          >
                            <v-icon>mdi-calendar-blank</v-icon>
                          </v-btn>
                        </template>
                        <v-card>
                          <v-list class="pa-0">
                            <v-list-item class="pa-0">
                              <v-list-item-content class="pa-0">
                                <v-date-picker
                                  v-model="signUpData.birthday"
                                  year-icon="mdi-calendar-blank"
                                  prev-icon="mdi-skip-previous"
                                  next-icon="mdi-skip-next"
                                  color="green darken-2"
                                ></v-date-picker>
                              </v-list-item-content>
                            </v-list-item>
                          </v-list>

                          <v-divider></v-divider>

                          <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-2" class="font-weight-bold" text @click="showCalendar=false">저장</v-btn>
                          </v-card-actions>
                        </v-card>
                     </v-menu>
                  </div>
                </div>
                <v-text-field
                  v-model="signUpData.birthday"
                  background-color="white"
                  :rules="[bDayRules.over]"
                  outlined
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
                  v-model="signUpData.location"
                ></v-select>
              </v-form>
            </v-container>
          </v-card>

          <div class="text-right">
            <v-btn color="grey darken-1 white--text" class="mr-2" @click="--present">취소</v-btn>
            <v-btn
              :disabled="!isValid"
              color="green darken-1 white--text"
              @click="signUp"
              :loading="loading"
            >
              회원가입
            </v-btn>
          </div>
        </v-stepper-content>

        <v-stepper-content step="3">
          <v-card
            class="mb-3"
            color="grey lighten-4"
            height="490px"
          >
            <v-container fluid>
              <div class="text-center font-weight-bold text-sm-h3 text-h6 mt-5" style="font-family: 'Handon3gyeopsal600g' !important;">
                <v-icon color="red">mdi-party-popper</v-icon>
                <v-icon color="yellow">mdi-party-popper</v-icon>
                가입을 환영합니다
                <v-icon color="green">mdi-party-popper</v-icon>
                <v-icon color="blue">mdi-party-popper</v-icon>
              </div>
              <div class="text-center mt-3 text-sm-h5 text-body-2" style="font-family: 'Handon3gyeopsal300g' !important;">입력하신 이메일로 회원가입 인증 메일이 발송되었습니다
                <v-icon>mdi-email-send-outline</v-icon>
              </div>
              <div class="text-center text-sm-h5 text-body-2" style="font-family: 'Handon3gyeopsal300g' !important;">인증을 통해 회원가입을 완료해주세요
                <v-icon>mdi-emoticon-excited</v-icon>
              </div>
              <div class="my-5 d-flex">
                <div class="text grey--text" style="font-family: 'Handon3gyeopsal300g' !important;">바로가기</div>
                <v-divider class="align-self-center ml-2"></v-divider>
              </div>
              <v-btn 
                block
                x-large
                class="my-2 font-weight-bold text-h6"
                color="white"
                href="https://mail.daum.net/login?url=https%3A%2F%2Fmail.daum.net%2F"
                style="font-family: 'Handon3gyeopsal600g' !important;"
              >
                <div class="blue--text text--darken-1">d</div>
                <div class="light-green--text text--lighten-1">a</div>
                <div class="amber--text text--lighten-1">u</div>
                <div class="red--text text--lighten-1">m</div>
              </v-btn>
              <v-btn block 
                x-large 
                class="my-2 font-weight-bold text-h6" 
                color="white"
                href="https://accounts.google.com/ServiceLogin/signinchooser?service=mail&passive=true&rm=false&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F%3Ftab%3Dwm%26ogbl&scc=1&ltmpl=default&ltmplcache=2&emr=1&osid=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin"
                style="font-family: 'Handon3gyeopsal600g' !important;"
              >
                <div class="blue--text text--darken-1">g</div>
                <div class="red--text">o</div>
                <div class="amber--text text--lighten-1">o</div>
                <div class="blue--text text--darken-1">g</div>
                <div class="green--text">l</div>
                <div class="red--text text--lighten-1">e</div>
              </v-btn>            
              <v-btn 
                block
                x-large 
                color="red"
                class="my-2 font-weight-bold white--text text-h6"
                href="http://home.mail.nate.com/login/login.html?s=mail&redirect=http%3A%2F%2Fmail3.nate.com%2F#index"
                style="font-family: 'Handon3gyeopsal600g' !important;"
              >nate</v-btn>
              <v-btn 
                block 
                x-large 
                class="my-2 font-weight-bold white--text text-h6" 
                color="#19CD61"
                href="https://nid.naver.com/nidlogin.login?url=http%3A%2F%2Fmail.naver.com%2F"
                style="font-family: 'Handon3gyeopsal600g' !important;"
              >naver</v-btn>
            </v-container>
          </v-card>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
  </v-container>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'

export default {
  name: 'SignUp',
  data() {
    return {
      present: 1,
      values: {
        jara: 'JARA 서비스를 이용해 주셔서 감사합니다.\n\nSNS 서비스를 제공하는 JARA가 아래 준비한 약관을 읽어주시면 감사드리겠습니다.\n\n계정 관련\nJARA는 이메일로 계정을 생성하실 수 있습니다. 다만, 실제 이메일의 소유주임을 확인하기 위해서 가입 당시 인증 절차를 거치게 됩니다. 아래의 경우에는 계정 생성을 승인하지 않을 수 있습니다.\n다른 사람의 개인정보를 이용하여 계정을 생성하려 한 경우\n계정 생성시 필요한 정보를 입력하지 않거나 허위 정보를 입력한 경우\nJARA가 과거에 운영원칙 또는 법률 위반 등의 정당한 사유로 해당 계정을 삭제 또는 징계한 경우\n계정은 본인만 이용할 수 있고, 다른 사람에게 이용을 허락하거나 양도할 수 없습니다. 사용자는 계정과 관련된 정보, 즉 프로필 사진이나 닉네임 등을 수정할 수 있습니다. 이메일이 바뀐 경우에는 서비스 내 설정 메뉴나 고객센터 문의를 통해 새 이메일로 인증절차를 걸쳐 수정할 수 있습니다.',
        privacy: 'JARA는 서비스 제공을 위한 필요 최소한의 개인정보를 수집하고 있습니다.\n\n회원 가입 시 또는 서비스 이용 과정에서 홈페이지 또는 개별 어플리케이션이나 프로그램 등을 통해 아래와 같은 서비스 제공을 위해 필요한 최소한의 개인정보를 수집하고 있습니다.\n\n이메일, 비밀번호, 닉네임, 프로필사진, 주소, 생년월일, 성별',
        location: '위치정보 이용약관에 동의하시면, 위치를 활용한 날씨 정보를 제공하는 JARA의 위치기반 서비스를 이용하실 수 있습니다.\n\nJARA가 제공하는 서비스의 종류, 세부 내용, 이용 요금은 아래와 같습니다.\n1.종류 : SNS\n2.서비스명 : JARA\n3.설명 : 자취 인구(1인 가구)를 위한 SNS\n4.위치정보 이용 목적: 날씨 정보 제공\n5.이용 요금 : 무료',
      },
      items: [
        { text: '남성', value: 0 },
        { text: '여성', value: 1 },
      ],
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
      signUpData: {
        email: '',
        password: '',
        passwordConfirm: '',
        nickname: '',
        sex: null,
        birthday: new Date().toISOString().substr(0, 10),
        location: ''
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
      passwordConfirmRules: {
        required: value => !!value || '비밀번호를 한번 더 입력해주세요.',
        same: v => v == this.signUpData.password || '비밀번호가 일치하지 않습니다.'
      },
      nickNameRules: {
        required: value => !!value || '닉네임을 입력해주세요.',
        max: v => v.length <= 8 || '닉네임은 8자리 이하이어야 합니다.'
      },
      sexRules: {
        required: value => value!=null || '성별을 선택해주세요.'
      },
      bDayRules: {
        over: value => value < this.current || '유효하지 않은 날짜입니다.'
      },
      districtsRules: {
        required: value => !!value || '주소를 선택해주세요.',
      },
      isError: false,
      isAgree: false,
      isValid: false,
      showPWD: false,
      showConfirmPWD: false,
      showCalendar: false,
      loader: null,
      loading: false,
      current: new Date().toISOString().substr(0, 10),
    }
  },
  methods: {
    next() {
      this.isAgree = !this.isAgree
      this.present = ++this.present
      this.isError = false
    },
    signUp() {
      this.loader = 'loading'
      this.$vuetify.goTo(0)
      axios.post(`${this.$store.state.api_server}/accounts/signup`, this.signUpData)
        .then(() => {
          this.next()
        })
        .catch(() => {
          this.isError = !this.isError
        })
    },
  },
  computed: {
    ...mapState([
      'api_server'
    ])
  },
  watch: {
    loader() {
      const l = this.loader
      this[l] = !this[l]
      setTimeout(() => (this[l] = false), 3000)
      this.loader = null
    }
  },
}
</script>

<style>

</style>