<template>
  <v-container v-if="isTaken" fill-height fluid grid-list-md style="font-family: 'Handon3gyeopsal300g';">
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
              <v-card-title class="headline pb-0 justify-space-between" style="font-family: 'Handon3gyeopsal600g' !important;">
                {{ barter.title }}
                  <v-btn color="green" x-large icon v-if="!barter.status" @click="saleCompleted">
                    <v-icon>
                      mdi-toggle-switch
                    </v-icon>
                  </v-btn>
                <!-- <v-btn icon v-else> -->
                  <v-icon color="gray" v-else x-large>
                    mdi-toggle-switch-off
                  </v-icon>
                <!-- </v-btn> -->
                
              </v-card-title>
              <v-layout>
                <v-flex xs7 class='pr-0'>
                  <v-list class='pt-0'>
                    <v-list-item>
                      <v-list-item-avatar>
                      <!-- <img :src="'https://steemitimages.com/u/' + author + '/avatar/small'" alt="avatar" onerror="this.src='https://steemitimages.com/u/monawoo/avatar/small'"> -->
                      <v-avatar>
                        <v-icon v-if="psas[barter.writer]==null" x-large>mdi-account-circle</v-icon>
                        <img v-else :src="psas[barter.writer]">
                      </v-avatar>
                      </v-list-item-avatar>
                      <v-list-item-content>
                        <v-list-item-title style="font-family: 'Handon3gyeopsal600g';">{{users[barter.writer]}}
                        </v-list-item-title>
                        <v-list-item-title v-if="!barter.updated_at">{{barter.created_at | filterCreated }}</v-list-item-title>
                        <v-list-item-title v-else>{{barter.updated_at | filterCreated }}<p style="font-size: x-small; display: inline-block; margin: 0;">(수정됨)</p></v-list-item-title>
                      </v-list-item-content>
                    </v-list-item>
                  </v-list>
                </v-flex>
                <v-flex xs5 text-xs-right class='pr-4 pt-3'>
                  <div class="text-right mr-3">
                    <v-btn icon>
                      <v-icon>mdi-comment-processing-outline</v-icon>
                    </v-btn> {{ comments.length }}
                  </div>
                </v-flex>
              </v-layout>
              <v-divider></v-divider>
              <v-card-text>
                <v-img v-if="barter.img_src" class="my-3" width="100%" height="auto" :src="barter.img_src"></v-img>
                <article class="black--text">{{ barter.contents }}</article>
              </v-card-text>
              <v-card-text>
                <v-toolbar flat color="white">
                  <v-row justify="center" align="center">
                    <template>
                      <v-chip class='tag'>#{{ tag_name[barter.tag_id] }}</v-chip>
                    </template>
                    <v-spacer></v-spacer>
                    <v-dialog v-if="$store.state.userInfo.id === barter.writer" v-model="dialog" persistent max-width="600px">
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn icon @click="deleteItem">
                          <v-icon>
                            mdi-delete
                          </v-icon>
                        </v-btn>
                        <v-btn
                          v-if="!barter.status"
                          v-bind="attrs"
                          v-on="on"
                          @click="updateItem"
                          icon
                        >
                          <v-icon>mdi-pencil</v-icon>
                        </v-btn>
                        
                      </template>
                      <v-card style="font-family: 'Handon3gyeopsal300g';">
                        <v-card-title>
                          <span class="headline" style="font-family: 'Handon3gyeopsal600g' !important;">수정하기</span>
                        </v-card-title>
                        <v-card-text>
                          <v-container>
                            <v-row>
                              <v-col cols="12" sm="6" md="4">
                                <v-text-field v-model="editedItem.title" label="제목" color="green darken-2"></v-text-field>
                              </v-col>
                              <v-col cols="12" sm="6" md="4">
                                <v-text-field v-model="editedItem.contents" label="내용" color="green darken-2"></v-text-field>
                              </v-col>
                              <v-col cols="12" sm="6" md="4">
                                <v-text-field v-model="editedItem.price" label="가격" color="green darken-2"></v-text-field>
                              </v-col>
                              <v-col cols="12" sm="6" md="4">
                                <v-autocomplete
                                  label="분류"
                                  v-model="tag"
                                  :items="tags"
                                  color="green darken-2"
                                  required
                                ></v-autocomplete>
                              </v-col>
                            </v-row>
                          </v-container>
                        </v-card-text>
                        <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn color="grey" text @click="close">취소</v-btn>
                          <v-btn color="green darken-2" text @click="save">수정</v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-dialog>
                  </v-row>
                </v-toolbar>
              </v-card-text>
            </v-card>
          </v-flex>
          <v-flex xs12>
            <v-subheader class='pl-0' >댓글</v-subheader>
            <v-card ref='comments'>
              <v-card-text>
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
                <BarterComment
                  @change_comment="updateComment"
                  @find_commentId="deleteComment"
                  v-for="comment in comments"
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
import { mapState} from 'vuex'
import firebase from 'firebase'
import BarterComment from '../Barters/BarterComment.vue'

export default {
  name: 'BartersItemDetail',
  components: {
    BarterComment,
  },
  data() {
    return {
      dialog: false,
      barter: {},
      comments: [],
      isTaken : false,
      tag_name: {5:'구해요',6:'사요',7:'팔아요',8:'나눠요'},
      tags: ['구해요','사요','팔아요','나눠요'],
      tag: '',
      new_comment: {
        contents: '',
        writer: this.$store.state.userInfo.id,
        item_id: this.$route.params.item_id
      },
      editedItem: {
        title: '',
        tag_id: null,
        contents: '',
        writer: this.$store.state.userInfo.id,
        price: 0,
      },
      defaultItem: {
        title: '',
        tag_id: null,
        contents: '',
        writer: this.$store.state.userInfo.id,
        price: 0,
      },
    }
  },
  created() {
    axios.get(`${this.$store.state.api_server}/barters/${this.$route.params.item_id}`)
      .then(res => {
        this.barter = res.data
      })
    axios.get(`${this.$store.state.api_server}/barters/${this.$route.params.item_id}/comments`)
      .then(res => {
        this.comments = res.data
      })
    this.isTaken = true
  },
  methods: {
    newComment() {
      axios.post(`${this.$store.state.api_server}/barters/${this.barter.id}/comments`, this.new_comment)
        .then(res => {
          this.comments.push(res.data)
          this.new_comment.contents = ''
        })
    },
    saleCompleted() {
      if (!this.barter.status) {
        if (this.$store.state.userInfo.id === this.barter.writer) {
          const response = confirm('거래를 완료하시겠습니까?')
          if (response) {
            this.editedItem = this.barter
            this.editedItem.status = true
            axios.put(`${this.$store.state.api_server}/barters/${this.barter.id}`,this.editedItem)
              .then(res => {
                this.barter = res.data
                this.$nextTick(() => {
                  this.editedItem = Object.assign({}, this.defaultItem)
                })
              })
          }
        }
      } else {
        alert('이미 완료된 거래입니다.')
      }
    },
    updateComment(changeComment) {
      axios.put(`${this.$store.state.api_server}/barters/${this.barter.id}/comments/${changeComment.id}`,changeComment)
        .then(res => {
          this.comments.splice(this.comments.findIndex(x => x.id === changeComment.id), 1, res.data)
        })
    },
    deleteComment(commentId) {
      if (confirm('정말로 삭제하시겠습니까?')) {
        axios.delete(`${this.$store.state.api_server}/barters/${this.barter.id}/comments/${commentId}`)
          .then(() => {
            this.comments.splice(this.comments.findIndex(x => x.id === commentId), 1)
          })
      }
    },
    deleteItem() {
      if (this.$store.state.userInfo.id === this.barter.writer) {
        const flag = this.barter.img_sec ? true : false
        const response = confirm('정말로 삭제 하시겠습니까?')
        if (response) {
          axios.delete(`${this.$store.state.api_server}/barters/${this.barter.id}`)
            .then(() => {
              if (flag) {
                firebase.storage().ref().child(`barters/${this.tip.id}`).delete()
              }
              this.$router.push('/barters')
            })
        }
      } else {
        alert('작성자만 삭제 가능 합니다.')
      }
    },
    updateItem() {
      this.editedItem = Object.assign({}, this.barter)
    },
    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
      })
    },
    save() {
      if ((this.$store.state.userInfo.id === this.barter.writer) && !this.barter.status) {
        const tag_id_dict = {'구해요':5,'사요':6,'팔아요':7,'나눠요':8}
        this.editedItem.tag_id = tag_id_dict[this.tag]
        axios.put(`${this.$store.state.api_server}/barters/${this.barter.id}`,this.editedItem)
          .then(res => {
            this.barter = res.data
          })
        this.close()
      } else {
        alert('작성자만 수정 가능 합니다.')
      }
    }
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

<style>

</style>