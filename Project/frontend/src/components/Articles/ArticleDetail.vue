<template>
  <v-container fluid mt-5 v-if="isTaken" style="font-family: 'Handon3gyeopsal300g';">
    <div class="d-flex justify-space-between align-center">
      <div class="d-flex align-center" v-if="psas[article.writer]==null">
        <v-btn text class="pa-0 font-weight-bold text-sm-h5 text-h6" @click="goToUser" x-large><v-icon x-large>mdi-account-circle</v-icon>{{ users[article.writer] }}</v-btn>      
      </div>
      <div class="d-flex align-center" v-else>
        <v-btn text class="pa-0 font-weight-bold text-sm-h5 text-h6" @click="goToUser" x-large style="font-family: 'Handon3gyeopsal600g' !important;">
          <v-avatar><img :src="psas[article.writer]" alt="프로필 사진"></v-avatar>
          {{ users[article.writer] }}
        </v-btn>      
      </div>
      <div class="d-flex align-center">
        <div class="grey--text">{{ article.created_at }}</div>
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
              @click="updateOrDelete(idx)"
            >
              <v-list-item-title style="font-family: 'Handon3gyeopsal300g' !important;">{{ menu.title }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </div>
    <v-img v-if="article.img_src!=null" class="mt-3" width="100%" height="auto" :src="article.img_src"></v-img>
    <div class="mt-5">{{ article.contents }}</div>   
    <v-divider class="mt-5"></v-divider>
    <div class="d-flex justify-space-around">
      <div v-if="!liked">
        <v-btn @click="like" icon><v-icon>mdi-heart-outline</v-icon></v-btn>{{ article.likeAccounts.length }}
      </div>
      <div v-else>
        <v-btn @click="unLike" icon><v-icon color="red darken-1">mdi-heart</v-icon></v-btn>{{ article.likeAccounts.length }}
      </div>
      <div><v-btn icon><v-icon>mdi-comment-processing</v-icon></v-btn>{{ article.comments.length }}</div>
    </div>
    <v-divider class="mb-5"></v-divider>
    <div class="text-sm-h6 text-subtitle-2">댓글</div>
    <v-form class="mt-5 d-flex">
      <v-textarea
        v-model="commentData.contents"
        label="댓글을 남겨주세요."
        color="green darken-1"
        auto-grow
        outlined
        rows="1"
        row-height="15"
      ></v-textarea>
      <div>
        <v-btn v-if="!isUpdate" @click="addComment" text class="mt-3 font-weight-bold" color="green darken-1">등록</v-btn>
        <v-btn v-else @click="updateComment" text class="mt-3 font-weight-bold" color="teal">수정</v-btn>
      </div>
    </v-form>
    <ArticleComment :comments="comments" @updateOrDeleteOrHide="updateOrDeleteOrHide"/>
    <v-btn @click="scrollToTop" class="top" color="light-green" fab small dark>
      <v-icon>mdi-apple-keyboard-control</v-icon>
    </v-btn>
  </v-container>
</template>

<script>
import { mapState } from 'vuex'
import ArticleComment from '../Articles/ArticleComment.vue'
import axios from 'axios'
import firebase from 'firebase'


export default {
  name: 'ArticleDetail',
  components: {
    ArticleComment
  },
  data() {
    return {
      isTaken: false,
      liked: false,
      isUpdate: false,
      article: [],
      commentData: {
        contents: '',
        writer: this.$store.state.userInfo.id
      },
      comments: [],
      menuItems: [
        { title: '수정' },
        { title: '삭제' }
      ],
    }
  },
  methods: {
    fetchArticle() {
      axios.get(`${this.$store.state.api_server}/articles/${this.$route.params.article_id}`)
        .then(res => {
          this.article = res.data
          if (this.article.likeAccounts.length > 0 && this.article.likeAccounts.includes(this.$store.state.userInfo.id)) {
            this.liked = true
          }
          this.comments = res.data.comments
          this.isTaken = true
        })
        .catch(() => this.$router.push({name: 'PageNotFound'}))
    },
    updateOrDelete(val) {
      if (this.$store.state.userInfo.id == this.article.writer) {
        if (val == 0) { this.$router.push({ name: 'UpdateArticle', params: { article_id : this.article.id }}) }
        else { 
          const response = confirm('정말로 삭제 하시겠습니까?')
          if (response) { 
            firebase.storage().ref().child(`articles/${this.article.id}`).delete()
            axios.delete(`${this.$store.state.api_server}/articles/${this.article.id}`)
              .then(() => this.$router.push('/main'))
          }
        }
      } else { alert('작성자만 사용할 수 있어요.') }
    },
    like() {
      const key = firebase.database().ref(`liked/${this.article.writer}`).push().key
      const update = {}
      update['key'] = key
      update['by'] = this.$store.state.userInfo.id
      firebase.database().ref(`liked/${this.article.writer}`).push(update)
      axios.post(`${this.$store.state.api_server}/articles/${this.article.id}/like`, '' ,{ params: { user_id : this.$store.state.userInfo.id }})
        .then(() => {
          this.article.likeAccounts.push(this.$store.state.userInfo.id)
          this.liked = true
        })
    },
    unLike() {
      axios.delete(`${this.$store.state.api_server}/articles/${this.article.id}/like`, { params: { user_id : this.$store.state.userInfo.id }})
        .then(() => {
          this.article.likeAccounts.pop(this.$store.state.userInfo.id)
          this.liked = false
        })
    },
    addComment() {
      if (this.commentData.contents.trim().length <= 0) {
        alert('유효한 입력이 아닙니다.')
        this.commentData.contents = ''
      } 
      else {
        const key = firebase.database().ref(`comment/${this.article.writer}`).push().key
        const update = {}
        update['by'] = this.$store.state.userInfo.id
        update['key'] = key
        firebase.database().ref(`comment/${this.article.writer}/${key}`).update(update)
        axios.post(`${this.$store.state.api_server}/articles/${this.article.id}/comments`, this.commentData)
          .then(res => {
            this.comments.unshift(res.data)
            this.commentData.contents = ''
            alert('댓글이 성공적으로 작성되었습니다.')
          })
      }
    },
    updateOrDeleteOrHide(comment, item, index) {
      if (item === 0) {
        if (this.$store.state.userInfo.id===comment.writer) {
          this.isUpdate = true
          this.$vuetify.goTo(0)
          this.commentData.contents = comment.contents
          this.commentData['id'] = comment.id
        } else { alert('작성자만 사용할 수 있어요.')}
      }
      else if (item === 1) {
        if (this.$store.state.userInfo.id===comment.writer) {
          if (confirm('정말로 삭제하시겠습니까?')) {
            axios.delete(`${this.$store.state.api_server}/articles/${this.article.id}/comments/${comment.id}`)
              .then(() => this.comments.splice(index,1))
          }
        } else { alert('작성자만 사용할 수 있어요.')}
      }
      else {
        if (this.$store.state.userInfo.nickname===this.$store.state.users[this.article.writer]) {
          axios.put(`${this.$store.state.api_server}/articles/${this.article.id}/comments/${comment.id}/invisible`, '')
            .then(() => this.comments.splice(index, 1))
        } else { alert('게시글의 작성자만 사용할 수 있어요.')}
      }
    },
    updateComment() {
      if (this.commentData.contents.trim().length == 0) {
        alert('유효한 입력이 아닙니다.')
        this.commentData.contents = ''
      }
      else {
        axios.put(`${this.$store.state.api_server}/articles/${this.article.id}/comments/${this.commentData.id}`, this.commentData)
          .then(res => {
            const idx = this.comments.findIndex(x => x.id === this.commentData.id)
            this.comments[idx].updated_at = res.data.updated_at
            this.comments[idx].contents = res.data.contents
            this.isUpdate = false
            this.commentData = {
              contents: '',
              writer: this.$store.state.userInfo.id
            }
          })
      }
    },
    scrollToTop() {
      this.$vuetify.goTo(0)
    },
    goToUser() {
      this.$router.push(`/accounts/${this.article.writer}`)
    }
  },
  computed: {
    ...mapState([
      'userInfo',
      'users',
      'api_server',
      'psas'
    ])
  },
  created() {
    this.fetchArticle()
  },
  beforeRouteLeave(to, from, next) {
    if (this.commentData.contents.length > 0) {
      if (confirm('입력 내용이 저장되지 않습니다. 정말로 이 페이지에서 벗어나시겠습니까?')) {next()}
      else {next(false)}
    } else {next()}
  },
}
</script>

<style scoped>
  .top {
    position: fixed;
    bottom: 3vh;
    right: 3vh;
  }
</style>