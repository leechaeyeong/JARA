<template>
  <v-container fluid mt-5 style="font-family: 'Handon3gyeopsal300g'">
    <v-alert v-if="isError" type="error">
      유효하지 않은 입력입니다.
    </v-alert>
    <div id="loading" v-if="isLoading" class="text-center">
      <v-progress-circular
        :size="50"
        color="green lighten-3"
        indeterminate
      ></v-progress-circular>
    </div>
    <div class="d-flex align-center">
      <v-icon x-large v-if="psas[userInfo.id]==null">mdi-account-circle</v-icon>
      <v-avatar v-else><img :src="psas[userInfo.id]"></v-avatar>
      <div class="ml-2 font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">{{ userInfo.nickname }}</div>
      <v-btn fixed bottom right fab color="green lighten-1" @click="createArticle" small dark><v-icon>mdi-pencil</v-icon></v-btn>
    </div>
    <v-img class="mt-5" v-if="file != null" :src="imageURL"></v-img>
    <v-img class="mt-5" v-else-if="file==null && imageURL.length !=0" :src="imageURL"></v-img>
    <v-textarea
      v-model="article.contents"
      append-outer-icon="mdi-head-dots-horizontal"
      color="green darken-2"
      class="mt-5"
      auto-grow
      rows="1"
      row-height="20"
      label="어떤 이야기를 공유하고 싶으신가요?"
      autofocus
    ></v-textarea>
    <v-file-input
      @change="image"
      v-model="file"
      placeholder="사진을 첨부해 주세요."
      color="green darken-2"
    >
    </v-file-input>
  </v-container>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'
import firebase from 'firebase'

export default {
  name: 'NewArticle',
  computed: {
    ...mapState([
      'userInfo',
      'api_server',
      'psas'
    ])
  },
  mounted() {
    if (this.$route.path != '/main/new') {
      axios.get(`${this.$store.state.api_server}/articles/${this.$route.params.article_id}`)
        .then(res => {
          this.article = res.data
          this.imageURL = res.data.img_src
        })
    }
  },
  data() {
    return {
      article: {
        contents: '',
        writer: this.$store.state.userInfo.id,
      },
      file: null,
      imageURL: '',
      img_src: '',
      isLoading: false,
      id: null,
      isError: false,
    }
  },
  methods: {
    image() {
      this.imageURL = URL.createObjectURL(this.file)
    },
    createArticle() {
      if (this.$route.path == '/main/new') {
        if (this.article.contents.trim().length == 0 && this.file == null) {
          this.isError = true
          this.article.contents = ''
          setTimeout(() => {
            this.isError = false
          }, 2000)
        }
        else {
          this.isLoading = true
          axios.post(`${this.$store.state.api_server}/articles`, this.article)
            .then(res => {
              this.id = res.data
              if (this.file==null) {
                this.$router.push('/main')
              } else {this.uploadImg()}
            })
        }
      } else {
        axios.put(`${this.$store.state.api_server}/articles/${this.article.id}`, this.article)
          .then(() => this.$router.push('/main'))  
      }
    },
    uploadImg() {
      firebase.storage().ref(`articles/${this.id}`).put(this.file).then(() => {
        firebase.storage().ref(`articles/${this.id}`).getDownloadURL().then(url => {
          axios.put(`${this.$store.state.api_server}/articles/${this.id}/img`, { id: this.id, img_src: url })
            .then(()=> this.$router.push('/main'))
        })
      })
    }
  },
}
</script>

<style scoped>
  #loading {
    position: absolute;
    left: 43%;
    top: 50%;
  }
</style>