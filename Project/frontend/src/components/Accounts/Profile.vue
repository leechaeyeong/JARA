<template>
  <v-container fluid mt-5 v-if="isLoad" style="font-family: 'Handon3gyeopsal300g' !important;">
    <div class="d-flex justify-space-between align-center">
      <div class="font-weight-bold text-sm-h3 text-h6" v-if="user.img_src==null" style="font-family: 'Handon3gyeopsal600g' !important;">
        <v-icon x-large>mdi-account-circle</v-icon>
        {{ user.nickname }}
      </div>
      <div class="text-sm-h3 text-h6 font-weight-bold" v-else style="font-family: 'Handon3gyeopsal600g' !important;">
        <v-avatar>
          <img :src="user.img_src" alt="프로필 사진">
        </v-avatar>
        {{ user.nickname }}
      </div>
    </div>
    <div class="mt-2">
      <div class="ml-2">
        <v-icon v-if="user.sex" color="red darken-1">mdi-human-female</v-icon>
        <v-icon v-else color="blue darken-1">mdi-human-male</v-icon>
        {{ user.bio }}
      </div>
    </div>
    <div class="mt-5 d-flex justify-space-around text-sm-h6 text-subtitle-2 text-center">
      <div>게시글 <div class="text-center"><v-btn text class="text-h6" style="font-family: 'Handon3gyeopsal600g' !important;">{{ user.myArticleList.length }}</v-btn></div></div>
      <div class='ml-3'>
        팔로워
        <div class="text-center">
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                text
                class="text-h6"
                v-bind="attrs"
                v-on="on"
                style="font-family: 'Handon3gyeopsal600g' !important;"
              >{{ user.followerList.length }}
              </v-btn>
            </template>
            <v-list v-if="user.followerList.length > 0">
              <v-list-item
                v-for="(user, index) in user.followerList"
                :key="index"
              >
                <v-list-item-title style="font-family: 'Handon3gyeopsal300g' !important;">{{ users[user] }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </div>
      </div>
      <div class="ml-3">
        팔로잉
        <div class="text-center">
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                text
                class="text-h6"
                v-bind="attrs"
                v-on="on"
                style="font-family: 'Handon3gyeopsal600g' !important;"
              >{{ user.followingList.length }}
              </v-btn>
            </template>
            <v-list v-if="user.followingList.length > 0">
              <v-list-item
                v-for="(user, index) in user.followingList"
                :key="index"
              >
                <v-list-item-title style="font-family: 'Handon3gyeopsal300g' !important;">{{ users[user] }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </div>
      </div>
    </div>
    <div class="text-right">
      <v-btn text @click="updateUserInfo"><v-icon>mdi-account-edit-outline</v-icon>회원정보 수정</v-btn>
    </div>
    <v-divider class="my-5"></v-divider>
    <div class="d-flex justify-space-around align-center">
      <v-btn text class="font-weight-bold" @click="isArticles = true">게시글
        <v-icon v-if="isArticles" class="ml-1" color="green lighten-1">mdi-pencil-box</v-icon>
        <v-icon v-else class="ml-1" color="green lighten-1">mdi-pencil-box-outline</v-icon>
      </v-btn>
      |
      <v-btn text class="font-weight-bold" @click="isArticles = false">저장
        <v-icon v-if="isArticles" class="ml-1" color="teal">mdi-bookmark-outline</v-icon>
        <v-icon v-else class="ml-1" color="teal">mdi-bookmark</v-icon>
      </v-btn>
    </div>
    <v-row class="mt-5 px-3 fill-height" v-if="isArticles">
      <Articles v-for="article in articles" :key="article.id" :article="article"/>
    </v-row>
    <v-row class="mt-5 px-3 fill-height" v-else>
      <Scraps v-for="scrap in scraps" :key="scrap.id" :scrap="scrap"/>
    </v-row>
  </v-container>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'
import Articles from './Articles.vue'
import Scraps from './Scraps.vue'

export default {
  name: 'Profile',
  components: {
    Articles,
    Scraps,
  },
  computed: {
    ...mapState([
      'userInfo',
      'users',
      'api_server'
    ])
  },
  data() {
    return {
      user: null,
      isLoad: false,
      articles: [],
      scraps: [],
      isArticles: true,
    }
  },
  methods: {
    getUserData() {
      axios.get(`${this.$store.state.api_server}/accounts/${this.$store.state.userInfo.id}`)
        .then(res => {
          this.user = res.data
          this.articles = res.data.myArticleList
          this.scraps = res.data.scrapTipList
          this.isLoad = !this.isLoad
        })
    },
    updateUserInfo() {
      this.$router.push(`/accounts/${this.user.id}/info`)
    },
  },
  created() {
    this.getUserData()
  }
}
</script>

<style>

</style>