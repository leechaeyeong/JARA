<template>
  <v-container fluid class="mt-5" style="font-family: 'Handon3gyeopsal300g';">
    <div class="text-sm-h3 text-h4 font-weight-bold ml-3" style="font-family: 'Handon3gyeopsal600g' !important;">신중한 자라<v-icon x-large class="ml-2">mdi-checkbox-multiple-marked-outline</v-icon></div>
    <div class="d-flex mt-5 ml-3">
      <v-chip
        color="green darken-2"
        text-color="white"
        @click="showAll"
      >
        전체
      </v-chip>
      <v-chip
        color="green"
        text-color="white"
        @click="showIng"
      >
        진행중
        <v-icon right>mdi-forum-outline</v-icon>
      </v-chip>
      <v-chip
        color="grey"
        text-color="white"
        @click="showDone"
      >
        완료
        <v-icon right>mdi-checkbox-multiple-marked</v-icon>
      </v-chip>
    </div>
    <div class="mt-5">
      <v-card
        class="mx-auto my-5"
        v-for="either in hotEithers"
        :key="either.id"
      >
        <v-card-text v-if="all||ing" @click="goToEither(either.id)">
          <div class="d-flex justify-space-between align-center">
            <div class="font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
              <v-icon class="mr-2" v-if="psas[either.writer]==null" x-large>mdi-account-circle</v-icon>
              <v-avatar class="mr-2" v-else><img :src="psas[either.writer]"></v-avatar>
              {{ users[either.writer] }}
            </div>
            <div>
              <v-chip
                color="red darken-1"
                text-color="white"
              >
                인기
                <v-icon right>mdi-fire</v-icon>
              </v-chip>
              <v-chip
                color="green"
                text-color="white"
              >
                진행중
                <v-icon right>mdi-forum-outline</v-icon>
              </v-chip>
            </div>
          </div>
          <p class="text-h6 text--primary mt-5 ml-3" style="font-family: 'Handon3gyeopsal300g' !important;">
            {{ either.question }}
          </p>
          <!-- <div
            class="or d-inline-flex justify-center align-center grey darken-4 rounded-circle font-weight-bold text-center white--text text-h6" 
            style="width: 48px; height: 48px;"
          >OR</div> -->
          <v-row justify-space-around class="px-3 text-center white--text text-sm-h3 text-h6 font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
            <v-col cols="6" class="red darken-1 rounded">{{ either.choiceA }}</v-col>
            <v-col cols="6" class="blue darken-2 rounded">{{ either.choiceB }}</v-col>
          </v-row>
        </v-card-text>
        <v-card-actions v-if="all||ing">
          <v-btn
            class="ml-auto font-weight-bold"
            text
            color="accent-4"
            @click="goToEither(either.id)"
          >
            투표하기
            <v-icon color="deep-purple">mdi-vote</v-icon>
          </v-btn>
        </v-card-actions>
      </v-card>
    </div>
    <div class="mt-5">
      <v-card
        class="mx-auto my-5"
        v-for="either in eithers"
        :key="either.id"
        @click="goToEither(either.id)"
      >
        <v-card-text v-if="all" @click="goToEither(either.id)">
          <div class="d-flex justify-space-between align-center">
            <div class="font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
              <v-icon class="mr-2" v-if="psas[either.writer]==null" x-large>mdi-account-circle</v-icon>
              <v-avatar class="mr-2" v-else><img :src="psas[either.writer]"></v-avatar>
              {{ users[either.writer] }}
            </div>
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
          <p class="text-h6 text--primary mt-5 ml-3" style="font-family: 'Handon3gyeopsal300g' !important;">
            {{ either.question }}
          </p>
          <!-- <div
            class="or d-inline-flex justify-center align-center grey darken-4 rounded-circle font-weight-bold text-center white--text text-h6" 
            style="width: 48px; height: 48px;"
          >OR</div> -->
          <v-row justify-space-around class="px-3 text-center white--text text-sm-h3 text-h6 font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
            <v-col cols="6" class="red darken-1 rounded">{{ either.choiceA }}</v-col>
            <v-col cols="6" class="blue darken-2 rounded">{{ either.choiceB }}</v-col>
          </v-row>
        </v-card-text>
        <v-card-text v-else-if="ing&&either.status==0" @click="goToEither(either.id)">
          <div class="d-flex justify-space-between align-center">
            <div class="font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
              <v-icon class="mr-2" v-if="psas[either.writer]==null" x-large>mdi-account-circle</v-icon>
              <v-avatar class="mr-2" v-else><img :src="psas[either.writer]"></v-avatar>
              {{ users[either.writer] }}
            </div>
            <v-chip
              color="green"
              text-color="white"
            >
              진행중
              <v-icon right>mdi-forum-outline</v-icon>
            </v-chip>
          </div>
          <p class="text-h6 text--primary mt-5 ml-3" style="font-family: 'Handon3gyeopsal300g' !important;">
            {{ either.question }}
          </p>
          <!-- <div
            class="or d-inline-flex justify-center align-center grey darken-4 rounded-circle font-weight-bold text-center white--text text-h6" 
            style="width: 48px; height: 48px;"
          >OR</div> -->
          <v-row justify-space-around class="px-3 text-center white--text text-sm-h3 text-h6 font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
            <v-col cols="6" class="red darken-1 rounded">{{ either.choiceA }}</v-col>
            <v-col cols="6" class="blue darken-2 rounded">{{ either.choiceB }}</v-col>
          </v-row>
        </v-card-text>
        <v-card-text v-else-if="done&&either.status==1" @click="goToEither(either.id)"> 
          <div class="d-flex justify-space-between align-center">
            <div class="font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
              <v-icon class="mr-2" v-if="psas[either.writer]==null" x-large>mdi-account-circle</v-icon>
              <v-avatar class="mr-2" v-else><img :src="psas[either.writer]"></v-avatar>
              {{ users[either.writer] }}
            </div>
            <v-chip
              color="grey"
              text-color="white"
            >
              완료
              <v-icon right>mdi-checkbox-multiple-marked</v-icon>
            </v-chip>
          </div>
          <p class="text-h6 text--primary mt-5 ml-3" style="font-family: 'Handon3gyeopsal300g' !important;">
            {{ either.question }}
          </p>
          <div
            class="or d-inline-flex justify-center align-center grey darken-4 rounded-circle font-weight-bold text-center white--text text-h6" 
            style="width: 48px; height: 48px;"
          >OR</div>
          <v-row justify-space-around class="px-3 text-center white--text text-sm-h3 text-h6 font-weight-bold" style="font-family: 'Handon3gyeopsal600g' !important;">
            <v-col cols="6" class="red darken-1 rounded">{{ either.choiceA }}</v-col>
            <v-col cols="6" class="blue darken-2 rounded">{{ either.choiceB }}</v-col>
          </v-row>
        </v-card-text>
        <v-card-actions v-if="all">
          <v-btn
            v-if="either.status==0"
            class="ml-auto font-weight-bold"
            text
            color="accent-4"
            @click="goToEither(either.id)"
          >
            투표하기
            <v-icon color="deep-purple">mdi-vote</v-icon>
          </v-btn>
          <v-btn
            v-else
            class="ml-auto font-weight-bold"
            text
            color="accent-4"
            @click="goToEither(either.id)"
          >
            결과보기
            <v-icon color="deep-purple">mdi-align-horizontal-right</v-icon>
          </v-btn>
        </v-card-actions>
        <v-card-actions v-else-if="ing&&either.status==0">
          <v-btn
            class="ml-auto font-weight-bold"
            text
            color="accent-4"
            @click="goToEither(either.id)"
          >
            투표하기
            <v-icon color="deep-purple">mdi-vote</v-icon>
          </v-btn>
        </v-card-actions>
        <v-card-actions v-else-if="done&&either.status==1">
          <v-btn
            class="ml-auto font-weight-bold"
            text
            color="accent-4"
            @click="goToEither(either.id)"
          >
            결과보기
            <v-icon color="deep-purple">mdi-align-horizontal-right</v-icon>
          </v-btn>
        </v-card-actions>
      </v-card>
      <div v-if="(isLoad&&(eithers.length < numOfEithers))" v-view="loadEithers" id="bottom"></div>
    </div>
    <v-speed-dial
      v-model="fab"
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
        @click="createEither"
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
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'Eithers',
  mounted() {
    this.fetchEithers()
  },
  updated() {
    if (this.eithers.length < this.numOfEithers) {
      setTimeout(() => {
        this.loadEithers('enter')
      }, 1000)
    }
  },
  data() {
    return {
      hotEithers: [],
      eithers: [],
      fab: false,
      isLoad: false,
      from: 0,
      eitherPerRQ: 5,
      numOfEithers: 0,
      all: true,
      ing: false,
      done: false,
    }
  },
  methods: {
    fetchEithers() {
      axios.get(`${this.$store.state.api_server}/eithers`)
        .then(res => {
          this.numOfEithers = res.data.length
          this.isLoad = true
        })
      axios.get(`${this.$store.state.api_server}/eithers/top3`)
        .then(res => this.hotEithers = res.data)
    },
    loadEithers(e) {
      if (e.type === 'exit') {
        return
      }
      if (e.type === 'progress') {
        return
      }    

      if (this.eithers.length < this.numOfEithers) {
        const checkBottom = document.querySelector('#bottom')
        const bottom = checkBottom.getBoundingClientRect(checkBottom)
        if (bottom.top <= (window.innerHeight || document.documentElement.clientWidth)) {
          axios.get(`${this.$store.state.api_server}/eithers/${this.from}/${this.eitherPerRQ}`)
            .then(res => {
              this.eithers = [ ...this.eithers, ...res.data ]
              this.from += this.eitherPerRQ
            })
        }
      }
    },
    goToEither(id) {
      this.$router.push(`/eithers/${id}`)
    },
    scrollToTop() {
      this.$vuetify.goTo(0)
    },
    createEither() {
      this.$router.push('eithers/new')
    },
    showAll() {
      this.all = true
      this.ing = false
      this.done = false
    },
    showIng() {
      this.all = false
      this.ing = true
      this.done = false
    },
    showDone() {
      this.all = false
      this.ing = false
      this.done = true
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
  
}
</script>

<style scoped>
  #bottom {
    height: 50px;
  }

  .v-speed-dial {
    position: fixed;
  }

  #create .v-btn--floating {
    position: relative;
  }

  .or {
    position: absolute;
    left: 43%;
    top: 53%;
  }
</style>