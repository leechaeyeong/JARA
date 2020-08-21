<template>
  <v-col cols="6">
    <v-hover v-slot:default="{ hover }">
      <v-card
        :elevation="hover ? 12 : 2"
        :class="{ 'on-hover': hover }"
      >
        <v-img
          :src="scrap.img_src"
          width="100%"
          height="150"
        >
          <v-dialog
            v-model="dialog"
            width="500"
          >
            <template v-slot:activator="{ on, attrs }">
              <div class="back" @click="dialog = true" v-bind="attrs" v-on="on"></div>
            </template>

            <v-card style="font-family: 'Handon3gyeopsal300g' !important;">
              <v-card-title class="headline d-flex align-center justify-space-between">
                <div style="font-family: 'Handon3gyeopsal600g' !important;">
                  <v-icon v-if="psas[scrap.writer]==null" class="mr-1">mdi-account-circle</v-icon>
                  <v-avatar v-else class="mr-1"><img :src="psas[scrap.writer]"></v-avatar>
                  {{ users[scrap.writer] }}
                </div>
                <v-menu offset-y >
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
                      @click="goToArticle(scrap.id)"
                    >
                      <v-list-item-title style="font-family: 'Handon3gyeopsal300g' !important;">{{ menu }}</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-menu>
              </v-card-title>

              <v-card-text style="font-family: 'Handon3gyeopsal300g' !important;">
                <v-img :src="scrap.img_src" width="100%" height="300"></v-img>
                <div class="font-weight-bold">{{ scrap.title }}</div>
                <div class="text-truncate mt-2">{{ scrap.contents }}</div>
                <div class="d-flex mt-3">
                  <v-icon v-if="scrap.likeAccounts.includes(userInfo.id)" color="red darken-1" class="mr-3">mdi-heart</v-icon>
                  <v-icon v-else class="mr-3">mdi-heart-outline</v-icon>
                  <v-icon>mdi-chat-outline</v-icon>
                </div>
                <div class="mt-3">{{ scrap.created_at }}</div>
              </v-card-text>
            </v-card>
          </v-dialog>
          <v-row class="fill-height" justify="center" align="center">
            <div class="align-self-center">
              <v-btn
                v-for="(icon, index) in icons"
                :key="index"
                :class="{ 'show-btns': hover }"
                class="mx-3"
                color="transparent"
                icon
              >
                <v-icon
                  :class="{ 'show-btns': hover }"
                  color="transparent"
                >
                  {{ icon }}
                </v-icon>
                {{ counts[index] }}
              </v-btn>
            </div>
          </v-row>
        </v-img>
      </v-card>
    </v-hover>
  </v-col>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'Scraps',
  props: {
    scrap: {
      type: Object
    }
  },
  data() {
    return {
      icons: ['mdi-heart', 'mdi-chat'],
      counts: [ this.scrap.likeAccounts.length, this.scrap.comments.length ],
      dialog: false,
      menu: '글 보러가기'
    }
  },
  computed: {
    ...mapState([
      'users',
      'userInfo',
      'psas'
    ])
  },
  methods: {
    goToArticle(id) {
      this.$router.push(`/tips/${id}`)
    }
  }
}
</script>

<style scoped>
.back {
  width: 100%;
  height: 150px;
  position: absolute;
}

.back:hover {
  background-color: rgba(0, 0, 0, 0.4);
  cursor: pointer;
}

.show-btns {
  color: rgba(255, 255, 255, 1) !important;
}
</style>