<template>
  <v-container fluid mt-5>
    <div class="text-sm-h3 text-h4 font-weight-bold ml-3" style="font-family: 'Handon3gyeopsal600g' !important;">심심한 자라<v-icon x-large class="ml-2">mdi-cellphone-message</v-icon></div>
    <v-list id="chats" three-line style="font-family: 'Handon3gyeopsal300g';">
      <template v-for="(item, idx) in chats">
        <v-list-item :key="idx">
          <v-list-item-avatar v-if="item.userName!=nickname">
            <v-img v-if="psas[item.user_id]!=null" :src="psas[item.user_id]"></v-img>
            <v-icon v-else x-large>mdi-account-circle</v-icon>
          </v-list-item-avatar>

          <v-list-item-content :class="{ 'text-right': item.userName==nickname}">
            <v-list-item-subtitle>{{ item.userName }}</v-list-item-subtitle>
            <v-list-item-title>{{ item.content }}</v-list-item-title>
          </v-list-item-content>

          <v-list-item-avatar v-if="item.userName==nickname">
            <v-img v-if="psas[item.user_id]!=null" :src="psas[userInfo.id]"></v-img>
            <v-icon v-else x-large>mdi-account-circle</v-icon>
          </v-list-item-avatar>
        </v-list-item>
      </template>
    </v-list>
    <v-form class="input mt-5 d-flex" style="width: 100%; font-family: 'Handon3gyeopsal300g' !important;">
      <v-textarea
        v-model="chat"
        autofocus
        label="채팅을 남겨주세요."
        color="green darken-1"
        auto-grow
        outlined
        rows="1"
        row-height="15"
        @keyup.enter="sendChat"
      ></v-textarea>
      <div>
        <v-btn @click="sendChat" text class="mt-3 font-weight-bold" color="green darken-1">등록</v-btn>
      </div>
    </v-form>
    <v-speed-dial
      id="jara"
      v-model="fab"
      fixed
      top
      right
      direction="bottom"
      transition="slide-y-transition"
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
        color="green"
        @click="goToHome"
      >
        <v-icon>mdi-home</v-icon>
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
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
  name: 'LiveChat',
  computed: {
    ...mapState([
      'users',
      'userInfo',
      'psas',
      'api_server',
    ])
  },
  data() {
    return {
      nickname: this.$store.state.userInfo.nickname,
      id: this.$store.state.userInfo.id,
      chat: '',
      chats: [],
      fab: false,
    }
  },
  methods: {
    sendChat() {
      if (this.nickname !== '' && this.chat !== '') {
        this.send()
        this.chat = ''
      }
    },
    send() {
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
          userName: this.nickname,
          content: this.chat,
          user_id: this.id
        }
        this.stompClient.send("/receive", JSON.stringify(msg), {})
      }
    },   
    connect() {
      const serverURL = this.$store.state.api_server
      let socket = new SockJS(serverURL)
      this.stompClient = Stomp.over(socket)
      this.stompClient.connect(
        {},
        () => {
          this.connected = true
          this.stompClient.subscribe("/send", res => {
            this.chats.push(JSON.parse(res.body))
          })
        },
      )        
    },
    scrollToTop() {
      this.$vuetify.goTo(0)
    },
    goToHome() {
      this.$router.push('/main')
    }
  },
  created() {
    this.connect()
  },
  updated() {
    const chatbox = document.querySelector('#chats') 
    chatbox.scrollTop = chatbox.scrollHeight
  }
} 
</script>

<style scoped>
  #jara {
    top: 10vh;
  }

  .input {
    position: absolute;
    bottom: 0;
  }

  #chats{
    position: absolute;
    overflow-y: scroll;
    height: 500px;
    width: 100%;
    top: 100px;
  }
</style>