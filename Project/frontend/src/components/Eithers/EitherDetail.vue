<template>
  <v-container fluid style="font-family: 'Handon3gyeopsal300g';">
    <div class="mt-5 text-right">
      <v-chip
        v-if="either.status==0"
        color="green"
        text-color="white"
      >
        진행중
        <v-icon right>mdi-forum-outline</v-icon>
      </v-chip>
      <v-chip
        v-else
        color="grey"
        text-color="white"
      >
        완료
        <v-icon right>mdi-checkbox-multiple-marked</v-icon>
      </v-chip>
    </div>
    <div class="d-flex justify-space-between align-center">
      <div>
        <v-btn text class="pa-0 font-weight-bold text-sm-h5 text-h6" @click="goToUser" x-large style="font-family: 'Handon3gyeopsal600g' !important;">
          <v-icon x-large v-if="psas[either.writer]==null">mdi-account-circle</v-icon>
          <v-avatar v-else><img :src="psas[either.writer]"></v-avatar>
          {{ users[either.writer] }}</v-btn>
      </div>
      <div class="grey--text d-flex align-center">
        <div>{{ either.created_at }}</div>
        <v-menu offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              icon
              v-bind="attrs"
              v-on="on"
            >
              <v-icon>mdi-dots-horizontal</v-icon>
            </v-btn>
          </template>
          <v-list>
            <v-list-item
              v-for="(menu, idx) in menuItems"
              :key="idx"
              @click="completeOrDelete(idx)"
            >
              <v-list-item-title style="font-family: 'Handon3gyeopsal300g';">{{ menu.title }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </div>
    <div class="mt-5 ml-5 text-h6" style="font-family: 'Handon3gyeopsal300g' !important;">{{ either.question }}</div>
    <v-row v-if="!isVoted" justify-space-around class="mt-5 px-5 text-center white--text text-sm-h3 text-h6 font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
      <v-col @click="pick(0)" class="red darken-1 rounded" id="A">{{ either.choiceA }}</v-col>
      <v-col @click="pick(1)" class="blue darken-2 rounded" id="B">{{ either.choiceB }}</v-col>
    </v-row>
    <v-row v-else justify-space-around class="mt-5 px-5 text-center white--text text-sm-h3 text-h6 font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
      <v-col v-if="choiceA.includes(userInfo.id)" class="red darken-4 rounded">{{ either.choiceA }}<v-icon class="ml-2" x-large>mdi-check-bold</v-icon></v-col>
      <v-col v-else class="red darken-1 rounded">{{ either.choiceA }}</v-col>
      <v-col v-if="choiceB.includes(userInfo.id)" class="blue darken-4 rounded">{{ either.choiceB }}<v-icon class="ml-2" x-large>mdi-check-bold</v-icon></v-col>
      <v-col v-else class="blue darken-2 rounded">{{ either.choiceB }}</v-col>
    </v-row>
    <div class="text-center mt-5">
      <v-dialog
        v-model="dialog"
        width="500"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            text
            x-large
            class="font-weight-bold text-h6"
            v-bind="attrs"
            v-on="on"
            style="font-family: 'Handon3gyeopsal600g' !important;"
          >결과보기<v-icon color="deep-purple">mdi-align-vertical-bottom</v-icon></v-btn>
        </template>

        <v-card>
          <v-card-title class="headline grey lighten-2 font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
            <v-icon x-large class="mr-2">mdi-alpha-q-circle</v-icon>{{ either.question }}
          </v-card-title>

          <v-card-text class="mt-5" style="font-family: 'Handon3gyeopsal300g' !important;">
            <div v-if="choiceA.length > 0 || choiceB.length > 0">
              <div>참여자: {{ choiceA.length + choiceB.length }}명</div>
              <div class="my-5 black--text font-weight-bold text-h6" style="font-family: 'Handon3gyeopsal300g' !important;">
                <v-chip
                  color="red darken-1"
                  text-color="white"
                >{{ either.choiceA }}</v-chip>
                {{ (((choiceA.length)/(choiceA.length+choiceB.length))*100).toFixed(2) }}% {{ choiceA.length }}명
              </div>
              <div class="black--text font-weight-bold text-h6" style="font-family: 'Handon3gyeopsal300g' !important;">
                <v-chip
                  color="blue darken-2"
                  text-color="white"
                >{{ either.choiceB }}</v-chip>
                {{ (((choiceB.length)/(choiceA.length+choiceB.length))*100).toFixed(2) }}% {{ choiceB.length }}명
              </div>
            </div>
            <div v-else>
              <div>참여자: {{ choiceA.length + choiceB.length }}명</div>
              <div class="my-5 black--text font-weight-bold text-h6">
                <v-chip
                  color="red darken-1"
                  text-color="white"
                >{{ either.choiceA }}</v-chip>
                0% {{ choiceA.length }}명
              </div>
              <div class="black--text font-weight-bold text-h6">
                <v-chip
                  color="blue darken-2"
                  text-color="white"
                >{{ either.choiceB }}</v-chip>
                0% {{ choiceB.length }}명
              </div>
            </div>
          </v-card-text>

          <v-divider></v-divider>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              class="font-weight-bold"
              color="deep-purple"
              text
              @click="dialog = false"
              style="font-family: 'Handon3gyeopsal300g' !important;"
            >
              닫기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
    <v-divider class="my-5"></v-divider>
    <div class="text-sm-h6 text-subtitle-2" style="font-family: 'Handon3gyeopsal300g' !important;">댓글</div>
    <v-form class="mt-5">
      <v-row>
        <v-col cols="6" class="py-0">
          <v-select
            :items="choices"
            item-text="text"
            item-value="value"
            outlined
            color="green darken-2"
            item-color="green darken-2"
            v-model="commentData.choice"
            style="font-family: 'Handon3gyeopsal300g' !important;"
          ></v-select>
        </v-col>
      </v-row>
      <v-chip
        v-if="!commentData.choice&&commentData.choice!=null"
        color="red darken-1"
        text-color="white"
      >
      {{ either.choiceA }}
      </v-chip>
      <v-chip
        v-else-if="commentData&&commentData.choice!=null"
        color="blue darken-2"
        text-color="white"
      >
      {{ either.choiceB }}
      </v-chip>
      <div class="d-flex mt-2">
        <v-textarea
          v-model="commentData.contents"
          label="댓글을 남겨주세요."
          color="green darken-1"
          auto-grow
          outlined
          rows="1"
          row-height="15"
        ></v-textarea>
        <div style="font-family: 'Handon3gyeopsal300g' !important;">
          <v-btn v-if="!isUpdate" @click="addComment" text class="mt-3 font-weight-bold" color="green darken-1">등록</v-btn>
          <v-btn v-else @click="updateComment" text class="mt-3 font-weight-bold" color="teal">수정</v-btn>
        </div>
      </div>
    </v-form>
    <EitherComment :comments="comments" :either="either" @updateOrDelete="updateOrDelete"/>
  </v-container>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'
import EitherComment from '../Eithers/EitherComment.vue'

export default {
  name: 'EitherDetail',
  components: {
    EitherComment
  },
  data() {
    return {
      either: {},
      choiceA: [],
      choiceB: [],
      dialog: false,
      isValid: false,
      isUpdate: false,
      isVoted: false,
      pickData: {
        either_id: this.$route.params.either_id,
        user_id: this.$store.state.userInfo.id,
        pick: ''
      },
      choices: [
        { text: '', value: false },
        { text: '', value: true },
      ],
      menuItems: [
        {title: '완료'},
        {title: '삭제'},
      ],
      commentData: {
        writer: this.$store.state.userInfo.id,
        contents: '',
        either_id: this.$route.params.either_id,
        choice: null
      },
      comments: [],
    }
  },
  computed: {
    ...mapState([
      'api_server',
      'userInfo',
      'users',
      'psas'
    ])
  },
  mounted() {
    this.fetchEither()
  },
  methods: {
    fetchEither() {
      axios.get(`${this.$store.state.api_server}/eithers/${this.$route.params.either_id}`)
        .then(res => {
          this.either = res.data.either
          this.choices[0].text = res.data.either.choiceA
          this.choices[1].text = res.data.either.choiceB
          this.choiceA = res.data.choiceA
          this.choiceB = res.data.choiceB
          this.comments = res.data.eitherComments
          if (this.choiceA.includes(this.$store.state.userInfo.id) || this.choiceB.includes(this.$store.state.userInfo.id)) {
            this.isVoted = true
          }
        })
        .catch(() => this.$router.push({ name: 'PageNotFound' }))
    },
    pick(v) {
      if (this.either.status == 0) {
        if (v == 0) {
          if (confirm('인생은 낙장불입!' + '\n' + `${this.either.choiceA}` + '을(를) 선택하시겠습니까?')) {
            this.pickData.pick = v
            axios.post(`${this.$store.state.api_server}/eithers/${this.$route.params.either_id}/pick`, this.pickData)
              .then(() => {
                this.isVoted = true
                if (v == 0) {this.choiceA.push(this.$store.state.userInfo.id)}
                else {this.choiceB.push(this.$store.state.userInfo.id)}
              })
          }
        } else {
          if (confirm('인생은 낙장불입!' + '\n' + `${this.either.choiceB}` + '을(를) 선택하시겠습니까?')) {
            this.pickData.pick = v
            axios.post(`${this.$store.state.api_server}/eithers/${this.$route.params.either_id}/pick`, this.pickData)
              .then(() => {
                this.isVoted = true
                if (v == 0) {this.choiceA.push(this.$store.state.userInfo.id)}
                else {this.choiceB.push(this.$store.state.userInfo.id)}
              })
          }
        }

      } else {alert('이미 종료된 투표입니다.')}
    },
    completeOrDelete(val) {
      if (this.$store.state.userInfo.id == this.either.writer) {
        if (val == 0) { 
          if (this.either.status == 1) {
            alert('이미 완료된 투표입니다.')
          } 
          else {
            this.either.status = 1
            axios.put(`${this.$store.state.api_server}/eithers/${this.either.id}`)
              .then(() => alert('완료하였습니다.'))
          }
        }
        else { 
          const response = confirm('정말로 삭제 하시겠습니까?')
          if (response) { 
            axios.delete(`${this.$store.state.api_server}/eithers/${this.either.id}`)
              .then(() => this.$router.push('/eithers'))
          }
        }
      } else { alert('작성자만 사용할 수 있어요.') }
    },
    goToUser() {
      this.$router.push(`/accounts/${this.either.writer}`)
    },
    addComment() {
      if (this.commentData.contents.trim().length <= 0 || this.commentData.choice == null) {
        alert('유효한 입력이 아닙니다.')
        this.commentData.contents = ''
        this.commentData.choice = null
      }
      else {
        axios.post(`${this.$store.state.api_server}/eithers/${this.either.id}/comments`, this.commentData)
          .then(res => {
            this.comments.unshift(res.data)
            this.commentData.contents = ''
            this.commentData.choice = null
            alert('댓글이 성공적으로 작성되었습니다.')
          })
      }
    },
    updateOrDelete(comment, item, index) {
      if (item === 0) {
        if (this.$store.state.userInfo.id===comment.writer) {
          this.isUpdate = true
          this.$vuetify.goTo(0)
          this.commentData.contents = comment.contents
          this.commentData.id = comment.id
          this.commentData.choice = comment.choice
        } else { alert('작성자만 사용할 수 있어요.')}
      }
      else if (item === 1) {
        if (this.$store.state.userInfo.id===comment.writer) {
          if (confirm('정말로 삭제하시겠습니까?')) {
            axios.delete(`${this.$store.state.api_server}/eithers/${this.either.id}/comments/${comment.id}`)
              .then(() => this.comments.splice(index,1))
          }
        } else { alert('작성자만 사용할 수 있어요.')}
      }
    },
    updateComment() {
      if (this.commentData.contents.trim().length == 0) {
        alert('유효한 입력이 아닙니다.')
        this.commentData.contents = ''
      }
      else {
        axios.put(`${this.$store.state.api_server}/eithers/${this.either.id}/comments/${this.commentData.id}`, this.commentData)
          .then(res => {
            this.isUpdate = false
            const idx = this.comments.findIndex(x => x.id === this.commentData.id)
            this.comments[idx].updated_at = res.data.updated_at
            this.comments[idx].contents = res.data.contents
            this.comments[idx].choice = res.data.choice
            this.commentData = {
              contents: '',
              writer: this.$store.state.userInfo.id,
              either_id: this.either.id,
              choice: null
            }
          })
      }
    },
  },
}
</script>

<style scoped>
  #or {
    position: absolute;
    top: 50%;
    left: 50%;
  }

  #A:hover {
    cursor: pointer;
    opacity: 0.8;
  }

  #B:hover {
    cursor: pointer;
    opacity: 0.8;
  }
</style>