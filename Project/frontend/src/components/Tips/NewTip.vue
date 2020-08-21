<template>
    <v-container style="font-family: 'Handon3gyeopsal300g' !important;">
    <v-row justify="center">
      <v-col cols="12" sm="10" md="8" lg="6">
        <v-card ref="form">
          <v-card-text>
            <v-text-field
              ref="title"
              v-model="tip.title"
              :rules="[() => !!tip.title || '필수 입력입니다.']"
              label="제목"
              placeholder="제목을 입력해 주세요."
              required
              color="green darken-2"
            ></v-text-field>
            <v-img v-if="file != null" :src="imageURL"></v-img>
            <v-file-input
              @change="image"
              v-model="file"
              placeholder="사진을 첨부해 주세요."
              color="green darken-2"
            >
            </v-file-input>
            <v-text-field
              ref="contents"
              v-model="tip.contents"
              :rules="[() => !!tip.contents || '필수 입력입니다.']"
              label="내용"
              placeholder="내용을 입력해 주세요."
              required
              color="green darken-2"
            ></v-text-field>
            <v-autocomplete
              ref="tag"
              v-model="tag"
              :rules="[() => !!tag || '필수 입력입니다.']"
              :items="tags"
              label="분류"
              placeholder="분류를 선택해 주세요."
              required
              color="green darken-2"
            ></v-autocomplete>
          </v-card-text>
          <v-divider class="mt-12"></v-divider>
          <v-card-actions>
            <v-btn @click="close" text>취소</v-btn>
            <v-spacer></v-spacer>
            <v-btn color="green darken-2" text @click="createTip">제출</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'
import firebase from 'firebase'

export default {
  name: 'NewTip',
  computed: {
    ...mapState([
      'userInfo',
      'api_server'
    ])
  },
  data() {
    return {
      tip: {
        title: '',
        contents: '',
        tag_id: 0,
        writer: this.$store.state.userInfo.id,
      },
      tags: ['요리', '세탁', '청소', '보관'],
      tag: '',
      formHasErrors: false,
      file: null,
      imageURL: '',
      img_src: '',
      id: null
    }
  },
  mounted() {
    if (this.$route.path != '/tips/new') {
      axios.get(`${this.$store.state.api_server}/tips/${this.$route.params.tip_id}`)
        .then(res => {
          const tag_id_dict = {1:'요리', 2:'세탁', 3:'청소', 4:'보관'}
          this.tip = res.data
          this.tag = tag_id_dict[res.data.tag_id]
        })
    }
  },
  methods: {
    image() {
      this.imageURL = URL.createObjectURL(this.file)
    },
    close() {
      this.$router.push('/tips')
    },
    createTip() {
      const tag_id_dict = {'요리': 1, '세탁': 2, '청소': 3, '보관': 4}
      this.tip.tag_id = tag_id_dict[this.tag]

      if (this.$route.path == '/tips/new') {
        axios.post(`${this.$store.state.api_server}/tips`, this.tip)
          .then(res => {
            this.id = res.data
            if (!this.file) {
              this.$router.push('/tips')
            } else {this.uploadImg()}
          })
      } else {
        axios.put(`${this.$store.state.api_server}/tips/${this.$route.params.tip_id}`, this.tip)
          .then(() => this.$router.push(`/tips/${this.$route.params.tip_id}`))
      }
    },
    uploadImg() {
      firebase.storage().ref(`tips/${this.id}`).put(this.file)
        .then(() => {
          firebase.storage().ref(`tips/${this.id}`).getDownloadURL()
            .then(url => {
              axios.put(`${this.$store.state.api_server}/tips/${this.id}/img`, { id: this.id, img_src: url })
                .then(() => this.$router.push('/tips'))
            })
        })
    }
  }
}
</script>

<style>

</style>