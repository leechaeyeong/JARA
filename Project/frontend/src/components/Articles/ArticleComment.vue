<template>
  <div style="font-family: 'Handon3gyeopsal300g' !important;">
    <div v-if="comments.length > 0">
      <div v-for="(comment, index) in comments" :key="index" >
        <div class="d-flex justify-space-between align-center">
          <v-btn text x-large class="pa-0 font-weight-bold" @click="goToUser(comment.writer)" style="font-family: 'Handon3gyeopsal600g' !important;">
            <v-icon class="mr-1" v-if="psas[comment.writer]==null">mdi-account-circle</v-icon>
            <v-avatar class="mr-1" v-else><img :src="psas[comment.writer]"></v-avatar>
            {{ users[comment.writer] }}</v-btn>
          <div class="d-flex align-center">
            <div v-if="comment.updated_at==null" class="grey--text">{{ comment.created_at }}</div> 
            <div v-else class="grey--text">{{ comment.updated_at }} (수정됨)</div>
            <v-menu offset-y>
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
                  v-for="(menu, idx) in menuItems"
                  :key="idx"
                  @click="updateOrDeleteOrHide(comment, idx, index)"
                >
                  <v-list-item-title style="font-family: 'Handon3gyeopsal300g' !important;">{{ menu.title }}</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
          </div>
        </div>
        <div class="mx-5">{{ comment.contents }}</div>
        <v-divider class="my-3"></v-divider>
      </div>  
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'ArticleComment',
  computed: {
    ...mapState([
      'userInfo',
      'users',
      'psas'
    ])  
  },
  props: {
    comments: {
      type: Array,
    }
  },
  data() {
    return {
      menuItems: [
        { title: '수정' },
        { title: '삭제' },
        { title: '숨김' },
      ],
    }
  },
  methods: {
    updateOrDeleteOrHide(comment, item, index) {
      this.$emit('updateOrDeleteOrHide', comment, item, index)
    },
    goToUser(writer) {
      this.$router.push(`/accounts/${writer}`)
    }
  },
}
</script>

<style>

</style>