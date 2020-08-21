<template>
  <div class="mt-5">
    <v-card
      class="d-flex align-start flex-column mx-3 my-3"
      max-width="400"
    >
      <v-container
        class="px-0 py-0"
        @click="goTipDetail(tip.id)"
        :tip="tip"
      >
        <v-img
          id="img"
          class="white--text align-end"
          width="100%"
          height="250"
          :src="tip.img_src"
        >
        </v-img>
        
        <v-card-title class="text-center font-weight-bold">{{ tip.title }}</v-card-title>
        <v-card-subtitle class="pb-0 text-start">{{ tip.created_at }}</v-card-subtitle>

      </v-container>
      <v-card-actions style="width: 100%;">
        <v-chip class="tag">#{{ tag[tip.tag_id] }}</v-chip>
        <v-spacer></v-spacer>

        <v-btn v-if="!liked" @click="like" icon>
          <v-icon>mdi-heart-outline</v-icon>
        </v-btn>

        <v-btn v-else @click="like" icon>
          <v-icon color="red darken-1">mdi-heart</v-icon>
        </v-btn>

        <v-btn v-if="!scraped" @click="scrapTip(tip.id)" icon>
          <v-icon>mdi-bookmark-outline</v-icon>
        </v-btn>

        <v-btn v-else color="teal" icon>
          <v-icon>mdi-bookmark</v-icon>
        </v-btn>

      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
import axios from "axios"
import { mapState } from 'vuex'

export default {
  name: 'TipsItem',
  props: {
    tip: {
      type: Object,
    }
  },
  data() {
    return {
      show: false,
      tag: {1:'요리',2:'세탁',3:'청소',4:'보관'},
      liked: false,
      likeAccounts: [],
      scrapAccounts: [],
      scraped: false,
    } 
  },
  methods: {
    goTipDetail(t) {
      this.$router.push(`/tips/${t}`) // new -> t
    },
    scrapTip(t) {
      axios.post(`${this.$store.state.api_server}/tips/${t}/scrap`, '', { params: { user_id: this.$store.state.userInfo.id}})
        .then(() => {
          alert('팁이 저장되었습니다.')
          this.scraped = true
          this.tip.scrapAccounts.push(this.$store.state.userInfo.id)
        })
    },
    like() {
      axios.post(`${this.$store.state.api_server}/tips/${this.tip.id}/like`, '',{ params : { user_id: this.$store.state.userInfo.id }})
        .then(() => {
          if (this.liked) {
            this.liked = false
            this.likeAccounts.splice(this.likeAccounts.indexOf(this.$store.state.userInfo.id),1)
          } else {
            this.liked = true
            this.likeAccounts.push(this.$store.state.userInfo.id)
          }
        })
    },
  },
  created() {
    axios.get(`${this.$store.state.api_server}/tips/${this.tip.id}`)
      .then(res => {
        this.likeAccounts = res.data.likeAccounts
        this.scrapAccounts = res.data.scrapAccounts
        if (this.likeAccounts.length>0 && this.likeAccounts.includes(this.$store.state.userInfo.id)) {
          this.liked = true
        }
        if (this.scrapAccounts.includes(this.$store.state.userInfo.id)) {this.scraped = true}
      })
  },
  computed: {
    ...mapState([
      'api_server',
      'users',
      'userInfo'
    ])
  },
}
</script>

<style scoped>
.box {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
 }

</style>