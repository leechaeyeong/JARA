<template>
  <v-container fluid style="potition: relative; font-family: 'Handon3gyeopsal300g' !important;">
    <div class="mt-5">
      <div class="mx-3 font-weight-bold text-sm-h3 text-h4" style="font-family: 'Handon3gyeopsal600g' !important;">
        유익한 자라
        <v-icon x-large>mdi-lightbulb-outline</v-icon>
      </div>
      <div>
        <v-flex class="sm6 offset-sm3 justify-center mt-5">
          <v-text-field
            label="검색어를 입력해주세요."
            v-model="search"
            class="col-12 px-3"
            outlined
            append-icon="mdi-magnify"
            color="green darken-2"
          >
          </v-text-field>
        </v-flex>
      </div>
      <div v-if="!search" class="mx-3 font-weight-bold text-sm-h4 text-h5">인기 팁<v-icon color="yellow">mdi-sparkles</v-icon></div>
      <v-container v-if="!search" d-flex justify="center" align="center" class="pt-0">
        <v-slide-group
          class="pa-0"
          active-class="success"
          :show-arrows="false"
        >
          <v-slide-item
            v-for="(top, i) in top5"
            :key="i"
          >
            <v-card
              class="ma-4"
              height="200"
              width="150"
              @click="goToDetail(top.id)"
            >
              <v-img
                id="img"
                v-if="top.img_src!=null"
                class="white--text align-end"
                :src="top.img_src"
                height="120"
                width="100%"
              >
              </v-img>
              <v-card-title class="py-1"><p class="text-truncate box mb-0">{{ top.title }}</p></v-card-title>
              <v-container py-0><v-divider></v-divider></v-container>
              <div class="d-flex justify-space-around">
                <div>
                  <v-btn icon><v-icon>mdi-heart-outline</v-icon></v-btn>{{ top.likes }}
                </div>
                <div>
                  <v-btn icon><v-icon>mdi-bookmark</v-icon></v-btn>{{ top.scraps }}
                </div>
              </div>
            </v-card>
          </v-slide-item>
        </v-slide-group>
      </v-container>
      <v-divider></v-divider>
      <div align="center" justify="center">
        <TipsItem
          v-for="(tip, index) in calData"
          :key="index"
          :tip="tip"
        />
        <v-pagination
          class="pt-2"
          v-model="curPageNum"
          :length="numOfPages"
          color="green darken-2"
          total-visible="7"
          light
        >
        </v-pagination>
      </div>
    </div>
    <div class="pb-16"></div>
    <v-speed-dial
      v-model="fab"
      fixed
      bottom
      right
      direction="top"
      transition="slide-y-reverse-transition"
    >
      <template v-slot:activator>
        <v-btn
          v-model="fab"
          color="green darken-2"
          dark
          fab
        >
          <v-icon v-if="fab">mdi-close</v-icon>
          <v-icon v-else>mdi-turtle</v-icon>
        </v-btn>
      </template>
      <v-btn
        fab
        dark
        small
        color="green lighten-1"
        @click="write"
      >
        <v-icon>mdi-pencil</v-icon>
      </v-btn>
      <v-btn
        fab
        dark
        small
        color="light-green"
        @click="scrollToTop"
      >
        <v-icon>mdi-apple-keyboard-control</v-icon>
      </v-btn>
    </v-speed-dial>
  </v-container>
</template>

<script>
import axios from 'axios'
import TipsItem from '../components/Tips/TipsItem.vue'
import { mapState } from 'vuex'
import _ from 'lodash'


export default {
  name: 'Tips',
  components: {
    TipsItem,
  },
  data() {
    return {
      tips: [],
      search: '',
      tipPerPage: 5,
      curPageNum: 1,
      serPageNum: 1,
      top5: [],
      fab: false,
    }
  },
  created() {
    axios.get(`${this.$store.state.api_server}/tips/`)
      .then(res => {
        this.tips = _.orderBy(res.data, 'id', 'desc')
      })
    axios.get(`${this.$store.state.api_server}/tips/top5`)
      .then(res => {
        this.top5 = res.data
      })
  },
  methods: {
    write() {
      this.$router.push('/tips/new')
    },
    goToDetail(id) {
      this.$router.push(`/tips/${id}`)
    },
    scrollToTop() {
      this.$vuetify.goTo(0)
    }
  },
  computed: {
    startOffset() {
      return ((this.curPageNum - 1) * this.tipPerPage)
    },
    endOffset() {
      return (this.startOffset + this.tipPerPage)
    },
    numOfPages() {
      return Math.ceil(this.tips.filter((tip) => {
        return tip.title.toLowerCase().includes(this.search.toLowerCase())
      }).slice(0).length / this.tipPerPage) // 여기 부분 살리기
    },
    calData() {
      return this.tips.filter((tip) => {
        return tip.title.toLowerCase().includes(this.search.toLowerCase())
      }).slice(this.startOffset, this.endOffset)
    },
    ...mapState([
      'api_server'
    ])
  },
}
</script>


<style scoped>
 .box {
   font-size: 0.8em;
   font-weight: bold;
 }
</style>