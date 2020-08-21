<template>
  <v-container v-if="isTaken" fill-height fluid grid-list-md style="font-family: 'Handon3gyeopsal300g' !important;">
    <!-- {{ tip }} -->
    <!-- <v-layout v-if="loading" align-center justify-center>
      <v-progress-circular size="50" color="primary" indeterminate></v-progress-circular>
    </v-layout> -->
    <!-- <v-layout v-if="!loading"> -->
    <v-layout>
      <v-flex xs12 md8 offset-md2>
        <!-- <v-layout justify-start column fill-height v-scroll="onScroll" -->
        <v-layout justify-start column fill-height>
          <v-flex xs12>
            <v-card>
              <v-card-title class="headline pb-0" style="font-family: 'Handon3gyeopsal600g' !important;">
                {{ tip.title }}
              </v-card-title>
              <v-layout>
                <v-flex xs7 class='pr-0'>
                  <v-list class='pt-0'>
                    <v-list-item>
                      <v-list-item-avatar>
                      <!-- <img :src="'https://steemitimages.com/u/' + author + '/avatar/small'" alt="avatar" onerror="this.src='https://steemitimages.com/u/monawoo/avatar/small'"> -->
                        <v-icon v-if="psas[tip.writer]==null" x-large>mdi-account-circle</v-icon>
                        <img v-else :src="psas[tip.writer]" alt="avatar">
                      </v-list-item-avatar>
                      <v-list-item-content>
                        <v-list-item-title style="font-family: 'Handon3gyeopsal600g';">{{users[tip.writer]}}
                          <!-- <span>({{ tip.writer }})</span> -->
                        </v-list-item-title>
                        <v-list-item-title class="grey--text">{{tip.created_at | filterCreated }}</v-list-item-title>
                        <!-- <v-list-item-sub-title>{{tip.created_at | filterCreated}} · {{category}}</v-list-item-sub-title> -->
                      </v-list-item-content>
                    </v-list-item>
                  </v-list>
                </v-flex>
                <v-flex xs5 text-xs-right class='pt-3'>
                  <v-btn v-if="!liked" @click="like" icon><v-icon>mdi-heart-outline</v-icon></v-btn>
                  <v-btn v-else @click="like" icon><v-icon color="red darken-1">mdi-heart</v-icon></v-btn>{{ tip.likeAccounts.length }}
                  <v-btn icon><v-icon>mdi-comment-processing-outline</v-icon></v-btn>{{ tip.comments.length }}
                  <v-btn icon v-if="!scraped" @click="scrap"><v-icon>mdi-bookmark-outline</v-icon></v-btn>
                  <v-btn icon v-else color="teal"><v-icon>mdi-bookmark</v-icon></v-btn>
                </v-flex>
              </v-layout>
              <v-divider></v-divider>
              <v-card-text>
                <v-img v-if="tip.img_src!=null" class="mt-3" width="100%" height="auto" :src="tip.img_src"></v-img>
                <article class="mt-5 black--text">{{ tip.contents }}</article>
              </v-card-text>
              <v-card-text class="pb-1">
                <template>
                  <v-toolbar v-if="$store.state.userInfo.id === tip.writer" flat color="white">
                    <v-row justify="center" align="center">
                      <template>
                      <v-chip class="tag">#{{ tag[tip.tag_id] }}</v-chip>
                      </template>
                      <v-spacer></v-spacer>
                      <template>
                        <v-btn @click="goTipsUpdate" icon>
                          <v-icon>mdi-pencil</v-icon>
                        </v-btn>
                        <v-btn icon @click="goTipsDelete">
                          <v-icon>
                            mdi-delete
                          </v-icon>
                        </v-btn>
                      </template>
                    </v-row>
                  </v-toolbar>
                </template>
              </v-card-text>
            </v-card>
          </v-flex>
          <v-flex xs12>
            <!-- <v-subheader class='pl-0' >댓글 ({{tip.comments.length}})</v-subheader> -->
            <v-subheader class='pl-0' >댓글</v-subheader>
            <v-card ref='comments'>
              <v-card-text class="pb-0">
                <v-text-field
                  ref="contents"
                  v-model="new_comment.contents"
                  placeholder="댓글을 입력해 주세요."
                  required
                  @keyup.enter="newComment"
                  color="green darken-2"
                >
                </v-text-field>
              </v-card-text>
              <!-- <v-container grid-list-md v-if='!loadedComments'>
                <v-layout align-center justify-center>
                  <v-progress-circular color="primary" indeterminate></v-progress-circular>
                </v-layout>
              </v-container> -->
              <template>
              <!-- <template v-if='loadedComments'> 로딩 창-->
                <!-- <Comments :comments='comments'></Comments>  // 여기 포인트 -->
                <TipComment
                  @find_commentId="deleteComment"
                  @change_comment="updateComment"
                  v-for="comment in tip.comments"
                  :key="comment.id"
                  :comment="comment"
                />
              </template>
            </v-card>
          </v-flex>
        </v-layout>
      </v-flex>
    </v-layout>
  </v-container>
</template>


<script>
import axios from 'axios'
import { mapState } from 'vuex'
import TipComment from '../Tips/TipComment.vue'
import firebase from 'firebase'

export default {
  name: 'TipsItemDetail',
  components: {
    TipComment,
  },
  data() {
    return {
      tip: {},
      liked: false,
      scraped: false,
      new_comment : {
        contents: '',
        writer: this.$store.state.userInfo.id,
        tip_id: this.$route.params.tip_id
      },
      isTaken : false,
      tag: {1:'요리',2:'세탁',3:'청소',4:'보관'}
    }
  },
  methods: {
    goTipsDelete() {
      if (this.$store.state.userInfo.id === this.tip.writer) {
        const response = confirm('게시글을 정말로 삭제하시겠습니까?')
        if (response) {
          axios.delete(`${this.$store.state.api_server}/tips/${this.tip.id}`)
            .then(() => {
              firebase.storage().ref().child(`tips/${this.tip.id}`).delete()
              this.$router.push('/tips')
            })
        }
      } else {
        alert('게시글 작성자만 삭제 가능 합니다.')
      }
    },
    goTipsUpdate() {
      if (this.$store.state.userInfo.id === this.tip.writer) {
        this.$router.push({ name: 'UpdateTip', params: { tip_id: this.tip.id }})
      }
    },
    like() {
      axios.post(`${this.$store.state.api_server}/tips/${this.tip.id}/like`, '',{ params : { user_id: this.$store.state.userInfo.id }})
        .then(() => {
          if (this.liked) {
            this.liked = false
            this.tip.likeAccounts.splice(this.tip.likeAccounts.indexOf(this.$store.state.userInfo.id),1)
          } else {
            this.liked = true
            this.tip.likeAccounts.push(this.$store.state.userInfo.id)
          }
        })
    },
    scrap() {
      axios.post(`${this.$store.state.api_server}/tips/${this.tip.id}/scrap`, '', { params: { user_id: this.$store.state.userInfo.id}})
        .then(() => {
          this.tip.scarpAccounts.push(this.$store.state.userInfo.id)
          this.scraped = true
          alert('팁이 저장되었습니다.')
        })
    },
    deleteComment(commentId) {
      axios.delete(`${this.$store.state.api_server}/tips/${this.$route.params.tip_id}/comments/${commentId}`)
        .then(() => {
          this.tip.comments.splice(this.tip.comments.findIndex(x => x.id === commentId),1)
        })
    },
    newComment() {
      axios.post(`${this.$store.state.api_server}/tips/${this.tip.id}/comments`, this.new_comment, { params: {tip_id: this.tip.id }})
        .then(res => {
          this.tip.comments.push(res.data)
          this.new_comment.contents = ''
        })
    },
    updateComment(changeComment) {
      axios.put(`${this.$store.state.api_server}/tips/${this.tip.id}/comments/${changeComment.id}`,changeComment)
        .then(res => {
          this.tip.comments.splice(this.tip.comments.findIndex(x => x.id === changeComment.id), 1, res.data)
        })
    }
  },
  created() {
    axios.get(`${this.$store.state.api_server}/tips/${this.$route.params.tip_id}`)
      .then(res => {
        this.tip = res.data
        if (this.tip.likeAccounts.length>0 && this.tip.likeAccounts.includes(this.$store.state.userInfo.id)) {
          this.liked = true
        }
        if (res.data.scrapAccounts.includes(this.$store.state.userInfo.id)) {
          this.scraped = true
        }
        this.isTaken = true
      })
  },
  computed: {
    ...mapState([
      'api_server',
      'users',
      'userInfo',
      'psas'
    ])
  }
}
</script>

<style scoped>

</style>